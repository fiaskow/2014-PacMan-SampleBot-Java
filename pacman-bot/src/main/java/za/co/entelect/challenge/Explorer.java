package za.co.entelect.challenge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marais on 2014/07/28.
 */
public class Explorer implements Strategy {
  private static long nodesEvaluated;
  private int searchDepth = 26;
  private Move pvMove = null;
  private int[][] playerReachability = new int[Main.HEIGHT][Main.WIDTH];

  private Move getMove(GameState s, Move lastMove) {
    List<Move> moves = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,true);
    return moves.get(0);
  }

  @Override
  public Move getMove(GameState s) {
    nodesEvaluated = 0;
    System.err.println("Running strategy Explorer with look ahead depth of " + searchDepth + " nodes for player " + Main.PLAYER_SYMBOL);
    pvMove = getMove(s,null);
    System.err.println("Done - evaluated " + getNodesEvaluated() + " nodes.");
    return pvMove;
  }

  @Override
  public Move getMoveIterativeDeepening(GameState s) {
    return getMove(s);
  }

  @Override
  public long getNodesEvaluated() {
    return nodesEvaluated;
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
