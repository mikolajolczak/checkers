package checkers.src.main.java;

import java.util.ArrayList;

public class Bot {
  private final BoardState board;
  private final MoveService moveService;
  private final MoveEvaluator moveEvaluator;

  public Bot(BoardState board, MoveService analyzer,
             MoveEvaluator decisionMaker) {
    this.board = board;
    this.moveService = analyzer;
    this.moveEvaluator = decisionMaker;
  }

  public BotDecision makeMove() {
    ArrayList<BotDecision> possibleMoves = moveService.getPossibleMoves(board);
    return moveEvaluator.chooseBestMove(possibleMoves, board);
  }
}