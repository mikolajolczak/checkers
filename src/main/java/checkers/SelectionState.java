package checkers;

/**
 * Represents the current selection state on a checkers board.
 *
 * <p>This class keeps track of the currently selected row and column on the
 * board.
 * By default, no position is selected, which is indicated by setting both
 * row and column to {@link GameConstants#BOARD_SIZE}.
 * </p>
 *
 * <p>Provides getter and setter methods to update and retrieve the selected
 * position.
 * </p>
 */
public final class SelectionState {

  /**
   * The currently selected row on the board. Defaults to BOARD_SIZE (no
   * selection).
   */
  private int selectedRow = GameConstants.BOARD_SIZE;

  /**
   * The currently selected column on the board. Defaults to BOARD_SIZE (no
   * selection).
   */
  private int selectedColumn = GameConstants.BOARD_SIZE;

  /**
   * Returns the currently selected row.
   *
   * @return the selected row index
   */
  public int getSelectedRow() {
    return selectedRow;
  }

  /**
   * Sets the currently selected row.
   *
   * @param row the row index to select
   */
  public void setSelectedRow(final int row) {
    this.selectedRow = row;
  }

  /**
   * Returns the currently selected column.
   *
   * @return the selected column index
   */
  public int getSelectedColumn() {
    return selectedColumn;
  }

  /**
   * Sets the currently selected column.
   *
   * @param col the column index to select
   */
  public void setSelectedColumn(final int col) {
    this.selectedColumn = col;
  }
}
