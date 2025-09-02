package checkers.src.main.java;

public final class CaptureHandler {

  private final MoveExecutor moveExecutor;
  private final PromotionService promotionService;
  private final TurnManager turnManager;
  private final BoardState boardState;
  private final BotController botController;
  private final Move move;

  public CaptureHandler(MoveExecutor moveExecutor, PromotionService promotionService,
                        TurnManager turnManager, BoardState boardState,
                        BotController botController, Move move) {
    this.moveExecutor = moveExecutor;
    this.promotionService = promotionService;
    this.turnManager = turnManager;
    this.boardState = boardState;
    this.botController = botController;
    this.move = move;
  }

  public void handleCapture(int fromRow, int fromCol, int toRow, int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);

    if (!move.legalTakeMove(toCol, toRow, fromCol, fromRow, pieceColor)) return;

    if (boardState.isItKing(pieceColor)) {
      moveExecutor.executeQueenCapture(fromRow, fromCol, toRow, toCol,
          turnManager.getCurrentKingColor(), boardState);
    } else {
      moveExecutor.executeCapture(fromRow, fromCol, toRow, toCol,
          turnManager.getCurrentColor(), boardState);
    }

    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
    turnManager.switchTurn();

    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }
}
