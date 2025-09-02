
package checkers.src.main.java;

public final class PositionValidator {

  public static boolean isValidPosition(int row, int col) {
    return row >= 0 && row < GameConstants.BOARD_SIZE && col >= 0
        && col < GameConstants.BOARD_SIZE;
  }


  public static boolean isNotOnSameDiagonal(int c1, int r1, int c2, int r2) {
    return Math.abs(r2 - r1) != Math.abs(c2 - c1) && r1 + c1 != c2 + r2;
  }
}
