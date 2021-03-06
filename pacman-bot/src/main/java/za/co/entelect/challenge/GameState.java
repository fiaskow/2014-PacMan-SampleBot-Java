package za.co.entelect.challenge;


import java.awt.*;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by marais on 2014/07/04.
 * GameState is an immutable object.
 * All operations on the game state should result in a new object being returned.
 */
public class GameState implements Serializable {
  private static final long serialVersionUID = 9282786647133L;

  public final char[][] maze;
  public final int[] player;
  public final int[] opponent;
  public Player playerObject;
  public static final int PLAYER_SIZE = 6;  //length of player state array
  public static final int POSITION_X = 0;   //index into player state array
  public static final int POSITION_Y = 1;
  public static final int PREVIOUS_X = 2;
  public static final int PREVIOUS_Y = 3;
  public static final int HAS_POISON = 4;
  public static final int SCORE      = 5;
  public static final int UN_PLAYER = 0;
  public static final int UN_POISON = 1;
  public static final int UN_OFFSET = 2;


  public GameState (final char[][] maze, final int[] player, final int[] opponent) {
    this.maze = maze;
    this.player = player;
    this.opponent = opponent;
  }

  /**
   * Make a move on the current game state
   * @param move the symbol and new position of the player
   * @param performTeleport if player symbols should be teleported
   * @return the GameState after the move
   */
  public GameState makeMove(Move move, final boolean performTeleport) {
    return makeMove(maze, move, performTeleport);
  }

  /**
   * Makes a move for the provided player and use the provided maze as the base.
   * @param oldMaze which maze to clone to create the new GameState
   * @param move new position
   * @param performTeleport if player symbols should be teleported
   * @return the GameState
   */
  public GameState makeMove(final char[][] oldMaze, final Move move, final boolean performTeleport) {
    int[] updatedPlayer;
    int[] updatedOpponent;
    int[] mover;
    int[] other;

    //clone players
    updatedPlayer = player.clone();
    updatedOpponent = opponent.clone();
    //determine which player array to update based on the symbol.
    mover = move.moverSymbol == Main.PLAYER_SYMBOL ? updatedPlayer : updatedOpponent;
    other = move.moverSymbol == Main.PLAYER_SYMBOL ? updatedOpponent : updatedPlayer;
    char oppSymbol = move.moverSymbol == Main.PLAYER_SYMBOL ? Main.OPPONENT_SYMBOL : Main.PLAYER_SYMBOL;

    //clone maze
    char[][] newMaze = new char[oldMaze.length][];
    for (int i = 0; i < oldMaze.length; i++) {
      newMaze[i] = oldMaze[i].clone();
    }

    //keep history of the current position
    mover[PREVIOUS_X] = mover[POSITION_X];
    mover[PREVIOUS_Y] = mover[POSITION_Y];

    //Calculate moving player's score delta
    int addScore;
    switch (maze[move.to.x][move.to.y]) {
      //Eat a pill + 1
      case Main.PILL_SYMBOL : addScore = 1;
        break;
      //Eat a bonus pill
      case Main.BONUS_SYMBOL : addScore = 10;
        break;
      default: addScore = 0;
    }

    mover[SCORE] += addScore;

    //set old position of player on maze
    if (move.dropPoison) {
      mover[HAS_POISON] = Main.DROPPED_POISON;
      newMaze[mover[PREVIOUS_X]][mover[PREVIOUS_Y]] = '!';
    } else {
      newMaze[mover[PREVIOUS_X]][mover[PREVIOUS_Y]] = ' ';
    }

    //update player position .. this can change later if she is teleported
    mover[POSITION_X] = move.to.x;
    mover[POSITION_Y] = move.to.y;

    //Make teleport moves that would usually be handled by the competition harness.
    //This will overwrite a previous move made in the code above.
    if (performTeleport) {
      //if player eats the opponent, teleport her to center
      if (oldMaze[move.to.x][move.to.y] == oppSymbol) {
        newMaze[other[POSITION_X]][other[POSITION_Y]] = ' ';
        other[POSITION_X] = Main.SPAWN_X;
        other[POSITION_Y] = Main.SPAWN_Y;
        //speedup System.err.println("Player ate opponent, teleporting opponent to Respawn area.");
      }

      //if player eats a poison pil, teleport player to center
      if (oldMaze[move.to.x][move.to.y] == Main.POISON_SYMBOL) {
        newMaze[mover[POSITION_X]][mover[POSITION_Y]] = ' ';
        mover[PREVIOUS_X] = mover[POSITION_X];
        mover[PREVIOUS_Y] = mover[POSITION_Y];
        mover[POSITION_X] = Main.SPAWN_X;
        mover[POSITION_Y] = Main.SPAWN_Y;
        //speedup System.err.println("Player ate a Poison Pill, teleporting to the Respawn area.");
      }
    }

    newMaze[other[POSITION_X]][other[POSITION_Y]] = oppSymbol;
    newMaze[mover[POSITION_X]][mover[POSITION_Y]] = move.moverSymbol;
    return new GameState(newMaze,updatedPlayer,updatedOpponent);
  }

