package checkers.src.main.java;

public final class MoveHandler {

  private final MoveExecutor moveExecutor;
  private final PromotionService promotionService;
  private final TurnManager turnManager;
  private final BoardState boardState;
  private final BotController botController;
  private final MoveService moveService;
  private final UIController uiController;

  public MoveHandler(MoveExecutor moveExecutor,
                     PromotionService promotionService,
                     TurnManager turnManager, BoardState boardState,
                     BotController botController,
                     MoveService moveServiceParam,
                     UIController uiControllerParam) {
    this.moveExecutor = moveExecutor;
    this.promotionService = promotionService;
    this.turnManager = turnManager;
    this.boardState = boardState;
    this.botController = botController;
    moveService = moveServiceParam;
    uiController = uiControllerParam;
  }

  public void handleMove(int fromRow, int fromCol, int toRow, int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    if (!moveService.isLegalMove(toRow, toCol, fromCol, fromRow, pieceColor)) {
      return;
    }
    moveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol, pieceColor,
        boardState);
    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
    turnManager.switchTurn();

    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }

  public boolean isValidPosition(int row, int col) {
    return row >= 0 && row < GameConstants.BOARD_SIZE &&
        col >= 0 && col < GameConstants.BOARD_SIZE;
  }

  public boolean canSelectPiece(int row, int col) {
    return moveService.canSelectPiece(row, col, boardState);
  }

  public boolean mustTake() {
    return moveService.mustTake();
  }


  public void refreshBoard() {
    uiController.refreshBoard();
  }
}
