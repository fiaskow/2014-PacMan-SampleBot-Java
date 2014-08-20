package za.co.entelect.challenge;

import org.junit.Test;

import java.util.ArrayList;

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
    NegamaxAB strategy = new NegamaxAB(new Quiesce3(), 30);
    Main.endtime = System.currentTimeMillis() + 3200;
    Move pvm = strategy.getMoveIterativeDeepening(copy);
    Main.printMaze(state, strategy.getPrincipalVariation(), System.err);
    for (Move m : strategy.getPrincipalVariation()) {
      state.makeMove2(m, true);
    }
    Main.printMaze(state, new ArrayList<Move>(), System.err);
    //Quiesce2 q = new Quiesce2(new ArrayList<Move>());
    //q.evaluateDebug(state);
    System.err.println("Player A score: " + state.player[GameState.SCORE]);
    System.err.println("Player B score: " + state.opponent[GameState.SCORE]);
  }

  @Test
  public void testScenario2() {
    char[][] oldMaze = new char[][]{
        "###################".toCharArray(),
        "#........#........#".toCharArray(),
        "#*##.###.#.###.##*#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#.................#".toCharArray(),
        "#.##.#.#####.#.##.#".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "####.###.#.###.####".toCharArray(),
        "####.#.......#.####".toCharArray(),
        "####.#.## ##.#.####".toCharArray(),
        "   A...#   #...B   ".toCharArray(),
        "####.#.## ##.#.####".toCharArray(),
        "####.#.......#.####".toCharArray(),
        "####.#.#####.#.####".toCharArray(),
        "#........#........#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    GameState state = GameState.initGameState(oldMaze);
    GameState copy = state.clone();
    NegamaxAB strategy = new NegamaxAB(new Quiesce3(), 30);
    Main.endtime = Long.MAX_VALUE;
    Move pvm = strategy.getMove(copy);
    Main.printMaze(state, strategy.getPrincipalVariation(), System.err);
  }

  @Test
  public void testScenario3() {
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
        "#  #A.         #  #".toCharArray(),
        "## # # ##### # # ##".toCharArray(),
        "#    #   #   #    #".toCharArray(),
        "# ###### #B###### #".toCharArray(),
        "#                 #".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{16, 4, 15, 4, Main.DROPPED_POISON, 109};
    int[] oppone = new int[]{19, 10, 20, 10, Main.DROPPED_POISON, 109};
    GameState state = new GameState(oldMaze, player, oppone);
    GameState copy = state.clone();
    NegamaxAB strategy = new NegamaxAB(new Quiesce3(), 30);
    Main.endtime = Long.MAX_VALUE;
    Move pvm = strategy.getMove(copy);
    Main.printMaze(state, strategy.getPrincipalVariation(), System.err);
  }
}
