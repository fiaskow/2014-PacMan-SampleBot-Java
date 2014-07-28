package za.co.entelect.challenge;

/**
 * Created by marais on 2014/07/21.
 */

import java.awt.*;

/**
 * This encapsulates a move instruction with some additional fields to be used in searching algorithms
 */
public class Move {
  public final char moverSymbol;
  public final Point to;
  public final boolean dropPoison;
  public int score;
  public Move next;

  public Move(char moverSymbol, Point to, boolean dropPoison) {
    this.moverSymbol = moverSymbol;
    this.to = to;
    this.dropPoison = dropPoison;
    this.score = 0;
    this.next = null;
  }

  public Move(char moverSymbol, Point to, boolean dropPoison, int score) {
    this.moverSymbol = moverSymbol;
    this.to = to;
    this.dropPoison = dropPoison;
    this.score = score;
  }

  public void setScore(int newScore) {
    this.score = newScore;
  }

  @Override
  public String toString() {
    return moverSymbol + ":" + to.x + "," + to.y + (dropPoison ? "!" : "") + "(" + score + ")";
  }
}
