package checkers.src.main.java;

import java.util.ArrayList;

public class KingMoveGenerator {

  private final MoveRules moveRules;
  private final PositionValidator positionValidator;
  private final DiagonalValidator diagonalValidator;

  public KingMoveGenerator(MoveRules moveRules, PositionValidator positionValidator,
                           DiagonalValidator diagonalValidator) {
    this.moveRules = moveRules;
    this.positionValidator = positionValidator;
    this.diagonalValidator = diagonalValidator;
  }

  public void findKingMoves(int row, int col, int piece,
                            ArrayList<BotDecision> moves, BoardState boardState) {
    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE; dist++) {
        int newRow = row + dist * dir[0];
        int newCol = col + dist * dir[1];

        if (!positionValidator.isValidPosition(newRow, newCol)) {
          break;
        }

        if (moveRules.isLegalMove(newCol, newRow, col, row, piece, boardState)) {
          if (diagonalValidator.hasObstaclesBetween(col, row, newCol, newRow, boardState)) {
            moves.add(new BotDecision(row, col, newRow, newCol, GameConstants.MOVE));
          }
        } else {
          break;
        }
      }
    }
  }
}