  public int[] makeMove2(Move move, final boolean performTeleport) {
    int[] mover;
    int[] other;
    int[] unmake = new int[PLAYER_SIZE*2+UN_OFFSET];
    char oppSymbol;

    //determine which player array to update based on the symbol.
    if (move.moverSymbol == Main.PLAYER_SYMBOL) {
      mover = player;
      other = opponent;
      unmake[UN_PLAYER] = 0;
      oppSymbol = Main.OPPONENT_SYMBOL;
    } else {
      mover = opponent;
      other = player;
      unmake[UN_PLAYER] = 1;
      oppSymbol = Main.PLAYER_SYMBOL;
    }

    //keep player buffers for unmake move
    System.arraycopy(mover,0,unmake,UN_OFFSET,PLAYER_SIZE);
    System.arraycopy(other,0,unmake,UN_OFFSET+PLAYER_SIZE,PLAYER_SIZE);

    //keep history of the current position
    mover[PREVIOUS_X] = mover[POSITION_X];
    mover[PREVIOUS_Y] = mover[POSITION_Y];

    //Calculate moving player's score delta
    int addScore;
    switch (maze[move.to.x][move.to.y]) {
      //Eat a pill + 1
      case Main.PILL_SYMBOL : addScore = 1;
        break;
      //Eat a bonus pill
      case Main.BONUS_SYMBOL : addScore = 10;
        break;
      default: addScore = 0;
    }

    mover[SCORE] += addScore;

    //set old position of player on maze
    if (move.dropPoison) {
      mover[HAS_POISON] = Main.DROPPED_POISON;
      maze[mover[PREVIOUS_X]][mover[PREVIOUS_Y]] = '!';
    } else {
      maze[mover[PREVIOUS_X]][mover[PREVIOUS_Y]] = ' ';
    }

    //update player position .. this can change later if she is teleported
    mover[POSITION_X] = move.to.x;
    mover[POSITION_Y] = move.to.y;

    //Make teleport moves that would usually be handled by the competition harness.
    //This will overwrite a previous move made in the code above.
    if (performTeleport) {
      //if player eats the opponent, teleport her to center
      if (maze[move.to.x][move.to.y] == oppSymbol) {
        //maze[other[POSITION_X]][other[POSITION_Y]] = ' ';
        other[POSITION_X] = Main.SPAWN_X;
        other[POSITION_Y] = Main.SPAWN_Y;
        //speedup System.err.println("Player ate opponent, teleporting opponent to Respawn area.");
      }

      //if player eats a poison pil, teleport player to center
      else if (maze[mover[POSITION_X]][mover[POSITION_Y]] == Main.POISON_SYMBOL) {
        maze[mover[POSITION_X]][mover[POSITION_Y]] = ' ';
        mover[PREVIOUS_X] = mover[POSITION_X];
        mover[PREVIOUS_Y] = mover[POSITION_Y];
        mover[POSITION_X] = Main.SPAWN_X;
        mover[POSITION_Y] = Main.SPAWN_Y;
        unmake[UN_POISON] = 1;
        //speedup System.err.println("Player ate a Poison Pill, teleporting to the Respawn area.");
      }
    }

    maze[other[POSITION_X]][other[POSITION_Y]] = oppSymbol;
    maze[mover[POSITION_X]][mover[POSITION_Y]] = move.moverSymbol;
    return unmake;
  }

