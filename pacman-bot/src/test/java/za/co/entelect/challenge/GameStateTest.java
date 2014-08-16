package za.co.entelect.challenge;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by marais on 2014/08/16.
 */
public class GameStateTest {

  @Test
  public void isEndStateTest() {
    GameState s;
    //Searching for a win
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,110}, new int[] {1,1,1,1,1,109});
    assertTrue(s.isEndState(true));
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,110}, new int[] {1,1,1,1,1,108});
    assertTrue(s.isEndState(true));
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,108}, new int[] {1,1,1,1,1,110});
    assertTrue(s.isEndState(true));
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,109}, new int[] {1,1,1,1,1,108});
    assertFalse(s.isEndState(true));
    //Dont care about win, just ea all the pills
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,110}, new int[] {1,1,1,1,1,109});
    assertTrue(s.isEndState(false));
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,110}, new int[] {1,1,1,1,1,109});
    assertTrue(s.isEndState(false));
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,110}, new int[] {1,1,1,1,1,108});
    assertFalse(s.isEndState(false));
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,108}, new int[] {1,1,1,1,1,110});
    assertFalse(s.isEndState(false));
    s = new GameState(new char[1][1],new int[] {1,1,1,1,1,112}, new int[] {1,1,1,1,1,107});
    assertTrue(s.isEndState(false));

  }
}
