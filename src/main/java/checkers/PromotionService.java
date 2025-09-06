package checkers;

/**
 * Service responsible for handling the promotion of checkers pieces.
 *
 * <p>In standard checkers, when a piece reaches the farthest row on the
 * opponent's side,
 * it is promoted to a king. This class provides the functionality to check
 * whether
 * a piece should be promoted and performs the promotion if needed.
 * </p>
 *
 * @param boardState the current state of the board, used to determine
 *                   piece positions and perform promotions
 */
public record PromotionService(BoardState boardState) {
  /**
   * Promotes a piece to a king if it has reached the row where promotion is
   * required.
   *
   * @param row   the current row of the piece
   * @param col   the current column of the piece
   * @param color the color of the piece, expected to be
   *              {@link GameConstants#RED} or {@link GameConstants#BLACK}
   */
  public void promoteIfNeeded(final int row, final int col, final int color) {
    if (color == GameConstants.RED && row == 0) {
      boardState.setPiece(row, col, GameConstants.RED_KING);
    } else if (color == GameConstants.BLACK
        && row == GameConstants.LAST_ROW_INDEX) {
      boardState.setPiece(row, col, GameConstants.BLACK_KING);
    }
  }
}
