package checkers.src.main.java;

public final class PieceRules {

  private PieceRules() {
  }

  public static boolean isRed(final int piece) {
    return piece == GameConstants.RED;
  }

  public static boolean isBlack(final int piece) {
    return piece == GameConstants.BLACK;
  }

  public static boolean isRedKing(final int piece) {
    return piece == GameConstants.RED_KING;
  }

  public static boolean isBlackKing(final int piece) {
    return piece == GameConstants.BLACK_KING;
  }

  public static boolean isKing(final int piece) {
    return piece == GameConstants.RED_KING || piece == GameConstants.BLACK_KING;
  }

  public static boolean isEmpty(final int piece) {
    return piece == GameConstants.EMPTY;
  }
}
