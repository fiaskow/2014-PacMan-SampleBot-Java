package za.co.entelect.challenge;


import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;

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
    int[] player = new int[]{1,1,1,1,Main.CARRY_POISON,0};
    int[] oppone = new int[]{2,3,2,3,Main.CARRY_POISON,0};
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
    assertEquals(n.player[GameState.PREVIOUS_X],1);
    assertEquals(n.player[GameState.PREVIOUS_Y],1);
    assertEquals(n.opponent[GameState.POSITION_X],2);
    assertEquals(n.opponent[GameState.POSITION_Y],3);
    assertEquals(n.opponent[GameState.SCORE],0);
    assertEquals(n.opponent[GameState.PREVIOUS_X],2);
    assertEquals(n.opponent[GameState.PREVIOUS_Y],3);
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
    int[] player = new int[]{1,1,1,1,Main.CARRY_POISON,0};
    int[] oppone = new int[]{2,3,2,3,Main.CARRY_POISON,0};
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
      "####.#   A!  #.####".toCharArray(),
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
    int[] player = new int[]{12,9,12,10,Main.DROPPED_POISON,10};
    int[] oppone = new int[]{14,14,14,14,Main.CARRY_POISON,9};
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
        "#........#..  !B..#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{12,9,12,10,Main.DROPPED_POISON,10};
    int[] oppone = new int[]{14,14,14,13,Main.CARRY_POISON,9};
    GameState s = new GameState(oldMaze,player,oppone);
    GameState n = s.updateFromInput(newMaze);
    assertNotSame(newMaze,n.maze);
    assertArrayEquals(s.player,n.player);
    assertArrayEquals(new int[]{14,15,14,14,Main.DROPPED_POISON,10},n.opponent);
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
        "####.#   A!  #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#..  B!..#".toCharArray(),
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
        "####.#   A!  #.####".toCharArray(),
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
    int[] player = new int[]{12,9,12,10,Main.DROPPED_POISON,10};
    int[] oppone = new int[]{14,14,14,15,Main.DROPPED_POISON,9};
    GameState s = new GameState(oldMaze,player,oppone);
    GameState n = s.updateFromInput(newMaze);
    assertNotSame(newMaze,n.maze);
    assertArrayEquals(s.player,n.player);
    assertArrayEquals(new int[]{Main.SPAWN_X, Main.SPAWN_Y,14,14,Main.DROPPED_POISON,9},n.opponent);
    assertNotEquals(oldMaze[14][15], n.maze[14][15]);
  }

  @Test
  public void eatPoisonTest2() {
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
        "####.#   A!  #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#.. !B...#".toCharArray(),
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
        "####.#   A!  #.####".toCharArray(),
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
    int[] player = new int[]{12,9,12,10,Main.DROPPED_POISON,10};
    int[] oppone = new int[]{14,14,14,13,Main.DROPPED_POISON,9};
    GameState s = new GameState(oldMaze,player,oppone);
    Move m = new Move(Main.OPPONENT_SYMBOL,new Point(14,13),false);
    GameState n = s.makeMove(m,true);
    assertNotSame(newMaze,n.maze);
    assertArrayEquals(s.player,n.player);
    assertArrayEquals(new int[]{Main.SPAWN_X, Main.SPAWN_Y, 14, 14, Main.DROPPED_POISON,9},n.opponent);
    assertNotEquals(oldMaze[14][13],newMaze[14][13]);
  }

  @Test
  public void determineMovesTest1() {
    char[][] maze = new char[][]{
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
        "####.#   A   #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#.. !B...#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{12,9,12,8,Main.CARRY_POISON,0};
    int[] oppone = new int[]{14,14,14,13,Main.DROPPED_POISON,0};
    GameState s = new GameState(maze,player,oppone);
    List<Move> moves = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,false);
    assertEquals(4,moves.size());
  }

  @Test
  public void determineMovesTest2() {
    char[][] maze = new char[][]{
        "###################".toCharArray(),
        "#........#........#".toCharArray(),
        "#*##.###.#.###.##*#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#.................#".toCharArray(),
        "#.##.#.#####.#.##.#".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "####.###.#.###.####".toCharArray(),
        "####.#.......#.####".toCharArray(),
        "####.#.##B##.#.####".toCharArray(),
        "       # A #       ".toCharArray(),
        "####.# ## ## #.####".toCharArray(),
        "####.#       #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#..   ...#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{10,9,12,8,Main.DROPPED_POISON,0};
    int[] oppone = new int[]{9,9,10,9,Main.DROPPED_POISON,0};
    GameState s = new GameState(maze,player,oppone);
    List<Move> moves = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,false);
    assertEquals(1,moves.size());
  }


  @Test
  public void determineMovesTest3() {
    char[][] maze = new char[][]{
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
        "####.#   A   #.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#.. !B...#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{12,9,12,8,Main.CARRY_POISON,0};
    int[] oppone = new int[]{14,14,14,13,Main.DROPPED_POISON,0};
    GameState s = new GameState(maze,player,oppone);
    List<Move> moves = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,true);
    assertEquals(2,moves.size());
  }
}
