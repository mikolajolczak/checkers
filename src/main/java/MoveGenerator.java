package checkers.src.main.java;

import java.util.ArrayList;

public class MoveGenerator {
  private final Move move;
  private final PlayerConfiguration playerConfiguration;
  private final ArrayList<int[]> possibleMoves = new ArrayList<>();
  public MoveGenerator(Move moveParam,
                       PlayerConfiguration playerConfigurationParam) {
    move = moveParam;
    playerConfiguration = playerConfigurationParam;
  }

  public ArrayList<int[]> getPossibleMoves(BoardState boardState) {
    possibleMoves.clear();
    boolean mustTake = move.checkAllPiecesPossibleTakes(
        playerConfiguration.getBotColor(), playerConfiguration.getBotKingColor(),
        boardState);

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardState.getPiece(row, col);

        if (!isBotPiece(piece)) {
          continue;
        }

        if (mustTake && move
            .canITake(col, row, boardState)) {
          findCaptureMoves(row, col, piece);
        } else if (!mustTake && move.canIMove(col, row)) {
          findRegularMoves(row, col, piece);
        }
      }
    }
    return possibleMoves;
  }
  private void findCaptureMoves(int row, int col, int piece) {
    if (move.isKing(piece)) {
      findKingCaptures(row, col, piece);
    } else {
      findRegularCaptures(row, col, piece);
    }
  }
  private boolean isBotPiece(int piece) {
    return piece == playerConfiguration.getBotColor() ||
        piece == playerConfiguration.getBotKingColor();
  }

  private void findRegularMoves(int row, int col, int piece) {
    if (move.isKing(piece)) {
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

      if (move.isValidPosition(newRow, newCol) &&
          move
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

      if (move.isValidPosition(newRow, newCol) &&
          move
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

        if (!move.isValidPosition(newRow, newCol)) {
          break;
        }

        if (move
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

        if (!move.isValidPosition(newRow, newCol)) {
          break;
        }

        if (move
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
          !move
              .checkRightTopDiagonalEmptySpaces(fromCol, fromRow, toCol, toRow)
              &&
              !move
                  .checkRightBotDiagonalEmptySpaces(fromCol, fromRow, toCol,
                      toRow);
    }
    return true;
  }
}
