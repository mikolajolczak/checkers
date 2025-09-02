package checkers.src.main.java;

import java.util.ArrayList;

public class Bot {
  private final BoardState boardState;
  private ArrayList<BotDecision> possibleMoves = new ArrayList<>();
  private final MoveGenerator moveGenerator;
  private BotDecision bestMove = new BotDecision(-1, -1, -1,-1, -1);
  private BoardState boardStateCopy;
  private final MoveEvaluator moveEvaluator;


  public Bot(BoardState boardStateParam,
             MoveGenerator moveGeneratorParam,
             MoveEvaluator moveEvaluatorParam) {
    this.boardState = boardStateParam;
    moveGenerator = moveGeneratorParam;
    moveEvaluator = moveEvaluatorParam;
  }

  public void analyze() {
    possibleMoves.clear();
    possibleMoves = moveGenerator.getPossibleMoves(boardState);

  }


  public void simulate() {
    int bestScore = GameConstants.INITIAL_SUM_MAX;

    for (BotDecision moveArray : possibleMoves) {
      copyBoard();
      int score = moveEvaluator.evaluateMove(moveArray, boardStateCopy);

      if (score >= bestScore) {
        bestMove = new BotDecision(moveArray);
        bestScore = score;
      }
    }
    if (bestMove.equals(new BotDecision(-1, -1, -1,-1, -1))) {
      bestMove = new BotDecision(possibleMoves.getFirst());
    }
  }

  private void copyBoard() {
    this.boardStateCopy = new BoardState(boardState.getPieces());
  }

  public BotDecision makeMove() {
    analyze();
    simulate();
    return new BotDecision(bestMove);
  }


}