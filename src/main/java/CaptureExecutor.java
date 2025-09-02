package checkers.src.main.java;

public class CaptureExecutor {
  private final MoveExecutor moveExecutor;
  private final PromotionService promotionService;

  public CaptureExecutor(PromotionService promotionService) {
    this.moveExecutor = new MoveExecutor();
    this.promotionService = promotionService;
  }

  public void execute(BoardState state, int fromRow, int fromCol, int toRow, int toCol, int pieceColor, TurnManager turnManager) {
    if (PieceRules.isKing(pieceColor)) {
      moveExecutor.executeQueenCapture(fromRow, fromCol, toRow, toCol,
          turnManager.getCurrentKingColor(), state);
    } else {
      moveExecutor.executeCapture(fromRow, fromCol, toRow, toCol,
          turnManager.getCurrentColor(), state);
    }
    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
  }
}
