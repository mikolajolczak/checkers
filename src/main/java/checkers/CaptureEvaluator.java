package checkers;

public final class CaptureEvaluator {

  private CaptureEvaluator() {
  }

  public static int evaluateCaptureOpportunities(final BoardState boardState,
                                                 final PlayerConfiguration playerConfiguration) {
    if (botCanCaptureAfterMove(boardState, playerConfiguration)) {
      return GameConstants.SCORE_TAKE_POSSIBLE;
    }
    return 0;
  }

  private static boolean botCanCaptureAfterMove(final BoardState boardState,
                                                final PlayerConfiguration playerConfiguration) {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        playerConfiguration.getBotColor(),
        playerConfiguration.getBotKingColor(),
        boardState);
  }
}
