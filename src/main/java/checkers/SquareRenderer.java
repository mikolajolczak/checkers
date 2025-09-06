package checkers;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Utility class responsible for rendering individual squares on a checkers
 * board.
 *
 * <p>This class provides a single static method to draw a square with
 * appropriate
 * coloring depending on its position and selection state. It is not intended
 * to be instantiated.</p>
 */
public final class SquareRenderer {

  private SquareRenderer() {
  }

  /**
   * Renders a single square on a checkers board.
   *
   * <p>The square is filled with a color depending on whether it is
   * selected or its position on the board. Selected squares are
   * displayed in dark gray, while unselected squares alternate
   * between light gray and gray based on their row and column indices.</p>
   *
   * @param g          the {@link Graphics} context used for drawing
   * @param row        the row index of the square on the board
   * @param col        the column index of the square on the board
   * @param selected   {@code true} if the square is currently selected;
   *                   {@code false} otherwise
   * @param squareSize the width and height of the square in pixels
   */
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
