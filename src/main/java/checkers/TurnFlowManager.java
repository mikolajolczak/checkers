package checkers;

/**
 * Manages the flow of turns in a game of checkers.
 * It handles switching turns
 * and triggering bot moves when necessary.
 *
 * @param turnManager   the manager responsible for controlling turn order
 * @param botController the controller responsible for handling bot moves
 */
public record TurnFlowManager(TurnManager turnManager,
                              BotController botController) {
  /**
   * Handles actions that should occur immediately after a move is made.
   * <ul>
   *     <li>Switches the turn to the next player.</li>
   *     <li>If the current player is a bot, invokes the bot controller to
   *     execute the bot's move.</li>
   * </ul>
   */
  public void afterMove() {
    turnManager.switchTurn();
    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }
}
