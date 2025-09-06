package checkers;

/**
 * Represents the current state of a bot in the checkers game.
 *
 * <p>This includes the current state of the game board and the configuration
 * of the player associated with the bot.
 * </p>
 *
 * @param board        the current state of the board
 * @param playerConfig the configuration of the player controlling the bot
 */
public record BotState(BoardState board,
                       PlayerConfig playerConfig) {
}
