package checkers;

public record CaptureExecutor(PromotionService promotionService) {

  public void execute(final BoardState state, final int fromRow,
                      final int fromCol, final int toRow,
                      final int toCol, final int pieceColor,
                      final TurnManager turnManager) {
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
