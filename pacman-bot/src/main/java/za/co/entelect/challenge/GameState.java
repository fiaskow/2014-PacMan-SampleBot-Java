package za.co.entelect.challenge;


import java.awt.*;
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
  public static final int PLAYER_SIZE = 5;  //length of player state array
  public static final int POSITION_X = 0;   //index into player state array
  public static final int POSITION_Y = 1;
  public static final int POISON_X = 2;
  public static final int POISON_Y = 3;
  public static final int SCORE = 4;


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
    final int[] oldPlayer;
    final int[] oldOpponent;
    int[] updatedPlayer;
    int[] updatedOpponent;
    int[] mover;
    int[] other;

    //clone players
    oldPlayer = player;
    oldOpponent = opponent;
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
      mover[POISON_X] = mover[POSITION_X];
      mover[POISON_Y] = mover[POSITION_Y];
      newMaze[mover[POSITION_X]][mover[POSITION_Y]] = '!';
    } else {
      newMaze[mover[POSITION_X]][mover[POSITION_Y]] = ' ';
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
      if (move.to.x == oldPlayer[POISON_X] && move.to.y == oldPlayer[POISON_Y]) {      //steps on own poison pill
        updatedPlayer[POISON_X] = Main.CONSUMED_POISON;
        updatedPlayer[POISON_Y] = Main.CONSUMED_POISON;
        mover[POSITION_X] = Main.SPAWN_X;
        mover[POSITION_Y] = Main.SPAWN_Y;
        //speedup System.err.println("Player ate a Poison Pill, teleporting to the Respawn area.");
      }  else if (move.to.x == oldOpponent[POISON_X] && move.to.y == oldOpponent[POISON_Y]) {  //steps on opponents poison pill
        updatedOpponent[POISON_X] = Main.CONSUMED_POISON;
        updatedOpponent[POISON_Y] = Main.CONSUMED_POISON;
        mover[POSITION_X] = Main.SPAWN_X;
        mover[POSITION_Y] = Main.SPAWN_Y;
        //speedup System.err.println("Player ate a Poison Pill, teleporting to the Respawn area.");
      }
    }

    newMaze[mover[POSITION_X]][mover[POSITION_Y]] = move.moverSymbol;
    newMaze[other[POSITION_X]][other[POSITION_Y]] = oppSymbol;
    return new GameState(newMaze,updatedPlayer,updatedOpponent);
  }

  public boolean insideRespawn(int[] mover) {
    return mover[POSITION_X] >= 9 && mover[POSITION_X] <= 11 && mover[POSITION_Y] == 9;
  }

  public java.util.List<Move> determineAllBasicMoves(char moverChar) {
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
    java.util.List<Move> moveList = new ArrayList<Move>();
    boolean hasPoison = mover[POISON_X] == Main.CARRY_POISON;

    if (insideRespawn(mover)) {
      //Can only move up and down, can't eat another player
      //If in the middle, move up or down except if there is another player there.
      if (mover[POSITION_X] == Main.SPAWN_X && mover[POSITION_Y] == Main.SPAWN_Y) {
        //Move down
        if (maze[mover[POSITION_X] + 1][mover[POSITION_Y]] != Main.OPPONENT_SYMBOL ||
            maze[mover[POSITION_X] + 1][mover[POSITION_Y]] != Main.PLAYER_SYMBOL)
          addMoveToList(moveList, moverChar, new Point(mover[POSITION_X] + 1, mover[POSITION_Y]), hasPoison);

        //Move up
        if (maze[mover[POSITION_X] - 1][mover[POSITION_Y]] != Main.OPPONENT_SYMBOL ||
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
      //Move Right
      if (mover[POSITION_Y] + 1 < Main.WIDTH)
        if (maze[mover[POSITION_X]][mover[POSITION_Y] + 1] != Main.WALL)
          addMoveToList(moveList,moverChar,new Point(mover[POSITION_X], mover[POSITION_Y] + 1),hasPoison);

      //Move Left
      if (mover[POSITION_Y] - 1 >= 0)
        if (maze[mover[POSITION_X]][mover[POSITION_Y] - 1] != Main.WALL)
          addMoveToList(moveList,moverChar,new Point(mover[POSITION_X], mover[POSITION_Y] - 1),hasPoison);

      //Move down
      if (mover[POSITION_X] + 1 < Main.HEIGHT)
        if (maze[mover[POSITION_X] + 1][mover[POSITION_Y]] != Main.WALL)
          addMoveToList(moveList,moverChar,new Point(mover[POSITION_X] + 1, mover[POSITION_Y]),hasPoison);

      //Move up
      if (mover[POSITION_X] - 1 >= 0)
        if (maze[mover[POSITION_X] - 1][mover[POSITION_Y]] != Main.WALL)
          addMoveToList(moveList,moverChar,new Point(mover[POSITION_X] - 1, mover[POSITION_Y]),hasPoison);

      //Jump Portal 1 => Portal 2
      if (mover[POSITION_X] == Main.PORTAL1_X && mover[POSITION_Y] == Main.PORTAL1_Y)
        addMoveToList(moveList,moverChar,new Point(Main.PORTAL2_X, Main.PORTAL2_Y),hasPoison);

      //Jump Portal 2 => Portal 1
      if (mover[POSITION_X] == Main.PORTAL2_X && mover[POSITION_Y] == Main.PORTAL2_Y)
        addMoveToList(moveList,moverChar,new Point(Main.PORTAL1_X, Main.PORTAL1_Y),hasPoison);
      //restore walls
      maze[9][9] = top;
      maze[11][9] = bot;
    }
    //speedup System.err.println("Player " + moverChar + " has " + moveList.size() + " possible moves");
    return moveList;
  }

  private void addMoveToList(final List<Move> moveList, final char moverSymbol, final Point to, final boolean hasPoison) {
    moveList.add(new Move(moverSymbol, to,false));
    if (hasPoison) moveList.add(new Move(moverSymbol,to,true));
  }

  public boolean playerHasPoison() {
    return player[POISON_X] == Main.CARRY_POISON;
  }

  public boolean opponentHasPoison() {
    return opponent[POISON_X] == Main.CARRY_POISON;
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
    player[POISON_X] = Main.CARRY_POISON;
    player[POISON_Y] = Main.CARRY_POISON;
    player[SCORE] = 0;
    opp[POSITION_X] = oppPosition.x;
    opp[POSITION_Y] = oppPosition.y;
    opp[POISON_X] = Main.CARRY_POISON;
    opp[POISON_Y] = Main.CARRY_POISON;
    opp[SCORE] = 0;
    return new GameState(newMaze,player,opp);

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
    return newMaze[opponent[POSITION_X]][opponent[POSITION_Y]] == Main.PILL_SYMBOL;
  }

  public GameState updateFromInput(char[][] newMaze) {
    //get opponent move
    Point oppPos = getCurrentPosition(newMaze, Main.OPPONENT_SYMBOL);
    Move oppMove = new Move(Main.OPPONENT_SYMBOL,oppPos,detectPoisonDropped(newMaze));
    System.err.println("Opponent (" + Main.OPPONENT_SYMBOL + ") moved to " + oppPos.x + "," + oppPos.y + " from " + opponent[POSITION_X] + "," + opponent[POSITION_Y]);
    //make move on current game state
    GameState newState = makeMove(newMaze,oppMove,false);
    //harness may have teleported us
    Point myPos = getCurrentPosition(newMaze, Main.PLAYER_SYMBOL);
    if (player[POSITION_X] != myPos.x || player[POSITION_Y] != myPos.y) {
      System.err.println("Player (" + Main.PLAYER_SYMBOL + ") has been teleported. Updating position");
      newState.player[POSITION_X] = myPos.x;
      newState.player[POSITION_Y] = myPos.y;
    }

    //detect poison consumed
    //this part breaks the rules about GameState being immutable.
    //Poison values are changed on the newState
    if (!(player[POISON_X] == Main.CARRY_POISON || player[POISON_X] == Main.CONSUMED_POISON)) // if dropped and not yet consumed
      if (newMaze[player[POISON_X]][player[POISON_Y]] != Main.PILL_SYMBOL) {                   // if pill missing, set to consumed
        newState.player[POISON_X] = newState.player[POISON_Y] = Main.CONSUMED_POISON;
        System.err.println("A poison pill at " + player[POISON_X] + "," + player[POISON_Y] + " has been consumed");
      }
    if (!(opponent[POISON_X] == Main.CARRY_POISON || opponent[POISON_X] == Main.CONSUMED_POISON))
      if (newMaze[opponent[POISON_X]][opponent[POISON_Y]] != Main.PILL_SYMBOL) {
        newState.opponent[POISON_X] = newState.opponent[POISON_Y] = Main.CONSUMED_POISON;
        System.err.println("A poison pill at " + opponent[POISON_X] + "," + opponent[POISON_Y] + " has been consumed");
      }
    return newState;
  }

  public boolean isEndState() {
    return (219 - player[SCORE] - opponent[SCORE]) == 0;
  }
}