  public void unmakeMove(int[] unmake) {
    int[] mover;
    int[] other;
    char moverChar;
    char opponChar;
    if (unmake[UN_PLAYER] == 0) {
      mover = player;
      other = opponent;
      moverChar = Main.PLAYER_SYMBOL;
      opponChar = Main.OPPONENT_SYMBOL;
    } else {
      mover = opponent;
      other = player;
      moverChar = Main.OPPONENT_SYMBOL;
      opponChar = Main.PLAYER_SYMBOL;
    }

    int delta = mover[SCORE] - unmake[UN_OFFSET+SCORE];
    char restore;
    if (delta == 1)
      restore = Main.PILL_SYMBOL;
    else if (delta == 10)
      restore = Main.BONUS_SYMBOL;
    else
      restore = ' ';

    //This is still the later position
    maze[mover[POSITION_X]][mover[POSITION_Y]] = restore;
    maze[other[POSITION_X]][other[POSITION_Y]] = ' ';
    if (unmake[UN_POISON] == 1)
      maze[mover[PREVIOUS_X]][mover[PREVIOUS_Y]] = Main.POISON_SYMBOL;

    //Restore old buffers
    System.arraycopy(unmake,UN_OFFSET,mover,0,PLAYER_SIZE);
    System.arraycopy(unmake,UN_OFFSET+PLAYER_SIZE,other,0,PLAYER_SIZE);

    //restore player symbols
    maze[mover[POSITION_X]][mover[POSITION_Y]] = moverChar;
    maze[other[POSITION_X]][other[POSITION_Y]] = opponChar;
  }

  public java.util.List<Move> determineAllBasicMoves(char moverChar, boolean excludeHistory) {
    int[] mover;
    //speedup System.err.println("Calculating possible moves for " + moverChar);
    if (moverChar == Main.PLAYER_SYMBOL) {
      mover = player;
    }
    else if (moverChar == Main.OPPONENT_SYMBOL) {
      mover = opponent;
    }
    else {
      throw new IllegalArgumentException("Unknown player symbol.");
    }
    java.util.List<Move> moveList = new LinkedList<Move>();// ArrayList<Move>();
    boolean hasPoison = mover[HAS_POISON] == Main.CARRY_POISON;

    //if inside respawn area
    if (mover[POSITION_X] >= 9 && mover[POSITION_X] <= 11 && mover[POSITION_Y] == 9) {
      //Can only move up and down, can't eat another player
      //If in the middle, move up or down except if there is another player there.
      if (mover[POSITION_X] == Main.SPAWN_X && mover[POSITION_Y] == Main.SPAWN_Y) {
        //Move down
        if (maze[mover[POSITION_X] + 1][mover[POSITION_Y]] != Main.OPPONENT_SYMBOL &&
            maze[mover[POSITION_X] + 1][mover[POSITION_Y]] != Main.PLAYER_SYMBOL)
          addMoveToList(moveList, moverChar, new Point(mover[POSITION_X] + 1, mover[POSITION_Y]), hasPoison);

        //Move up
        if (maze[mover[POSITION_X] - 1][mover[POSITION_Y]] != Main.OPPONENT_SYMBOL &&
            maze[mover[POSITION_X] - 1][mover[POSITION_Y]] != Main.PLAYER_SYMBOL)
          addMoveToList(moveList, moverChar, new Point(mover[POSITION_X] - 1, mover[POSITION_Y]), hasPoison);
        //can only move down if first move was down
      } else if (mover[POSITION_X] == Main.SPAWN_X + 1) {
        addMoveToList(moveList, moverChar, new Point(mover[POSITION_X] + 1, mover[POSITION_Y]), hasPoison);
        //similarly, can only move up if first move was up
      } else if (mover[POSITION_X] == Main.SPAWN_X - 1) {
        addMoveToList(moveList, moverChar, new Point(mover[POSITION_X] - 1, mover[POSITION_Y]), hasPoison);
      }
    } else {
      //add walls to close the respawn area
      char top = maze[9][9]; maze[9][9] = Main.WALL;
      char bot = maze[11][9]; maze[11][9] = Main.WALL;

      //Move down
      if (mover[POSITION_X] + 1 < Main.HEIGHT)
        if (maze[mover[POSITION_X] + 1][mover[POSITION_Y]] != Main.WALL &&
            !(excludeHistory && mover[POSITION_X] + 1 == mover[PREVIOUS_X] && mover[POSITION_Y] == mover[PREVIOUS_Y]))
          addMoveToList(moveList,moverChar,new Point(mover[POSITION_X] + 1, mover[POSITION_Y]),hasPoison);

      //Move up
      if (mover[POSITION_X] - 1 >= 0)
        if (maze[mover[POSITION_X] - 1][mover[POSITION_Y]] != Main.WALL &&
            !(excludeHistory && mover[POSITION_X] - 1 == mover[PREVIOUS_X] && mover[POSITION_Y] == mover[PREVIOUS_Y]))
          addMoveToList(moveList,moverChar,new Point(mover[POSITION_X] - 1, mover[POSITION_Y]),hasPoison);

      //Move Right
      if (mover[POSITION_Y] + 1 < Main.WIDTH)
        if (maze[mover[POSITION_X]][mover[POSITION_Y] + 1] != Main.WALL &&
            !(excludeHistory && mover[POSITION_X] == mover[PREVIOUS_X] && mover[POSITION_Y] + 1 == mover[PREVIOUS_Y]))
          addMoveToList(moveList,moverChar,new Point(mover[POSITION_X], mover[POSITION_Y] + 1),hasPoison);

      //Move Left
      if (mover[POSITION_Y] - 1 >= 0)
        if (maze[mover[POSITION_X]][mover[POSITION_Y] - 1] != Main.WALL &&
            !(excludeHistory && mover[POSITION_X] == mover[PREVIOUS_X] && mover[POSITION_Y] - 1 == mover[PREVIOUS_Y]))
          addMoveToList(moveList,moverChar,new Point(mover[POSITION_X], mover[POSITION_Y] - 1),hasPoison);

      //Jump Portal 1 => Portal 2
      if (mover[POSITION_X] == Main.PORTAL1_X && mover[POSITION_Y] == Main.PORTAL1_Y &&
          !(excludeHistory && mover[PREVIOUS_X] == Main.PORTAL2_X && mover[PREVIOUS_Y] == Main.PORTAL2_Y))
        addMoveToList(moveList,moverChar,new Point(Main.PORTAL2_X, Main.PORTAL2_Y),hasPoison);

      //Jump Portal 2 => Portal 1
      if (mover[POSITION_X] == Main.PORTAL2_X && mover[POSITION_Y] == Main.PORTAL2_Y &&
          !(excludeHistory && mover[PREVIOUS_X] == Main.PORTAL1_X && mover[PREVIOUS_Y] == Main.PORTAL1_Y))
        addMoveToList(moveList,moverChar,new Point(Main.PORTAL1_X, Main.PORTAL1_Y),hasPoison);
      //restore walls
      maze[9][9] = top;
      maze[11][9] = bot;
    }
    return moveList;
  }

