package checkers.src.main.java;

import java.util.ArrayList;

public class MoveEvaluator {

  private final BestMoveSelector bestMoveSelector;

  public MoveEvaluator() {
    this.bestMoveSelector = new BestMoveSelector();
  }

  public BotDecision chooseBestMove(ArrayList<BotDecision> possibleMoves,
                                    BoardState boardState,
                                    PlayerConfiguration playerConfiguration) {
    return bestMoveSelector.chooseBestMove(possibleMoves, boardState, playerConfiguration);
  }
}