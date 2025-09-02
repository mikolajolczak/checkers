package checkers;

public final class ThreatEvaluator {

  private ThreatEvaluator() {
  }

  public static int evaluatePlayerThreats(final BotDecision decision,
                                          final BoardState boardState,
                                          final PlayerConfiguration playerConfiguration) {
    if (!playerCanCaptureAfterMove(boardState, playerConfiguration)) {
      return 0;
    }

    int movedPiece = boardState.getPiece(decision.toRow(), decision.toCol());
    return PieceRules.isKing(movedPiece)
        ? -GameConstants.SCORE_PLAYER_THREAT_KING
        : -GameConstants.SCORE_PLAYER_THREAT;
  }

  private static boolean playerCanCaptureAfterMove(final BoardState boardState,
                                                   final PlayerConfiguration playerConfiguration) {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        playerConfiguration.getHumanColor(),
        playerConfiguration.getHumanKingColor(),
        boardState);
  }
}
