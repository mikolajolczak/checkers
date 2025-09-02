package checkers;

public record BotUIHandler(UIController uiController, TurnManager turnManager) {

  public void updateUIAndSwitchTurn() {
    uiController.refreshBoard();
    uiController.checkGameEnd();
    turnManager.switchTurn();
  }
}
