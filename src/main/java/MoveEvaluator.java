package checkers.src.main.java;

import java.util.ArrayList;

public class MoveEvaluator {

  private final BestMoveSelector bestMoveSelector;

  public MoveEvaluator(BestMoveSelector bestMoveSelector) {
    this.bestMoveSelector = bestMoveSelector;
  }

  public BotDecision chooseBestMove(ArrayList<BotDecision> possibleMoves,
                                    BoardState boardState,
                                    PlayerConfiguration playerConfiguration) {
    return bestMoveSelector.chooseBestMove(possibleMoves, boardState, playerConfiguration);
  }
}