package za.co.entelect.challenge;

import javax.swing.undo.StateEdit;
import java.awt.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by marais on 2014/07/21.
 */
public class Quiesce2 implements Evaluator, Serializable {
  List<Move> principalVariation;

  final Point[] bonuspills = new Point[] {new Point(2,1),new Point(2,17),new Point(16,1),new Point(16,17)};
  //                                X   Y
  final int bp1index = Main.WIDTH * 2 + 1;
  final int bp2index = Main.WIDTH * 2 + 17;
  final int bp3index = Main.WIDTH * 16 + 1;
  final int bp4index = Main.WIDTH * 16 + 17;

  public Quiesce2(List<Move> principalVariation) { this.principalVariation = principalVariation; }

  @Override
  public List<Move> orderMoves(List<Move> moves, final GameState s) {
    return moves;
  }


  @Override
  public int evaluate(GameState state, Move lastMove) {
    boolean moreBonus = true;
    int score = 0;
    score += state.player[GameState.SCORE] - state.opponent[GameState.SCORE];
    score = score << 8;
    //if bonus pills left, attract towards them.
//    Point opponent = new Point(state.opponent[GameState.POSITION_X], state.opponent[GameState.POSITION_Y]);
//    int pindex1 = Main.WIDTH * state.player[GameState.POSITION_X] + state.player[GameState.POSITION_Y];
//    int oindex1 = Main.WIDTH * state.opponent[GameState.POSITION_X] + state.opponent[GameState.POSITION_Y];
    if (moreBonus) {
      Point player = new Point(state.player[GameState.POSITION_X], state.player[GameState.POSITION_Y]);
      moreBonus = false;
      for (Point p : bonuspills) {
        if (state.maze[p.x][p.y] == Main.BONUS_SYMBOL) {
          moreBonus = true;
          score += Math.max(9 - ShortestPaths.shortestDistance(player, p), 0);
        }
      }
    }
    if (!moreBonus) {
      int distance = 0;
      int pills = 0;
      int pindex1 = Main.WIDTH * state.player[GameState.POSITION_X] + state.player[GameState.POSITION_Y];
      for (int i = 1; i < Main.HEIGHT - 1; i++) {
        for (int j = 1; j < Main.WIDTH - 1; j++) {
          if (state.maze[i][j] == Main.PILL_SYMBOL) {
            int index2 = Main.WIDTH * i + j;
            pills++;
            distance += ShortestPaths.shortestDistance(pindex1, index2);
          }
        }
      }
      if (pills > 0)
        score -= distance * 2 / pills;
      //score += Math.max(2048 - distance,0);
    }


    //this is a win for us!
    if (state.player[GameState.SCORE] >= 110)
      if (state.opponent[GameState.SCORE] < 110)
        score += 100000;
    return score;
  }



  public int evaluateDebug(GameState state) {
    boolean moreBonus = true;
    int score = 0;
    score += state.player[GameState.SCORE] - state.opponent[GameState.SCORE];
    score = score << 8;
    //if bonus pills left, attract towards them.
//    Point opponent = new Point(state.opponent[GameState.POSITION_X], state.opponent[GameState.POSITION_Y]);
//    int pindex1 = Main.WIDTH * state.player[GameState.POSITION_X] + state.player[GameState.POSITION_Y];
//    int oindex1 = Main.WIDTH * state.opponent[GameState.POSITION_X] + state.opponent[GameState.POSITION_Y];
    if (moreBonus) {
      Point player = new Point(state.player[GameState.POSITION_X], state.player[GameState.POSITION_Y]);
      moreBonus = false;
      for (Point p : bonuspills) {
        if (state.maze[p.x][p.y] == Main.BONUS_SYMBOL) {
          moreBonus = true;
          score += Math.max(9 - ShortestPaths.shortestDistance(player, p), 0);
        }
      }
    }
    if (!moreBonus) {
      int distance = 0;
      int pills = 0;
      for (int i = 1; i < Main.HEIGHT - 1; i++) {
        for (int j = 1; j < Main.WIDTH - 1; j++) {
          if (state.maze[i][j] == Main.PILL_SYMBOL) {
            int pindex1 = Main.WIDTH * state.player[GameState.POSITION_X] + state.player[GameState.POSITION_Y];
            int index2 = Main.WIDTH * i + j;
            pills++;
            distance += ShortestPaths.shortestDistance(pindex1, index2);
          }
        }
      }
      System.err.println("Heuristic before avg distance: " + score);
      System.err.println("Total distance: " + distance);
      System.err.println("Total pills left: " + pills);
      System.err.println("Avg distance: " + (distance * 2 / pills));
      if (pills > 0)
        score -= distance * 2 / pills;
      System.err.println("Heuristic after avg distance: " + score);

      //score += Math.max(2048 - distance,0);
    }


    //this is a win for us!
    if (state.player[GameState.SCORE] >= 110)
      if (state.opponent[GameState.SCORE] < 110)
        score += 100000;
    return score;
  }
}
