package checkers.src.main.java;

public class BoardState {
  private final int[][] pieces;
  private int selectedRow = -1;
  private int selectedColumn = -1;

  public BoardState() {
    pieces = new int[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
    setUpPawns();
  }

  public void setUpPawns() {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        if (row % 2 != col % 2) {
          if (row < GameConstants.NUM_STARTING_ROWS) {
            pieces[row][col] = GameConstants.BLACK;
          } else if (row
              >= GameConstants.BOARD_SIZE - GameConstants.NUM_STARTING_ROWS) {
            pieces[row][col] = GameConstants.RED;
          } else {
            pieces[row][col] = GameConstants.EMPTY;
          }
        } else {
          pieces[row][col] = GameConstants.EMPTY;
        }
      }
    }
  }

  public int getPiece(int row, int col) { return pieces[row][col]; }
  public void setPiece(int row, int col, int piece) { pieces[row][col] = piece; }

  public void setSelected(int row, int col) { this.selectedRow = row; this.selectedColumn = col; }
  public int getSelectedRow() { return selectedRow; }
  public int getSelectedColumn() { return selectedColumn; }
}
