package checkers.src.main.java;

public class PieceRules {

  public static boolean isRed(int piece) {
    return piece == GameConstants.RED;
  }

  public static boolean isBlack(int piece) {
    return piece == GameConstants.BLACK;
  }

  public static boolean isRedKing(int piece) {
    return piece == GameConstants.RED_KING;
  }

  public static boolean isBlackKing(int piece) {
    return piece == GameConstants.BLACK_KING;
  }

  public static boolean isKing(int piece) {
    return piece == GameConstants.RED_KING || piece == GameConstants.BLACK_KING;
  }

  public static boolean isEmpty(int piece) {
    return piece == GameConstants.EMPTY;
  }
}
