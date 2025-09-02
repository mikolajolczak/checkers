package checkers.src.main.java;

public class ThreatEvaluator {

  private final CaptureRules captureRules;

  public ThreatEvaluator() {
    this.captureRules = new CaptureRules();
  }

  public int evaluatePlayerThreats(BotDecision decision, BoardState boardState,
                                   PlayerConfiguration playerConfiguration) {
    if (!playerCanCaptureAfterMove(boardState, playerConfiguration)) {
      return 0;
    }

    int movedPiece = boardState.getPiece(decision.toRow(), decision.toCol());
    return PieceRules.isKing(movedPiece)
        ? -GameConstants.SCORE_PLAYER_THREAT_KING
        : -GameConstants.SCORE_PLAYER_THREAT;
  }

  private boolean playerCanCaptureAfterMove(BoardState boardState,
                                            PlayerConfiguration playerConfiguration) {
    return captureRules.checkAllPiecesPossibleCaptures(
        playerConfiguration.getHumanColor(),
        playerConfiguration.getHumanKingColor(),
        boardState);
  }
}