package checkers;

/**
 * Utility class for validating diagonal movements in a checkers game.
 *
 * <p>This class provides methods to check if a diagonal path between two
 * positions
 * on the board is clear or if there are obstacles that would block a move.
 * The class cannot be instantiated.</p>
 */
public final class DiagonalValidator {

  private DiagonalValidator() {
  }

  /**
   * Checks if there are any pieces between two positions along a diagonal path.
   *
   * @param c1         the starting column
   * @param r1         the starting row
   * @param c2         the target column
   * @param r2         the target row
   * @param dc         the column increment per step along the path
   * @param dr         the row increment per step along the path
   * @param boardState the current state of the board
   * @return {@code true} if there is at least one piece blocking the path;
   *     {@code false} if the path is clear
   */
  public static boolean isPathClearBetween(final int c1, final int r1,
                                           final int c2, final int r2,
                                           final int dc,
                                           final int dr,
                                           final BoardState boardState) {
    for (int i = r1 + dr, j = c1 + dc; i != r2 && j != c2; i += dr, j += dc) {
      if (!PositionValidator.isValidPosition(i, j)) {
        break;
      }
      if (Math.abs(i - r2) <= 1 && Math.abs(j - c2) <= 1) {
        break;
      }
      if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if there are obstacles between two positions along a diagonal.
   *
   * @param fromCol    the starting column
   * @param fromRow    the starting row
   * @param toCol      the target column
   * @param toRow      the target row
   * @param boardState the current state of the board
   * @return {@code true} if there is at least one piece blocking the path or
   *     the move is only one step; {@code false} otherwise
   */
  public static boolean hasObstaclesBetween(final int fromCol,
                                            final int fromRow, final int toCol,
                                            final int toRow,
                                            final BoardState boardState) {
    if (Math.abs(toRow - fromRow) <= 1) {
      return true;
    }

    int dc = Integer.compare(toCol, fromCol);
    int dr = Integer.compare(toRow, fromRow);

    return !isPathClearBetween(fromCol, fromRow, toCol, toRow, dc, dr,
        boardState);
  }
}

