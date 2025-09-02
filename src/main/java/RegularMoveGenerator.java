package checkers.src.main.java;

import java.util.ArrayList;

public final class RegularMoveGenerator {

  private RegularMoveGenerator() {
  }

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

