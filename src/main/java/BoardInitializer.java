package checkers.src.main.java;

public final class BoardInitializer {

  private BoardInitializer() {
  }

  public static void setUpPawns(BoardState state) {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {

        if ((row + col) % 2 == 0) {
          state.setPiece(row, col, GameConstants.EMPTY);
          continue;
        }

        if (row < GameConstants.NUM_STARTING_ROWS) {
          state.setPiece(row, col, GameConstants.BLACK);
        } else if (row >= GameConstants.BOARD_SIZE - GameConstants.NUM_STARTING_ROWS) {
          state.setPiece(row, col, GameConstants.RED);
        } else {
          state.setPiece(row, col, GameConstants.EMPTY);
        }
      }
    }
  }
}
