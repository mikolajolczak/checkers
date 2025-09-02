package checkers.src.main.java;

public record MoveCoordinator(MovePerformer performer, MoveValidator validator,
                              UIController uiController,
                              TurnManager turnManager,
                              BotController botController) {

  public void handleMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (!validator.isLegalMove(fromRow, fromCol, toRow, toCol)) {
      return;
    }

    performer.performMove(fromRow, fromCol, toRow, toCol);
    turnManager.switchTurn();
    uiController.refreshBoard();

    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }
}
