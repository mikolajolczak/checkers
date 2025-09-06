package checkers;

/**
 * Handles mouse input events on the checkers board, managing piece selection,
 * move execution, and captures according to the rules of the game.
 *
 * <p>This class coordinates with other components such as
 * {@link MoveValidator},
 * {@link CaptureHandler}, {@link SelectionState}, {@link UiController}, and
 * {@link MoveCoordinator} to ensure that user interactions result in valid
 * game actions. It maintains the state of the first and second clicks in a
 * move sequence and updates the UI accordingly.</p>
 */
public final class MouseInputHandler {

  /**
   * Validates whether moves and selections are legal according to game rules.
   */
  private final MoveValidator moveValidator;

  /**
   * Handles the capture of opponent pieces when a mandatory capture is
   * available.
   */
  private final CaptureHandler captureHandler;

  /**
   * Maintains the currently selected row and column on the board.
   */
  private final SelectionState selectionState;

  /**
   * Controls UI updates, such as refreshing the game board.
   */
  private final UiController uiController;

  /**
   * Coordinates the execution of standard moves on the board.
   */
  private final MoveCoordinator moveCoordinator;

  /**
   * Indicates whether the next click is the first click in a move sequence.
   */
  private boolean firstClick = true;

  /**
   * Row of the first clicked piece in a move sequence.
   */
  private int firstClickRow;

  /**
   * Column of the first clicked piece in a move sequence.
   */
  private int firstClickCol;

  /**
   * Constructs a MouseInputHandler with the given dependencies.
   *
   * @param moveValidatorParam   validates moves and piece selections
   * @param captureHandlerParam  handles captures of opponent pieces
   * @param selectionStateParam  maintains the current selection state
   * @param uiControllerParam    updates and refreshes the UI
   * @param moveCoordinatorParam executes standard moves
   */
  public MouseInputHandler(final MoveValidator moveValidatorParam,
                           final CaptureHandler captureHandlerParam,
                           final SelectionState selectionStateParam,
                           final UiController uiControllerParam,
                           final MoveCoordinator moveCoordinatorParam) {
    this.moveValidator = moveValidatorParam;
    this.captureHandler = captureHandlerParam;
    this.selectionState = selectionStateParam;
    this.uiController = uiControllerParam;
    this.moveCoordinator = moveCoordinatorParam;
  }

  /**
   * Handles a mouse click on the board at the specified row and column.
   *
   * <p>Depending on whether this is the first or second click in a move
   * sequence,
   * the method will either select a piece or attempt to perform a move or
   * capture.
   * </p>
   *
   * @param row the row of the clicked cell
   * @param col the column of the clicked cell
   */
  public void handleMouseInput(final int row, final int col) {
    if (!moveValidator.isValidPosition(row, col)) {
      if (!firstClick) {
        clearSelectionAndReset();
      }
      uiController.refreshBoard();
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

  private void clearSelectionAndReset() {
    selectionState.setSelectedColumn(GameConstants.BOARD_SIZE);
    selectionState.setSelectedRow(GameConstants.BOARD_SIZE);
    firstClick = true;
  }
}
