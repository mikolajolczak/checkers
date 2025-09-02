package checkers;

public record MoveValidator(MoveService moveService, BoardState boardState) {

  public boolean isLegalMove(final int fromRow, final int fromCol,
                             final int toRow, final int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    return moveService.isLegalMove(toRow, toCol, fromCol, fromRow, pieceColor);
  }

  public boolean canSelectPiece(final int row, final int col) {
    return moveService.canSelectPiece(row, col, boardState);
  }

  public boolean mustTake() {
    return moveService.mustTake();
  }

  public boolean isValidPosition(final int row, final int col) {
    return PositionValidator.isValidPosition(row, col);
  }
}
