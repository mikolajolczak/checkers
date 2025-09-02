package checkers.src.main.java;

public class BotController {
  private final Bot bot;
  private final MoveExecutor moveExecutor;
  private final BoardState boardState;
  private final PromotionService promotionService;
  private final UIController uiController;
  private final PlayerConfiguration playerConfig;
  private final TurnManager turnManager;

  public BotController(Bot botParam, MoveExecutor moveExecutorParam,
                       BoardState boardStateParam,
                       PromotionService promotionServiceParam,
                       UIController uiControllerParam,
                       PlayerConfiguration playerConfigParam,
                       TurnManager turnManagerParam) {
    bot = botParam;
    moveExecutor = moveExecutorParam;
    boardState = boardStateParam;
    promotionService = promotionServiceParam;
    uiController = uiControllerParam;
    playerConfig = playerConfigParam;
    turnManager = turnManagerParam;
  }
  public void executeTurn() {
    bot.analyze();
    bot.simulate();
    new Thread(() -> {
      try {
        Thread.sleep(GameConstants.BOT_MOVE_DELAY_MS);
      } catch (InterruptedException e) {
        System.err.println("Thread was interrupted: " + e.getMessage());
      }
      BotDecision decision = bot.makeMove();
      executeMove(decision);
    }).start();
  }
  private void executeMove(BotDecision decision) {
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
