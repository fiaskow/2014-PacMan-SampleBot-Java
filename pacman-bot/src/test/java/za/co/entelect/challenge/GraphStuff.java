package za.co.entelect.challenge;

import static org.junit.Assert.*;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Test;

import java.awt.Point;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by marais on 2014/08/10.
 */
public class GraphStuff {

  public void ShortestPathsTest () {
    UndirectedGraph<Point, DefaultEdge> boardGraph = getGraph();

    System.out.println("Final graph contains " + boardGraph.vertexSet().size() + " vertices");
    System.out.println("Final graph contains " + boardGraph.edgeSet().size() + " edges");

    FloydWarshallShortestPaths<Point, DefaultEdge> paths = new FloydWarshallShortestPaths<Point, DefaultEdge>(boardGraph);

    System.out.println("There are " + paths.getShortestPathsCount() + " possible shortest paths");
    System.out.println("Longest path in the graph is " + paths.getDiameter() + " moves.");
    System.out.println("From 1,1 to 20,17 is " + paths.shortestDistance(new Point(1,1),new Point(20,17)) + " moves." );
    //System.out.println("Path is: " + paths.getShortestPath(new Point(1,1),new Point(20,17)));
    System.out.println("Shortest path between starting points: " + paths.shortestDistance(new Point(10, 3), new Point(10,15)) + " moves");
    System.out.println("Shortest path between same points: " + paths.shortestDistance(new Point(1,1), new Point(1,1)));
    byte[][] l = new byte[WIDTH*HEIGHT][WIDTH*HEIGHT];
    for (int j = 0; j < HEIGHT; j++)
      for (int i = 0; i < WIDTH; i++)
        for (int x = 0; x < HEIGHT; x++)
          for (int y = 0; y < WIDTH; y++) {
            int index1 = WIDTH * j + i;
            int index2 = WIDTH * x + y;
            try {
              l[index1][index2] = (byte)paths.shortestDistance(new Point(j,i),new Point(x,y));
            } catch (Exception e) {
              l[index1][index2] = -1;
            }
          }
    writeLookupMatrix(l,"pathLookup.state");
  }

  private UndirectedGraph<Point, DefaultEdge> getGraph() {
    char[][] maze = ReadMaze("graph.state");
    UndirectedGraph<Point, DefaultEdge> boardGraph = new SimpleGraph<Point, DefaultEdge>(DefaultEdge.class);
    int vcount = 0;
    for (int j = 0; j < maze.length; j++) {
      for (int i = 0; i < maze[j].length; i++) {
        if (maze[j][i] == '.') {
          Point v = new Point(j,i);
          boardGraph.addVertex(v);
          vcount++;
        }
      }
    }

    int ecount = 0;
    for (Point v : boardGraph.vertexSet()) {
      for (Point m : determineMoves(v, maze)) {
        boardGraph.addEdge(v,m);
        ecount++;
      }
    }
    boardGraph.addEdge(new Point(Main.PORTAL1_X, Main.PORTAL1_Y), new Point(Main.PORTAL2_X, Main.PORTAL2_Y));
    ecount++;
    System.out.println("Added " + vcount + " vertices");
    System.out.println("Added " + ecount + " edges");
    return boardGraph;
  }

  @Test
  public void testMatrixFromFile() throws IOException {
    UndirectedGraph<Point, DefaultEdge> boardGraph = getGraph();
    FloydWarshallShortestPaths<Point, DefaultEdge> paths = new FloydWarshallShortestPaths<Point, DefaultEdge>(boardGraph);
    ShortestPaths s = new ShortestPaths();
    for (Point p : boardGraph.vertexSet()) {
      for (Point pp : boardGraph.vertexSet()) {
        assertEquals((byte)paths.shortestDistance(p,pp),s.shortestDistance(p, pp));
        assertEquals((byte)paths.shortestDistance(p,pp),s.shortestDistance(pp, p));
      }
    }
  }

  private static final int HEIGHT = 22;
  private static final int WIDTH = 19;
  public static final char WALL = '#';

  private static char[][] ReadMaze(String filePath) {
    char[][] map = new char[HEIGHT][];
    try {
      Scanner reader = new Scanner(new File(filePath));
      int rowCount = 0;
      while (reader.hasNext()) {
        String row = reader.nextLine();
        map[rowCount] = row.toCharArray();
        rowCount++;
      }
    } catch (IOException e) {
      System.err.println(e);
    }
    return map;
  }

  private static List<Point> determineMoves(Point currentPoint, char[][] maze) {
    List<Point> moveList = new ArrayList<Point>();
    if (currentPoint.getY() + 1 < WIDTH)
      if (maze[(int) currentPoint.getX()][(int) currentPoint.getY() + 1] != WALL)
        moveList.add(new Point((int) currentPoint.getX(), (int) currentPoint.getY() + 1));

    if ((int) currentPoint.getY() - 1 >= 0)
      if (maze[(int) currentPoint.getX()][(int) currentPoint.getY() - 1] != WALL)
        moveList.add(new Point((int) currentPoint.getX(), (int) currentPoint.getY() - 1));

    if ((int) currentPoint.getX() + 1 < HEIGHT)
      if (maze[(int) currentPoint.getX() + 1][(int) currentPoint.getY()] != WALL)
        moveList.add(new Point((int) currentPoint.getX() + 1, (int) currentPoint.getY()));

    if ((int) currentPoint.getX() - 1 >= 0)
      if (maze[(int) currentPoint.getX() - 1][(int) currentPoint.getY()] != WALL)
        moveList.add(new Point((int) currentPoint.getX() - 1, (int) currentPoint.getY()));

    return moveList;
  }

  private static void writeLookupMatrix(byte[][] l, String filename) {
    try {
      for (byte[] b : l) {
        Files.write(Paths.get(filename), b, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private byte[][] lazyReadMatrix(String filename) throws IOException {
    byte[] buff = Files.readAllBytes(Paths.get(filename));
    byte[][] matrix = new byte[Main.WIDTH * Main.HEIGHT][Main.WIDTH * Main.HEIGHT];
    for (int i = 0; i < Main.WIDTH*Main.HEIGHT; i++) {
      System.arraycopy(buff,i * Main.WIDTH * Main.HEIGHT,matrix[i],0,Main.WIDTH*Main.HEIGHT);
    }
    return matrix;
  }
}
