package checkers.src.main.java;

public record MoveValidator(MoveService moveService, BoardState boardState) {

  public boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    return moveService.isLegalMove(toRow, toCol, fromCol, fromRow, pieceColor);
  }

  public boolean canSelectPiece(int row, int col) {
    return moveService.canSelectPiece(row, col, boardState);
  }

  public boolean mustTake() {
    return moveService.mustTake();
  }

  public boolean isValidPosition(int row, int col) {
    return PositionValidator.isValidPosition(row, col);
  }
}