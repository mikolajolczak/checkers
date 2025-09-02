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
      int score = MoveEvaluator.evaluateMove(move, copy, playerConfiguration);

      if (score >= bestScore) {
        bestMove = move;
        bestScore = score;
      }
    }
    return bestMove;
  }
}

