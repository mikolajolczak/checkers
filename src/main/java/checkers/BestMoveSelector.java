package checkers;

import java.util.ArrayList;

/**
 * Utility class responsible for selecting the best move from a set of
 * possible moves in a checkers game. It uses {@link MoveEvaluator}
 * to score candidate moves and chooses the one with the highest score
 * according to the current board state and player configuration.
 *
 * <p>This class cannot be instantiated.</p>
 */
public final class BestMoveSelector {

  private BestMoveSelector() {
  }

  /**
   * Selects the best move from the list of possible moves based on the
   * current board state
   * and player configuration. Each move is evaluated using
   * {@link MoveEvaluator}, and
   * the move with the highest score is chosen. If no valid move is found,
   * the first move
   * from the list is returned as a fallback.
   *
   * @param possibleMoves     a list of candidate moves that can be played
   * @param boardState        the current state of the game board
   * @param playerConfigParam the configuration and preferences of the
   *                          player (e.g., side, strategy)
   * @return the {@link BotDecision} representing the most favorable move
   */
  public static BotDecision chooseBestMove(
      final ArrayList<BotDecision> possibleMoves,
      final BoardState boardState,
      final PlayerConfig playerConfigParam) {
    BotDecision bestMove = new BotDecision(-1, -1, -1, -1, -1);
    int bestScore = GameConstants.INITIAL_SUM_MAX;

    for (BotDecision move : possibleMoves) {
      BoardState copy = boardState.copy();
      int score = MoveEvaluator.evaluateMove(move, copy, playerConfigParam);

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

