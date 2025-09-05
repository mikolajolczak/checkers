package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovePerformerTest {

  @Mock
  private PromotionService promotionService;

  @Mock
  private BoardState boardState;

  private MovePerformer movePerformer;

  @BeforeEach
  void setUp() {
    movePerformer = new MovePerformer(promotionService, boardState);
  }

  @Test
  void shouldPerformNormalMoveAndCheckPromotion() {

    int fromRow = 2, fromCol = 1, toRow = 3, toCol = 2;
    int pieceColor = GameConstants.RED;

    when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);

    try (MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
        MoveExecutor.class)) {
      movePerformer.performMove(fromRow, fromCol, toRow, toCol);

      moveExecutorMock.verify(() ->
              MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
                  pieceColor, boardState),
          times(1)
      );

      verify(promotionService, times(1))
          .promoteIfNeeded(toRow, toCol, pieceColor);

      verify(boardState, times(1))
          .getPiece(fromRow, fromCol);
    }
  }

  @Test
  void shouldPerformMoveForBlackPiece() {

    int fromRow = 5, fromCol = 4, toRow = 4, toCol = 3;

    when(boardState.getPiece(fromRow, fromCol)).thenReturn(GameConstants.BLACK);

    try (MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
        MoveExecutor.class)) {
      movePerformer.performMove(fromRow, fromCol, toRow, toCol);

      moveExecutorMock.verify(() ->
              MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
                  GameConstants.BLACK, boardState),
          times(1)
      );

      verify(promotionService, times(1))
          .promoteIfNeeded(toRow, toCol, GameConstants.BLACK);
    }
  }

  @Test
  void shouldPerformMoveForRedPiece() {

    int fromRow = 1, fromCol = 2, toRow = 2, toCol = 3;

    when(boardState.getPiece(fromRow, fromCol)).thenReturn(GameConstants.RED);

    try (MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
        MoveExecutor.class)) {
      movePerformer.performMove(fromRow, fromCol, toRow, toCol);

      moveExecutorMock.verify(() ->
              MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
                  GameConstants.RED, boardState),
          times(1)
      );

      verify(promotionService, times(1))
          .promoteIfNeeded(toRow, toCol, GameConstants.RED);
    }
  }

  @Test
  void shouldPerformMoveOnDifferentBoardPositions() {

    int[][] testPositions = {
        {0, 1, 1, 2},
        {7, 6, 6, 5},
        {3, 0, 4, 1},
        {4, 7, 5, 6}
    };

    for (int[] positions : testPositions) {
      int fromRow = positions[0], fromCol = positions[1];
      int toRow = positions[2], toCol = positions[3];
      int pieceColor = 1;

      when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);

      try (MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
          MoveExecutor.class)) {
        movePerformer.performMove(fromRow, fromCol, toRow, toCol);

        moveExecutorMock.verify(() ->
                MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
                    pieceColor, boardState),
            times(1)
        );

        verify(promotionService, times(1))
            .promoteIfNeeded(toRow, toCol, pieceColor);
      }

      reset(boardState, promotionService);
    }
  }

  @Test
  void shouldHandleZeroPieceValue() {

    int fromRow = 3, fromCol = 4, toRow = 4, toCol = 5;
    int emptySquare = GameConstants.EMPTY;

    when(boardState.getPiece(fromRow, fromCol)).thenReturn(emptySquare);

    try (MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
        MoveExecutor.class)) {
      movePerformer.performMove(fromRow, fromCol, toRow, toCol);

      moveExecutorMock.verify(() ->
              MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
                  emptySquare, boardState),
          times(1)
      );

      verify(promotionService, times(1))
          .promoteIfNeeded(toRow, toCol, emptySquare);
    }
  }

  @Test
  void shouldMaintainCorrectMethodCallOrder() {

    int fromRow = 2, fromCol = 3, toRow = 3, toCol = 4;
    int pieceColor = GameConstants.RED;

    when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);

    try (MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
        MoveExecutor.class)) {
      movePerformer.performMove(fromRow, fromCol, toRow, toCol);

      var inOrder = inOrder(boardState, promotionService);

      int _ = inOrder.verify(boardState).getPiece(fromRow, fromCol);

      inOrder.verify(promotionService)
          .promoteIfNeeded(toRow, toCol, pieceColor);

      moveExecutorMock.verify(() ->
              MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
                  pieceColor, boardState),
          times(1)
      );
    }
  }

  @Test
  void shouldPassAllParametersToMoveExecutor() {

    int fromRow = 1, fromCol = 0, toRow = 2, toCol = 1;
    int pieceColor = GameConstants.BLACK;

    when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);

    try (MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
        MoveExecutor.class)) {
      movePerformer.performMove(fromRow, fromCol, toRow, toCol);

      moveExecutorMock.verify(() ->
              MoveExecutor.executeNormalMove(
                  eq(fromRow),
                  eq(fromCol),
                  eq(toRow),
                  eq(toCol),
                  eq(pieceColor),
                  eq(boardState)
              ),
          times(1)
      );
    }
  }

  @Test
  void shouldPassAllParametersToPromotionService() {

    int fromRow = 6, fromCol = 5, toRow = 7, toCol = 6;
    int pieceColor = GameConstants.RED;

    when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);

    try (MockedStatic<MoveExecutor> _ = mockStatic(MoveExecutor.class)) {
      movePerformer.performMove(fromRow, fromCol, toRow, toCol);

      verify(promotionService, times(1))
          .promoteIfNeeded(
              eq(toRow),
              eq(toCol),
              eq(pieceColor)
          );
    }
  }

  @Test
  void shouldAcceptRequiredDependencies() {

    MovePerformer newMovePerformer =
        new MovePerformer(promotionService, boardState);

    assertNotNull(newMovePerformer);

    assertEquals(promotionService, newMovePerformer.promotionService());
    assertEquals(boardState, newMovePerformer.boardState());
  }

  @Test
  void shouldWorkWithActualCoordinateValues() {

    int[][] validMoves = {
        {0, 1, 1, 0}, {1, 2, 2, 3}, {2, 1, 3, 2},
        {3, 4, 4, 5}, {4, 3, 5, 4}, {5, 6, 6, 7},
        {6, 5, 7, 6}, {7, 0, 6, 1}
    };

    for (int i = 0; i < validMoves.length; i++) {
      int[] move = validMoves[i];
      int fromRow = move[0], fromCol = move[1];
      int toRow = move[2], toCol = move[3];
      int pieceColor = (i % 2) + 1;

      when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);

      try (MockedStatic<MoveExecutor> moveExecutorMock = mockStatic(
          MoveExecutor.class)) {

        assertDoesNotThrow(() ->
            movePerformer.performMove(fromRow, fromCol, toRow, toCol)
        );

        moveExecutorMock.verify(() ->
                MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
                    pieceColor, boardState),
            times(1)
        );

        verify(promotionService, times(1))
            .promoteIfNeeded(toRow, toCol, pieceColor);
      }

      reset(boardState, promotionService);
    }
  }
}