package za.co.entelect.challenge;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marais on 2014/07/23.
 */
public class NegamaxAB implements Strategy {
  private static long nodesEvaluated;
  public long getNodesEvaluated() {return nodesEvaluated;};

  private Evaluator eval;
  private int searchDepth = 16;
  private Move pvMove = null;

  @Inject
  public NegamaxAB(Evaluator evaluator){
    this.eval = evaluator;
  }

  public NegamaxAB(Evaluator evaluator, int searchDepth){
    this.eval = evaluator;
    this.searchDepth = searchDepth;
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
    if (depth == 0 || s.isEndState()) {     //TODO check for end state?
      nodesEvaluated++;
      return new Move(lastMove.moverSymbol, lastMove.to,lastMove.dropPoison, colour * eval.evaluate(s));
    }
    char moverSymbol = colour > 0 ? Main.PLAYER_SYMBOL : Main.OPPONENT_SYMBOL;
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;
    List<Move> moves = s.determineAllBasicMoves(moverSymbol);
    moves = eval.orderMoves(moves, s);
    for (Move m : moves) {
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
    System.err.println("Running strategy NegamaxAB with " + searchDepth + " for player " + Main.PLAYER_SYMBOL);
    pvMove = getMove(s,null,searchDepth,-2147483640,2147483640,1);
    //dont use Integer.MIN_VALUE and MAX_VALUE, when you negate it strange things happen. -(-2147483648) != 2147483648
    System.err.println("Done - evaluated " + getNodesEvaluated() + " nodes.");
    return pvMove;
  }

  public void setSearchDepth(int depth)
  {
    this.searchDepth = depth;
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
