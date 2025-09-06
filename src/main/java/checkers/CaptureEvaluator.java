package checkers;

/**
 * Utility class responsible for evaluating capture opportunities for the bot
 * player in a game of checkers.
 *
 * <p>This class provides methods to assess the current board state and
 * determine
 * whether the bot has possible captures available, returning an appropriate
 * score.
 * </p>
 */
public final class CaptureEvaluator {

  private CaptureEvaluator() {
  }

  /**
   * Evaluates the capture opportunities for the bot player given the current
   * board state.
   *
   * @param boardState the current state of the board
   * @param config     the configuration of the players
   * @return the score representing capture opportunities for the bot
   */
  public static int evaluateCaptureOpportunities(final BoardState boardState,
                                                 final PlayerConfig config) {
    if (botCanCaptureAfterMove(boardState, config)) {
      return GameConstants.SCORE_TAKE_POSSIBLE;
    }
    return 0;
  }

  private static boolean botCanCaptureAfterMove(final BoardState boardState,
                                                final PlayerConfig config) {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        config.getBotColor(),
        config.getBotKingColor(),
        boardState);
  }
}
