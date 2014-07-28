package za.co.entelect.challenge;


import org.junit.Before;
import org.junit.Test;

import javax.swing.text.Position;

import static org.junit.Assert.*;

import java.awt.*;

/**
 * Created by marais on 2014/07/25.
 */
public class MoveTest {


  @Before
  public void setupGS() {

  }

  @Test
  public void makeMoveTest1() {
    char[][] maze = new char[][]{
        {'#','#','#','#','#'},
        {'#','A','.',' ','#'},
        {'#',' ','*','B','#'},
        {'#','#','#','#','#'}
    };
    int[] player = new int[]{1,1,Main.CARRY_POISON,Main.CARRY_POISON,0};
    int[] oppone = new int[]{2,3,Main.CARRY_POISON,Main.CARRY_POISON,0};
    //Move A to 1,2
    //Do not drop poison
    //Do no teleport
    GameState s = new GameState(maze,player,oppone);
    GameState n = s.makeMove(maze,new Move('A',new Point(1,2),false),false);
    assertNotSame("Shouldnt return the same object but a clone",s.maze,n.maze);
    assertNotSame("Shouldnt return the same object but a clone",s.player,n.player);
    assertNotSame("Shouldnt return the same object but a clone",s.opponent,n.opponent);
    assertEquals(n.player[GameState.POSITION_X],1);
    assertEquals(n.player[GameState.POSITION_Y],2);
    assertEquals(n.player[GameState.SCORE],1);
    assertEquals(n.opponent[GameState.POSITION_X],2);
    assertEquals(n.opponent[GameState.POSITION_Y],3);
    assertEquals(n.opponent[GameState.SCORE],0);
    assertEquals(n.maze[1][1],' ');
    assertEquals(n.maze[1][2],'A');
    assertEquals(n.maze[2][3],'B');
  }

  @Test
  public void makeMoveTest2() {
    char[][] maze = new char[][]{
        {'#','#','#','#','#'},
        {'#','A','.',' ','#'},
        {'#',' ','*','B','#'},
        {'#','#','#','#','#'}
    };
    int[] player = new int[]{1,1,Main.CARRY_POISON,Main.CARRY_POISON,0};
    int[] oppone = new int[]{2,3,Main.CARRY_POISON,Main.CARRY_POISON,0};
    //Move B to 1,3
    //Do not drop poison
    //Do no teleport
    GameState s = new GameState(maze,player,oppone);
    GameState n = s.makeMove(maze,new Move('B',new Point(1,3),false),false);
    assertNotSame("Shouldnt return the same object but a clone",s.maze,n.maze);
    assertNotSame("Shouldnt return the same object but a clone",s.player,n.player);
    assertNotSame("Shouldnt return the same object but a clone",s.opponent,n.opponent);
    assertEquals(n.player[GameState.POSITION_X], 1);
    assertEquals(n.player[GameState.POSITION_Y], 1);
    assertEquals(n.player[GameState.SCORE], 0);
    assertEquals(n.opponent[GameState.POSITION_X],1);
    assertEquals(n.opponent[GameState.POSITION_Y],3);
    assertEquals(n.opponent[GameState.SCORE],0);
    assertEquals(n.maze[1][1],'A');
    assertEquals(n.maze[2][3],' ');
    assertEquals(n.maze[1][3],'B');
  }

  @Test
  public void updateFromInputTest1() {
    char[][] oldMaze = new char[][] {
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
        "####.#   A.  #.####".toCharArray(),
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
    char[][] newMaze = new char[][] {
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
      "####.#   A.  #.####".toCharArray(),
      "####.#.##### #.####".toCharArray(),
      "#........#..   B..#".toCharArray(),
      "#.##.###.#.###.##.#".toCharArray(),
      "#*.#...........#.*#".toCharArray(),
      "##.#.#.#####.#.#.##".toCharArray(),
      "#....#...#...#....#".toCharArray(),
      "#.######.#.######.#".toCharArray(),
      "#.................#".toCharArray(),
      "###################".toCharArray()
    };
    int[] player = new int[]{12,9,12,10,10};
    int[] oppone = new int[]{14,14, Main.CARRY_POISON, Main.CARRY_POISON,9};
    GameState s = new GameState(oldMaze,player,oppone);
    GameState n = s.updateFromInput(newMaze);
    assertNotSame(newMaze,n.maze);
    assertArrayEquals(s.player,n.player);
    assertEquals(14,n.opponent[GameState.POSITION_X]);
    assertEquals(15,n.opponent[GameState.POSITION_Y]);
  }

  @Test
  public void updateFromInputTest2() {
    char[][] oldMaze = new char[][] {
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
        "####.#   A.  #.####".toCharArray(),
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

    char[][] newMaze = new char[][] {
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
        "####.#   A.  #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#..  .B..#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{12,9,12,10,10};
    int[] oppone = new int[]{14,14, Main.CARRY_POISON, Main.CARRY_POISON,9};
    GameState s = new GameState(oldMaze,player,oppone);
    GameState n = s.updateFromInput(newMaze);
    assertNotSame(newMaze,n.maze);
    assertArrayEquals(s.player,n.player);
    assertArrayEquals(new int[]{14,15,14,14,10},n.opponent);
  }
  @Test
  public void eatPoisonTest() {
    char[][] oldMaze = new char[][] {
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
        "####.#   A.  #.####".toCharArray(),
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

    char[][] newMaze = new char[][] {
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
        "       # B #       ".toCharArray(),
        "####.# ## ## #.####".toCharArray(),
        "####.#   A.  #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#..    ..#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{12,9,12,10,10};
    int[] oppone = new int[]{14,14, 14,15,9};
    GameState s = new GameState(oldMaze,player,oppone);
    GameState n = s.updateFromInput(newMaze);
    assertNotSame(newMaze,n.maze);
    assertArrayEquals(s.player,n.player);
    assertArrayEquals(new int[]{Main.SPAWN_X, Main.SPAWN_Y, Main.CONSUMED_POISON, Main.CONSUMED_POISON,9},n.opponent);
  }
}
