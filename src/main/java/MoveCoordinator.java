package checkers.src.main.java;

public class MoveCoordinator {

  private final MovePerformer performer;
  private final MoveValidator validator;
  private final UIController uiController;
  private final TurnManager turnManager;
  private final BotController botController;

  public MoveCoordinator(MovePerformer performer, MoveValidator validator,
                         UIController uiHandler, TurnManager turnManager,
                         BotController botController) {
    this.performer = performer;
    this.validator = validator;
    this.uiController = uiHandler;
    this.turnManager = turnManager;
    this.botController = botController;
  }

  public void handleMove(int fromRow, int fromCol, int toRow, int toCol) {
    if (!validator.isLegalMove(fromRow, fromCol, toRow, toCol)) return;

    performer.performMove(fromRow, fromCol, toRow, toCol);
    turnManager.switchTurn();
    uiController.refreshBoard();

    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }
}
