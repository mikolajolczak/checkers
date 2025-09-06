package checkers;

import java.awt.Color;

/**
 * Represents a view of a checkers piece on the board.
 *
 * <p>Each piece has a position (row and column), a type (empty, red, black, red
 * king, or black king),
 * and a selection state.
 * </p>
 *
 * <p>This class provides utility methods to check if a piece is empty, is a
 * king, and to get its color.
 * </p>
 *
 * @param row      the row position of the piece on the board
 * @param col      the column position of the piece on the board
 * @param type     the type of the piece (empty, red, black, red king, or
 *                 black king)
 * @param selected true if the piece is currently selected, false otherwise
 */
public record PieceView(int row, int col, int type, boolean selected) {
  /**
   * Checks if the current piece is empty (i.e., there is no piece on this
   * square).
   *
   * @return {@code true} if the piece type is EMPTY; {@code false} otherwise
   */
  public boolean isEmpty() {
    return type == GameConstants.EMPTY;
  }

  /**
   * Checks if the current piece is a king.
   *
   * @return {@code true} if the piece is a RED_KING or BLACK_KING; {@code
   *     false} otherwise
   */
  public boolean isKing() {
    return type == GameConstants.RED_KING || type == GameConstants.BLACK_KING;
  }

  /**
   * Returns the color of the piece based on its type.
   *
   * @return {@link Color#RED} for red pieces, {@link Color#BLACK} for black
   *     pieces,
   *     or {@code null} if the piece is empty
   */
  public Color getColor() {
    return switch (type) {
      case GameConstants.RED, GameConstants.RED_KING -> Color.RED;
      case GameConstants.BLACK, GameConstants.BLACK_KING -> Color.BLACK;
      default -> null;
    };
  }

}
