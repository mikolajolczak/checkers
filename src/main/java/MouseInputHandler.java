package checkers.src.main.java;

public class MouseInputHandler {

  private final MoveValidator moveValidator;
  private final CaptureHandler captureHandler;
  private final SelectionState selectionState;
  private final UIController uiController;
  private final MoveCoordinator moveCoordinator;
  private boolean firstClick = true;
  private int firstClickRow;
  private int firstClickCol;

  public MouseInputHandler(MoveValidator moveValidator, CaptureHandler captureHandler,
                           SelectionState selectionState, UIController uiController,
                           MoveCoordinator moveCoordinator) {
    this.moveValidator = moveValidator;
    this.captureHandler = captureHandler;
    this.selectionState = selectionState;
    this.uiController = uiController;
    this.moveCoordinator = moveCoordinator;
  }

  public void handleMouseInput(int row, int col) {
    if (!moveValidator.isValidPosition(row, col)) {
      return;
    }

    if (firstClick) {
      handleFirstClick(row, col);
    } else {
      handleSecondClick(row, col);
    }
    uiController.refreshBoard();
  }

  private void handleFirstClick(int row, int col) {
    if (!moveValidator.canSelectPiece(row, col)) {
      return;
    }

    firstClickRow = row;
    firstClickCol = col;

    selectionState.setSelectedColumn(col);
    selectionState.setSelectedRow(row);

    firstClick = false;
  }

  private void handleSecondClick(int row, int col) {
    selectionState.setSelectedColumn(GameConstants.BOARD_SIZE);
    selectionState.setSelectedRow(GameConstants.BOARD_SIZE);

    if (moveValidator.mustTake()) {
      captureHandler.handleCapture(firstClickRow, firstClickCol, row, col);
    } else {
      moveCoordinator.handleMove(firstClickRow, firstClickCol, row, col);
    }

    firstClick = true;
  }
}