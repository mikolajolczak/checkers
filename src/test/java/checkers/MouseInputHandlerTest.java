package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MouseInputHandlerTest {

  @Mock
  private MoveValidator moveValidator;

  @Mock
  private CaptureHandler captureHandler;

  @Mock
  private SelectionState selectionState;

  @Mock
  private UIController uiController;

  @Mock
  private MoveCoordinator moveCoordinator;

  private MouseInputHandler mouseInputHandler;

  @BeforeEach
  void setUp() {
    mouseInputHandler = new MouseInputHandler(
        moveValidator,
        captureHandler,
        selectionState,
        uiController,
        moveCoordinator
    );
  }

  @Test
  void handleMouseInput_WhenInvalidPosition_ShouldReturnEarlyAndRefreshBoard() {

    int row = 3, col = 4;
    when(moveValidator.isValidPosition(row, col)).thenReturn(false);

    mouseInputHandler.handleMouseInput(row, col);

    verify(moveValidator).isValidPosition(row, col);
    verify(uiController).refreshBoard();
    verifyNoMoreInteractions(moveValidator, captureHandler, selectionState,
        moveCoordinator);
  }

  @Test
  void handleMouseInput_FirstClickWithValidPiece_ShouldSelectPiece() {

    int row = 2, col = 3;
    when(moveValidator.isValidPosition(row, col)).thenReturn(true);
    when(moveValidator.canSelectPiece(row, col)).thenReturn(true);

    mouseInputHandler.handleMouseInput(row, col);

    verify(moveValidator).isValidPosition(row, col);
    verify(moveValidator).canSelectPiece(row, col);
    verify(selectionState).setSelectedColumn(col);
    verify(selectionState).setSelectedRow(row);
    verify(uiController).refreshBoard();
    verifyNoInteractions(captureHandler, moveCoordinator);
  }

  @Test
  void handleMouseInput_FirstClickWithInvalidPiece_ShouldNotSelectPiece() {

    int row = 2, col = 3;
    when(moveValidator.isValidPosition(row, col)).thenReturn(true);
    when(moveValidator.canSelectPiece(row, col)).thenReturn(false);

    mouseInputHandler.handleMouseInput(row, col);

    verify(moveValidator).isValidPosition(row, col);
    verify(moveValidator).canSelectPiece(row, col);
    verify(uiController).refreshBoard();
    verify(selectionState, never()).setSelectedColumn(anyInt());
    verify(selectionState, never()).setSelectedRow(anyInt());
    verifyNoInteractions(captureHandler, moveCoordinator);
  }

  @Test
  void handleMouseInput_SecondClickWhenMustTake_ShouldHandleCapture() {

    int firstRow = 2, firstCol = 3;
    int secondRow = 4, secondCol = 5;

    when(moveValidator.isValidPosition(firstRow, firstCol)).thenReturn(true);
    when(moveValidator.canSelectPiece(firstRow, firstCol)).thenReturn(true);
    mouseInputHandler.handleMouseInput(firstRow, firstCol);

    when(moveValidator.isValidPosition(secondRow, secondCol)).thenReturn(true);
    when(moveValidator.mustTake()).thenReturn(true);

    mouseInputHandler.handleMouseInput(secondRow, secondCol);

    verify(moveValidator).mustTake();
    verify(captureHandler).handleCapture(firstRow, firstCol, secondRow,
        secondCol);
    verify(selectionState).setSelectedColumn(GameConstants.BOARD_SIZE);
    verify(selectionState).setSelectedRow(GameConstants.BOARD_SIZE);
    verify(uiController, times(2)).refreshBoard();
    verifyNoInteractions(moveCoordinator);
  }

  @Test
  void handleMouseInput_SecondClickWhenNotMustTake_ShouldHandleMove() {

    int firstRow = 2, firstCol = 3;
    int secondRow = 4, secondCol = 5;

    when(moveValidator.isValidPosition(firstRow, firstCol)).thenReturn(true);
    when(moveValidator.canSelectPiece(firstRow, firstCol)).thenReturn(true);
    mouseInputHandler.handleMouseInput(firstRow, firstCol);

    when(moveValidator.isValidPosition(secondRow, secondCol)).thenReturn(true);
    when(moveValidator.mustTake()).thenReturn(false);

    mouseInputHandler.handleMouseInput(secondRow, secondCol);

    verify(moveValidator).mustTake();
    verify(moveCoordinator).handleMove(firstRow, firstCol, secondRow,
        secondCol);
    verify(selectionState).setSelectedColumn(GameConstants.BOARD_SIZE);
    verify(selectionState).setSelectedRow(GameConstants.BOARD_SIZE);
    verify(uiController, times(2)).refreshBoard();
    verifyNoInteractions(captureHandler);
  }

  @Test
  void handleMouseInput_MultipleClickSequences_ShouldResetStateCorrectly() {

    int firstRow1 = 1, firstCol1 = 2;
    int secondRow1 = 3, secondCol1 = 4;
    when(moveValidator.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(moveValidator.canSelectPiece(anyInt(), anyInt())).thenReturn(true);
    when(moveValidator.mustTake()).thenReturn(false);

    mouseInputHandler.handleMouseInput(firstRow1, firstCol1);
    mouseInputHandler.handleMouseInput(secondRow1, secondCol1);

    verify(moveCoordinator).handleMove(firstRow1, firstCol1, secondRow1,
        secondCol1);

    int firstRow2 = 5, firstCol2 = 6;
    int secondRow2 = 7, secondCol2 = 8;

    mouseInputHandler.handleMouseInput(firstRow2, firstCol2);
    mouseInputHandler.handleMouseInput(secondRow2, secondCol2);

    verify(moveCoordinator).handleMove(firstRow2, firstCol2, secondRow2,
        secondCol2);
    verify(uiController, times(4)).refreshBoard();
  }

  @Test
  void handleMouseInput_ThreeConsecutiveClicks_ShouldTreatThirdAsFirstClick() {

    int row1 = 1, col1 = 1;
    int row2 = 2, col2 = 2;
    int row3 = 3, col3 = 3;

    when(moveValidator.isValidPosition(anyInt(), anyInt())).thenReturn(true);
    when(moveValidator.canSelectPiece(anyInt(), anyInt())).thenReturn(true);
    when(moveValidator.mustTake()).thenReturn(false);

    mouseInputHandler.handleMouseInput(row1, col1);
    mouseInputHandler.handleMouseInput(row2, col2);
    mouseInputHandler.handleMouseInput(row3, col3);

    verify(moveCoordinator).handleMove(row1, col1, row2, col2);

    verify(selectionState).setSelectedColumn(col3);
    verify(selectionState).setSelectedRow(row3);
    verify(uiController, times(3)).refreshBoard();
  }

  @Test
  void handleMouseInput_SecondClickOnInvalidPosition_ShouldStillClearSelectionAndResetState() {

    int firstRow = 2, firstCol = 3;
    int secondRow = 4, secondCol = 5;

    when(moveValidator.isValidPosition(firstRow, firstCol)).thenReturn(true);
    when(moveValidator.canSelectPiece(firstRow, firstCol)).thenReturn(true);
    mouseInputHandler.handleMouseInput(firstRow, firstCol);

    when(moveValidator.isValidPosition(secondRow, secondCol)).thenReturn(false);

    mouseInputHandler.handleMouseInput(secondRow, secondCol);

    verify(moveValidator).isValidPosition(secondRow, secondCol);
    verify(uiController, times(2)).refreshBoard();

    verify(moveValidator, never()).mustTake();
    verifyNoInteractions(captureHandler, moveCoordinator);

    int thirdRow = 6, thirdCol = 7;
    when(moveValidator.isValidPosition(thirdRow, thirdCol)).thenReturn(true);
    when(moveValidator.canSelectPiece(thirdRow, thirdCol)).thenReturn(true);

    mouseInputHandler.handleMouseInput(thirdRow, thirdCol);

    verify(moveValidator).canSelectPiece(thirdRow, thirdCol);
  }

  @Test
  void constructor_ShouldInitializeAllDependencies() {

    int row = 1, col = 1;
    when(moveValidator.isValidPosition(row, col)).thenReturn(true);
    when(moveValidator.canSelectPiece(row, col)).thenReturn(true);

    mouseInputHandler.handleMouseInput(row, col);

    verify(moveValidator).canSelectPiece(row, col);
  }
}