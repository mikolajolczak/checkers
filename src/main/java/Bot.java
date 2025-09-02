package checkers.src.main.java;

import java.util.ArrayList;

public class Bot {
  private final BoardState board;
  private final MoveService moveService;
  private final MoveEvaluator moveEvaluator;
  private final PlayerConfiguration playerConfiguration;

  public Bot(BoardState board, MoveService analyzer,
             MoveEvaluator decisionMaker,
             PlayerConfiguration playerConfigurationParam) {
    this.board = board;
    this.moveService = analyzer;
    this.moveEvaluator = decisionMaker;
    playerConfiguration = playerConfigurationParam;
  }

  public BotDecision makeMove() {
    ArrayList<BotDecision> possibleMoves = moveService.getPossibleMoves(board);
    return moveEvaluator.chooseBestMove(possibleMoves, board, playerConfiguration);
  }
}