package checkers;

/**
 * Handles the capture logic in a checkers game.
 *
 * <p>This class is responsible for validating and executing capture moves on
 * the board.
 * It works with a {@link CaptureExecutor} to perform the capture and a
 * {@link TurnFlowManager}
 * to update the game's turn state. The validation of the capture is
 * delegated to
 * {@link CaptureValidator}.</p>
 *
 * @param executor   the {@link CaptureExecutor} used to perform the capture
 * @param turnFlow   the {@link TurnFlowManager} responsible for handling
 *                   turn flow after a move
 * @param boardState the current {@link BoardState} of the game
 */
public record CaptureHandler(CaptureExecutor executor, TurnFlowManager turnFlow,
                             BoardState boardState) {
  /**
   * Handles a capture move on the board.
   *
   * <p>This method checks if the attempted capture from the specified source
   * coordinates ({@code fromRow}, {@code fromCol}) to the target coordinates
   * ({@code toRow}, {@code toCol}) is valid for the piece at the source
   * position.
   * If the capture is valid, it executes the capture using the provided
   * {@link CaptureExecutor} and updates the turn flow through
   * {@link TurnFlowManager}.
   * </p>
   *
   * @param fromRow the row index of the piece to capture from
   * @param fromCol the column index of the piece to capture from
   * @param toRow   the row index of the target square
   * @param toCol   the column index of the target square
   */
  public void handleCapture(final int fromRow, final int fromCol,
                            final int toRow, final int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    if (!CaptureValidator.isValidCapture(boardState, fromRow, fromCol, toRow,
        toCol, pieceColor)) {
      return;
    }
    executor.execute(boardState, fromRow, fromCol, toRow, toCol, pieceColor,
        turnFlow.turnManager());
    turnFlow.afterMove();
  }
}
