package checkers;

/**
 * Utility class responsible for executing moves in a checkers game.
 *
 * <p>This class provides static methods to apply different types of moves on
 * the
 * game board, including normal moves, captures, and king captures. It ensures
 * that the board state is updated correctly according to the move performed.
 * </p>
 */
public final class MoveExecutor {

  private MoveExecutor() {
  }

  /**
   * Executes a normal move on the board by moving a piece from the specified
   * source position to the target position.
   *
   * @param fromRow the row index of the piece to move
   * @param fromCol the column index of the piece to move
   * @param toRow   the row index of the target position
   * @param toCol   the column index of the target position
   * @param color   the color of the moving piece
   * @param board   the current state of the board
   */
  public static void executeNormalMove(final int fromRow, final int fromCol,
                                       final int toRow,
                                       final int toCol,
                                       final int color,
                                       final BoardState board) {
    board.setPiece(fromRow, fromCol, GameConstants.EMPTY);
    board.setPiece(toRow, toCol, color);
  }

  /**
   * Executes a capture move on the board by moving a piece from the specified
   * source position to the target position and removing the captured piece
   * located between them.
   *
   * @param fromRow the row index of the piece to move
   * @param fromCol the column index of the piece to move
   * @param toRow   the row index of the target position
   * @param toCol   the column index of the target position
   * @param color   the color of the moving piece
   * @param board   the current state of the board
   */
  public static void executeCapture(final int fromRow, final int fromCol,
                                    final int toRow,
                                    final int toCol,
                                    final int color, final BoardState board) {
    executeNormalMove(fromRow, fromCol, toRow, toCol, color, board);

    int capturedRow = (fromRow + toRow) / 2;
    int capturedCol = (fromCol + toCol) / 2;
    board.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }

  /**
   * Executes a king capture move on the board by moving a king piece from
   * the specified source position to the target position and removing the
   * captured piece that is adjacent in the direction of movement.
   *
   * @param fromRow the row index of the king piece to move
   * @param fromCol the column index of the king piece to move
   * @param toRow   the row index of the target position
   * @param toCol   the column index of the target position
   * @param color   the color of the king piece
   * @param board   the current state of the board
   */
  public static void executeKingCapture(final int fromRow, final int fromCol,
                                        final int toRow,
                                        final int toCol, final int color,
                                        final BoardState board) {
    executeNormalMove(fromRow, fromCol, toRow, toCol, color, board);

    int rowDir = Integer.signum(toRow - fromRow);
    int colDir = Integer.signum(toCol - fromCol);

    int capturedRow = toRow - rowDir;
    int capturedCol = toCol - colDir;
    board.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }

  /**
   * Applies a move decision to the board according to the type of move.
   * The method handles normal moves, captures, and king captures.
   *
   * @param decision          the move decision to apply
   * @param boardState        the current state of the board
   * @param playerConfigParam the player configuration providing piece colors
   */
  public static void applyMoveToBoard(final BotDecision decision,
                                      final BoardState boardState,
                                      final PlayerConfig playerConfigParam) {
    switch (decision.moveType()) {
      case GameConstants.MOVE:
        int color = boardState.getPiece(decision.fromRow(), decision.fromCol());
        executeNormalMove(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(), color, boardState);
        break;
      case GameConstants.TAKE:
        executeCapture(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(),
            playerConfigParam.getBotColor(), boardState);
        break;
      case GameConstants.KING_TAKE:
        executeKingCapture(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(),
            playerConfigParam.getBotKingColor(), boardState);
        break;
      default:
        break;
    }
  }
}
