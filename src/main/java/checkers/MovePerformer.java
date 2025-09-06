package checkers;

/**
 * Represents a service that performs moves on a checkers board.
 *
 * <p>This class is responsible for executing a piece's move from a source
 * position to a target position and handling promotion if the move
 * results in a piece reaching the promotion row.
 * </p>
 *
 * @param promotionService the service responsible for promoting pieces
 * @param boardState       the current state of the checkers board
 */
public record MovePerformer(PromotionService promotionService,
                            BoardState boardState) {
  /**
   * Executes a move from the specified source position to the target
   * position on the board. After the move is performed, it checks
   * whether the piece should be promoted and performs promotion if needed.
   *
   * @param fromRow the starting row of the piece
   * @param fromCol the starting column of the piece
   * @param toRow   the destination row of the piece
   * @param toCol   the destination column of the piece
   */
  public void performMove(final int fromRow, final int fromCol, final int toRow,
                          final int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol, pieceColor,
        boardState);
    promotionService.promoteIfNeeded(toRow, toCol, pieceColor);
  }
}
