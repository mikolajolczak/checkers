package checkers.src.main.java;

import java.util.ArrayList;

public class BotAI {

  private final MoveService moveService;

  public BotAI(MoveService moveService) {
    this.moveService = moveService;
  }

  public BotDecision makeMove(BotState botState) {
    ArrayList<BotDecision> possibleMoves = moveService.getPossibleMoves(botState.board());
    return MoveEvaluator.chooseBestMove(possibleMoves, botState.board(), botState.playerConfiguration());
  }
}
