package checkers;

/**
 * Provides utility methods to validate moves in a checkers game.
 *
 * <p>This class is final and contains only static methods to check whether a
 * piece
 * can move and to validate the legality of specific moves, including normal
 * pieces
 * and kings. It interacts with {@link BoardState} to inspect the current state
 * of the game board and {@link PieceRules} for piece-specific movement rules.
 */
public final class MoveRules {

  private MoveRules() {
  }

  /**
   * Checks whether a piece at the given board position can move.
   *
   * @param col        the column index of the piece
   * @param row        the row index of the piece
   * @param boardState the current state of the game board
   * @return true if the piece can make at least one valid move, false otherwise
   */
  public static boolean canMove(final int col, final int row,
                                final BoardState boardState) {
    int piece = boardState.getPiece(row, col);

    if (piece == GameConstants.RED) {
      return canRedPieceMove(col, row, boardState);
    } else if (piece == GameConstants.BLACK) {
      return canBlackPieceMove(col, row, boardState);
    } else {
      return PieceRules.isKing(piece);
    }
  }

  private static boolean canRedPieceMove(final int col, final int row,
                                         final BoardState boardState) {
    return (col < GameConstants.LAST_ROW_INDEX && PieceRules.isEmpty(
        boardState.getPiece(row - 1, col + 1))) || (col > 0
        && PieceRules.isEmpty(boardState.getPiece(row - 1, col - 1)));
  }

  private static boolean canBlackPieceMove(final int col, final int row,
                                           final BoardState boardState) {
    return (col < GameConstants.LAST_ROW_INDEX && PieceRules.isEmpty(
        boardState.getPiece(row + 1, col + 1))) || (col > 0
        && PieceRules.isEmpty(boardState.getPiece(row + 1, col - 1)));
  }

  /**
   * Determines if a move from one position to another is legal for a piece
   * of a given color.
   *
   * @param c2         the destination column
   * @param r2         the destination row
   * @param c1         the starting column
   * @param r1         the starting row
   * @param color      the color/type of the piece
   * @param boardState the current state of the game board
   * @return true if the move is legal, false otherwise
   */
  public static boolean isLegalMove(final int c2, final int r2, final int c1,
                                    final int r1, final int color,
                                    final BoardState boardState) {
    if (!PieceRules.isEmpty(boardState.getPiece(r2, c2))) {
      return false;
    }

    return switch (color) {
      case GameConstants.RED_KING, GameConstants.BLACK_KING ->
          isLegalKingMove(c1, r1, c2, r2, boardState);
      case GameConstants.RED -> Math.abs(c2 - c1) == 1 && r1 - r2 == 1;
      case GameConstants.BLACK -> Math.abs(c2 - c1) == 1 && r2 - r1 == 1;
      default -> false;
    };
  }

  private static boolean isLegalKingMove(final int c1, final int r1,
                                         final int c2, final int r2,
                                         final BoardState boardState) {
    if (PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2)) {
      return false;
    }

    int dc = Integer.compare(c2, c1);
    int dr = Integer.compare(r2, r1);

    for (int i = r1 + dr, j = c1 + dc; i != r2 || j != c2; i += dr, j += dc) {
      if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
        return false;
      }
    }
    return true;
  }
}

