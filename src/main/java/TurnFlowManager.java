package checkers.src.main.java;

public class TurnFlowManager {
  private final TurnManager turnManager;
  private final BotController botController;

  public TurnFlowManager(TurnManager turnManager, BotController botController) {
    this.turnManager = turnManager;
    this.botController = botController;
  }

  public void afterMove() {
    turnManager.switchTurn();
    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }

  public TurnManager getTurnManager() {
    return turnManager;
  }
}
