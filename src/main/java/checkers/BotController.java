package checkers;

public record BotController(BotDecisionService decisionService,
                            BotMoveExecutor moveExecutor,
                            BotUIHandler uiHandler) {

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
