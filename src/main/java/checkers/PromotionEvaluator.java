package checkers;

/**
 * Utility class for evaluating promotion opportunities in a checkers game.
 *
 * <p>This class provides static methods to determine whether a piece can be
 * promoted to a king based on the current board state and the bot's color.
 * It is designed to be non-instantiable and only provides evaluation logic.
 *
 * <p>All methods are static and should be accessed directly via the class name.
 */
public final class PromotionEvaluator {

  private PromotionEvaluator() {
    throw new AssertionError("Instantiation not allowed");
  }

  /**
   * Evaluates the chance that a piece moved by the bot can be promoted to a
   * king.
   *
   * @param decision     the bot's move decision
   * @param boardState   the current state of the board
   * @param playerConfig the bot's player configuration
   * @return the score representing the chance for promotion to king; returns
   *     0 if promotion is not possible
   */
  public static int evaluatePromotionChance(final BotDecision decision,
                                            final BoardState boardState,
                                            final PlayerConfig playerConfig) {
    int movedPiece = boardState.getPiece(decision.toRow(), decision.toCol());

    if (canPromoteToKing(boardState, movedPiece,
        playerConfig.getBotColor())) {
      return GameConstants.SCORE_CHANCE_FOR_KING;
    }
    return 0;
  }

  /**
   * Determines whether a specific piece can be promoted to a king.
   *
   * @param boardState the current state of the board
   * @param movedPiece the piece being moved
   * @param botColor   the color of the bot's pieces
   * @return true if the piece can be promoted to a king, false otherwise
   */
  public static boolean canPromoteToKing(final BoardState boardState,
                                         final int movedPiece,
                                         final int botColor) {
    return isChanceForKing(botColor, boardState, movedPiece);
  }

  /**
   * Checks if there is a chance for a piece of a given color to be promoted
   * to king.
   *
   * @param colorToCheck the color of the piece
   * @param boardState   the current state of the board
   * @param pieceType    the type of the piece
   * @return true if the piece can be promoted, false if it is already a king
   *     or cannot be promoted
   */
  public static boolean isChanceForKing(final int colorToCheck,
                                        final BoardState boardState,
                                        final int pieceType) {
    if (PieceRules.isKing(pieceType)) {
      return false;
    }

    int promotionRow = getPromotionRow(colorToCheck);
    return hasPieceOnPromotionRow(boardState, colorToCheck, promotionRow);
  }

  /**
   * Returns the row index on which pieces of a specific color get promoted
   * to king.
   *
   * @param colorToCheck the color of the piece
   * @return the row index for promotion
   */
  public static int getPromotionRow(final int colorToCheck) {
    return (colorToCheck == GameConstants.BLACK)
        ? GameConstants.LAST_ROW_INDEX
        : 0;
  }

  /**
   * Checks if there is a piece of the specified color on the promotion row.
   *
   * @param boardState   the current state of the board
   * @param colorToCheck the color of the piece
   * @param targetRow    the row to check for promotion
   * @return true if a piece of the specified color exists on the promotion
   *     row, false otherwise
   */
  public static boolean hasPieceOnPromotionRow(final BoardState boardState,
                                               final int colorToCheck,
                                               final int targetRow) {
    for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
      if (boardState.getPiece(targetRow, col) == colorToCheck) {
        return true;
      }
    }
    return false;
  }
}

