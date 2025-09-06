package checkers;

/**
 * Utility class for validating positions on a checkers board.
 *
 * <p>This class provides static methods to:
 * <ul>
 *   <li>Check if a given row and column represent a valid board position.</li>
 *   <li>Determine whether two positions are not on the same diagonal,
 *       which is useful for enforcing movement rules in checkers.</li>
 * </ul>
 *
 * <p>This class is final and cannot be instantiated.
 */
public final class PositionValidator {

  private PositionValidator() {
  }

  /**
   * Checks whether the given row and column represent a valid position
   * on the checkers board.
   *
   * @param row the row index of the position
   * @param col the column index of the position
   * @return {@code true} if the position is within the bounds of the board,
   *     {@code false} otherwise
   */
  public static boolean isValidPosition(final int row, final int col) {
    return row >= 0 && row < GameConstants.BOARD_SIZE && col >= 0
        && col < GameConstants.BOARD_SIZE;
  }

  /**
   * Determines whether two positions are not on the same diagonal.
   * In checkers, diagonal moves are valid, so this can be used to
   * enforce or check movement rules.
   *
   * @param c1 the column index of the first position
   * @param r1 the row index of the first position
   * @param c2 the column index of the second position
   * @param r2 the row index of the second position
   * @return {@code true} if the positions are not on the same diagonal,
   *     {@code false} if they are on the same diagonal
   */
  public static boolean isNotOnSameDiagonal(final int c1, final int r1,
                                            final int c2, final int r2) {
    return Math.abs(r2 - r1) != Math.abs(c2 - c1) && r1 + c1 != c2 + r2;
  }
}
