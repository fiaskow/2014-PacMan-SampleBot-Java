package za.co.entelect.challenge;

import java.awt.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class Main {
  public static final int WIDTH = 19;
  public static final int HEIGHT = 22;
  public static final int PORTAL1_X = 10;
  public static final int PORTAL1_Y = 18;
  public static final int PORTAL2_X = 10;
  public static final int PORTAL2_Y = 0;
  public static final int SPAWN_X = 10;
  public static final int SPAWN_Y = 9;
  public static final char WALL = '#';
  public static char PLAYER_SYMBOL = 'A';
  public static char OPPONENT_SYMBOL = 'B';
  public static final char PILL_SYMBOL = '.';
  public static final char BONUS_SYMBOL = '*';
  public static final char POISON_SYMBOL = '!';
  public static final String OUTPUT_FILE_NAME = "game.state";
  public static final String INTERNAL_FILE_NAME = "internal.state";
  //The coordinate values will contain this value while player is
  // carrying poison pill, or it is not on the map anymore
  public static final int CARRY_POISON = 1;
  public static final int DROPPED_POISON = 0;
  public static boolean PERFORM_TELEPORT = false;
  public static boolean printBoard = false;
  public static long calctime = 0;
  public static long starttime = 0;
  public static long endtime = 0;

    /*
        Read previous game state.
        Compare to current Game state (Determine poison pil dropped by opponent, determine opponent points)

        Start MinMax on current GameState
     */





  public static void main(String[] args) {
    starttime = System.currentTimeMillis();
    if (args.length < 5) {
      System.out.println("Params <statefile> <playersymbol> <opponentSymbol> <printBoard> <calcmillis> <performTeleport>");
      System.exit(1);
    }
    PLAYER_SYMBOL = args[1].charAt(0);
    OPPONENT_SYMBOL = args[2].charAt(0);
    printBoard = Boolean.parseBoolean(args[3]);
    endtime = starttime + Integer.parseInt(args[4]);
    if (args.length > 5) PERFORM_TELEPORT = Boolean.parseBoolean(args[5]);
    System.err.println("Player symbol: " + PLAYER_SYMBOL);
    System.err.println("Opponent symbol: " + OPPONENT_SYMBOL);
    System.err.println("Printing board " + printBoard);
    char[][] maze = ReadMaze(args[0]);
//    boolean newGame = false;
//    if (args.length > 4 && Boolean.parseBoolean(args[4]))
//      newGame = true;
    //read game state from file
    GameState previousState = ReadOldGameState();
    GameState currentState;
    Player player;
    if (previousState != null) { // && !newGame) {
      currentState = previousState.updateFromInput(maze);    //This is an ongoing game
      player = previousState.playerObject;
    } else {
      currentState = GameState.initGameState(maze);             //This is a new game
      player = (PLAYER_SYMBOL == 'A') ? new Player(new Negamax(new Quiesce(new ArrayList<Move>()))) : new Player(new NegamaxAB(new Quiesce(new ArrayList<Move>()),14),true);
      System.err.println("========= Starting new game. ========== " + (new Date(System.currentTimeMillis()).toString() ));
    }
    //System.err.println("Calculated state after input");
    //printGameState(currentState,null, System.err);


    //System.err.println("Determining Negamax move for player");


//    List<Point> possibleMoveList = currentState.determineAllBasicMoves(PLAYER_SYMBOL);
//    Random random = new Random();
//    int randomMoveIndex = random.nextInt(possibleMoveList.size());
//    int dropPoison = Integer.MAX_VALUE;
//    if (currentState.playerHasPoison())
//      dropPoison = random.nextInt(100);

    //Negamax strategy = new Negamax(new SimpleEval());
    //Move move = strategy.getMove(currentState,null,11,1);
    //GameState newState = currentState.makeMove(Main.PLAYER_SYMBOL,move,noHarness);
    GameState newState = player.makeMove(currentState,PERFORM_TELEPORT);
    writeMaze(newState.maze, OUTPUT_FILE_NAME);
    writeGameState(newState, player);
    if (printBoard) printGameState(newState,player,System.out);
    System.err.println(System.currentTimeMillis() - starttime + " ms");
    System.err.println("========= DONE =========");
  }


  private static void printMaze(GameState state, List<Move> principalVariation, PrintStream stream) {
    StringBuilder s = new StringBuilder();
    for (int x = 0; x < HEIGHT; x++) {
      for (int y = 0; y < WIDTH; y++) {
        s.append(state.maze[x][y]);
//        if (state.maze[x][y] == PLAYER_SYMBOL ||
//            state.maze[x][y] == OPPONENT_SYMBOL ||
//            state.maze[x][y] == PILL_SYMBOL ||
//            state.maze[x][y] == BONUS_SYMBOL ||
//            state.maze[x][y] == '!') {
          int pvMoveNumber = -1;
          for (Move m : principalVariation) {
            if (m.to.x == x && m.to.y == y) {
              pvMoveNumber = principalVariation.indexOf(m);
              break;
            }
          }

          s.append(pvMoveNumber > -1 ? "0" : state.maze[x][y] == '.' ? " " : state.maze[x][y] );
//        }
//        else {
//          s.append(state.maze[x][y]);
//        }
      }
      if (x != HEIGHT - 1) s.append('\n');
    }
    stream.println(s);
  }

  private static void printGameState(GameState state, Player p, PrintStream stream) {
    StringBuilder s = new StringBuilder();
    s.append("\n");
    s.append("\n");
    s.append("MY SCORE: ");
    s.append(state.player[GameState.SCORE]);
    s.append("\tCARRY POISON: ");
    s.append(state.playerHasPoison() ? "YES" : "NO");
    s.append("\n");
    s.append("OPP SCORE: ");
    s.append(state.opponent[GameState.SCORE]);
    s.append("\tCARRY POISON: ");
    s.append(state.opponentHasPoison() ? "YES" : "NO");
    s.append("\n");
    if (p != null) {
      s.append("Strategy: " + p.getStrategy().getClass().getName());
      s.append("\n");
      s.append("Evaluated " + p.getStrategy().getNodesEvaluated() + " nodes");
      s.append("\n");
      s.append("PV moves:" + p.getStrategy().getPrincipalVariation().toString());
    }
    stream.println(s);
    if (p == null || p.getStrategy() == null) {
      printMaze(state, new ArrayList<Move>(), stream);
    }
    else {
      printMaze(state, p.getStrategy().getPrincipalVariation(), stream);
    }
    calctime = System.currentTimeMillis() - starttime;
    s.append("CalculationTime: ");
    s.append(calctime + "ms");
  }

  private static void writeMaze(char[][] maze, String filePath) {
    try {
      PrintWriter writer = new PrintWriter(filePath);
      String output = "";
      for (int x = 0; x < HEIGHT; x++) {
        for (int y = 0; y < WIDTH; y++) {
          output += maze[x][y];
        }
        if (x != HEIGHT - 1) output += ('\n');
      }
      writer.print(output);
      writer.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private static char[][] ReadMaze(String filePath) {
    char[][] map = new char[HEIGHT][];
    try {
      Scanner reader = new Scanner(new File(filePath));
      int rowCount = 0;
      while (reader.hasNext()) {
        String row = reader.nextLine();
        map[rowCount] = row.toCharArray();
        rowCount++;
      }
    } catch (IOException e) {
      System.err.println(e);
    }
    return map;
  }

  private static void writeGameState(GameState state, Player pObject) {
    OutputStream file = null;
    ObjectOutput output = null;
    state.playerObject = pObject;
    try {
      Path internalStateFile = FileSystems.getDefault().getPath(Main.PLAYER_SYMBOL+INTERNAL_FILE_NAME);
      Files.deleteIfExists(internalStateFile);
      file = new FileOutputStream(Main.PLAYER_SYMBOL+INTERNAL_FILE_NAME);
      OutputStream buffer = new BufferedOutputStream(file);
      output = new ObjectOutputStream(buffer);
      output.writeObject(state);
    }
    catch(IOException ex){
      System.err.println("Cannot write game state file: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      if (file != null) {
        try {
          output.flush();
          output.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static GameState ReadOldGameState() {
    System.err.println("Reading old game state for player " + PLAYER_SYMBOL);
    ObjectInput input = null;
    GameState s = null;
    try {
      InputStream file = new FileInputStream(Main.PLAYER_SYMBOL+INTERNAL_FILE_NAME);
      InputStream buffer = new BufferedInputStream(file);
      input = new ObjectInputStream(buffer);
      s = (GameState)input.readObject();
    } catch (IOException ex) {
      if (ex instanceof FileNotFoundException)
        System.err.println("No game state exists on disk");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      if (input != null)
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    return s;
  }

}
