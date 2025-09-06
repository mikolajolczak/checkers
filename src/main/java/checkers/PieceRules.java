package checkers;

/**
 * Utility class containing rules and helper methods for checkers pieces.
 *
 * <p>This class provides static methods to determine the type or state of a
 * piece,
 * such as whether it is a red piece, black piece, king, or empty square.
 * It is not meant to be instantiated.
 * </p>
 */
public final class PieceRules {
  private PieceRules() {
  }

  /**
   * Checks if the given piece is a red piece.
   *
   * @param piece the piece to check
   * @return true if the piece is red, false otherwise
   */
  public static boolean isRed(final int piece) {
    return piece == GameConstants.RED;
  }

  /**
   * Checks if the given piece is a black piece.
   *
   * @param piece the piece to check
   * @return true if the piece is black, false otherwise
   */
  public static boolean isBlack(final int piece) {
    return piece == GameConstants.BLACK;
  }

  /**
   * Checks if the given piece is a red king.
   *
   * @param piece the piece to check
   * @return true if the piece is a red king, false otherwise
   */
  public static boolean isRedKing(final int piece) {
    return piece == GameConstants.RED_KING;
  }

  /**
   * Checks if the given piece is a black king.
   *
   * @param piece the piece to check
   * @return true if the piece is a black king, false otherwise
   */
  public static boolean isBlackKing(final int piece) {
    return piece == GameConstants.BLACK_KING;
  }

  /**
   * Checks if the given piece is any type of king (red or black).
   *
   * @param piece the piece to check
   * @return true if the piece is a king, false otherwise
   */
  public static boolean isKing(final int piece) {
    return piece == GameConstants.RED_KING || piece == GameConstants.BLACK_KING;
  }

  /**
   * Checks if the given piece is empty.
   *
   * @param piece the piece to check
   * @return true if the piece is empty, false otherwise
   */
  public static boolean isEmpty(final int piece) {
    return piece == GameConstants.EMPTY;
  }
}
