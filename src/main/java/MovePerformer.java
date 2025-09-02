package checkers.src.main.java;

public record MovePerformer(PromotionService promotionService,
                            BoardState boardState) {

  public void performMove(int fromRow, int fromCol, int toRow, int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol, pieceColor,
        boardState);
    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
  }
}