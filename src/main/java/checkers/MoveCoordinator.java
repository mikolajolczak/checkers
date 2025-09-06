package checkers;

/**
 * Coordinates the process of executing a move in the checkers game.
 *
 * <p>This class acts as the central controller for handling moves on the
 * board.
 * </p>
 *
 * @param performer     the component responsible for performing moves
 * @param validator     the component responsible for validating moves
 * @param uiController  the component responsible for updating the UI
 * @param turnManager   the component responsible for managing turns
 * @param botController the component responsible for handling bot turns
 */
public record MoveCoordinator(MovePerformer performer, MoveValidator validator,
                              UiController uiController,
                              TurnManager turnManager,
                              BotController botController) {
  /**
   * Handles a move from one position to another on the board.
   *
   * <p>The move is first validated using the {@link MoveValidator}. If the move
   * is legal, it
   * is performed using the {@link MovePerformer}, the turn is switched using
   * the
   * {@link TurnManager}, and the UI is refreshed via {@link UiController}.
   * If the next
   * turn belongs to a bot, it is executed automatically using
   * {@link BotController}.
   * </p>
   *
   * @param fromRow the row index of the piece to move
   * @param fromCol the column index of the piece to move
   * @param toRow   the row index of the target position
   * @param toCol   the column index of the target position
   */
  public void handleMove(final int fromRow, final int fromCol, final int toRow,
                         final int toCol) {
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
