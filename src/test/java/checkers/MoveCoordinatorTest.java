package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveCoordinatorTest {

  @Mock
  private MovePerformer movePerformer;

  @Mock
  private MoveValidator moveValidator;

  @Mock
  private UIController uiController;

  @Mock
  private TurnManager turnManager;

  @Mock
  private BotController botController;

  private MoveCoordinator moveCoordinator;

  @BeforeEach
  void setUp() {
    moveCoordinator = new MoveCoordinator(
        movePerformer,
        moveValidator,
        uiController,
        turnManager,
        botController
    );
  }

  @Test
  void shouldHandleValidMoveForHumanPlayer() {

    int fromRow = 2, fromCol = 1, toRow = 3, toCol = 2;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    verify(moveValidator).isLegalMove(fromRow, fromCol, toRow, toCol);
    verify(movePerformer).performMove(fromRow, fromCol, toRow, toCol);
    verify(turnManager).switchTurn();
    verify(uiController).refreshBoard();
    verify(botController, never()).executeTurn();
  }

  @Test
  void shouldHandleValidMoveAndTriggerBotTurn() {

    int fromRow = 2, fromCol = 1, toRow = 3, toCol = 2;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(true);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    verify(moveValidator).isLegalMove(fromRow, fromCol, toRow, toCol);
    verify(movePerformer).performMove(fromRow, fromCol, toRow, toCol);
    verify(turnManager).switchTurn();
    verify(uiController).refreshBoard();
    verify(botController).executeTurn();
  }

  @Test
  void shouldNotPerformMoveWhenMoveIsIllegal() {

    int fromRow = 0, fromCol = 0, toRow = 7, toCol = 7;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        false);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    verify(moveValidator).isLegalMove(fromRow, fromCol, toRow, toCol);
    verify(movePerformer, never()).performMove(anyInt(), anyInt(), anyInt(),
        anyInt());
    verify(turnManager, never()).switchTurn();
    verify(uiController, never()).refreshBoard();
    verify(botController, never()).executeTurn();
  }

  @Test
  void shouldVerifyCorrectOrderOfOperationsForValidMove() {

    int fromRow = 5, fromCol = 4, toRow = 4, toCol = 3;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    var inOrder =
        inOrder(moveValidator, movePerformer, turnManager, uiController);
    inOrder.verify(moveValidator).isLegalMove(fromRow, fromCol, toRow, toCol);
    inOrder.verify(movePerformer).performMove(fromRow, fromCol, toRow, toCol);
    inOrder.verify(turnManager).switchTurn();
    inOrder.verify(uiController).refreshBoard();
  }

  @Test
  void shouldHandleEdgeCaseCoordinates() {

    int fromRow = 0, fromCol = 0, toRow = 1, toCol = 1;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    verify(moveValidator).isLegalMove(0, 0, 1, 1);
    verify(movePerformer).performMove(0, 0, 1, 1);
  }

  @Test
  void shouldHandleMaximumCoordinates() {

    int fromRow = 7, fromCol = 7, toRow = 6, toCol = 6;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    verify(moveValidator).isLegalMove(7, 7, 6, 6);
    verify(movePerformer).performMove(7, 7, 6, 6);
  }

  @Test
  void shouldNotCallBotControllerWhenCurrentPlayerIsHumanAfterTurnSwitch() {

    int fromRow = 1, fromCol = 2, toRow = 2, toCol = 3;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    verify(turnManager).isCurrentPlayerBot();
    verify(botController, never()).executeTurn();
  }

  @Test
  void shouldHandleValidMoveWithNegativeCoordinates() {

    int fromRow = -1, fromCol = -1, toRow = 0, toCol = 0;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    verify(moveValidator).isLegalMove(-1, -1, 0, 0);
    verify(movePerformer).performMove(-1, -1, 0, 0);
    verify(turnManager).switchTurn();
    verify(uiController).refreshBoard();
  }

  @Test
  void shouldVerifyBotExecutionHappensAfterUIRefresh() {

    int fromRow = 3, fromCol = 2, toRow = 4, toCol = 3;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(true);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    var inOrder = inOrder(uiController, botController);
    inOrder.verify(uiController).refreshBoard();
    inOrder.verify(botController).executeTurn();
  }

  @Test
  void shouldHandleSameSourceAndDestinationCoordinates() {

    int row = 3, col = 3;
    when(moveValidator.isLegalMove(row, col, row, col)).thenReturn(false);

    moveCoordinator.handleMove(row, col, row, col);

    verify(moveValidator).isLegalMove(row, col, row, col);
    verify(movePerformer, never()).performMove(anyInt(), anyInt(), anyInt(),
        anyInt());
    verify(turnManager, never()).switchTurn();
    verify(uiController, never()).refreshBoard();
    verify(botController, never()).executeTurn();
  }

  @Test
  void shouldVerifyAllMethodsCalledExactlyOnceForValidMove() {

    int fromRow = 2, fromCol = 1, toRow = 3, toCol = 2;
    when(moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol)).thenReturn(
        true);
    when(turnManager.isCurrentPlayerBot()).thenReturn(true);

    moveCoordinator.handleMove(fromRow, fromCol, toRow, toCol);

    verify(moveValidator, times(1)).isLegalMove(fromRow, fromCol, toRow, toCol);
    verify(movePerformer, times(1)).performMove(fromRow, fromCol, toRow, toCol);
    verify(turnManager, times(1)).switchTurn();
    verify(uiController, times(1)).refreshBoard();
    verify(turnManager, times(1)).isCurrentPlayerBot();
    verify(botController, times(1)).executeTurn();
  }
}