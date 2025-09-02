package checkers;

public final class PromotionEvaluator {

  private PromotionEvaluator() {
  }

  public static int evaluatePromotionChance(final BotDecision decision,
                                            final BoardState boardState,
                                            final PlayerConfiguration playerConfiguration) {
    int movedPiece = boardState.getPiece(decision.toRow(), decision.toCol());

    if (canPromoteToQueen(boardState, movedPiece,
        playerConfiguration.getBotColor())) {
      return GameConstants.SCORE_CHANCE_FOR_QUEEN;
    }
    return 0;
  }

  public static boolean canPromoteToQueen(final BoardState boardState,
                                          final int movedPiece,
                                          final int botColor) {
    return isChanceForQueen(botColor, boardState, movedPiece);
  }

  public static boolean isChanceForQueen(final int colorToCheck,
                                         final BoardState boardState,
                                         final int pieceType) {
    if (PieceRules.isKing(pieceType)) {
      return false;
    }

    int promotionRow = getPromotionRow(colorToCheck);
    return hasPieceOnPromotionRow(boardState, colorToCheck, promotionRow);
  }

  private static int getPromotionRow(final int colorToCheck) {
    return (colorToCheck == GameConstants.BLACK)
        ? GameConstants.LAST_ROW_INDEX
        : 0;
  }

  private static boolean hasPieceOnPromotionRow(final BoardState boardState,
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

