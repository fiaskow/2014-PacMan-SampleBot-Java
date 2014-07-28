package za.co.entelect.challenge;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by marais on 2014/07/21.
 */
public class CounterEval implements Evaluator {

  @Override
  public int evaluate(GameState state) {
    return (state.player[GameState.SCORE] - state.opponent[GameState.SCORE])+(state.player[GameState.SCORE] + state.opponent[GameState.SCORE])*10;
  }

  @Override
  public List<Move> orderMoves(List<Move> moves, final GameState s) {
    Collections.sort(moves,new Comparator<Move>() {
      @Override
      public int compare(Move o1, Move o2) {
        return evaluate(s.makeMove(o1,true)) - evaluate(s.makeMove(o2,true));
      }
    });
    return moves;
  }
}
