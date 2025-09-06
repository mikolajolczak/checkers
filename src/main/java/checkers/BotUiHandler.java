package checkers;

/**
 * Handles interactions between the bot and the user interface in a Checkers
 * game.
 *
 * <p>This class is responsible for updating the game board on the UI, checking
 * if the game has ended, and managing the switching of turns between players.
 * </p>
 *
 * @param uiController the controller that manages the user interface
 * @param turnManager  the manager responsible for handling player turns
 */
public record BotUiHandler(UiController uiController, TurnManager turnManager) {
  /**
   * Updates the game board on the UI, checks if the game has ended,
   * and switches the current player's turn.
   */
  public void updateUiAndSwitchTurn() {
    uiController.refreshBoard();
    uiController.checkGameEnd();
    turnManager.switchTurn();
  }
}
