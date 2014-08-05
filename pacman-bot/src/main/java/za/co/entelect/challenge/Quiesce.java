package za.co.entelect.challenge;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by marais on 2014/07/21.
 */
public class Quiesce implements Evaluator, Serializable {
  List<Move> principalVariation;

  public Quiesce(List<Move> principalVariation) { this.principalVariation = principalVariation; }

  @Override
  public int evaluate(GameState state) {
    int score = (state.player[GameState.SCORE] - state.opponent[GameState.SCORE]) << 8;
//    if (state.player[GameState.POSITION_X] != state.player[GameState.PREVIOUS_X] ||
//        state.player[GameState.POSITION_Y] != state.player[GameState.PREVIOUS_Y] )
//      score += 1; //Bias any move that is not going to the
    return score;
  }

  @Override
  public List<Move> orderMoves(List<Move> moves, final GameState s) {
    Collections.sort(moves,new Comparator<Move>() {
      @Override
      public int compare(Move o1, Move o2) {
        //if move is in the pv, then it gets priority

        return evaluate(s.makeMove(o2,true)) - evaluate(s.makeMove(o1,true));
      }
    });
    return moves;
  }
}