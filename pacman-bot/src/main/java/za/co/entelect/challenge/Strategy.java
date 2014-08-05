package za.co.entelect.challenge;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marais on 2014/07/23.
 */
public interface Strategy {
  Move getMove(GameState s);
  Move getMoveIterativeDeepening(GameState s);
  long getNodesEvaluated();

  List<Move> getPrincipalVariation();
}
