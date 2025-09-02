package checkers.src.main.java;

public class MoveValidator {

  private final MoveService moveService;
  private final PositionValidator positionValidator;
  private final BoardState boardState;

  public MoveValidator(MoveService moveService, PositionValidator positionValidator,
                       BoardState boardState) {
    this.moveService = moveService;
    this.positionValidator = positionValidator;
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
    return positionValidator.isValidPosition(row, col);
  }
}