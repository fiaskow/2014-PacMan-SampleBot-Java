package za.co.entelect.challenge;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * Created by marais on 2014/07/23.
 */
public class NegamaxAB implements Strategy, Serializable {
  private static long nodesEvaluated;
  public long getNodesEvaluated() {return nodesEvaluated;};

  private Evaluator eval;
  private int searchDepth;
  private int currDepth;
  private Move pvMove = null;
  private boolean stillSearching = true;
  private Deque<int[]> us;

  public NegamaxAB(Evaluator evaluator, int searchDepth){
    this.eval = evaluator;
    this.searchDepth = searchDepth;
    this.currDepth = searchDepth;
  }

  /**
   * Determines the best move for player starting form a node
   * where the opponent finished a move.
   * @param s current state
   * @param lastMove move made to get to the current state
   * @param depth ply depth to search
   * @param colour who is moving 1 = player, -1 = opponent
   * @return best move for colour
   */
  public Move getMove(GameState s, Move lastMove, int depth, int A, int B, int colour) {
    if (depth == 0 || s.isEndState(stillSearching)) {
      if (System.currentTimeMillis() > Main.endtime) throw new TimeoutException();
      nodesEvaluated++;
      return new Move(lastMove.moverSymbol, lastMove.to,lastMove.dropPoison, colour * eval.evaluate(s));
    }
    char moverSymbol = colour > 0 ? Main.PLAYER_SYMBOL : Main.OPPONENT_SYMBOL;
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;

    boolean excludeHistory = excludeStepBack(s, depth, lastMove);

    List<Move> moves = s.determineAllBasicMoves(moverSymbol,excludeHistory);

    //assert moves.size() > 0 : "There are no moves???! Last move: " + lastMove + " Game state: " + s.toString();// + Main.printMaze(s, null, System.err);
    //moves = eval.orderMoves(moves, s);
    for (Move m : moves) {
      m.previous = lastMove; //build a chain forward
      us.push(s.makeMove2(m,true));
      Move nextMove = getMove(s, m, depth - 1, -B, -A, -colour);
      s.unmakeMove(us.pop());
      m.score = -nextMove.score;

      if (m.score >= B) {
        if (lastMove != null) lastMove.next = m;
        return m;
      }
      if (m.score > bestScore) {
        bestScore = m.score;
        bestMove = m;
        if ( m.score > A )
          A = m.score;
      }
    }
    if (lastMove != null) lastMove.next = bestMove; //build a chain of PV moves.
    return bestMove;
  }

  private boolean excludeStepBack(final GameState s, final int depth, final Move lastMove) {
    if (depth == currDepth || depth == currDepth-1)
      {return false;} //first and second (opponent's) move can consider backwards move.
    if (lastMove != null
        && lastMove.previous != null
        && lastMove.previous.dropPoison)
      {return false;}
    byte dist = ShortestPaths.shortestDistance(
        Main.WIDTH * s.player[GameState.POSITION_X] + s.player[GameState.POSITION_Y],
        Main.WIDTH * s.opponent[GameState.POSITION_X] + s.opponent[GameState.POSITION_Y]);
    if (dist < 2 && dist > 0)
      {return false;}
    return true;
//    int[] mover = null;
//    //speedup System.err.println("Calculating possible moves for " + moverChar);
//    if (moverChar == Main.PLAYER_SYMBOL) {
//      mover = s.player;
//    }
//    else if (moverChar == Main.OPPONENT_SYMBOL) {
//      mover = s.opponent;
//    }
//    boolean pill = false;
//    char ref;
//    //Move down
//    if (mover[GameState.POSITION_X] + 1 < Main.HEIGHT) {
//      ref = s.maze[mover[GameState.POSITION_X] + 1][mover[GameState.POSITION_Y]];
//      if (ref == Main.POISON_SYMBOL) return false;
//      if (ref == Main.PILL_SYMBOL || ref == Main.BONUS_SYMBOL) pill = true;
//    }
//
//    //Move up
//    if (mover[GameState.POSITION_X] - 1 >= 0) {
//      ref = s.maze[mover[GameState.POSITION_X] - 1][mover[GameState.POSITION_Y]];
//      if (ref == Main.POISON_SYMBOL) return false;
//      if (ref == Main.PILL_SYMBOL || ref == Main.BONUS_SYMBOL) pill = true;
//    }
//
//    //Move Right
//    if (mover[GameState.POSITION_Y] + 1 < Main.WIDTH) {
//      ref = s.maze[mover[GameState.POSITION_X]][mover[GameState.POSITION_Y] + 1];
//      if (ref == Main.POISON_SYMBOL) return false;
//      if (ref == Main.PILL_SYMBOL || ref == Main.BONUS_SYMBOL) pill = true;
//    }
//
//    //Move Left
//    if (mover[GameState.POSITION_Y] - 1 >= 0) {
//      ref = s.maze[mover[GameState.POSITION_X]][mover[GameState.POSITION_Y] - 1];
//      if (ref == Main.POISON_SYMBOL) return false;
//      if (ref == Main.PILL_SYMBOL || ref == Main.BONUS_SYMBOL) pill = true;
//    }
//    return pill;
  }

  @Override
  public Move getMove(GameState s) {
    nodesEvaluated = 0;
    us = new ArrayDeque<int[]>(searchDepth);
    //dont use Integer.MIN_VALUE and MAX_VALUE, when you negate it strange things happen. -(-2147483648) != 2147483648
    pvMove = getMove(s, null, searchDepth, -2147483640, 2147483640, 1);
    System.err.println("NegamaxAB evaluated " + nodesEvaluated + " nodes at depth " + searchDepth + " for player " + Main.PLAYER_SYMBOL);
    return pvMove;
  }

  /**
   * Deepen the depth that the search wil start at while there is still time left. This method is destructive on the GameState
   * @param s GameState to start from, will be modified, so doen't rely on it.
   * @return The Principal Variation move.
   */
  public Move getMoveIterativeDeepening(GameState s) {
    //Switch to a different end state evaluation after a bot has won.
    if (stillSearching && (s.player[GameState.SCORE] >= 110 || s.opponent[GameState.SCORE] >= 110))
      stillSearching = false;
    nodesEvaluated = 0;
    currDepth = searchDepth;
    //default move if we time out is a random one.
    pvMove = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,true).get(0);

    try {
      //Iterative deepening
      while (true) {
        us = new ArrayDeque<int[]>(currDepth);
        //dont use Integer.MIN_VALUE and MAX_VALUE, when you negate it strange things happen. -(-2147483648) != 2147483648
        pvMove = getMove(s, null, currDepth, -2147483640, 2147483640, 1);
        //System.err.println("NegamaxAB evaluated " + nodesEvaluated + " nodes at depth " + currDepth + " for player " + Main.PLAYER_SYMBOL);
        currDepth++;
      }
    } catch (TimeoutException e) {
      System.err.println("NegamaxAB depth " + (currDepth - 1) + ", Score " + pvMove.score);
      //We didn't get to finish the iteration, so pvMove is still set to the best move
      //from the previous deepening.
    }
    System.err.println("Done - evaluated " + getNodesEvaluated() + " nodes.");
    return pvMove;
  }

  @Override
  public List<Move> getPrincipalVariation() {
    ArrayList<Move> pv = new ArrayList<Move>();
    if (pvMove == null)
      return pv;
    Move currMove = pvMove;
    pv.add(currMove);
    while (currMove.next != null) {
      currMove = currMove.next;
      pv.add(currMove);
    }
    return pv;
  }
}
