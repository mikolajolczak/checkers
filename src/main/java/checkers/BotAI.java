package checkers;

import java.util.ArrayList;

public record BotAI(MoveService moveService) {

  public BotDecision makeMove(final BotState botState) {
    ArrayList<BotDecision> possibleMoves =
        moveService.getPossibleMoves(botState.board());
    return BestMoveSelector.chooseBestMove(possibleMoves, botState.board(),
        botState.playerConfiguration());
  }
}
