package checkers.src.main.java;


/**
 * Controller class for managing the Checkers game board, handling player
 * and bot moves, and interacting with the game frame.
 */
public final class BoardController {
  private final PlayerConfiguration playerConfig;
  private final Bot bot;
  private final TurnManager turnManager;
  private final MoveService moveService;
  private final MoveExecutor moveExecutor;
  private final UIController uiController;
  private final BoardState boardState;
  private final PromotionService promotionService;

  public TurnManager getTurnManager() {
    return turnManager;
  }

  public MoveService getMoveService() {
    return moveService;
  }

  public MoveExecutor getMoveExecutor() {
    return moveExecutor;
  }

  public UIController getUiController() {
    return uiController;
  }

  public BoardState getBoardState() {
    return boardState;
  }

  public PromotionService getPromotionService() {
    return promotionService;
  }

  public BoardController(PlayerConfiguration playerConfigParam, Bot botParam,
                         TurnManager turnManagerParam,
                         MoveService moveServiceParam,
                         MoveExecutor moveExecutorParam,
                         UIController uiControllerParam,
                         BoardState boardStateParam,
                         PromotionService promotionServiceParam) {
    playerConfig = playerConfigParam;
    bot = botParam;
    turnManager = turnManagerParam;
    moveService = moveServiceParam;
    moveExecutor = moveExecutorParam;
    uiController = uiControllerParam;
    boardState = boardStateParam;
    promotionService = promotionServiceParam;
  }

  public boolean canSelectPiece(int row, int col) {
    return moveService.canSelectPiece(row, col,
        turnManager.getCurrentColor(),
        turnManager.getCurrentKingColor(), boardState);
  }

  public void executeBotTurn() {
    bot.analyze();
    bot.simulate();
    new Thread(() -> {
      try {
        Thread.sleep(GameConstants.BOT_MOVE_DELAY_MS);
      } catch (InterruptedException e) {
        System.err.println("Thread was interrupted: " + e.getMessage());
      }
      BotDecision decision = bot.makeMove();
      executeBotMove(decision);
    }).start();
  }
  private void executeBotMove(BotDecision decision) {
    switch (decision.moveType()) {
      case GameConstants.MOVE:
        int color = boardState.getPiece(decision.fromRow(), decision.fromCol());
        moveExecutor.executeNormalMove(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(), color, boardState);
        break;
      case GameConstants.TAKE:
        moveExecutor.executeCapture(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(), playerConfig.getBotColor(), boardState);
        break;
      case GameConstants.QUEEN_TAKE:
        moveExecutor.executeQueenCapture(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(), playerConfig.getBotKingColor(), boardState);
        break;
    }

    promotionService.promoteIfNeeded(decision.toRow(), decision.toCol(),
        playerConfig.getBotColor());
    uiController.refreshBoard();
    uiController.checkGameEnd();
    turnManager.switchTurn();
  }
}