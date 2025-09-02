package checkers;

public final class MoveEvaluator {
  private MoveEvaluator() {
  }

  public static int evaluateMove(final BotDecision decision,
                                 final BoardState boardState,
                                 final PlayerConfiguration playerConfiguration) {
    MoveExecutor.applyMoveToBoard(decision, boardState, playerConfiguration);

    int score = 0;
    score += ThreatEvaluator.evaluatePlayerThreats(decision, boardState,
        playerConfiguration);
    score += CaptureEvaluator.evaluateCaptureOpportunities(boardState,
        playerConfiguration);
    score += PromotionEvaluator.evaluatePromotionChance(decision, boardState,
        playerConfiguration);

    return score;
  }
}
