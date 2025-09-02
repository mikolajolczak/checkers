package checkers.src.main.java;

import java.util.ArrayList;

public class CaptureGenerator {

  private final CaptureRules captureRules;
  private final PositionValidator positionValidator;
  private final DiagonalValidator diagonalValidator;

  public CaptureGenerator(CaptureRules captureRules, PositionValidator positionValidator,
                          DiagonalValidator diagonalValidator) {
    this.captureRules = captureRules;
    this.positionValidator = positionValidator;
    this.diagonalValidator = diagonalValidator;
  }

  public void findRegularCaptures(int row, int col, int piece,
                                  ArrayList<BotDecision> moves, BoardState boardState) {
    int direction = (piece == GameConstants.RED) ? -1 : 1;

    for (int[] dir : new int[][]{{direction, -1}, {direction, 1}}) {
      int newRow = row + 2 * dir[0];
      int newCol = col + 2 * dir[1];

      if (positionValidator.isValidPosition(newRow, newCol) &&
          captureRules.isLegalCapture(newCol, newRow, col, row, piece, boardState)) {
        moves.add(new BotDecision(row, col, newRow, newCol, GameConstants.TAKE));
      }
    }
  }

  public void findKingCaptures(int row, int col, int piece,
                               ArrayList<BotDecision> moves, BoardState boardState) {
    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE; dist++) {
        int newRow = row + dist * dir[0];
        int newCol = col + dist * dir[1];

        if (!positionValidator.isValidPosition(newRow, newCol)) {
          break;
        }

        if (captureRules.isLegalCapture(newCol, newRow, col, row, piece, boardState)) {
          if (diagonalValidator.hasObstaclesBetween(col, row, newCol, newRow, boardState)) {
            moves.add(new BotDecision(row, col, newRow, newCol, GameConstants.QUEEN_TAKE));
          }
        }
      }
    }
  }
}
