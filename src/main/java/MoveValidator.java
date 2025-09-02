package checkers.src.main.java;

public class MoveValidator {

  private final MoveService moveService;
  private final BoardState boardState;

  public MoveValidator(MoveService moveService, BoardState boardState) {
    this.moveService = moveService;
    this.boardState = boardState;
  }

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
    return row >= 0 && row < GameConstants.BOARD_SIZE &&
        col >= 0 && col < GameConstants.BOARD_SIZE;
  }
}