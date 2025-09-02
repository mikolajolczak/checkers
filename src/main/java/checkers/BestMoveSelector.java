package checkers;

import java.util.ArrayList;

public final class BestMoveSelector {

  private BestMoveSelector() {
  }

  public static BotDecision chooseBestMove(
      final ArrayList<BotDecision> possibleMoves,
      final BoardState boardState,
      final PlayerConfiguration playerConfiguration) {
    BotDecision bestMove = new BotDecision(-1, -1, -1, -1, -1);
    int bestScore = GameConstants.INITIAL_SUM_MAX;

    for (BotDecision move : possibleMoves) {
      BoardState copy = boardState.copy();
      int score = MoveEvaluator.evaluateMove(move, copy, playerConfiguration);

      if (score >= bestScore) {
        bestMove = move;
        bestScore = score;
      }
    }
    if (bestMove.equals(new BotDecision(-1, -1, -1, -1, -1))) {
      bestMove = possibleMoves.getFirst();
    }
    return bestMove;
  }
}

