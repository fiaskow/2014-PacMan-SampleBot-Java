package za.co.entelect.challenge;

import java.io.Serializable;
import java.util.List;

/**
 * Created by marais on 2014/07/23.
 */
public class Player implements Serializable {

  private final Strategy strategy;
  private List<Move> pv;
  private boolean iterative = false;

  public Player(Strategy strategy) {
    this.strategy = strategy;
  }

  public Player(Strategy strategy, boolean iterative) {
    this.strategy = strategy;
    this.iterative = iterative;
  }

  /**
   * Makes a move on the passed in game state and returns the new game state
   */
  public GameState makeMove(final GameState s, final boolean performTeleport)
  {
    GameState copy = s.clone();
    //Get Move is destructive on the GameState, so only do it on a copy.
    Move m = iterative ? strategy.getMoveIterativeDeepening(copy) : strategy.getMove(copy);
    pv = strategy.getPrincipalVariation();
    return s.makeMove(m,performTeleport);
  }

  public Strategy getStrategy() {
    return strategy;
  }

  public List<Move> getPrincipalVariation() {
    return pv;
  }
}
