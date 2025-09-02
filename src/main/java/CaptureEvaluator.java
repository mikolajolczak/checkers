package checkers.src.main.java;

public final class CaptureEvaluator {

  public static int evaluateCaptureOpportunities(BoardState boardState,
                                          PlayerConfiguration playerConfiguration) {
    if (botCanCaptureAfterMove(boardState, playerConfiguration)) {
      return GameConstants.SCORE_TAKE_POSSIBLE;
    }
    return 0;
  }

  private static boolean botCanCaptureAfterMove(BoardState boardState,
                                         PlayerConfiguration playerConfiguration) {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        playerConfiguration.getBotColor(),
        playerConfiguration.getBotKingColor(),
        boardState);
  }
}
