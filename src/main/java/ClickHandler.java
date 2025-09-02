package checkers.src.main.java;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class ClickHandler extends MouseAdapter {

  private final MoveHandler moveHandler;
  private final CaptureHandler captureHandler;
  private final BoardPanel panel;
  private final SelectionState selectionState;
  private boolean firstClick = true;
  private int firstClickRow;
  private int firstClickCol;

  public ClickHandler(BoardPanel panel, MoveHandler moveHandler, CaptureHandler captureHandler,
                      SelectionState selectionStateParam) {
    this.panel = panel;
    this.moveHandler = moveHandler;
    this.captureHandler = captureHandler;
    selectionState = selectionStateParam;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    int col = e.getX() / GameConstants.SQUARE_SIZE;
    int row = e.getY() / GameConstants.SQUARE_SIZE;

    if (!moveHandler.isValidPosition(row, col)) return;

    if (firstClick) {
      handleFirstClick(row, col);
    } else {
      handleSecondClick(row, col);
    }
    moveHandler.refreshBoard();
  }

  private void handleFirstClick(int row, int col) {
    if (!moveHandler.canSelectPiece(row, col)) return;

    firstClickRow = row;
    firstClickCol = col;

    selectionState.setSelectedColumn(col);
    selectionState.setSelectedRow(row);

    firstClick = false;
  }

  private void handleSecondClick(int row, int col) {
    selectionState.setSelectedColumn(GameConstants.BOARD_SIZE);
    selectionState.setSelectedRow(GameConstants.BOARD_SIZE);

    if (moveHandler.mustTake()) {
      captureHandler.handleCapture(firstClickRow, firstClickCol, row, col);
    } else {
      moveHandler.handleMove(firstClickRow, firstClickCol, row, col);
    }

    firstClick = true;
  }
}
