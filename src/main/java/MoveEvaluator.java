package checkers.src.main.java;

public final class MoveEvaluator {
  public static int evaluateMove(BotDecision decision, BoardState boardState,
                                  PlayerConfiguration playerConfiguration) {
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