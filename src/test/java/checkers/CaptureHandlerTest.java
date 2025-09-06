package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptureHandlerTest {

  private static final int FROM_ROW = 2;
  private static final int FROM_COL = 3;
  private static final int TO_ROW = 4;
  private static final int TO_COL = 5;
  private static final int PIECE_COLOR = GameConstants.RED;
  @Mock
  private CaptureExecutor executor;
  @Mock
  private TurnFlowManager turnFlow;
  @Mock
  private BoardState boardState;
  @Mock
  private TurnManager turnManager;
  private CaptureHandler captureHandler;

  @BeforeEach
  void setUp() {
    captureHandler = new CaptureHandler(executor, turnFlow, boardState);
  }

  @Test
  void shouldExecuteCaptureWhenValidCapture() {

    when(boardState.getPiece(FROM_ROW, FROM_COL)).thenReturn(PIECE_COLOR);
    when(turnFlow.turnManager()).thenReturn(turnManager);

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(boardState, FROM_ROW, FROM_COL,
                  TO_ROW, TO_COL, PIECE_COLOR))
          .thenReturn(true);

      captureHandler.handleCapture(FROM_ROW, FROM_COL, TO_ROW, TO_COL);

      verify(executor).execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL,
          PIECE_COLOR, turnManager);
      verify(turnFlow).afterMove();
    }
  }

  @Test
  void shouldNotExecuteCaptureWhenInvalidCapture() {

    when(boardState.getPiece(FROM_ROW, FROM_COL)).thenReturn(PIECE_COLOR);

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(boardState, FROM_ROW, FROM_COL,
                  TO_ROW, TO_COL, PIECE_COLOR))
          .thenReturn(false);

      captureHandler.handleCapture(FROM_ROW, FROM_COL, TO_ROW, TO_COL);

      verify(executor, never()).execute(any(), anyInt(), anyInt(), anyInt(),
          anyInt(), anyInt(), any());
      verify(turnFlow, never()).afterMove();
    }
  }

  @Test
  void shouldHandleEdgeCaseCoordinates() {

    int cornerFromRow = 0, cornerFromCol = 0;
    int cornerToRow = 7, cornerToCol = 7;

    when(boardState.getPiece(cornerFromRow, cornerFromCol)).thenReturn(
        PIECE_COLOR);
    when(turnFlow.turnManager()).thenReturn(turnManager);

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(boardState, cornerFromRow,
                  cornerFromCol, cornerToRow, cornerToCol, PIECE_COLOR))
          .thenReturn(true);

      captureHandler.handleCapture(cornerFromRow, cornerFromCol, cornerToRow,
          cornerToCol);

      verify(executor).execute(boardState, cornerFromRow, cornerFromCol,
          cornerToRow, cornerToCol, PIECE_COLOR, turnManager);
      verify(turnFlow).afterMove();
    }
  }

  @Test
  void shouldCallGetPieceWithCorrectCoordinates() {

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(any(), anyInt(), anyInt(),
                  anyInt(),
                  anyInt(), anyInt()))
          .thenReturn(false);

      captureHandler.handleCapture(FROM_ROW, FROM_COL, TO_ROW, TO_COL);

      verify(boardState).getPiece(FROM_ROW, FROM_COL);
    }
  }

  @Test
  void shouldValidateCaptureWithCorrectParameters() {

    when(boardState.getPiece(FROM_ROW, FROM_COL)).thenReturn(PIECE_COLOR);

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(boardState, FROM_ROW, FROM_COL,
                  TO_ROW, TO_COL, PIECE_COLOR))
          .thenReturn(false);

      captureHandler.handleCapture(FROM_ROW, FROM_COL, TO_ROW, TO_COL);

      captureValidator.verify(() ->
          CaptureValidator.isValidCapture(boardState, FROM_ROW, FROM_COL,
              TO_ROW, TO_COL, PIECE_COLOR));
    }
  }

  @Test
  void shouldExecuteInCorrectOrderWhenValidCapture() {

    when(boardState.getPiece(FROM_ROW, FROM_COL)).thenReturn(PIECE_COLOR);
    when(turnFlow.turnManager()).thenReturn(turnManager);

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(boardState, FROM_ROW, FROM_COL,
                  TO_ROW, TO_COL, PIECE_COLOR))
          .thenReturn(true);

      captureHandler.handleCapture(FROM_ROW, FROM_COL, TO_ROW, TO_COL);

      var inOrder = inOrder(boardState, executor, turnFlow);
      int piece = inOrder.verify(boardState).getPiece(FROM_ROW, FROM_COL);
      assertEquals(GameConstants.EMPTY, piece);
      inOrder.verify(executor)
          .execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL, PIECE_COLOR,
              turnManager);
      inOrder.verify(turnFlow).afterMove();
    }
  }

  @Test
  void shouldHandleMultipleCapturesInSequence() {

    when(boardState.getPiece(anyInt(), anyInt())).thenReturn(PIECE_COLOR);
    when(turnFlow.turnManager()).thenReturn(turnManager);

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(any(), anyInt(), anyInt(),
                  anyInt(),
                  anyInt(), anyInt()))
          .thenReturn(true);

      captureHandler.handleCapture(1, 1, 3, 3);
      captureHandler.handleCapture(3, 3, 5, 5);
      captureHandler.handleCapture(5, 5, 7, 7);

      verify(executor, times(3)).execute(any(), anyInt(), anyInt(), anyInt(),
          anyInt(), anyInt(), any());
      verify(turnFlow, times(3)).afterMove();
    }
  }

  @Test
  void shouldNotCallAfterMoveWhenCaptureValidationFails() {

    when(boardState.getPiece(FROM_ROW, FROM_COL)).thenReturn(PIECE_COLOR);

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(boardState, FROM_ROW, FROM_COL,
                  TO_ROW, TO_COL, PIECE_COLOR))
          .thenReturn(false);

      captureHandler.handleCapture(FROM_ROW, FROM_COL, TO_ROW, TO_COL);

      verify(turnFlow, never()).afterMove();
      verify(turnFlow, never()).turnManager();
    }
  }

  @Test
  void shouldPassCorrectTurnManagerToExecutor() {

    TurnManager specificTurnManager = mock(TurnManager.class);
    when(boardState.getPiece(FROM_ROW, FROM_COL)).thenReturn(PIECE_COLOR);
    when(turnFlow.turnManager()).thenReturn(specificTurnManager);

    try (MockedStatic<CaptureValidator> captureValidator = mockStatic(
        CaptureValidator.class)) {
      captureValidator.when(() ->
              CaptureValidator.isValidCapture(boardState, FROM_ROW, FROM_COL,
                  TO_ROW, TO_COL, PIECE_COLOR))
          .thenReturn(true);

      captureHandler.handleCapture(FROM_ROW, FROM_COL, TO_ROW, TO_COL);

      verify(executor).execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL,
          PIECE_COLOR, specificTurnManager);
    }
  }
}