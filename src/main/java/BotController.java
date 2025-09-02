package checkers.src.main.java;

public class BotController {
  private final BotDecisionService decisionService;
  private final BotMoveExecutor moveExecutor;
  private final BotUIHandler uiHandler;

  public BotController(BotDecisionService decisionService,
                       BotMoveExecutor moveExecutor,
                       BotUIHandler uiHandler) {
    this.decisionService = decisionService;
    this.moveExecutor = moveExecutor;
    this.uiHandler = uiHandler;
  }

  public void executeTurn() {
    new Thread(() -> {
      try {
        Thread.sleep(GameConstants.BOT_MOVE_DELAY_MS);
      } catch (InterruptedException e) {
        System.err.println("Thread was interrupted: " + e.getMessage());
      }

      BotDecision decision = decisionService.getBotDecision();
      moveExecutor.executeMove(decision);
      uiHandler.updateUIAndSwitchTurn();
    }).start();
  }
}