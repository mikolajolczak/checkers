package checkers;

/**
 * Utility class responsible for initializing the checkers board.
 *
 * <p>Provides methods to place pawns in their standard starting positions.
 * This class cannot be instantiated.</p>
 */
public final class BoardInitializer {

  private BoardInitializer() {
  }

  /**
   * Initializes the board with pawns in their starting positions.
   *
   * <p>Black pieces are placed in the top rows, red pieces in the bottom rows,
   * and the remaining squares are set as empty. Only valid dark squares
   * ((row + col) % 2 != 0) are populated with pawns.
   * </p>
   *
   * @param state the {@link BoardState} object to be populated with the
   *              initial arrangement of pieces
   */
  public static void setUpPawns(final BoardState state) {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {

        if ((row + col) % 2 == 0) {
          state.setPiece(row, col, GameConstants.EMPTY);
          continue;
        }

        if (row < GameConstants.NUM_STARTING_ROWS) {
          state.setPiece(row, col, GameConstants.BLACK);
        } else if (row
            >= GameConstants.BOARD_SIZE - GameConstants.NUM_STARTING_ROWS) {
          state.setPiece(row, col, GameConstants.RED);
        } else {
          state.setPiece(row, col, GameConstants.EMPTY);
        }
      }
    }
  }
}
