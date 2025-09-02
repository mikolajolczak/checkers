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

  public MouseInputHandler(final MoveValidator moveValidatorParam,
                           final CaptureHandler captureHandlerParam,
                           final SelectionState selectionStateParam,
                           final UIController uiControllerParam,
                           final MoveCoordinator moveCoordinatorParam) {
    this.moveValidator = moveValidatorParam;
    this.captureHandler = captureHandlerParam;
    this.selectionState = selectionStateParam;
    this.uiController = uiControllerParam;
    this.moveCoordinator = moveCoordinatorParam;
  }

  public void handleMouseInput(final int row, final int col) {
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

  private void handleFirstClick(final int row, final int col) {
    if (!moveValidator.canSelectPiece(row, col)) {
      return;
    }

    firstClickRow = row;
    firstClickCol = col;

    selectionState.setSelectedColumn(col);
    selectionState.setSelectedRow(row);

    firstClick = false;
  }

  private void handleSecondClick(final int row, final int col) {
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
