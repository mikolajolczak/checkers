package checkers.src.main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Bot {
  private final BoardState boardState;
  private final BoardController boardController;
  private ArrayList<int[]> possibleMoves = new ArrayList<>();
  private final MoveGenerator moveGenerator;
  private int[] bestMove = new int[GameConstants.MOVE_ARRAY_LENGTH];
  private BoardState boardStateCopy;
  private final MoveEvaluator moveEvaluator;


  public Bot(BoardController boardControllerParam, BoardState boardStateParam,
             MoveGenerator moveGeneratorParam,
             MoveEvaluator moveEvaluatorParam) {
    this.boardController = boardControllerParam;
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

  public void move() {
    if (bestMove.length == 0) {
      return;
    }
    int fromRow = bestMove[0], fromCol = bestMove[1];
    int toRow = bestMove[2], toCol = bestMove[3];
    int moveType = bestMove[GameConstants.MOVE_TYPE];

    switch (moveType) {
      case GameConstants.MOVE:
        int color = boardController.getBoardState().getPiece(fromRow, fromCol);
        boardController.movePiece(toRow, toCol, fromCol, fromRow, color);
        break;
      case GameConstants.TAKE:
        boardController.take(fromRow, fromCol, toRow, toCol,
            boardController.getBotsColor(), boardState);
        break;
      case GameConstants.QUEEN_TAKE:
        boardController.queenTake(fromRow, fromCol, toRow, toCol,
            boardController.getBotsKingColor(), boardState);
        break;
    }
    boardController.getPromotionService()
        .promoteIfNeeded(toRow, toCol, boardController.getBotsColor());

    boardController.getFrame().getBoard().repaint();
    possibleMoves.clear();
    Arrays.fill(bestMove, 0);
    boardController.getFrame().isGameFinished();
  }


}