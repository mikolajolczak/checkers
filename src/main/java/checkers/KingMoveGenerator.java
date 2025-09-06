package checkers;

import java.util.ArrayList;

/**
 * Utility class for generating all possible legal moves for a king piece
 * in a checkers game.
 *
 * <p>This class provides a method to iterate through all diagonal directions
 * from a given position, checking for valid moves based on the current board
 * state, piece type, and obstacles. It is designed to be used by AI bots
 * or game logic to determine available king moves.</p>
 */
public final class KingMoveGenerator {

  private KingMoveGenerator() {
  }

  /**
   * Generates all possible legal moves for a king piece from a given position
   * on the board and adds them to the provided list of moves.
   *
   * <p>This method iterates through all diagonal directions and checks each
   * square along the diagonals up to the board boundaries. For each potential
   * move, it validates whether the move is legal and whether there are any
   * obstacles between the current position and the target position. If both
   * conditions are satisfied, a new {@link BotDecision} representing the move
   * is added to the provided moves list.
   * </p>
   *
   * @param row        the current row of the king piece
   * @param col        the current column of the king piece
   * @param piece      the type or color of the piece to move
   * @param moves      the list to which legal moves will be added
   * @param boardState the current state of the board
   */
  public static void findKingMoves(final int row, final int col,
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

        if (MoveRules.isLegalMove(newCol, newRow, col, row, piece,
            boardState)) {
          if (DiagonalValidator.hasObstaclesBetween(col, row, newCol, newRow,
              boardState)) {
            moves.add(
                new BotDecision(row, col, newRow, newCol, GameConstants.MOVE));
          }
        } else {
          break;
        }
      }
    }
  }
}

