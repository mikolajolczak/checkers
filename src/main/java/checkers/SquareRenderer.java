package checkers;

import java.awt.Color;
import java.awt.Graphics;

public final class SquareRenderer {

  private SquareRenderer() {
  }

  public static void drawSquare(final Graphics g, final int row, final int col,
                                final boolean selected,
                                final int squareSize) {
    if (selected) {
      g.setColor(Color.DARK_GRAY);
    } else {
      g.setColor((row % 2 == col % 2) ? Color.LIGHT_GRAY : Color.GRAY);
    }
    g.fillRect(col * squareSize, row * squareSize, squareSize, squareSize);
  }
}
