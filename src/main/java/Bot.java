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

  public void analyze() {
    possibleMoves.clear();

    boolean mustTake = boardController.getMove().checkAllPiecesPossibleTakes(
        boardController.getBotsColor(), boardController.getBotsKingColor(),
        boardState);

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardState.getPiece(row, col);

        if (!isBotPiece(piece)) {
          continue;
        }

        if (mustTake && boardController.getMove()
            .canITake(col, row, boardState)) {
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
          boardController.getMove()
              .legalTakeMove(newCol, newRow, col, row, piece)) {
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
          boardController.getMove()
              .isItLegalSecondClickMove(newCol, newRow, col, row, piece)) {
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

        if (boardController.getMove()
            .legalTakeMove(newCol, newRow, col, row, piece)) {
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

        if (boardController.getMove()
            .isItLegalSecondClickMove(newCol, newRow, col, row, piece)) {
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
          !boardController.getMove()
              .checkRightTopDiagonalEmptySpaces(fromCol, fromRow, toCol, toRow)
              &&
              !boardController.getMove()
                  .checkRightBotDiagonalEmptySpaces(fromCol, fromRow, toCol,
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

  private int evaluateMove(int[] moveArray, BoardState boardState) {
    applyMoveToBoard(moveArray, boardState);

    int score = 0;
    score += evaluatePlayerThreats(moveArray, boardState);
    score += evaluateTakeOpportunities(boardState);
    score += evaluateQueenPromotionChance(moveArray, boardState);

    return score;
  }

  private int evaluatePlayerThreats(int[] moveArray, BoardState boardState) {
    if (!playerCanTakeAfterMove(boardState)) {
      return 0;
    }

    int movedPiece = boardState.getPiece(moveArray[2], moveArray[3]);
    return boardController.getMove().isKing(movedPiece)
        ? -GameConstants.SCORE_PLAYER_THREAT_KING
        : -GameConstants.SCORE_PLAYER_THREAT;
  }

  private boolean playerCanTakeAfterMove(BoardState boardState) {
    return boardController.getMove().checkAllPiecesPossibleTakes(
        boardController.getPlayersColor(),
        boardController.getPlayersKingColor(),
        boardState);
  }

  private int evaluateTakeOpportunities(BoardState boardState) {
    if (botCanTakeAfterMove(boardState)) {
      return GameConstants.SCORE_TAKE_POSSIBLE;
    }
    return 0;
  }

  private boolean botCanTakeAfterMove(BoardState boardState) {
    return boardController.getMove().checkAllPiecesPossibleTakes(
        boardController.getBotsColor(),
        boardController.getBotsKingColor(),
        boardState);
  }

  private int evaluateQueenPromotionChance(int[] moveArray,
                                           BoardState boardState) {
    int movedPiece = boardState.getPiece(moveArray[2], moveArray[3]);

    if (canPromoteToQueen(boardState, movedPiece)) {
      return GameConstants.SCORE_CHANCE_FOR_QUEEN;
    }
    return 0;
  }

  private boolean canPromoteToQueen(BoardState boardState,
                                    int movedPiece) {
    return isChanceForQueen(
        boardController.getBotsColor(),
        boardState,
        movedPiece);
  }

  private void applyMoveToBoard(int[] moveArray, BoardState boardState) {
    int moveType = moveArray[GameConstants.MOVE_TYPE];
    int fromRow = moveArray[0], fromCol = moveArray[1];
    int toRow = moveArray[2], toCol = moveArray[3];

    switch (moveType) {
      case GameConstants.MOVE:
        executeRegularMove(fromRow, fromCol, toRow, toCol, boardState);
        break;
      case GameConstants.TAKE:
        executeCapture(fromRow, fromCol, toRow, toCol, boardState);
        break;
      case GameConstants.QUEEN_TAKE:
        executeQueenCapture(fromRow, fromCol, toRow, toCol, boardState);
        break;
    }
  }

  private void executeCapture(int fromRow, int fromCol, int toRow, int toCol,
                              BoardState boardState) {
    boardController.take(fromRow, fromCol, toRow, toCol,
        boardController.getBotsColor(), boardState);
  }

  private void executeQueenCapture(int fromRow, int fromCol, int toRow,
                                   int toCol, BoardState boardState) {
    boardController.queenTake(fromRow, fromCol, toRow, toCol,
        boardController.getBotsKingColor(), boardState);
  }

  private void executeRegularMove(int fromRow, int fromCol, int toRow,
                                  int toCol, BoardState boardState) {
    int piece = boardState.getPiece(fromRow, fromCol);

    boardState.setPiece(fromRow, fromCol, GameConstants.EMPTY);
    boardState.setPiece(toRow, toCol, piece);
  }

  public boolean isChanceForQueen(int colorToCheck, BoardState boardState,
                                  int pieceType) {
    if (isAlreadyKing(pieceType)) {
      return false;
    }

    int promotionRow = getPromotionRow(colorToCheck);
    return hasPieceOnPromotionRow(boardState, colorToCheck, promotionRow);
  }

  private boolean isAlreadyKing(int pieceType) {
    return boardController.getMove().isKing(pieceType);
  }

  private int getPromotionRow(int colorToCheck) {
    return (colorToCheck == GameConstants.BLACK)
        ? GameConstants.LAST_ROW_INDEX
        : 0;
  }

  private boolean hasPieceOnPromotionRow(BoardState boardState,
                                         int colorToCheck, int targetRow) {
    for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
      if (boardState.getPiece(targetRow, col) == colorToCheck) {
        return true;
      }
    }
    return false;
  }


}