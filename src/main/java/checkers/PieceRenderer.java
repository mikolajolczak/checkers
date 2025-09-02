package checkers;

import java.awt.Graphics;

public final class PieceRenderer {

  private PieceRenderer() {
  }

  public static void drawPiece(final Graphics g, final PieceView piece) {
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

