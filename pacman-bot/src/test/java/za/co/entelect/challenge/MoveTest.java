package za.co.entelect.challenge;


import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
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
    Main.PLAYER_SYMBOL = 'A';
    int[] player = new int[]{10,9,12,8,Main.DROPPED_POISON,0};
    int[] oppone = new int[]{9,9,10,9,Main.DROPPED_POISON,0};
    GameState s = new GameState(maze,player,oppone);
    List<Move> moves1 = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,false);
    List<Move> moves2 = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,true);
    assertEquals(1,moves1.size());
    assertEquals(1,moves2.size());
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

  @Test
  public void determineMovesTest4() {
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
        "    A..#   #..B    ".toCharArray(),
        "####.#.## ##.#.####".toCharArray(),
        "####.#.......#.####".toCharArray(),
        "####.#.##### #.####".toCharArray(),
        "#........#........#".toCharArray(),
        "#.##.###.#.###.##.#".toCharArray(),
        "#*.#...........#.*#".toCharArray(),
        "##.#.#.#####.#.#.##".toCharArray(),
        "#....#...#...#....#".toCharArray(),
        "#.######.#.######.#".toCharArray(),
        "#.................#".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{10,4,10,3,Main.CARRY_POISON,1};
    int[] oppone = new int[]{10,14,10,15,Main.CARRY_POISON,1};
    GameState s = new GameState(maze,player,oppone);
    List<Move> moves = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,true);
    assertEquals(6,moves.size());
    List<Move> moves2 = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,false);
    assertEquals(8,moves2.size());
  }

  @Test
  public void determineMovesTest5() {
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
        "       #   #       ".toCharArray(),
        "####.# ##A## #.####".toCharArray(),
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
    Main.PLAYER_SYMBOL = 'A';
    int[] player = new int[]{11,9,10,9,Main.DROPPED_POISON,0};
    int[] oppone = new int[]{9,9,10,9,Main.DROPPED_POISON,0};
    GameState s = new GameState(maze,player,oppone);
    List<Move> moves1 = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,false);
    List<Move> moves2 = s.determineAllBasicMoves(Main.PLAYER_SYMBOL,true);
    assertEquals(1,moves1.size());
    assertEquals(1,moves2.size());
  }

  @Test
  public void makeMove2Test() {
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
        "#........#..  B...#".toCharArray(),
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

    Move m1 = new Move('A',new Point(12,10),false);
    GameState s2 = s.makeMove2(m1,true);
    assertSame(s2,s);
//    assertEquals(12,s2.player[GameState.POSITION_X]);
//    assertEquals(10,s2.player[GameState.POSITION_Y]);
//    assertEquals(12,s2.player[GameState.PREVIOUS_X]);
//    assertEquals( 9,s2.player[GameState.PREVIOUS_Y]);
//    assertEquals(10,s2.player[GameState.SCORE]);


  }

  @Test
  public void updateFromInputTest() {
    char[][] oldMaze = new char[][] {
        "###################".toCharArray(),
        "#...     #        #".toCharArray(),
        "# ## ### # ### ## #".toCharArray(),
        "# ## ### # ### ## #".toCharArray(),
        "#      .   .      #".toCharArray(),
        "# ## # ##### # ## #".toCharArray(),
        "#    #   #   #    #".toCharArray(),
        "#### ### # ### ####".toCharArray(),
        "#### #       # ####".toCharArray(),
        "#### # ## ## # ####".toCharArray(),
        "     .!#   #       ".toCharArray(),
        "#### # ## ## # ####".toCharArray(),
        "#### # ..... # ####".toCharArray(),
        "#### # ##### # ####".toCharArray(),
        "#    .   #   .    #".toCharArray(),
        "# ##.### # ###B## #".toCharArray(),
        "#  #       .   #. #".toCharArray(),
        "## # # ##### #A#.##".toCharArray(),
        "#.   #   #   #....#".toCharArray(),
        "#.###### # ######.#".toCharArray(),
        "#.......   .......#".toCharArray(),
        "###################".toCharArray()
    };
    int[] player = new int[]{17,14,16,14,Main.DROPPED_POISON,10};
    int[] oppone = new int[]{15,14,14,14,Main.CARRY_POISON,9};
    GameState oldState = new GameState(oldMaze,player,oppone);

    char[][] newMaze = new char[][] {
        "###################".toCharArray(),
        "#...     #        #".toCharArray(),
        "# ## ### # ### ## #".toCharArray(),
        "# ## ### # ### ## #".toCharArray(),
        "#      .   .      #".toCharArray(),
        "# ## # ##### # ## #".toCharArray(),
        "#    #   #   #    #".toCharArray(),
        "#### ### # ### ####".toCharArray(),
        "#### #       # ####".toCharArray(),
        "#### # ## ## # ####".toCharArray(),
        "     .!#   #       ".toCharArray(),
        "#### # ## ## # ####".toCharArray(),
        "#### # ..... # ####".toCharArray(),
        "#### # ##### # ####".toCharArray(),
        "#    .   #   .    #".toCharArray(),
        "# ##.### # ### ## #".toCharArray(),
        "#  #       .  B#. #".toCharArray(),
        "## # # ##### #A#.##".toCharArray(),
        "#.   #   #   #....#".toCharArray(),
        "#.###### # ######.#".toCharArray(),
        "#.......   .......#".toCharArray(),
        "###################".toCharArray()
    };

    GameState newState = oldState.updateFromInput(newMaze);
    assertNotSame(oldState, newState);
    assertEquals(17,newState.player[GameState.POSITION_X]);
    assertEquals(14,newState.player[GameState.POSITION_Y]);
    assertEquals(16,newState.opponent[GameState.POSITION_X]);
    assertEquals(14,newState.opponent[GameState.POSITION_Y]);
    GameState finalState = newState.makeMove(new Move('A',new Point(16,14),false),false);
    assertEquals('A',finalState.maze[16][14]);
    assertEquals(' ',finalState.maze[17][14]);
    Main.endtime = Long.MAX_VALUE;
    //GameState s3 = np.makeMove(s2,false);
    //s2.determineAllBasicMoves(Main.PLAYER_SYMBOL)
    //System.err.println("A Positon: " + s3.player[1] + "," +s3.player[2]);
  }
}