  private void addMoveToList(final List<Move> moveList, final char moverSymbol, final Point to, final boolean hasPoison) {
    int score = 0;
    if (maze[to.x][to.y] == Main.PILL_SYMBOL) score = 1;
    else if (maze[to.x][to.y] == Main.BONUS_SYMBOL) score = 10;
    moveList.add(new Move(moverSymbol, to,false,score));
    if (hasPoison) moveList.add(new Move(moverSymbol,to,true,score));
  }

  public boolean playerHasPoison() {
    return player[HAS_POISON] == Main.CARRY_POISON;
  }

  public boolean opponentHasPoison() {
    return opponent[HAS_POISON] == Main.CARRY_POISON;
  }

  /**
   * Creates an initial game state using the provided maze and sets the scores to zero.
   * Each player has 1 poison pill.
   * @param maze initial state of the maze;
   * @return initial game state.
   */
  public static GameState initGameState(char[][] maze) {
    char[][] newMaze = new char[maze.length][];
    for (int i = 0; i < maze.length; i++) {
      newMaze[i] = maze[i].clone();
    }
    Point playerPosition = getCurrentPosition(maze,Main.PLAYER_SYMBOL);
    Point oppPosition = getCurrentPosition(maze,Main.OPPONENT_SYMBOL);
    int[] player = new int[PLAYER_SIZE];
    int[] opp = new int[PLAYER_SIZE];
    player[POSITION_X] = playerPosition.x;
    player[POSITION_Y] = playerPosition.y;
    player[PREVIOUS_X] = playerPosition.x;
    player[PREVIOUS_Y] = playerPosition.y;
    player[HAS_POISON] = Main.CARRY_POISON;
    player[SCORE] = 0;
    opp[POSITION_X] = oppPosition.x;
    opp[POSITION_Y] = oppPosition.y;
    opp[PREVIOUS_X] = oppPosition.x;
    opp[PREVIOUS_Y] = oppPosition.y;
    opp[HAS_POISON] = Main.CARRY_POISON;
    opp[SCORE] = 179 - countPills(maze);
    return new GameState(newMaze,player,opp);

  }

