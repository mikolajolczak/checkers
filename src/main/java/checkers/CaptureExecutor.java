package checkers;

/**
 * Handles the execution of capture moves in a checkers game.
 *
 * <p>This class delegates the actual move logic to {@link MoveExecutor} and
 * uses {@link PromotionService} to promote pieces when necessary.
 * It differentiates between regular pieces and kings, performing
 * the appropriate capture logic for each.
 * </p>
 *
 * @param promotionService the service used to promote pieces when they reach
 *                         the opposite side
 */
public record CaptureExecutor(PromotionService promotionService) {
  /**
   * Executes a capture move from the specified source coordinates to the
   * target coordinates.
   *
   * <p>If the piece being moved is a king, a king capture is performed;
   * otherwise, a regular
   * capture is executed. After the capture, the method checks if the piece
   * should be promoted.
   * </p>
   *
   * @param state       the current board state
   * @param fromRow     the row index of the piece to move
   * @param fromCol     the column index of the piece to move
   * @param toRow       the target row index
   * @param toCol       the target column index
   * @param pieceColor  the color of the piece being moved
   * @param turnManager the manager tracking the current turn and player
   */
  public void execute(final BoardState state, final int fromRow,
                      final int fromCol, final int toRow,
                      final int toCol, final int pieceColor,
                      final TurnManager turnManager) {
    if (PieceRules.isKing(pieceColor)) {
      MoveExecutor.executeKingCapture(fromRow, fromCol, toRow, toCol,
          turnManager.getCurrentKingColor(), state);
    } else {
      MoveExecutor.executeCapture(fromRow, fromCol, toRow, toCol,
          turnManager.getCurrentColor(), state);
    }
    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
  }
}
