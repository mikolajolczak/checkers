package checkers.src.main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Bot {
  private final BoardState boardState;
  private final BoardController boardController;
  private final ArrayList<int[]> possibleMoves = new ArrayList<>();
  private int[] bestMove = new int[GameConstants.MOVE_ARRAY_LENGTH];


  private static final int[][] DIRECTIONS =
      {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

  public Bot(BoardController boardControllerParam, BoardState boardStateParam) {
    this.boardController = boardControllerParam;
    this.boardState = boardStateParam;
  }

  public boolean checkAllPiecesPossibleTakes(int color, int colorQueen,
                                             int[][] boardParam) {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardParam[row][col];
        if ((piece == color || piece == colorQueen) && canITake(col, row,
            boardParam)) {
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

        if (mustTake && boardController.getMove().canITake(col, row)) {
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
    if (isKing(piece)) {
      findKingCaptures(row, col, piece);
    } else {
      findRegularCaptures(row, col, piece);
    }
  }

  private void findRegularMoves(int row, int col, int piece) {
    if (isKing(piece)) {
      findKingMoves(row, col, piece);
    } else {
      findRegularPieceMoves(row, col, piece);
    }
  }

  private boolean isKing(int piece) {
    return piece == GameConstants.BLACK_KING || piece == GameConstants.RED_KING;
  }

  private void findRegularCaptures(int row, int col, int piece) {
    int direction = (piece == GameConstants.RED) ? -1 : 1;

    for (int[] dir : new int[][]{{direction, -1}, {direction, 1}}) {
      int newRow = row + 2 * dir[0];
      int newCol = col + 2 * dir[1];

      if (isValidPosition(newRow, newCol) &&
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

      if (isValidPosition(newRow, newCol) &&
          boardController.getMove().isItLegalSecondClickMove(newCol, newRow, col, row, piece)) {
        possibleMoves.add(
            new int[]{row, col, newRow, newCol, GameConstants.MOVE});
      }
    }
  }

  private void findKingCaptures(int row, int col, int piece) {
    for (int[] dir : DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE; dist++) {
        int newRow = row + dist * dir[0];
        int newCol = col + dist * dir[1];

        if (!isValidPosition(newRow, newCol)) {
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
    for (int[] dir : DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE; dist++) {
        int newRow = row + dist * dir[0];
        int newCol = col + dist * dir[1];

        if (!isValidPosition(newRow, newCol)) {
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
      int[][] tempBoard = copyBoard();
      int score = evaluateMove(moveArray, tempBoard);

      if (score >= bestScore) {
        bestMove = Arrays.copyOf(moveArray, moveArray.length);
        bestScore = score;
      }
    }
  }

  private int[][] copyBoard() {
    int[][] copy = new int[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        copy[row][col] = boardState.getPiece(row, col);
      }
    }
    return copy;
  }

  private int evaluateMove(int[] moveArray, int[][] tempBoard) {

    applyMoveToBoard(moveArray, tempBoard);

    int score = 0;

    if (checkAllPiecesPossibleTakes(boardController.getPlayersColor(),
        boardController.getPlayersKingColor(), tempBoard)) {
      int movedPiece = tempBoard[moveArray[2]][moveArray[3]];
      score -= isKing(movedPiece) ? GameConstants.SCORE_PLAYER_THREAT_KING :
          GameConstants.SCORE_PLAYER_THREAT;
    }

    if (checkAllPiecesPossibleTakes(boardController.getBotsColor(),
        boardController.getBotsKingColor(), tempBoard)) {
      score += GameConstants.SCORE_TAKE_POSSIBLE;
    }

    if (isChanceForQueen(boardController.getBotsColor(), tempBoard,
        tempBoard[moveArray[2]][moveArray[3]])) {
      score += GameConstants.SCORE_CHANCE_FOR_QUEEN;
    }

    return score;
  }

  private void applyMoveToBoard(int[] moveArray, int[][] tempBoard) {
    int moveType = moveArray[GameConstants.MOVE_TYPE];
    int fromRow = moveArray[0], fromCol = moveArray[1];
    int toRow = moveArray[2], toCol = moveArray[3];

    switch (moveType) {
      case GameConstants.MOVE:
        executeRegularMove(fromRow, fromCol, toRow, toCol, tempBoard);
        break;
      case GameConstants.TAKE:
        take(fromRow, fromCol, toRow, toCol, boardController.getBotsColor(),
            tempBoard);
        break;
      case GameConstants.QUEEN_TAKE:
        queenTake(fromRow, fromCol, toRow, toCol,
            boardController.getBotsKingColor(), tempBoard);
        break;
    }
  }

  private void executeRegularMove(int fromRow, int fromCol, int toRow,
                                  int toCol, int[][] board) {
    int piece = board[fromRow][fromCol];
    board[fromRow][fromCol] = GameConstants.EMPTY;

    if (isKing(piece)) {
      board[toRow][toCol] = boardController.getBotsKingColor();
    } else {
      board[toRow][toCol] = boardController.getBotsColor();
    }
  }

  public boolean isChanceForQueen(int colorToCheck, int[][] boardParam,
                                  int typeOfFigure) {
    if (isKing(typeOfFigure)) {
      return false;
    }

    int targetRow =
        (colorToCheck == GameConstants.BLACK) ? GameConstants.LAST_ROW_INDEX
            : 0;

    for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
      if (boardParam[targetRow][col] == colorToCheck) {
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
            boardController.getBotsKingColor());
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

    if (isKing(piece)) {
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
                   int currentColor, int[][] boardParam) {
    boardParam[firstRow][firstColumn] = GameConstants.EMPTY;
    boardParam[secondRow][secondColumn] = currentColor;

    int capturedRow = (firstRow + secondRow) / 2;
    int capturedCol = (firstColumn + secondColumn) / 2;
    boardParam[capturedRow][capturedCol] = GameConstants.EMPTY;
  }

  public boolean canITake(int column, int row, int[][] boardParam) {
    int piece = boardParam[row][column];

    if (isKing(piece)) {
      return canKingTake(row, column, piece, boardParam);
    } else {
      return canRegularPieceTake(row, column, piece, boardParam);
    }
  }

  private boolean canRegularPieceTake(int row, int col, int piece,
                                      int[][] boardParam) {
    int direction = (piece == GameConstants.RED) ? -1 : 1;
    int enemyColor1 =
        (piece == GameConstants.RED) ? GameConstants.BLACK : GameConstants.RED;
    int enemyColor2 = (piece == GameConstants.RED) ? GameConstants.BLACK_KING
        : GameConstants.RED_KING;

    for (int colDir : new int[]{-1, 1}) {
      int jumpRow = row + 2 * direction;
      int jumpCol = col + 2 * colDir;
      int enemyRow = row + direction;
      int enemyCol = col + colDir;

      if (isValidPosition(jumpRow, jumpCol) && isValidPosition(enemyRow,
          enemyCol)) {
        int enemyPiece = boardParam[enemyRow][enemyCol];
        if ((enemyPiece == enemyColor1 || enemyPiece == enemyColor2) &&
            boardParam[jumpRow][jumpCol] == GameConstants.EMPTY) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean canKingTake(int row, int col, int piece, int[][] boardParam) {
    int enemyColor1 = (piece == GameConstants.RED_KING) ? GameConstants.BLACK
        : GameConstants.RED;
    int enemyColor2 =
        (piece == GameConstants.RED_KING) ? GameConstants.BLACK_KING
            : GameConstants.RED_KING;

    for (int[] dir : DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE - 1; dist++) {
        int checkRow = row + dist * dir[0];
        int checkCol = col + dist * dir[1];

        if (!isValidPosition(checkRow, checkCol)) {
          break;
        }

        int checkPiece = boardParam[checkRow][checkCol];
        if (checkPiece == enemyColor1 || checkPiece == enemyColor2) {

          int landRow = checkRow + dir[0];
          int landCol = checkCol + dir[1];

          if (isValidPosition(landRow, landCol) &&
              boardParam[landRow][landCol] == GameConstants.EMPTY) {
            return true;
          }
          break;
        } else if (checkPiece != GameConstants.EMPTY) {
          break;
        }
      }
    }
    return false;
  }

  private boolean isValidPosition(int row, int col) {
    return row >= 0 && row < GameConstants.BOARD_SIZE &&
        col >= 0 && col < GameConstants.BOARD_SIZE;
  }

  public void queenTake(int firstRow, int firstColumn, int secondRow,
                        int secondColumn,
                        int currentColor, int[][] boardParam) {
    boardParam[firstRow][firstColumn] = GameConstants.EMPTY;
    boardParam[secondRow][secondColumn] = currentColor;

    int rowDir = Integer.signum(secondRow - firstRow);
    int colDir = Integer.signum(secondColumn - firstColumn);

    int capturedRow = secondRow - rowDir;
    int capturedCol = secondColumn - colDir;
    boardParam[capturedRow][capturedCol] = GameConstants.EMPTY;
  }
}