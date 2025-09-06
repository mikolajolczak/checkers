package checkers;

/**
 * Represents the state of a checkers board.
 *
 * <p>The board is stored as a 2D array of integers, where each element
 * represents a piece or an empty square. This class provides methods for
 * querying, modifying, and copying the board state while ensuring
 * encapsulation through defensive copying where appropriate.</p>
 *
 * @param pieces the 2D array representing the initial arrangement of pieces
 *               on the board
 */
public record BoardState(int[][] pieces) {
  /**
   * Creates an empty board state with the default board size.
   */
  public BoardState() {
    this(new int[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE]);
  }

  /**
   * Creates a board state with a given 2D array of pieces.
   * Performs a deep copy of the array to prevent external modification.
   *
   * @param pieces the initial arrangement of pieces
   */
  public BoardState {
    int[][] copy = new int[pieces.length][];
    for (int i = 0; i < pieces.length; i++) {
      copy[i] = pieces[i].clone();
    }
    pieces = copy;
  }

  /**
   * Returns the piece at a specific position on the board.
   *
   * @param row the row index
   * @param col the column index
   * @return the piece at the given row and column
   */
  public int getPiece(final int row, final int col) {
    return pieces[row][col];
  }

  /**
   * Sets a piece at a specific position on the board.
   *
   * @param row   the row index
   * @param col   the column index
   * @param piece the piece to place
   */
  public void setPiece(final int row, final int col, final int piece) {
    pieces[row][col] = piece;
  }

  /**
   * Creates a deep copy of this board state.
   *
   * @return a new BoardState object with the same piece arrangement
   */
  public BoardState copy() {
    return new BoardState(pieces);
  }

  /**
   * Returns the entire 2D array of pieces.
   * Modifications to the returned array may affect this board state.
   *
   * @return a 2D array representing the board
   */
  public int[][] getPieces() {
    return pieces;
  }

  /**
   * Returns a specific row of pieces on the board.
   *
   * @param rowIndex the row index
   * @return an array representing the row at the given index
   */
  public int[] getPiecesRows(final int rowIndex) {
    return pieces[rowIndex];
  }
}
