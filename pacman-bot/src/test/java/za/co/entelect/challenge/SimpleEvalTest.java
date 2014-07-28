package za.co.entelect.challenge;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marais on 2014/07/27.
 */
public class SimpleEvalTest {
  @Test
  public void TestOrderMoves() {
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
    GameState s = new GameState(maze, player, oppone);
    Main.PLAYER_SYMBOL = 'B';
    Main.OPPONENT_SYMBOL = 'A';
    SimpleEval e = new SimpleEval();
    ArrayList<Move> moves = new ArrayList<Move>();
    Move move1 = new Move(Main.PLAYER_SYMBOL,new Point(14,13),false);
    Move move2 = new Move(Main.PLAYER_SYMBOL,new Point(14,15),false);
    moves.add(move1);
    moves.add(move2);
    List<Move> ordered = e.orderMoves(moves,s);
    assertSame(move2,ordered.get(0));
    assertSame(move1,ordered.get(1));
  }
}
