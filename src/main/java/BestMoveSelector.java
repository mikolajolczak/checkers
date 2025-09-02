package checkers.src.main.java;

import java.util.ArrayList;

public class BestMoveSelector {

  private final ThreatEvaluator threatEvaluator;
  private final CaptureEvaluator captureEvaluator;
  private final PromotionEvaluator promotionEvaluator;
  private final MoveExecutor moveExecutor;

  public BestMoveSelector(ThreatEvaluator threatEvaluator,
                          CaptureEvaluator captureEvaluator,
                          PromotionEvaluator promotionEvaluator,
                          MoveExecutor moveExecutor) {
    this.threatEvaluator = threatEvaluator;
    this.captureEvaluator = captureEvaluator;
    this.promotionEvaluator = promotionEvaluator;
    this.moveExecutor = moveExecutor;
  }

  public BotDecision chooseBestMove(ArrayList<BotDecision> possibleMoves,
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

  private int evaluateMove(BotDecision decision, BoardState boardState,
                           PlayerConfiguration playerConfiguration) {
    moveExecutor.applyMoveToBoard(decision, boardState, playerConfiguration);

    int score = 0;
    score += threatEvaluator.evaluatePlayerThreats(decision, boardState,
        playerConfiguration);
    score += captureEvaluator.evaluateCaptureOpportunities(boardState,
        playerConfiguration);
    score += promotionEvaluator.evaluatePromotionChance(decision, boardState,
        playerConfiguration);

    return score;
  }
}

