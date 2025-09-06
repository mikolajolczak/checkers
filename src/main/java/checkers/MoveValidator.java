package checkers;

/**
 * Provides validation logic for checkers moves, including checking if a move
 * is legal,
 * whether a piece can be selected, whether a capture is mandatory, and if a
 * board
 * position is valid.
 *
 * @param moveService the service responsible for handling move operations
 * @param boardState  the current state of the game board
 */
public record MoveValidator(MoveService moveService, BoardState boardState) {
  /**
   * Determines whether a move from a starting position to a target position
   * is legal.
   *
   * @param fromRow the row index of the piece to move
   * @param fromCol the column index of the piece to move
   * @param toRow   the target row index
   * @param toCol   the target column index
   * @return {@code true} if the move is legal; {@code false} otherwise
   */
  public boolean isLegalMove(final int fromRow, final int fromCol,
                             final int toRow, final int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    return moveService.isLegalMove(toRow, toCol, fromCol, fromRow, pieceColor);
  }

  /**
   * Checks whether the piece at the given position can be selected.
   *
   * @param row the row index of the piece
   * @param col the column index of the piece
   * @return {@code true} if the piece can be selected; {@code false} otherwise
   */
  public boolean canSelectPiece(final int row, final int col) {
    return moveService.canSelectPiece(row, col, boardState);
  }

  /**
   * Determines whether the current player must make a capture move.
   *
   * @return {@code true} if a capture move is required; {@code false} otherwise
   */
  public boolean mustTake() {
    return moveService.mustTake();
  }

  /**
   * Checks whether the specified position is valid on the board.
   *
   * @param row the row index of the position
   * @param col the column index of the position
   * @return {@code true} if the position is valid; {@code false} otherwise
   */
  public boolean isValidPosition(final int row, final int col) {
    return PositionValidator.isValidPosition(row, col);
  }
}
