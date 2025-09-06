package checkers;

import java.util.ArrayList;

/**
 * Generates all possible moves for the bot in a game of checkers
 * based on the current board state and the bot's configuration.
 *
 * <p>This class considers both regular moves and capture moves,
 * and handles standard pieces as well as king pieces. It ensures
 * that the rules of mandatory captures are respected when generating moves.</p>
 *
 * @param playerConfig the configuration for the player, including color and
 *                     piece type
 */
public record MoveGenerator(PlayerConfig playerConfig) {
  /**
   * Computes a list of all possible moves for the bot based on the current
   * board state.
   *
   * @param boardState the current state of the game board
   * @return an {@link ArrayList} of {@link BotDecision} representing all
   *     valid moves
   *     the bot can make in this turn
   */
  public ArrayList<BotDecision> getPossibleMoves(final BoardState boardState) {
    ArrayList<BotDecision> possibleMoves = new ArrayList<>();

    boolean mustTake = CaptureRules.checkAllPiecesPossibleCaptures(
        playerConfig.getBotColor(),
        playerConfig.getBotKingColor(),
        boardState);

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardState.getPiece(row, col);

        if (!isBotPiece(piece)) {
          continue;
        }

        if (mustTake && CaptureRules.canCapture(col, row, boardState)) {
          generateCaptureMoves(row, col, piece, possibleMoves, boardState);
        } else if (!mustTake && MoveRules.canMove(col, row, boardState)) {
          generateRegularMoves(row, col, piece, possibleMoves, boardState);
        }
      }
    }

    return possibleMoves;
  }

  private boolean isBotPiece(final int piece) {
    return piece == playerConfig.getBotColor()
        || piece == playerConfig.getBotKingColor();
  }

  private void generateCaptureMoves(final int row, final int col,
                                    final int piece,
                                    final ArrayList<BotDecision> moves,
                                    final BoardState boardState) {
    if (PieceRules.isKing(piece)) {
      CaptureGenerator.findKingCaptures(row, col, piece, moves, boardState);
    } else {
      CaptureGenerator.findRegularCaptures(row, col, piece, moves, boardState);
    }
  }

  private void generateRegularMoves(final int row, final int col,
                                    final int piece,
                                    final ArrayList<BotDecision> moves,
                                    final BoardState boardState) {
    if (PieceRules.isKing(piece)) {
      KingMoveGenerator.findKingMoves(row, col, piece, moves, boardState);
    } else {
      RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
          boardState);
    }
  }
}
