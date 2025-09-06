package checkers;

/**
 * Utility class responsible for evaluating potential moves in the game of
 * checkers.
 *
 * <p>This class provides a method to calculate a composite score for a given
 * move
 * by considering multiple strategic factors, including:
 * <ul>
 *     <li>Player threats resulting from the move ({@link ThreatEvaluator})</li>
 *     <li>Opportunities to capture opponent pieces ({@link CaptureEvaluator}
 *     )</li>
 *     <li>Potential promotions resulting from the move
 *     ({@link PromotionEvaluator})</li>
 * </ul>
 */
public final class MoveEvaluator {
  private MoveEvaluator() {
  }

  /**
   * Evaluates a potential move for the player by applying the move to the
   * given board state
   * and calculating a composite score based on multiple factors.
   *
   * <p>The evaluation considers:
   * <ul>
   *     <li>Player threats resulting from the move ({@link ThreatEvaluator}
   *     )</li>
   *     <li>Opportunities to capture opponent pieces
   *     ({@link CaptureEvaluator})</li>
   *     <li>Potential promotions resulting from the move
   *     ({@link PromotionEvaluator})</li>
   * </ul>
   *
   * @param decision          the move decision to evaluate
   * @param boardState        the current state of the game board
   * @param playerConfigParam the configuration and settings of the player
   *                          making the move
   * @return an integer score representing the desirability of the move;
   *     higher scores indicate more favorable moves
   */
  public static int evaluateMove(final BotDecision decision,
                                 final BoardState boardState,
                                 final PlayerConfig playerConfigParam) {
    MoveExecutor.applyMoveToBoard(decision, boardState, playerConfigParam);

    int score = 0;
    score += ThreatEvaluator.evaluatePlayerThreats(decision, boardState,
        playerConfigParam);
    score += CaptureEvaluator.evaluateCaptureOpportunities(boardState,
        playerConfigParam);
    score += PromotionEvaluator.evaluatePromotionChance(decision, boardState,
        playerConfigParam);

    return score;
  }
}
