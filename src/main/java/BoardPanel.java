package checkers.src.main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

  private List<PieceView> piecesToDraw;

  public BoardPanel() {
  }

  public void setPiecesToDraw(List<PieceView> pieces) {
    this.piecesToDraw = pieces;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (piecesToDraw == null) return;

    for (PieceView piece : piecesToDraw) {
      drawSquare(g, piece.row, piece.col, piece.selected);
      drawPiece(g, piece.row, piece.col, piece.type);
    }
  }

  private void drawSquare(Graphics g, int row, int col, boolean selected) {
    if (selected) {
      g.setColor(Color.DARK_GRAY);
    } else {
      g.setColor((row % 2 == col % 2) ? Color.LIGHT_GRAY : Color.GRAY);
    }
    g.fillRect(col * GameConstants.SQUARE_SIZE,
        row * GameConstants.SQUARE_SIZE,
        GameConstants.SQUARE_SIZE,
        GameConstants.SQUARE_SIZE);
  }

  private void drawPiece(Graphics g, int row, int col, int type) {
    if (type == GameConstants.EMPTY) return;

    Color color = (type == GameConstants.RED || type == GameConstants.RED_KING)
        ? Color.RED : Color.BLACK;
    g.setColor(color);
    g.fillOval(GameConstants.PIECE_PADDING + col * GameConstants.SQUARE_SIZE,
        GameConstants.PIECE_PADDING + row * GameConstants.SQUARE_SIZE,
        GameConstants.PIECE_SIZE, GameConstants.PIECE_SIZE);

    if (type == GameConstants.RED_KING || type == GameConstants.BLACK_KING) {
      g.setColor(Color.WHITE);
      g.drawOval(GameConstants.KING_MARKER_PADDING + col * GameConstants.SQUARE_SIZE,
          GameConstants.KING_MARKER_PADDING + row * GameConstants.SQUARE_SIZE,
          GameConstants.KING_MARKER_SIZE, GameConstants.KING_MARKER_SIZE);
    }
  }
}