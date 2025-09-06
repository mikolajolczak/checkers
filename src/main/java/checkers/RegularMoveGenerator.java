package checkers;

import java.util.ArrayList;

/**
 * Utility class for generating all possible moves for regular (non-king)
 * checkers pieces on the board.
 *
 * <p>This class provides methods to determine the legal moves for a piece
 * based on its color and current position. RED pieces move upwards on the
 * board, while BLACK pieces move downwards.</p>
 */
public final class RegularMoveGenerator {

  private RegularMoveGenerator() {
  }

  /**
   * Finds all legal moves for a regular (non-king) piece located at the
   * specified row and column,
   * and adds them to the provided list of potential moves.
   *
   * <p>The direction of movement is determined by the piece color: RED pieces
   * move upwards,
   * while the opposing pieces move downwards.
   * </p>
   *
   * @param row        the current row of the piece
   * @param col        the current column of the piece
   * @param piece      the type of the piece (e.g., RED or BLACK)
   * @param moves      the list to which all valid moves will be added
   * @param boardState the current state of the game board
   */
  public static void findRegularPieceMoves(final int row, final int col,
                                           final int piece,
                                           final ArrayList<BotDecision> moves,
                                           final BoardState boardState) {
    int direction = (piece == GameConstants.RED) ? -1 : 1;

    for (int[] dir : new int[][]{{direction, -1}, {direction, 1}}) {
      int newRow = row + dir[0];
      int newCol = col + dir[1];

      if (PositionValidator.isValidPosition(newRow, newCol)
          && MoveRules.isLegalMove(newCol, newRow, col, row, piece,
          boardState)) {
        moves.add(
            new BotDecision(row, col, newRow, newCol, GameConstants.MOVE));
      }
    }
  }
}

