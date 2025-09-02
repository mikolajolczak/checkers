package checkers.src.main.java;

import java.util.ArrayList;

public final class MoveEvaluator {

  public static BotDecision chooseBestMove(ArrayList<BotDecision> possibleMoves,
                                    BoardState boardState,
                                    PlayerConfiguration playerConfiguration) {
    return BestMoveSelector.chooseBestMove(possibleMoves, boardState, playerConfiguration);
  }
}