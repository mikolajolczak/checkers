package checkers.src.main.java;

import java.util.ArrayList;

public record Bot(BoardState board, MoveService moveService,
                  PlayerConfiguration playerConfiguration) {

  public BotDecision makeMove() {
    ArrayList<BotDecision> possibleMoves = moveService.getPossibleMoves(board);
    return MoveEvaluator.chooseBestMove(possibleMoves, board,
        playerConfiguration);
  }
}