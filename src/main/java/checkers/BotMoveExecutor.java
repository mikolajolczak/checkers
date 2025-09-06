package checkers;

/**
 * Responsible for executing bot moves within the checkers game.
 *
 * <p>This record coordinates applying a bot's move decision to the current
 * {@link BoardState} and handling subsequent promotion logic. It uses the
 * {@link MoveExecutor} to update the board with the move and the
 * {@link PromotionService} to promote a piece when it reaches the promotion
 * row.</p>
 *
 * <p>The execution process is fully based on the bot configuration provided
 * through {@link PlayerConfig}, ensuring that moves and promotions are applied
 * correctly for the bot's color.</p>
 *
 * @param promotionService the service responsible for handling piece promotions
 * @param boardState       the current state of the game board
 * @param playerConfig     configuration details
 */
public record BotMoveExecutor(PromotionService promotionService,
                              BoardState boardState,
                              PlayerConfig playerConfig) {
  /**
   * Executes a move made by the bot on the current board state.
   *
   * <p>This method applies the given {@link BotDecision} to the
   * {@link BoardState}
   * using the {@link MoveExecutor} and then checks if a promotion is needed
   * for the bot's piece using the {@link PromotionService}.
   * </p>
   *
   * @param decision the bot's move decision to be executed
   */
  public void executeMove(final BotDecision decision) {
    MoveExecutor.applyMoveToBoard(decision, boardState, playerConfig);
    promotionService.promoteIfNeeded(decision.toRow(), decision.toCol(),
        playerConfig.getBotColor());
  }
}
