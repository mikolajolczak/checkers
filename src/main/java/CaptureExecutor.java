package checkers.src.main.java;

public record CaptureExecutor(PromotionService promotionService) {

  public void execute(BoardState state, int fromRow, int fromCol, int toRow,
                      int toCol, int pieceColor, TurnManager turnManager) {
    if (PieceRules.isKing(pieceColor)) {
      MoveExecutor.executeQueenCapture(fromRow, fromCol, toRow, toCol,
          turnManager.getCurrentKingColor(), state);
    } else {
      MoveExecutor.executeCapture(fromRow, fromCol, toRow, toCol,
          turnManager.getCurrentColor(), state);
    }
    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
  }
}
