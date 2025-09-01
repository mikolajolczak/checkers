package checkers.src.main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Bot {
  private final BoardState boardState;
  private ArrayList<int[]> possibleMoves = new ArrayList<>();
  private final MoveGenerator moveGenerator;
  private int[] bestMove = new int[GameConstants.MOVE_ARRAY_LENGTH];
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

    for (int[] moveArray : possibleMoves) {
      copyBoard();
      int score = moveEvaluator.evaluateMove(moveArray, boardStateCopy);

      if (score >= bestScore) {
        bestMove = Arrays.copyOf(moveArray, moveArray.length);
        bestScore = score;
      }
    }
    if (Arrays.equals(bestMove, new int[]{0, 0, 0, 0, 0})) {
      bestMove = Arrays.copyOf(possibleMoves.getFirst(), possibleMoves.size());
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