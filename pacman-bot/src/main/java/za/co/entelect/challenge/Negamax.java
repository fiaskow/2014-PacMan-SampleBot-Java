package za.co.entelect.challenge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marais on 2014/07/21.
 */
public class Negamax implements Strategy, Serializable {

  private final Evaluator eval;
  private int searchDepth = 12;
  private static long nodesEvaluated;
  private Move pvMove = null;

  public Negamax(Evaluator evaluator){
    this.eval = evaluator;
  }

  public Negamax(Evaluator evaluator, int searchDepth) {
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
  public Move getMove(GameState s, Move lastMove, int depth, int colour) {
    if (depth == 0) {     //TODO check for end state?
      nodesEvaluated++;
      return new Move(lastMove.moverSymbol, lastMove.to,lastMove.dropPoison, colour * eval.evaluate(s, lastMove));
    }
    char moverSymbol = colour > 0 ? Main.PLAYER_SYMBOL : Main.OPPONENT_SYMBOL;
    int bestScore = Integer.MIN_VALUE;
    Move bestMove = null;
    List<Move> moves = s.determineAllBasicMoves(moverSymbol,false);
    for (Move m : moves) {
      m.score = -getMove(s.makeMove(m, true), m, depth - 1, -colour).score;
      if (m.score > bestScore) {
        bestScore = m.score;
        bestMove = m;
      }
    }
    if (lastMove != null) lastMove.next = bestMove;
    return bestMove;
  }

  public long getNodesEvaluated() {return nodesEvaluated;};

  @Override
  public Move getMove(GameState s) {
    System.err.println("Running strategy Negamax with search depth " + searchDepth + " for player " + Main.PLAYER_SYMBOL);
    pvMove = getMove(s,null,searchDepth,1);
    System.err.println("Done - evaluated " + getNodesEvaluated() + " nodes.");
    return pvMove;
  }

  @Override
  public Move getMoveIterativeDeepening(GameState s) {
    nodesEvaluated = 0;
    try {
      //Iterative deepening
      while (true) {
        pvMove = getMove(s, null, searchDepth, 1);
        System.err.println("NegamaxAB evaluated " + nodesEvaluated + " nodes at depth " + searchDepth + " for player " + Main.PLAYER_SYMBOL);
        searchDepth++;
      }
    } catch (TimeoutException e) {
      System.err.println("Your calculation time is up. Stopping NegamaxAB");
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
