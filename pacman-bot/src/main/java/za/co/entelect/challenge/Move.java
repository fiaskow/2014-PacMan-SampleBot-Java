package za.co.entelect.challenge;

/**
 * Created by marais on 2014/07/21.
 */

import java.awt.*;
import java.io.Serializable;

/**
 * This encapsulates a move instruction with some additional fields to be used in searching algorithms
 */
public class Move implements Serializable {
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

  /**
   * Checks for equality but ignores the value of score.
   * @param obj to check for equality
   * @return true if equals (ignoring score)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Move) {
      Move r = (Move) obj;
      return moverSymbol == r.moverSymbol && dropPoison == r.dropPoison && this.to.equals(r.to);
    }
    return super.equals(obj);
  }
}
