package checkers.src.main.java;

public class CaptureEvaluator {

  private final CaptureRules captureRules;

  public CaptureEvaluator(CaptureRules captureRules) {
    this.captureRules = captureRules;
  }

  public int evaluateCaptureOpportunities(BoardState boardState,
                                          PlayerConfiguration playerConfiguration) {
    if (botCanCaptureAfterMove(boardState, playerConfiguration)) {
      return GameConstants.SCORE_TAKE_POSSIBLE;
    }
    return 0;
  }

  private boolean botCanCaptureAfterMove(BoardState boardState,
                                         PlayerConfiguration playerConfiguration) {
    return captureRules.checkAllPiecesPossibleCaptures(
        playerConfiguration.getBotColor(),
        playerConfiguration.getBotKingColor(),
        boardState);
  }
}
