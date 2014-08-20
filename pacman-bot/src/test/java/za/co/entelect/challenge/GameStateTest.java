package za.co.entelect.challenge;

import org.junit.Test;

import java.awt.*;

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

  @Test
  public void stepBackTest() {
    char[][] oldMaze = new char[][]{
        "###################".toCharArray(),
        "#        #        #".toCharArray(),
        "# ## ### # ### ## #".toCharArray(),
        "# ## ### # ### ## #".toCharArray(),
        "#                 #".toCharArray(),
        "# ## # ##### # ## #".toCharArray(),
        "#    #   #   #    #".toCharArray(),
        "#### ### # ### ####".toCharArray(),
        "#### #       # ####".toCharArray(),
        "#### # ## ## # ####".toCharArray(),
        "       #   #       ".toCharArray(),
        "#### # ## ## # ####".toCharArray(),
        "#### #       # ####".toCharArray(),
        "#### # ##### # ####".toCharArray(),
        "#        #        #".toCharArray(),
        "# ## ### # ### ## #".toCharArray(),
        "#  #A  .       #  #".toCharArray(),
        "## #.# ##### # # ##".toCharArray(),
        "#    #   #   #    #".toCharArray(),
        "# ###### #B###### #".toCharArray(),
        "#                 #".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{16, 4, 15, 4, Main.DROPPED_POISON, 109};
    int[] oppone = new int[]{19, 10, 20, 10, Main.DROPPED_POISON, 109};
    GameState state = new GameState(oldMaze, player, oppone);
    NegamaxAB negamax = new NegamaxAB(new Quiesce3(),30);
    Move a = new Move('A',new Point(16,4),false,1);
    Move b = new Move('B',new Point(19,10),false);
    b.previous = a;
    assertTrue(negamax.excludeStepBack(state,10,b));
  }
}
