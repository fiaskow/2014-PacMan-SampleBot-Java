package za.co.entelect.challenge;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by marais on 2014/08/10.
 */
public class ShortestPaths {
  private static byte[][] matrix;
  private static final String filename = "pathLookup.state";

  private static byte[][] lazyReadMatrix(String filename) {
    byte[] buff = new byte[0];
    try {
      buff = Files.readAllBytes(Paths.get(filename));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
    byte[][] matrix = new byte[Main.WIDTH * Main.HEIGHT][Main.WIDTH * Main.HEIGHT];
    for (int i = 0; i < Main.WIDTH*Main.HEIGHT; i++) {
      System.arraycopy(buff,i * Main.WIDTH * Main.HEIGHT,matrix[i],0,Main.WIDTH*Main.HEIGHT);
    }
    return matrix;
  }

  public static byte shortestDistance(Point a, Point b) {
    if (matrix == null) matrix = lazyReadMatrix(filename);
    int index1 = Main.WIDTH * a.x + a.y;
    int index2 = Main.WIDTH * b.x + b.y;
    return matrix[index1][index2];
  }

  public static byte shortestDistance(int index1, int index2) {
    if (matrix == null) matrix = lazyReadMatrix(filename);
    return matrix[index1][index2];
  }

}
