package checkers.src.main.java;

public final class PositionValidator {

  private PositionValidator() {
  }

  public static boolean isValidPosition(final int row, final int col) {
    return row >= 0 && row < GameConstants.BOARD_SIZE && col >= 0
        && col < GameConstants.BOARD_SIZE;
  }


  public static boolean isNotOnSameDiagonal(final int c1, final int r1,
                                            final int c2, final int r2) {
    return Math.abs(r2 - r1) != Math.abs(c2 - c1) && r1 + c1 != c2 + r2;
  }
}
