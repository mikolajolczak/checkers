package checkers.src.main.java;

public record MovePerformer(PromotionService promotionService,
                            BoardState boardState) {

  public void performMove(final int fromRow, final int fromCol, final int toRow,
                          final int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol, pieceColor,
        boardState);
    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
  }
}
