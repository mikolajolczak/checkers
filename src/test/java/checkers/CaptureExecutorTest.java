package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptureExecutorTest {

  private static final int FROM_ROW = 2;
  private static final int FROM_COL = 3;
  private static final int TO_ROW = 4;
  private static final int TO_COL = 5;
  private static final int REGULAR_PIECE_COLOR = GameConstants.RED;
  private static final int KING_PIECE_COLOR = GameConstants.RED_KING;
  private static final int CURRENT_COLOR = GameConstants.RED;
  private static final int CURRENT_KING_COLOR = GameConstants.RED_KING;
  @Mock
  private PromotionService promotionService;
  @Mock
  private BoardState boardState;
  @Mock
  private TurnManager turnManager;
  private CaptureExecutor captureExecutor;

  @BeforeEach
  void setUp() {
    captureExecutor = new CaptureExecutor(promotionService);
  }

  @Test
  void testExecute_WithRegularPiece_CallsExecuteCapture() {

    when(turnManager.getCurrentColor()).thenReturn(CURRENT_COLOR);

    try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class);
         MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
             MoveExecutor.class)) {

      pieceRulesMock.when(() -> PieceRules.isKing(REGULAR_PIECE_COLOR))
          .thenReturn(false);

      captureExecutor.execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL,
          REGULAR_PIECE_COLOR, turnManager);

      moveExecutorMock.verify(() -> MoveExecutor.executeCapture(
          FROM_ROW, FROM_COL, TO_ROW, TO_COL, CURRENT_COLOR, boardState));
      verify(promotionService).promoteIfNeeded(TO_ROW, TO_COL,
          REGULAR_PIECE_COLOR);
    }
  }

  @Test
  void testExecute_WithKingPiece_CallsExecuteQueenCapture() {

    when(turnManager.getCurrentKingColor()).thenReturn(CURRENT_KING_COLOR);

    try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class);
         MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
             MoveExecutor.class)) {

      pieceRulesMock.when(() -> PieceRules.isKing(KING_PIECE_COLOR))
          .thenReturn(true);

      captureExecutor.execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL,
          KING_PIECE_COLOR, turnManager);

      moveExecutorMock.verify(() -> MoveExecutor.executeQueenCapture(
          FROM_ROW, FROM_COL, TO_ROW, TO_COL, CURRENT_KING_COLOR, boardState));
      verify(promotionService).promoteIfNeeded(TO_ROW, TO_COL,
          KING_PIECE_COLOR);
    }
  }

  @Test
  void testExecute_AlwaysCallsPromotionService() {

    when(turnManager.getCurrentColor()).thenReturn(CURRENT_COLOR);

    try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class);
         MockedStatic<MoveExecutor> _ = mockStatic(
             MoveExecutor.class)) {

      pieceRulesMock.when(() -> PieceRules.isKing(REGULAR_PIECE_COLOR))
          .thenReturn(false);

      captureExecutor.execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL,
          REGULAR_PIECE_COLOR, turnManager);

      verify(promotionService).promoteIfNeeded(TO_ROW, TO_COL,
          REGULAR_PIECE_COLOR);
    }
  }

  @Test
  void testExecute_WithDifferentCoordinates_PassesCorrectParameters() {

    final int differentFromRow = 0;
    final int differentFromCol = 1;
    final int differentToRow = 6;
    final int differentToCol = 7;

    when(turnManager.getCurrentColor()).thenReturn(CURRENT_COLOR);

    try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class);
         MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
             MoveExecutor.class)) {

      pieceRulesMock.when(() -> PieceRules.isKing(REGULAR_PIECE_COLOR))
          .thenReturn(false);

      captureExecutor.execute(boardState, differentFromRow, differentFromCol,
          differentToRow, differentToCol, REGULAR_PIECE_COLOR, turnManager);

      moveExecutorMock.verify(() -> MoveExecutor.executeCapture(
          differentFromRow, differentFromCol, differentToRow, differentToCol,
          CURRENT_COLOR, boardState));
      verify(promotionService).promoteIfNeeded(differentToRow, differentToCol,
          REGULAR_PIECE_COLOR);
    }
  }

  @Test
  void testExecute_WithKingPiece_PassesCorrectKingColor() {

    final int expectedKingColor = GameConstants.RED;
    when(turnManager.getCurrentKingColor()).thenReturn(expectedKingColor);

    try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class);
         MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
             MoveExecutor.class)) {

      pieceRulesMock.when(() -> PieceRules.isKing(KING_PIECE_COLOR))
          .thenReturn(true);

      captureExecutor.execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL,
          KING_PIECE_COLOR, turnManager);

      moveExecutorMock.verify(() -> MoveExecutor.executeQueenCapture(
          FROM_ROW, FROM_COL, TO_ROW, TO_COL, expectedKingColor, boardState));
    }
  }

  @Test
  void testExecute_WithRegularPiece_PassesCorrectRegularColor() {

    final int expectedRegularColor = GameConstants.RED;
    when(turnManager.getCurrentColor()).thenReturn(expectedRegularColor);

    try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class);
         MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
             MoveExecutor.class)) {

      pieceRulesMock.when(() -> PieceRules.isKing(REGULAR_PIECE_COLOR))
          .thenReturn(false);

      captureExecutor.execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL,
          REGULAR_PIECE_COLOR, turnManager);

      moveExecutorMock.verify(() -> MoveExecutor.executeCapture(
          FROM_ROW, FROM_COL, TO_ROW, TO_COL, expectedRegularColor,
          boardState));
    }
  }

  @Test
  void testExecute_VerifyMethodCallOrder() {
    when(turnManager.getCurrentColor()).thenReturn(CURRENT_COLOR);

    try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class);
         MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
             MoveExecutor.class)) {

      pieceRulesMock.when(() -> PieceRules.isKing(REGULAR_PIECE_COLOR))
          .thenReturn(false);

      captureExecutor.execute(boardState, FROM_ROW, FROM_COL, TO_ROW, TO_COL,
          REGULAR_PIECE_COLOR, turnManager);

      var inOrder = inOrder(promotionService);

      moveExecutorMock.verify(() -> MoveExecutor.executeCapture(
          FROM_ROW, FROM_COL, TO_ROW, TO_COL, CURRENT_COLOR, boardState));

      inOrder.verify(promotionService)
          .promoteIfNeeded(TO_ROW, TO_COL, REGULAR_PIECE_COLOR);
    }
  }

  @Test
  void testExecute_WithBoundaryCoordinates_HandlesCorrectly() {

    final int minCoordinate = 0;
    final int maxCoordinate = GameConstants.BOARD_SIZE - 1;

    when(turnManager.getCurrentColor()).thenReturn(GameConstants.RED);

    try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class);
         MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
             MoveExecutor.class)) {

      pieceRulesMock.when(() -> PieceRules.isKing(GameConstants.RED))
          .thenReturn(false);

      captureExecutor.execute(boardState, minCoordinate, minCoordinate,
          maxCoordinate, maxCoordinate, GameConstants.RED, turnManager);

      moveExecutorMock.verify(() -> MoveExecutor.executeCapture(
          minCoordinate, minCoordinate, maxCoordinate, maxCoordinate,
          GameConstants.RED, boardState));
      verify(promotionService).promoteIfNeeded(maxCoordinate, maxCoordinate,
          GameConstants.RED);
    }
  }
}