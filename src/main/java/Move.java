package checkers.src.main.java;

public class Move {
  private final BoardState boardState;

  public Move(BoardState boardStateParam) {
    this.boardState = boardStateParam;
  }


  public boolean isItOnTheSameDiagonal(int c1, int r1, int c2, int r2) {
    return Math.abs(r2 - r1) != Math.abs(c2 - c1) && r1 + c1 != c2 + r2;
  }

  public boolean diagonalHasPieces(int c1, int r1, int c2, int r2, int dc,
                                   int dr) {
    for (int i = r1 + dr, j = c1 + dc; i != r2 && j != c2; i += dr, j += dc) {
      if (!isValidPosition(i, j)) {
        break;
      }
      if (Math.abs(i - r2) <= 1 && Math.abs(j - c2) <= 1) {
        break;
      }
      if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
        return true;
      }
    }
    return false;
  }

  public boolean checkAllPiecesPossibleTakes(int color, int colorQueen,
                                             BoardState bs) {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = bs.getPiece(row, col);
        if ((piece == color || piece == colorQueen) && canITake(col, row, bs)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean legalTakeMove(int c2, int r2, int c1, int r1, int color) {
    if (!boardState.isItEmpty(c2, r2) || isItOnTheSameDiagonal(c1, r1, c2,
        r2)) {
      return false;
    }

    int midC = (c1 + c2) / 2, midR = (r1 + r2) / 2;
    int dc = Integer.compare(c2, c1), dr = Integer.compare(r2, r1);

    return switch (color) {
      case GameConstants.RED ->
          Math.abs(c2 - c1) == 2 && Math.abs(r2 - r1) == 2 &&
              (boardState.isItBlack(midC, midR) || boardState.isItBlackKing(
                  midC, midR));
      case GameConstants.BLACK ->
          Math.abs(c2 - c1) == 2 && Math.abs(r2 - r1) == 2 &&
              (boardState.isItRed(midC, midR) || boardState.isItRedKing(midC,
                  midR));
      case GameConstants.BLACK_KING -> {
        if (diagonalHasPieces(c1, r1, c2, r2, dc, dr)) {
          yield false;
        }
        yield boardState.isItRed(c2 - dc, r2 - dr) || boardState.isItRedKing(
            c2 - dc, r2 - dr);
      }
      case GameConstants.RED_KING -> {
        if (diagonalHasPieces(c1, r1, c2, r2, dc, dr)) {
          yield false;
        }
        yield boardState.isItBlack(c2 - dc, r2 - dr)
            || boardState.isItBlackKing(c2 - dc, r2 - dr);
      }
      default -> false;
    };
  }

  public boolean canIMove(int col, int row) {
    int piece = boardState.getPiece(row, col);
    if (piece == GameConstants.RED) {
      return
          (col < GameConstants.LAST_ROW_INDEX && boardState.isItEmpty(col + 1,
              row - 1)) ||
              (col > 0 && boardState.isItEmpty(col - 1, row - 1));
    } else if (piece == GameConstants.BLACK) {
      return
          (col < GameConstants.LAST_ROW_INDEX && boardState.isItEmpty(col + 1,
              row + 1)) ||
              (col > 0 && boardState.isItEmpty(col - 1, row + 1));
    } else {
      return boardState.isItKing(piece);
    }
  }

  public boolean canITake(int col, int row, BoardState bs) {
    int piece = bs.getPiece(row, col);
    return boardState.isItKing(piece) ? canKingTake(row, col, piece, bs)
        : canRegularPieceTake(row, col, piece, bs);
  }

  public boolean canKingTake(int row, int col, int piece, BoardState bs) {
    int enemy1 = (piece == GameConstants.RED_KING) ? GameConstants.BLACK
        : GameConstants.RED;
    int enemy2 = (piece == GameConstants.RED_KING) ? GameConstants.BLACK_KING
        : GameConstants.RED_KING;

    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE - 1; dist++) {
        int r = row + dist * dir[0], c = col + dist * dir[1];
        if (!isValidPosition(r, c)) {
          break;
        }
        int check = bs.getPiece(r, c);
        if (check == enemy1 || check == enemy2) {
          int landR = r + dir[0], landC = c + dir[1];
          if (isValidPosition(landR, landC)
              && bs.getPiece(landR, landC) == GameConstants.EMPTY) {
            return true;
          }
          break;
        } else if (check != GameConstants.EMPTY) {
          break;
        }
      }
    }
    return false;
  }

  private int[] getEnemyPieces(int piece) {
    boolean isRed =
        (piece == GameConstants.RED || piece == GameConstants.RED_KING);
    return isRed ?
        new int[]{GameConstants.BLACK, GameConstants.BLACK_KING} :
        new int[]{GameConstants.RED, GameConstants.RED_KING};
  }

  public boolean canRegularPieceTake(int row, int col, int piece,
                                     BoardState bs) {
    int dir = (piece == GameConstants.RED) ? -1 : 1;
    int[] enemyPieces = getEnemyPieces(piece);

    for (int dc : new int[]{-1, 1}) {
      int jumpR = row + 2 * dir, jumpC = col + 2 * dc;
      int enemyR = row + dir, enemyC = col + dc;
      if (isValidPosition(jumpR, jumpC) && isValidPosition(enemyR, enemyC)) {
        int enemyPiece = bs.getPiece(enemyR, enemyC);
        if ((enemyPiece == enemyPieces[0] || enemyPiece == enemyPieces[1])
            && bs.getPiece(jumpR, jumpC) == GameConstants.EMPTY) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isValidPosition(int row, int col) {
    return row >= 0 && row < GameConstants.BOARD_SIZE && col >= 0
        && col < GameConstants.BOARD_SIZE;
  }

  public boolean isItLegalSecondClickMove(int c2, int r2, int c1, int r1,
                                          int color) {
    if (!boardState.isItEmpty(c2, r2)) {
      return false;
    }

    switch (color) {
      case GameConstants.RED_KING:
      case GameConstants.BLACK_KING:
        if (isItOnTheSameDiagonal(c1, r1, c2, r2)) {
          return false;
        }
        int dc = Integer.compare(c2, c1), dr = Integer.compare(r2, r1);
        for (int i = r1 + dr, j = c1 + dc; i != r2 || j != c2;
             i += dr, j += dc) {
          if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
            return false;
          }
        }
        return true;
      case GameConstants.RED:
        return Math.abs(c2 - c1) == 1 && r1 - r2 == 1;
      case GameConstants.BLACK:
        return Math.abs(c2 - c1) == 1 && r2 - r1 == 1;
      default:
        return false;
    }
  }
}
