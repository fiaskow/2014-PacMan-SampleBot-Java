package za.co.entelect.challenge;

import java.util.List;

/**
 * Created by marais on 2014/07/05.
 */
public interface Evaluator {

  int evaluate(GameState state, Move lastMove);

  List<Move> orderMoves(List<Move> moves, GameState s);
}
