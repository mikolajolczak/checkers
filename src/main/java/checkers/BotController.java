package checkers;

/**
 * Controller responsible for managing the bot's turn lifecycle
 * in a checkers game. It coordinates the decision-making process,
 * move execution, and UI updates for the bot player.
 *
 * <p>The bot's turn is executed asynchronously with a configurable delay
 * defined in {@link GameConstants#BOT_MOVE_DELAY_MS}.
 *
 * @param decisionService the service responsible for determining the bot's
 *                        next move
 * @param moveExecutor    the executor responsible for applying the bot's
 *                        move to the game state
 * @param uiHandler       the handler responsible for updating the UI and
 *                        switching turns
 */
public record BotController(BotDecisionService decisionService,
                            BotMoveExecutor moveExecutor,
                            BotUiHandler uiHandler) {
  /**
   * Executes the bot's turn asynchronously. This method:
   * <ul>
   *     <li>Delays execution by {@link GameConstants#BOT_MOVE_DELAY_MS}
   *     milliseconds.</li>
   *     <li>Obtains the bot's decision via {@link BotDecisionService}.</li>
   *     <li>Executes the chosen move using {@link BotMoveExecutor}.</li>
   *     <li>Updates the UI and switches the turn using {@link BotUiHandler}
   *     .</li>
   * </ul>
   */
  public void executeTurn() {
    new Thread(() -> {
      try {
        Thread.sleep(GameConstants.BOT_MOVE_DELAY_MS);
      } catch (InterruptedException e) {
        System.err.println("Thread was interrupted: " + e.getMessage());
      }

      BotDecision decision = decisionService.getBotDecision();
      moveExecutor.executeMove(decision);
      uiHandler.updateUiAndSwitchTurn();
    }).start();
  }
}
