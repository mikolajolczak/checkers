package checkers.src.main.java;

public class MovePerformer {

  private final MoveExecutor moveExecutor;
  private final PromotionService promotionService;
  private final BoardState boardState;

  public MovePerformer(PromotionService promotionService, BoardState boardState) {
    this.moveExecutor = new MoveExecutor();
    this.promotionService = promotionService;
    this.boardState = boardState;
  }

  public void performMove(int fromRow, int fromCol, int toRow, int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    moveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol, pieceColor, boardState);
    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
  }
}