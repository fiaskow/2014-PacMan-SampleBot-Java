package za.co.entelect.challenge;

import org.junit.Test;

import java.awt.*;

/**
 * Created by marais on 2014/07/27.
 */
public class NegamaxABTest {

  @Test
  public void testScenario1 () {
    char[][] maze = new char[][] {
        "###################".toCharArray(),
        "#     ...#........#".toCharArray(),
        "# ## ###.#.###.##*#".toCharArray(),
        "#### ###.#.###.##.#".toCharArray(),
        "#A .#         ....#".toCharArray(),
        "#.# ## #####.#.##.#".toCharArray(),
        "#.B ##   #...#....#".toCharArray(),
        "#### ###.#.###.####".toCharArray(),
        "#### #.......#.####".toCharArray(),
        "#### #.## ##.#.####".toCharArray(),
        "       #   #       ".toCharArray(),
        "####.# ## ## #.####".toCharArray(),
        "####.#   ..  #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#..  ....#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    Point a = GameState.getCurrentPosition(maze,'A');
    Point b = GameState.getCurrentPosition(maze,'B');
    int[] oppone = new int[]{a.x,a.y,Main.CARRY_POISON,Main.CARRY_POISON,0};
    int[] player = new int[]{b.x,b.y,Main.CARRY_POISON,Main.CARRY_POISON,0};
    Main.PLAYER_SYMBOL = 'B';
    Main.OPPONENT_SYMBOL = 'A';
    GameState s = new GameState(maze,player,oppone);
    Player pa = new Player(new NegamaxAB(new SimpleEval(),8));
    GameState n = pa.makeMove(s,false);
    System.out.println(pa.getStrategy().getPrincipalVariation());
  }

  @Test
  public void testScenario2 () {
    char[][] maze = new char[][] {
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
        "       #   #       ".toCharArray(),
        "####.# ## ## #.####".toCharArray(),
        "####.#    .A #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#..  B...#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    Point a = GameState.getCurrentPosition(maze,'A');
    Point b = GameState.getCurrentPosition(maze,'B');
    int[] oppone = new int[]{a.x,a.y,12,10,9}; //A dropped poison on 12,10
    int[] player = new int[]{b.x,b.y,Main.CARRY_POISON,Main.CARRY_POISON,9};
    Main.PLAYER_SYMBOL = 'B';
    Main.OPPONENT_SYMBOL = 'A';
    GameState s = new GameState(maze,player,oppone);
    Player pa = new Player(new NegamaxAB(new SimpleEval(),16));
    GameState n = pa.makeMove(s,false);
    System.out.println(pa.getStrategy().getPrincipalVariation());
  }
}