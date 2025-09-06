package checkers;

/**
 * Service responsible for determining the next move of a bot player
 * based on the current state of the game. It delegates the decision-making
 * process to the provided {@link BotAi} implementation.
 *
 * <p>This record encapsulates both the bot's AI logic and its current state,
 * ensuring that decisions are generated in a consistent and controlled
 * manner.</p>
 *
 * @param bot      the AI component responsible for calculating moves
 * @param botState the current state of the bot, including game context
 */
public record BotDecisionService(BotAi bot, BotState botState) {
  /**
   * Retrieves the bot's next move based on the current bot state.
   *
   * @return {@link BotDecision} representing the decision made by the bot
   */
  public BotDecision getBotDecision() {

    return bot.makeMove(botState);
  }
}
