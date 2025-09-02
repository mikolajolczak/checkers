package checkers.src.main.java;

public class MoveRules {

  private final PositionValidator positionValidator;

  public MoveRules(PositionValidator positionValidator) {
    this.positionValidator = positionValidator;
  }

  public boolean canMove(int col, int row, BoardState boardState) {
    int piece = boardState.getPiece(row, col);

    if (piece == GameConstants.RED) {
      return canRedPieceMove(col, row, boardState);
    } else if (piece == GameConstants.BLACK) {
      return canBlackPieceMove(col, row, boardState);
    } else {
      return PieceRules.isKing(piece);
    }
  }

  private boolean canRedPieceMove(int col, int row, BoardState boardState) {
    return (col < GameConstants.LAST_ROW_INDEX &&
        PieceRules.isEmpty(boardState.getPiece(row - 1, col + 1))) ||
        (col > 0 &&
            PieceRules.isEmpty(boardState.getPiece(row - 1, col - 1)));
  }

  private boolean canBlackPieceMove(int col, int row, BoardState boardState) {
    return (col < GameConstants.LAST_ROW_INDEX &&
        PieceRules.isEmpty(boardState.getPiece(row + 1, col + 1))) ||
        (col > 0 &&
            PieceRules.isEmpty(boardState.getPiece(row + 1, col - 1)));
  }

  public boolean isLegalMove(int c2, int r2, int c1, int r1, int color, BoardState boardState) {
    if (!PieceRules.isEmpty(boardState.getPiece(r2, c2))) {
      return false;
    }

    return switch (color) {
      case GameConstants.RED_KING, GameConstants.BLACK_KING ->
          isLegalKingMove(c1, r1, c2, r2, boardState);
      case GameConstants.RED ->
          Math.abs(c2 - c1) == 1 && r1 - r2 == 1;
      case GameConstants.BLACK ->
          Math.abs(c2 - c1) == 1 && r2 - r1 == 1;
      default -> false;
    };
  }

  private boolean isLegalKingMove(int c1, int r1, int c2, int r2, BoardState boardState) {
    if (positionValidator.isNotOnSameDiagonal(c1, r1, c2, r2)) {
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

