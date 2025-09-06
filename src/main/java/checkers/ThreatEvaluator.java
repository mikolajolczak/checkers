package checkers;

/**
 * Utility class for evaluating the threat level to a player's pieces in a
 * checkers game.
 *
 * <p>This class provides methods to assess potential captures by the opponent
 * after a proposed move by the bot.
 * </p>
 */
public final class ThreatEvaluator {

  private ThreatEvaluator() {
    throw new AssertionError("No instances.");
  }

  /**
   * Evaluates the threat posed to the player's pieces after a proposed move.
   *
   * @param decision     the proposed move by the bot
   * @param boardState   the current state of the game board
   * @param playerConfig configuration containing the player's piece colors
   * @return a negative score representing the level of threat to the
   *     player's pieces,
   *     or 0 if no capture is possible after the move
   * @throws NullPointerException if {@code decision} is null
   */
  public static int evaluatePlayerThreats(final BotDecision decision,
                                          final BoardState boardState,
                                          final PlayerConfig playerConfig) {
    if (decision == null) {
      throw new NullPointerException("decision is null");
    }

    if (!playerCanCaptureAfterMove(boardState, playerConfig)) {
      return 0;
    }

    int movedPiece = boardState.getPiece(decision.toRow(), decision.toCol());
    return PieceRules.isKing(movedPiece)
        ? -GameConstants.SCORE_PLAYER_THREAT_KING
        : -GameConstants.SCORE_PLAYER_THREAT;
  }

  private static boolean playerCanCaptureAfterMove(final BoardState boardState,
                                                   final PlayerConfig config) {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        config.getHumanColor(),
        config.getHumanKingColor(),
        boardState);
  }
}
