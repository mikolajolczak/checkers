package checkers.src.main.java;

import java.util.ArrayList;

public final class KingMoveGenerator {

  public static void findKingMoves(int row, int col, int piece,
                            ArrayList<BotDecision> moves, BoardState boardState) {
    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE; dist++) {
        int newRow = row + dist * dir[0];
        int newCol = col + dist * dir[1];

        if (!PositionValidator.isValidPosition(newRow, newCol)) {
          break;
        }

        if (MoveRules.isLegalMove(newCol, newRow, col, row, piece, boardState)) {
          if (DiagonalValidator.hasObstaclesBetween(col, row, newCol, newRow, boardState)) {
            moves.add(new BotDecision(row, col, newRow, newCol, GameConstants.MOVE));
          }
        } else {
          break;
        }
      }
    }
  }
}

