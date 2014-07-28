package za.co.entelect.challenge;

import javax.inject.Inject;

/**
 * Created by marais on 2014/07/23.
 */
public class Player {

  private final Strategy strategy;

  @Inject
  public Player(Strategy strategy) {
    this.strategy = strategy;
  }

  /**
   * Makes a move on the passed in game state and returns the new game state
   */
  public GameState makeMove(final GameState s, final boolean performTeleport)
  {
    Move m = strategy.getMove(s);
    return s.makeMove(m,performTeleport);
  }

  public Strategy getStrategy() {
    return strategy;
  }
}
