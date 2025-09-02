package checkers.src.main.java;

import java.util.ArrayList;

public record BotAI(MoveService moveService) {

  public BotDecision makeMove(BotState botState) {
    ArrayList<BotDecision> possibleMoves =
        moveService.getPossibleMoves(botState.board());
    return BestMoveSelector.chooseBestMove(possibleMoves, botState.board(),
        botState.playerConfiguration());
  }
}
