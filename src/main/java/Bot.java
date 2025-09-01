package checkers.src.main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Bot {
  private final BoardState boardState;
  private final BoardController boardController;
  private final ArrayList<int[]> possibleMoves = new ArrayList<>();
  private int[] bestMove = new int[GameConstants.MOVE_ARRAY_LENGTH];
  private BoardState boardStateCopy;



  public Bot(BoardController boardControllerParam, BoardState boardStateParam) {
    this.boardController = boardControllerParam;
    this.boardState = boardStateParam;
  }

  public boolean checkAllPiecesPossibleTakes(int color, int colorQueen,
                                             BoardState boardStateParam) {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardStateParam.getPiece(row, col);
        if ((piece == color || piece == colorQueen) && boardController.getMove().canITake(col, row,
            boardStateParam)) {//tu cos moze byc
          return true;
        }
      }
    }
    return false;
  }

  public void analyze() {
    possibleMoves.clear();

    boolean mustTake = boardController.getMove().checkAllPiecesPossibleTakes(
        boardController.getBotsColor(), boardController.getBotsKingColor());

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardState.getPiece(row, col);

        if (!isBotPiece(piece)) {
          continue;
        }

        if (mustTake && boardController.getMove().canITake(col, row, boardState)) {//tu cos moze byc
          findCaptureMoves(row, col, piece);
        } else if (!mustTake && boardController.getMove().canIMove(col, row)) {
          findRegularMoves(row, col, piece);
        }
      }
    }
  }

  private boolean isBotPiece(int piece) {
    return piece == boardController.getBotsColor() ||
        piece == boardController.getBotsKingColor();
  }

  private void findCaptureMoves(int row, int col, int piece) {
    if (boardController.getMove().isKing(piece)) {
      findKingCaptures(row, col, piece);
    } else {
      findRegularCaptures(row, col, piece);
    }
  }

  private void findRegularMoves(int row, int col, int piece) {
    if (boardController.getMove().isKing(piece)) {
      findKingMoves(row, col, piece);
    } else {
      findRegularPieceMoves(row, col, piece);
    }
  }



  private void findRegularCaptures(int row, int col, int piece) {
    int direction = (piece == GameConstants.RED) ? -1 : 1;

    for (int[] dir : new int[][]{{direction, -1}, {direction, 1}}) {
      int newRow = row + 2 * dir[0];
      int newCol = col + 2 * dir[1];

      if (boardController.getMove().isValidPosition(newRow, newCol) &&
          boardController.getMove().legalTakeMove(newCol, newRow, col, row, piece)) {
        possibleMoves.add(
            new int[]{row, col, newRow, newCol, GameConstants.TAKE});
      }
    }
  }

  private void findRegularPieceMoves(int row, int col, int piece) {
    int direction = (piece == GameConstants.RED) ? -1 : 1;

    for (int[] dir : new int[][]{{direction, -1}, {direction, 1}}) {
      int newRow = row + dir[0];
      int newCol = col + dir[1];

      if (boardController.getMove().isValidPosition(newRow, newCol) &&
          boardController.getMove().isItLegalSecondClickMove(newCol, newRow, col, row, piece)) {
        possibleMoves.add(
            new int[]{row, col, newRow, newCol, GameConstants.MOVE});
      }
    }
  }

  private void findKingCaptures(int row, int col, int piece) {
    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE; dist++) {
        int newRow = row + dist * dir[0];
        int newCol = col + dist * dir[1];

        if (!boardController.getMove().isValidPosition(newRow, newCol)) {
          break;
        }

        if (boardController.getMove().legalTakeMove(newCol, newRow, col, row, piece)) {
          if (hasObstaclesBetween(col, row, newCol, newRow)) {
            possibleMoves.add(
                new int[]{row, col, newRow, newCol, GameConstants.QUEEN_TAKE});
          }
        }
      }
    }
  }

  private void findKingMoves(int row, int col, int piece) {
    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE; dist++) {
        int newRow = row + dist * dir[0];
        int newCol = col + dist * dir[1];

        if (!boardController.getMove().isValidPosition(newRow, newCol)) {
          break;
        }

        if (boardController.getMove().isItLegalSecondClickMove(newCol, newRow, col, row, piece)) {
          if (hasObstaclesBetween(col, row, newCol, newRow)) {
            possibleMoves.add(
                new int[]{row, col, newRow, newCol, GameConstants.MOVE});
          }
        } else {
          break;
        }
      }
    }
  }


  private boolean hasObstaclesBetween(int fromCol, int fromRow, int toCol,
                                      int toRow) {

    if (Math.abs(toRow - fromRow) > 1) {
      return
          !boardController.getMove().checkRightTopDiagonalEmptySpaces(fromCol, fromRow, toCol, toRow)
              &&
              !boardController.getMove().checkRightBotDiagonalEmptySpaces(fromCol, fromRow, toCol,
                  toRow);
    }
    return true;
  }

  public void simulate() {
    int bestScore = GameConstants.INITIAL_SUM_MAX;

    for (int[] moveArray : possibleMoves) {
      copyBoard();
      int score = evaluateMove(moveArray, boardStateCopy);

      if (score >= bestScore) {
        bestMove = Arrays.copyOf(moveArray, moveArray.length);
        bestScore = score;
      }
    }
  }

  private void copyBoard() {
    this.boardStateCopy = new BoardState(boardState.getPieces());
  }

  private int evaluateMove(int[] moveArray, BoardState boardState) {

    applyMoveToBoard(moveArray, boardState);

    int score = 0;

    if (checkAllPiecesPossibleTakes(boardController.getPlayersColor(),
        boardController.getPlayersKingColor(), boardState)) {
      int movedPiece = boardState.getPiece(moveArray[2],moveArray[3]);
      score -= boardController.getMove().isKing(movedPiece) ? GameConstants.SCORE_PLAYER_THREAT_KING :
          GameConstants.SCORE_PLAYER_THREAT;
    }

    if (checkAllPiecesPossibleTakes(boardController.getBotsColor(),
        boardController.getBotsKingColor(), boardState)) {
      score += GameConstants.SCORE_TAKE_POSSIBLE;
    }

    if (isChanceForQueen(boardController.getBotsColor(), boardState,
        boardState.getPiece(moveArray[2],moveArray[3]))) {
      score += GameConstants.SCORE_CHANCE_FOR_QUEEN;
    }

    return score;
  }

  private void applyMoveToBoard(int[] moveArray, BoardState boardStateParam) {
    int moveType = moveArray[GameConstants.MOVE_TYPE];
    int fromRow = moveArray[0], fromCol = moveArray[1];
    int toRow = moveArray[2], toCol = moveArray[3];

    switch (moveType) {
      case GameConstants.MOVE:
        executeRegularMove(fromRow, fromCol, toRow, toCol, boardStateParam);
        break;
      case GameConstants.TAKE:
        take(fromRow, fromCol, toRow, toCol, boardController.getBotsColor(),
            boardStateParam);
        break;
      case GameConstants.QUEEN_TAKE:
        boardController.queenTake(fromRow, fromCol, toRow, toCol,
            boardController.getBotsKingColor(), boardStateParam);
        break;
    }
  }

  private void executeRegularMove(int fromRow, int fromCol, int toRow,
                                  int toCol, BoardState boardState) {
    int piece = boardState.getPiece(fromRow, fromCol);
    boardState.setPiece(fromRow, fromCol, piece);

    if (boardController.getMove().isKing(piece)) {
      boardState.setPiece(toRow, toCol, boardController.getBotsKingColor());
    } else {
      boardState.setPiece(toRow, toCol, boardController.getBotsColor());
    }
  }

  public boolean isChanceForQueen(int colorToCheck, BoardState boardStateParam,
                                  int typeOfFigure) {
    if (boardController.getMove().isKing(typeOfFigure)) {
      return false;
    }

    int targetRow =
        (colorToCheck == GameConstants.BLACK) ? GameConstants.LAST_ROW_INDEX
            : 0;

    for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
      if (boardStateParam.getPiece(targetRow, col) == colorToCheck) {
        return true;
      }
    }
    return false;
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
        executeMove(fromRow, fromCol, toRow, toCol);
        break;
      case GameConstants.TAKE:
        boardController.take(fromRow, fromCol, toRow, toCol,
            boardController.getBotsColor());
        break;
      case GameConstants.QUEEN_TAKE:
        boardController.queenTake(fromRow, fromCol, toRow, toCol,
            boardController.getBotsKingColor(), boardState);
        break;
    }

    promoteToKingIfNeeded(toRow, toCol);

    boardController.getFrame().getBoard().repaint();
    possibleMoves.clear();
    Arrays.fill(bestMove, 0);
    boardController.getFrame().isGameFinished();
  }

  private void executeMove(int fromRow, int fromCol, int toRow, int toCol) {
    int piece = boardState.getPiece(fromRow, fromCol);
    boardState.setPiece(fromRow, fromCol, GameConstants.EMPTY);

    if (boardController.getMove().isKing(piece)) {
      boardState.setPiece(toRow, toCol, boardController.getBotsKingColor());
    } else {
      boardState.setPiece(toRow, toCol, boardController.getBotsColor());
    }
  }

  private void promoteToKingIfNeeded(int row, int col) {
    if (row == GameConstants.LAST_ROW_INDEX &&
        boardController.getBotsColor() == GameConstants.BLACK) {
      boardState.setPiece(row, col, GameConstants.BLACK_KING);
    } else if (row == 0 &&
        boardController.getBotsColor() == GameConstants.RED) {
      boardState.setPiece(row, col, GameConstants.RED_KING);
    }
  }

  public void take(int firstRow, int firstColumn, int secondRow,
                   int secondColumn,
                   int currentColor, BoardState boardState) {
    boardState.setPiece(firstRow, firstColumn,GameConstants.EMPTY);
    boardState.setPiece(secondRow, secondColumn,currentColor);

    int capturedRow = (firstRow + secondRow) / 2;
    int capturedCol = (firstColumn + secondColumn) / 2;
    boardState.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }










}