package checkers.src.main.java;

public final class DiagonalValidator {

  private DiagonalValidator() {
  }

  public static boolean diagonalHasPieces(final int c1, final int r1,
                                          final int c2, final int r2,
                                          final int dc,
                                          final int dr,
                                          final BoardState boardState) {
    for (int i = r1 + dr, j = c1 + dc; i != r2 && j != c2; i += dr, j += dc) {
      if (!PositionValidator.isValidPosition(i, j)) {
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

  public static boolean hasObstaclesBetween(final int fromCol,
                                            final int fromRow, final int toCol,
                                            final int toRow,
                                            final BoardState boardState) {
    if (Math.abs(toRow - fromRow) <= 1) {
      return true;
    }

    int dc = Integer.compare(toCol, fromCol);
    int dr = Integer.compare(toRow, fromRow);

    return !diagonalHasPieces(fromCol, fromRow, toCol, toRow, dc, dr,
        boardState);
  }
}

