package checkers;

import java.awt.Graphics;

/**
 * Utility class for rendering checkers pieces on a graphical board.
 *
 * <p>This class provides a static method to draw individual pieces
 * on a {@link Graphics} context. It handles rendering the piece's color,
 * position, and king status. Instances of this class are not allowed
 * as it only contains static utility methods.</p>
 */
public final class PieceRenderer {

  private PieceRenderer() {
  }

  /**
   * Renders a checkers piece on the given graphics context.
   *
   * <p>If the piece is empty, nothing is drawn. Otherwise, the piece is drawn
   * as a filled oval with the appropriate color at its row and column
   * position on the board. If the piece is a king, a smaller white oval
   * is drawn on top to indicate its king status.
   * </p>
   *
   * @param g     the {@link Graphics} context to draw the piece on
   * @param piece the {@link PieceView} representing the piece to be drawn
   */
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

