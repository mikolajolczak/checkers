package checkers.src.main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Represents a checkers game board with pieces and selection state.
 * Responsible for drawing the board and managing piece positions.
 */
public class BoardPanel extends JPanel {
  private final BoardState state;

  public BoardPanel(BoardState state) {
    this.state = state;
  }

  /**
   * Paints the checkers board and pieces.
   *
   * @param g the Graphics object
   */
  @Override
  public void paintComponent(final Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        drawSquare(g, row, col);
        drawPiece(g, row, col);
      }
    }
  }

  private void drawSquare(Graphics g, int row, int col) {
    if (col == state.getSelectedColumn() && row == state.getSelectedRow()) {
      g.setColor(Color.DARK_GRAY);
    } else {
      g.setColor((row % 2 == col % 2) ? Color.LIGHT_GRAY : Color.GRAY);
    }
    g.fillRect(col * GameConstants.SQUARE_SIZE,
        row * GameConstants.SQUARE_SIZE,
        GameConstants.SQUARE_SIZE,
        GameConstants.SQUARE_SIZE);
  }

  private void drawPiece(Graphics g, int row, int col) {
    int piece = state.getPiece(row, col);
    if (piece == GameConstants.EMPTY) {
      return;
    }

    Color color =
        (piece == GameConstants.RED || piece == GameConstants.RED_KING)
            ? Color.RED : Color.BLACK;

    g.setColor(color);
    g.fillOval(GameConstants.PIECE_PADDING + col * GameConstants.SQUARE_SIZE,
        GameConstants.PIECE_PADDING + row * GameConstants.SQUARE_SIZE,
        GameConstants.PIECE_SIZE, GameConstants.PIECE_SIZE);

    if (piece == GameConstants.RED_KING || piece == GameConstants.BLACK_KING) {
      g.setColor(Color.WHITE);
      g.drawOval(
          GameConstants.KING_MARKER_PADDING + col * GameConstants.SQUARE_SIZE,
          GameConstants.KING_MARKER_PADDING + row * GameConstants.SQUARE_SIZE,
          GameConstants.KING_MARKER_SIZE, GameConstants.KING_MARKER_SIZE);
    }
  }
}
