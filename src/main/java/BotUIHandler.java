package checkers.src.main.java;

public class BotUIHandler {
  private final UIController uiController;
  private final TurnManager turnManager;

  public BotUIHandler(UIController uiController, TurnManager turnManager) {
    this.uiController = uiController;
    this.turnManager = turnManager;
  }

  public void updateUIAndSwitchTurn() {
    uiController.refreshBoard();
    uiController.checkGameEnd();
    turnManager.switchTurn();
  }
}
