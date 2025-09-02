package checkers.src.main.java;

public class DiagonalValidator {

  private final PositionValidator positionValidator;

  public DiagonalValidator(PositionValidator positionValidator) {
    this.positionValidator = positionValidator;
  }

  public boolean diagonalHasPieces(int c1, int r1, int c2, int r2, int dc,
                                   int dr, BoardState boardState) {
    for (int i = r1 + dr, j = c1 + dc; i != r2 && j != c2; i += dr, j += dc) {
      if (!positionValidator.isValidPosition(i, j)) {
        break;
      }
      if (Math.abs(i - r2) <= 1 && Math.abs(j - c2) <= 1) {
        break;
      }
      if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
        return true;
      }
    }
    return false;
  }

  public boolean hasObstaclesBetween(int fromCol, int fromRow, int toCol,
                                     int toRow, BoardState boardState) {
    if (Math.abs(toRow - fromRow) <= 1) {
      return true;
    }

    int dc = Integer.compare(toCol, fromCol);
    int dr = Integer.compare(toRow, fromRow);

    return !diagonalHasPieces(fromCol, fromRow, toCol, toRow, dc, dr, boardState);
  }
}

