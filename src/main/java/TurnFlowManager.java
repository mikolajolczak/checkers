package checkers.src.main.java;

public record TurnFlowManager(TurnManager turnManager,
                              BotController botController) {

  public void afterMove() {
    turnManager.switchTurn();
    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }
}
