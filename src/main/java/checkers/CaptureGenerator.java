package checkers;

import java.util.ArrayList;

/**
 * Utility class for generating all possible capture moves in a checkers game.
 *
 * <p>This class provides static methods to find capture moves for both regular
 * pieces and king pieces. It cannot be instantiated.</p>
 *
 * <p>All generated moves are added to the provided list of
 * {@link BotDecision} objects.</p>
 */
public final class CaptureGenerator {

  private CaptureGenerator() {
    throw new UnsupportedOperationException("Cannot instantiate utility class");
  }

  /**
   * Finds all possible capture moves for a regular piece at the given
   * position on the board and adds them to the provided list of moves.
   *
   * @param row        the current row of the piece
   * @param col        the current column of the piece
   * @param piece      the type of the piece (e.g., {@link GameConstants#RED})
   * @param moves      the list where generated capture moves will be added
   * @param boardState the current state of the board
   * @throws NullPointerException if {@code moves} is null
   */
  public static void findRegularCaptures(final int row, final int col,
                                         final int piece,
                                         final ArrayList<BotDecision> moves,
                                         final BoardState boardState) {

    if (moves == null) {
      throw new NullPointerException("moves is null");
    }

    int direction = (piece == GameConstants.RED) ? -1 : 1;

    for (int[] dir : new int[][]{{direction, -1}, {direction, 1}}) {
      int newRow = row + 2 * dir[0];
      int newCol = col + 2 * dir[1];

      if (PositionValidator.isValidPosition(newRow, newCol)
          && CaptureRules.isLegalCapture(newCol, newRow, col, row, piece,
          boardState)) {
        moves.add(
            new BotDecision(row, col, newRow, newCol, GameConstants.TAKE));
      }
    }
  }

  /**
   * Finds all possible capture moves for a king piece at the given
   * position on the board and adds them to the provided list of moves.
   *
   * @param row        the current row of the piece
   * @param col        the current column of the piece
   * @param piece      the type of the piece (king)
   * @param moves      the list where generated capture moves will be added
   * @param boardState the current state of the board
   */
  public static void findKingCaptures(final int row, final int col,
                                      final int piece,
                                      final ArrayList<BotDecision> moves,
                                      final BoardState boardState) {
    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE; dist++) {
        int newRow = row + dist * dir[0];
        int newCol = col + dist * dir[1];

        if (!PositionValidator.isValidPosition(newRow, newCol)) {
          break;
        }

        if (CaptureRules.isLegalCapture(newCol, newRow, col, row, piece,
            boardState)) {
          if (DiagonalValidator.hasObstaclesBetween(col, row, newCol, newRow,
              boardState)) {
            moves.add(new BotDecision(row, col, newRow, newCol,
                GameConstants.KING_TAKE));
          }
        }
      }
    }
  }
}
