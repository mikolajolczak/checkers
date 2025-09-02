package checkers.src.main.java;

import java.util.ArrayList;

public final class BestMoveSelector {

  public static BotDecision chooseBestMove(ArrayList<BotDecision> possibleMoves,
                                    BoardState boardState,
                                    PlayerConfiguration playerConfiguration) {
    BotDecision bestMove = new BotDecision(-1, -1, -1, -1, -1);
    int bestScore = GameConstants.INITIAL_SUM_MAX;

    for (BotDecision move : possibleMoves) {
      BoardState copy = boardState.copy();
      int score = evaluateMove(move, copy, playerConfiguration);

      if (score >= bestScore) {
        bestMove = move;
        bestScore = score;
      }
    }
    return bestMove;
  }

  private static int evaluateMove(BotDecision decision, BoardState boardState,
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

