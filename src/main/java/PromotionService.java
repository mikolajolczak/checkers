package checkers.src.main.java;

public class PromotionService {
  private final BoardState boardState;

  public PromotionService(BoardState boardStateParam) {
    boardState = boardStateParam;
  }

  public boolean isQueen(int color) {
    return color == GameConstants.BLACK_KING || color == GameConstants.RED_KING;
  }

  public void promoteIfNeeded(int row, int col, int color) {
    if (color == GameConstants.RED && row == 0) {
      boardState.setPiece(row, col, GameConstants.RED_KING);
    } else if (color == GameConstants.BLACK
        && row == GameConstants.LAST_ROW_INDEX) {
      boardState.setPiece(row, col, GameConstants.BLACK_KING);
    }
  }
}
