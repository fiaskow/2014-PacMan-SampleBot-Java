package za.co.entelect.challenge;

import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marais on 2014/08/20.
 */
public class QuiesceTest {

  @Test
  public void testScenario1() {
    char[][] oldMaze = new char[][]{
        "###################".toCharArray(),
        "#........#....    #".toCharArray(),
        "#*##.###.#.### ## #".toCharArray(),
        "#.##.###.#.###B## #".toCharArray(),
        "#...A  .....      #".toCharArray(),
        "#.##.# ##### #.##.#".toCharArray(),
        "#....#   #   #....#".toCharArray(),
        "####.### # ###.####".toCharArray(),
        "####.#..   ..#.####".toCharArray(),
        "####.#.## ##.#.####".toCharArray(),
        "     ..#   #..     ".toCharArray(),
        "#### #.## ##.# ####".toCharArray(),
        "#### #.......# ####".toCharArray(),
        "#### #.#####.# ####".toCharArray(),
        "#    ....#....    #".toCharArray(),
        "# ##.###.#.###.## #".toCharArray(),
        "# .#...........#  #".toCharArray(),
        "##.#.#.#####.#.# ##".toCharArray(),
        "#....#...#...#..  #".toCharArray(),
        "#.######.#.###### #".toCharArray(),
        "#...........      #".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{4, 4, 4, 5, Main.DROPPED_POISON, 39};
    int[] oppone = new int[]{3, 14, 2, 14, Main.DROPPED_POISON, 49};
    GameState state = new GameState(oldMaze, player, oppone);
    System.err.println("Player A score: " + state.player[GameState.SCORE]);
    System.err.println("Player B score: " + state.opponent[GameState.SCORE]);
    GameState copy = state.clone();
    NegamaxAB strategy = new NegamaxAB(new Quiesce2(new ArrayList<Move>()), 30);
    Main.endtime = System.currentTimeMillis() + 3200;
    Move pvm = strategy.getMoveIterativeDeepening(copy);
    Main.printMaze(state, strategy.getPrincipalVariation(), System.err);
    for (Move m : strategy.getPrincipalVariation()) {
      state.makeMove2(m, true);
    }
    Main.printMaze(state,new ArrayList<Move>(),System.err);
    Quiesce2 q = new Quiesce2(new ArrayList<Move>());
    q.evaluateDebug(state);
    System.err.println("Player A score: " + state.player[GameState.SCORE]);
    System.err.println("Player B score: " + state.opponent[GameState.SCORE]);
  }


}
