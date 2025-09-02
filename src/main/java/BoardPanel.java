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
  private final SelectionState selection;

  public BoardPanel(BoardState state, SelectionState selection) {
    this.state = state;
    this.selection = selection;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        drawSquare(g, row, col);
        drawPiece(g, row, col);
      }
    }
  }

  private void drawSquare(Graphics g, int row, int col) {
    if (col == selection.getSelectedColumn() && row == selection.getSelectedRow()) {
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
    if (piece == GameConstants.EMPTY) return;

    Color color = (piece == GameConstants.RED || piece == GameConstants.RED_KING)
        ? Color.RED : Color.BLACK;
    g.setColor(color);
    g.fillOval(GameConstants.PIECE_PADDING + col * GameConstants.SQUARE_SIZE,
        GameConstants.PIECE_PADDING + row * GameConstants.SQUARE_SIZE,
        GameConstants.PIECE_SIZE, GameConstants.PIECE_SIZE);

    if (piece == GameConstants.RED_KING || piece == GameConstants.BLACK_KING) {
      g.setColor(Color.WHITE);
      g.drawOval(GameConstants.KING_MARKER_PADDING + col * GameConstants.SQUARE_SIZE,
          GameConstants.KING_MARKER_PADDING + row * GameConstants.SQUARE_SIZE,
          GameConstants.KING_MARKER_SIZE, GameConstants.KING_MARKER_SIZE);
    }
  }
}
