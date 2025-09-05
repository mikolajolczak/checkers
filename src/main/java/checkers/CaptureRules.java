package checkers;

public final class CaptureRules {

  private CaptureRules() {
  }

  public static boolean canCapture(final int col, final int row,
                                   final BoardState boardState) {
    int piece = boardState.getPiece(row, col);
    return PieceRules.isKing(piece) ? canKingCapture(row, col, piece,
        boardState) : canRegularPieceCapture(row, col, piece, boardState);
  }

  public static boolean canKingCapture(final int row, final int col,
                                       final int piece,
                                       final BoardState boardState) {
    int[] enemies = getEnemyPieces(piece);

    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE - 1; dist++) {
        int r = row + dist * dir[0];
        int c = col + dist * dir[1];

        if (!PositionValidator.isValidPosition(r, c)) {
          break;
        }

        int check = boardState.getPiece(r, c);
        if (check == enemies[0] || check == enemies[1]) {
          int landR = r + dir[0];
          int landC = c + dir[1];

          if (PositionValidator.isValidPosition(landR, landC)
              && boardState.getPiece(landR, landC) == GameConstants.EMPTY) {
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

  public static boolean canRegularPieceCapture(final int row, final int col,
                                               final int piece,
                                               final BoardState boardState) {
    int dir = (piece == GameConstants.RED) ? -1 : 1;
    int[] enemyPieces = getEnemyPieces(piece);

    for (int dc : new int[]{-1, 1}) {
      int jumpR = row + 2 * dir;
      int jumpC = col + 2 * dc;
      int enemyR = row + dir;
      int enemyC = col + dc;

      if (PositionValidator.isValidPosition(jumpR, jumpC)
          && PositionValidator.isValidPosition(enemyR, enemyC)) {
        int enemyPiece = boardState.getPiece(enemyR, enemyC);

        if ((enemyPiece == enemyPieces[0] || enemyPiece == enemyPieces[1])
            && boardState.getPiece(jumpR, jumpC) == GameConstants.EMPTY) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isLegalCapture(final int c2, final int r2, final int c1,
                                       final int r1,
                                       final int color,
                                       final BoardState boardState) {
    if (!PieceRules.isEmpty(boardState.getPiece(r2, c2))
        || PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2)) {
      return false;
    }

    return switch (color) {
      case GameConstants.RED -> isLegalRedCapture(c1, r1, c2, r2, boardState);
      case GameConstants.BLACK ->
          isLegalBlackCapture(c1, r1, c2, r2, boardState);
      case GameConstants.BLACK_KING ->
          isLegalKingCapture(c1, r1, c2, r2, boardState, true);
      case GameConstants.RED_KING ->
          isLegalKingCapture(c1, r1, c2, r2, boardState, false);
      default -> false;
    };
  }

  private static boolean isNotJumpMove(final int r1, final int c1, final int r2,
                                       final int c2) {
    return Math.abs(c2 - c1) != 2 || Math.abs(r2 - r1) != 2;
  }

  private static boolean isLegalRedCapture(final int c1, final int r1,
                                           final int c2, final int r2,
                                           final BoardState boardState) {
    if (isNotJumpMove(r1, c1, r2, c2)) {
      return false;
    }

    int midC = (c1 + c2) / 2;
    int midR = (r1 + r2) / 2;
    int midPiece = boardState.getPiece(midR, midC);

    return PieceRules.isBlack(midPiece) || PieceRules.isBlackKing(midPiece);
  }

  private static boolean isLegalBlackCapture(final int c1, final int r1,
                                             final int c2, final int r2,
                                             final BoardState boardState) {
    if (isNotJumpMove(r1, c1, r2, c2)) {
      return false;
    }

    int midC = (c1 + c2) / 2;
    int midR = (r1 + r2) / 2;
    int midPiece = boardState.getPiece(midR, midC);

    return PieceRules.isRed(midPiece) || PieceRules.isRedKing(midPiece);
  }

  private static boolean isLegalKingCapture(final int c1, final int r1,
                                            final int c2, final int r2,
                                            final BoardState boardState,
                                            final boolean isBlackKing) {
    int dc = Integer.compare(c2, c1);
    int dr = Integer.compare(r2, r1);

    if (DiagonalValidator.isPathClearBetween(c1, r1, c2, r2, dc, dr,
        boardState)) {
      return false;
    }

    int capturedPiece = boardState.getPiece(r2 - dr, c2 - dc);

    if (isBlackKing) {
      return PieceRules.isRed(capturedPiece) || PieceRules.isRedKing(
          capturedPiece);
    } else {
      return PieceRules.isBlack(capturedPiece) || PieceRules.isBlackKing(
          capturedPiece);
    }
  }

  public static boolean checkAllPiecesPossibleCaptures(final int color,
                                                       final int colorQueen,
                                                       final BoardState boardState) {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardState.getPiece(row, col);
        if ((piece == color || piece == colorQueen) && canCapture(col, row,
            boardState)) {
          return true;
        }
      }
    }
    return false;
  }

  private static int[] getEnemyPieces(final int piece) {
    boolean isRed =
        (piece == GameConstants.RED || piece == GameConstants.RED_KING);
    return isRed ? new int[]{GameConstants.BLACK, GameConstants.BLACK_KING}
        : new int[]{GameConstants.RED, GameConstants.RED_KING};
  }
}

