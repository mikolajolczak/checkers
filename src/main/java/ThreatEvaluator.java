package checkers.src.main.java;

public final class ThreatEvaluator {

  public static int evaluatePlayerThreats(BotDecision decision, BoardState boardState,
                                   PlayerConfiguration playerConfiguration) {
    if (!playerCanCaptureAfterMove(boardState, playerConfiguration)) {
      return 0;
    }

    int movedPiece = boardState.getPiece(decision.toRow(), decision.toCol());
    return PieceRules.isKing(movedPiece)
        ? -GameConstants.SCORE_PLAYER_THREAT_KING
        : -GameConstants.SCORE_PLAYER_THREAT;
  }

  private static boolean playerCanCaptureAfterMove(BoardState boardState,
                                            PlayerConfiguration playerConfiguration) {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        playerConfiguration.getHumanColor(),
        playerConfiguration.getHumanKingColor(),
        boardState);
  }
}