package checkers.src.main.java;

import java.awt.Color;
import java.awt.Graphics;

public final class SquareRenderer {

  public static void drawSquare(Graphics g, int row, int col, boolean selected,
                         int squareSize) {
    if (selected) {
      g.setColor(Color.DARK_GRAY);
    } else {
      g.setColor((row % 2 == col % 2) ? Color.LIGHT_GRAY : Color.GRAY);
    }
    g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
  }
}
