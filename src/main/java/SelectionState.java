package checkers.src.main.java;

public class SelectionState {
  private int selectedRow = GameConstants.BOARD_SIZE;
  private int selectedColumn = GameConstants.BOARD_SIZE;

  public int getSelectedRow() { return selectedRow; }
  public int getSelectedColumn() { return selectedColumn; }
  public void setSelectedRow(int row) { this.selectedRow = row; }
  public void setSelectedColumn(int col) { this.selectedColumn = col; }
}
