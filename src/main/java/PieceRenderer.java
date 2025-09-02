package checkers.src.main.java;

import java.awt.Graphics;

public final class PieceRenderer {

  public static void drawPiece(Graphics g, PieceView piece) {
    if (piece.isEmpty()) {
      return;
    }

    g.setColor(piece.getColor());
    g.fillOval(
        GameConstants.PIECE_PADDING
            + piece.col() * GameConstants.SQUARE_SIZE,
        GameConstants.PIECE_PADDING
            + piece.row() * GameConstants.SQUARE_SIZE,
        GameConstants.PIECE_SIZE,
        GameConstants.PIECE_SIZE
    );

    if (piece.isKing()) {
      g.setColor(java.awt.Color.WHITE);
      g.drawOval(
          GameConstants.KING_MARKER_PADDING
              + piece.col() * GameConstants.SQUARE_SIZE,
          GameConstants.KING_MARKER_PADDING
              + piece.row() * GameConstants.SQUARE_SIZE,
          GameConstants.KING_MARKER_SIZE,
          GameConstants.KING_MARKER_SIZE
      );
    }
  }
}

