package checkers;

import java.util.ArrayList;

/**
 * Represents the bot's artificial intelligence responsible for determining
 * optimal moves in the checkers game.
 *
 * <p>The {@code BotAi} uses a {@link MoveService} to generate all legal moves
 * for the current board state and applies a selection strategy to identify
 * the most advantageous move for the bot.
 * </p>
 *
 * @param moveService the service used to generate all possible moves
 */
public record BotAi(MoveService moveService) {
  /**
   * Determines the best move for the bot based on the current game state.
   *
   * @param botState the current state of the bot, including the board and
   *                 player configuration
   * @return the {@link BotDecision} representing the chosen move
   */
  public BotDecision makeMove(final BotState botState) {
    ArrayList<BotDecision> possibleMoves =
        moveService.getPossibleMoves(botState.board());
    return BestMoveSelector.chooseBestMove(possibleMoves, botState.board(),
        botState.playerConfig());
  }
}
