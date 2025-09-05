package checkers;

import java.util.ArrayList;

public final class CaptureGenerator {

  private CaptureGenerator() {
    throw new UnsupportedOperationException("Cannot instantiate utility class");
  }

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
                GameConstants.QUEEN_TAKE));
          }
        }
      }
    }
  }
}
