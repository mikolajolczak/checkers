package checkers;

public record TurnFlowManager(TurnManager turnManager,
                              BotController botController) {

  public void afterMove() {
    turnManager.switchTurn();
    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }
}
