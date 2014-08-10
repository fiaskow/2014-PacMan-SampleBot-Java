package za.co.entelect.challenge;

import java.io.Serializable;
import java.util.ArrayList;
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
    if (depth == 0 || s.isEndState()) {
      if (System.currentTimeMillis() > Main.endtime) throw new TimeoutException();
      nodesEvaluated++;
//      assert lastMove != null : "lastMove is null!";
//      assert eval != null : "eval is null!";
      return new Move(lastMove.moverSymbol, lastMove.to,lastMove.dropPoison, colour * eval.evaluate(s));
    }
    char moverSymbol = colour > 0 ? Main.PLAYER_SYMBOL : Main.OPPONENT_SYMBOL;
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;
    List<Move> moves = s.determineAllBasicMoves(moverSymbol,false); //TODO, change back to false to include back-up moves
    assert moves.size() > 0 : "There are no moves???! Last move: " + lastMove + " Game state: " + s.toString();// + Main.printMaze(s, null, System.err);
    moves = eval.orderMoves(moves, s);
    for (Move m : moves) {
      assert m != null : "move is null, last move: " + lastMove;
      assert s != null : "game state is null";
      m.score = -getMove(s.makeMove(m, true), m, depth - 1, -B, -A, -colour).score;
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
    if (lastMove != null) lastMove.next = bestMove;
    return bestMove;
  }

  @Override
  public Move getMove(GameState s) {
    nodesEvaluated = 0;
    //dont use Integer.MIN_VALUE and MAX_VALUE, when you negate it strange things happen. -(-2147483648) != 2147483648
    pvMove = getMove(s, null, searchDepth, -2147483640, 2147483640, 1);
    System.err.println("NegamaxAB evaluated " + nodesEvaluated + " nodes at depth " + searchDepth + " for player " + Main.PLAYER_SYMBOL);
    return pvMove;
  }

  public Move getMoveIterativeDeepening(GameState s) {
    nodesEvaluated = 0;
    currDepth = searchDepth;
    try {
      //Iterative deepening
      while (true) {
        //dont use Integer.MIN_VALUE and MAX_VALUE, when you negate it strange things happen. -(-2147483648) != 2147483648
        pvMove = getMove(s, null, currDepth, -2147483640, 2147483640, 1);
        System.err.println("NegamaxAB evaluated " + nodesEvaluated + " nodes at depth " + currDepth + " for player " + Main.PLAYER_SYMBOL);
        currDepth++;
      }
    } catch (TimeoutException e) {
      System.err.println("Your calculation time is up. Stopping NegamaxAB at depth " + (currDepth - 1));
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