  private static int countPills(char[][] maze) {
    int count = 0;
    for (int j = 0; j < Main.HEIGHT; j++)
      for (int i = 0; i < Main.WIDTH; i++)
        if (maze[j][i] == Main.PILL_SYMBOL)
          count++;
    return count;
  }

  public static Point getCurrentPosition(final char[][] maze, char playerSymbol) {
    Point coordinate = new Point();
    for (int x = 0; x < Main.HEIGHT; x++) {
      for (int y = 0; y < Main.WIDTH; y++) {
        if (maze[x][y] == playerSymbol) {
          coordinate.setLocation(x, y);
          return coordinate;
        }
      }
    }
    //if not found, we are in the center
    coordinate.setLocation(Main.SPAWN_X, Main.SPAWN_Y);
    return coordinate;
  }

  /**
   * Determines if the opponent (player B) dropped a poison pill.
   * @param newMaze game state after opponent moved
   * @return weather a poison pill was dropped.
   */
  private boolean detectPoisonDropped(char[][] newMaze) {
    //Did the opponent drop a poison pill?
    if (newMaze[opponent[POSITION_X]][opponent[POSITION_Y]] == Main.PILL_SYMBOL)
      System.err.println("Opponent dropped poison on " + opponent[POSITION_X] + "," + opponent[POSITION_Y]);
    return newMaze[opponent[POSITION_X]][opponent[POSITION_Y]] == Main.POISON_SYMBOL;
  }

  public GameState updateFromInput(char[][] newMaze) {
    //get opponent move
    Point oppPos = getCurrentPosition(newMaze, Main.OPPONENT_SYMBOL);
    Move oppMove = new Move(Main.OPPONENT_SYMBOL,oppPos,detectPoisonDropped(newMaze));
    //System.err.println("Opponent (" + Main.OPPONENT_SYMBOL + ") moved to " + oppPos.x + "," + oppPos.y + " from " + opponent[POSITION_X] + "," + opponent[POSITION_Y]);

    //harness may have teleported us
    Point myPos = getCurrentPosition(newMaze, Main.PLAYER_SYMBOL);
    //System.err.println("My new position is " + myPos.x + "," + myPos.y);
    if (player[POSITION_X] != myPos.x || player[POSITION_Y] != myPos.y) {
      System.err.println("Player (" + Main.PLAYER_SYMBOL + ") has been teleported. Updating position");
      player[POSITION_X] = myPos.x;
      player[POSITION_Y] = myPos.y;
      // DEBUG make a backup copy of the internal state.
//      Path internalStateFile = FileSystems.getDefault().getPath(Main.PLAYER_SYMBOL+Main.INTERNAL_FILE_NAME);
//      Path internalStateFileBackup = FileSystems.getDefault().getPath("backup"+Main.PLAYER_SYMBOL+Main.INTERNAL_FILE_NAME);
//      try {
//        Files.copy(internalStateFile,internalStateFileBackup, StandardCopyOption.REPLACE_EXISTING);
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
    }
    //make move on current game state
    GameState newState = makeMove(newMaze,oppMove,false);

    return newState;
  }

  public boolean isEndState(boolean noWinner) {
    //consider a winning score an end state for Minimax search
    if (noWinner)
      return player[SCORE] >= 110 || opponent[SCORE] >= 110;
    else
      return (219 - player[SCORE] - opponent[SCORE]) == 0;
  }

//  @Override
//  public String toString() {
//    printMaze(this,null,System.err);
//  }

  public void printMaze(List<Move> principalVariation, PrintStream stream) {
    StringBuilder s = new StringBuilder();
    for (int x = 0; x < Main.HEIGHT; x++) {
      for (int y = 0; y < Main.WIDTH; y++) {
        s.append(maze[x][y]);
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

        s.append(pvMoveNumber > -1 ? "0" : maze[x][y] == '.' ? " " : maze[x][y] );
//        }
//        else {
//          s.append(state.maze[x][y]);
//        }
      }
      if (x != Main.HEIGHT - 1) s.append('\n');
    }
    stream.println(s);
  }

  public GameState clone() {
    int[] nPlayer = player.clone();
    int[] nOppone = opponent.clone();
    //clone maze
    char[][] newMaze = new char[maze.length][];
    for (int i = 0; i < maze.length; i++) {
      newMaze[i] = maze[i].clone();
    }
    return new GameState(newMaze,nPlayer,nOppone);
  }
}

