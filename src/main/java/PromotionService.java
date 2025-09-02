package checkers.src.main.java;

public record PromotionService(BoardState boardState) {

  public void promoteIfNeeded(int row, int col, int color) {
    if (color == GameConstants.RED && row == 0) {
      boardState.setPiece(row, col, GameConstants.RED_KING);
    } else if (color == GameConstants.BLACK
        && row == GameConstants.LAST_ROW_INDEX) {
      boardState.setPiece(row, col, GameConstants.BLACK_KING);
    }
  }
}
