package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveValidatorTest {

  @Mock
  private MoveService moveService;

  @Mock
  private BoardState boardState;

  private MoveValidator moveValidator;

  @BeforeEach
  void setUp() {
    moveValidator = new MoveValidator(moveService, boardState);
  }

  @Nested
  class IsLegalMoveTests {

    @Test
    void shouldReturnTrueWhenMoveServiceConfirmsLegalMove() {

      int fromRow = 2, fromCol = 1, toRow = 3, toCol = 2;
      int pieceColor = GameConstants.RED;
      when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);
      when(moveService.isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor)).thenReturn(true);

      boolean result =
          moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol);

      assertTrue(result);
      verify(boardState).getPiece(fromRow, fromCol);
      verify(moveService).isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor);
    }

    @Test
    void shouldReturnFalseWhenMoveServiceRejectsMove() {

      int fromRow = 2, fromCol = 1, toRow = 4, toCol = 3;
      int pieceColor = GameConstants.BLACK;
      when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);
      when(moveService.isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor)).thenReturn(false);

      boolean result =
          moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol);

      assertFalse(result);
      verify(boardState).getPiece(fromRow, fromCol);
      verify(moveService).isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor);
    }

    @Test
    void shouldHandleEdgePositionsCorrectly() {

      int fromRow = 0, fromCol = 0, toRow = 1, toCol = 1;
      int pieceColor = GameConstants.RED;
      when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);
      when(moveService.isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor)).thenReturn(true);

      boolean result =
          moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol);

      assertTrue(result);
      verify(boardState).getPiece(fromRow, fromCol);
      verify(moveService).isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor);
    }

    @Test
    void shouldHandleDifferentPieceColors() {

      int fromRow = 1, fromCol = 2, toRow = 2, toCol = 3;
      int pieceColor = GameConstants.RED_KING;
      when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);
      when(moveService.isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor)).thenReturn(true);

      boolean result =
          moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol);

      assertTrue(result);
      verify(boardState).getPiece(fromRow, fromCol);
      verify(moveService).isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor);
    }

    @Test
    void shouldHandleZeroPieceColor() {

      int fromRow = 3, fromCol = 4, toRow = 4, toCol = 5;
      int pieceColor = GameConstants.EMPTY;
      when(boardState.getPiece(fromRow, fromCol)).thenReturn(pieceColor);
      when(moveService.isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor)).thenReturn(false);

      boolean result =
          moveValidator.isLegalMove(fromRow, fromCol, toRow, toCol);

      assertFalse(result);
      verify(boardState).getPiece(fromRow, fromCol);
      verify(moveService).isLegalMove(toRow, toCol, fromCol, fromRow,
          pieceColor);
    }
  }

  @Nested
  class CanSelectPieceTests {

    @Test
    void shouldReturnTrueWhenMoveServiceAllowsPieceSelection() {

      int row = 2, col = 1;
      when(moveService.canSelectPiece(row, col, boardState)).thenReturn(true);

      boolean result = moveValidator.canSelectPiece(row, col);

      assertTrue(result);
      verify(moveService).canSelectPiece(row, col, boardState);
    }

    @Test
    void shouldReturnFalseWhenMoveServiceForbidsPieceSelection() {

      int row = 3, col = 4;
      when(moveService.canSelectPiece(row, col, boardState)).thenReturn(false);

      boolean result = moveValidator.canSelectPiece(row, col);

      assertFalse(result);
      verify(moveService).canSelectPiece(row, col, boardState);
    }

    @Test
    void shouldPassCorrectParametersToMoveService() {

      int row = 5, col = 6;
      when(moveService.canSelectPiece(row, col, boardState)).thenReturn(true);

      moveValidator.canSelectPiece(row, col);

      verify(moveService).canSelectPiece(eq(row), eq(col), eq(boardState));
    }

    @Test
    void shouldHandleCornerPositions() {

      int row = 7, col = 7;
      when(moveService.canSelectPiece(row, col, boardState)).thenReturn(false);

      boolean result = moveValidator.canSelectPiece(row, col);

      assertFalse(result);
      verify(moveService).canSelectPiece(row, col, boardState);
    }
  }

  @Nested
  class MustTakeTests {

    @Test
    void shouldReturnTrueWhenMoveServiceIndicatesForcedCapture() {

      when(moveService.mustTake()).thenReturn(true);

      boolean result = moveValidator.mustTake();

      assertTrue(result);
      verify(moveService).mustTake();
    }

    @Test
    void shouldReturnFalseWhenMoveServiceIndicatesNoForcedCapture() {

      when(moveService.mustTake()).thenReturn(false);

      boolean result = moveValidator.mustTake();

      assertFalse(result);
      verify(moveService).mustTake();
    }

    @Test
    void shouldDelegateCallToMoveServiceWithoutParameters() {

      when(moveService.mustTake()).thenReturn(true);

      moveValidator.mustTake();

      verify(moveService, times(1)).mustTake();
      verifyNoMoreInteractions(moveService);
    }
  }

  @Nested
  class IsValidPositionTests {

    @Test
    void shouldReturnTrueForValidPosition() {

      int row = 3, col = 4;

      try (MockedStatic<PositionValidator> mockedStatic = mockStatic(
          PositionValidator.class)) {
        mockedStatic.when(() -> PositionValidator.isValidPosition(row, col))
            .thenReturn(true);

        boolean result = moveValidator.isValidPosition(row, col);

        assertTrue(result);
        mockedStatic.verify(() -> PositionValidator.isValidPosition(row, col));
      }
    }

    @Test
    void shouldReturnFalseForInvalidPosition() {

      int row = -1, col = 8;

      try (MockedStatic<PositionValidator> mockedStatic = mockStatic(
          PositionValidator.class)) {
        mockedStatic.when(() -> PositionValidator.isValidPosition(row, col))
            .thenReturn(false);

        boolean result = moveValidator.isValidPosition(row, col);

        assertFalse(result);
        mockedStatic.verify(() -> PositionValidator.isValidPosition(row, col));
      }
    }

    @Test
    void shouldHandleBoundaryPositions() {

      int row = 0, col = 0;

      try (MockedStatic<PositionValidator> mockedStatic = mockStatic(
          PositionValidator.class)) {
        mockedStatic.when(() -> PositionValidator.isValidPosition(row, col))
            .thenReturn(true);

        boolean result = moveValidator.isValidPosition(row, col);

        assertTrue(result);
        mockedStatic.verify(() -> PositionValidator.isValidPosition(row, col));
      }
    }

    @Test
    void shouldHandleNegativeCoordinates() {

      int row = -5, col = -3;

      try (MockedStatic<PositionValidator> mockedStatic = mockStatic(
          PositionValidator.class)) {
        mockedStatic.when(() -> PositionValidator.isValidPosition(row, col))
            .thenReturn(false);

        boolean result = moveValidator.isValidPosition(row, col);

        assertFalse(result);
        mockedStatic.verify(() -> PositionValidator.isValidPosition(row, col));
      }
    }

    @Test
    void shouldHandleLargeCoordinates() {

      int row = 100, col = 200;

      try (MockedStatic<PositionValidator> mockedStatic = mockStatic(
          PositionValidator.class)) {
        mockedStatic.when(() -> PositionValidator.isValidPosition(row, col))
            .thenReturn(false);

        boolean result = moveValidator.isValidPosition(row, col);

        assertFalse(result);
        mockedStatic.verify(() -> PositionValidator.isValidPosition(row, col));
      }
    }
  }

  @Nested
  class IntegrationTests {

    @Test
    void shouldWorkCorrectlyWithAllMethodsInSequence() {

      int row = 2, col = 1, toRow = 3, toCol = 2;
      int pieceColor = GameConstants.RED;

      when(boardState.getPiece(row, col)).thenReturn(pieceColor);
      when(moveService.canSelectPiece(row, col, boardState)).thenReturn(true);
      when(moveService.isLegalMove(toRow, toCol, col, row,
          pieceColor)).thenReturn(true);
      when(moveService.mustTake()).thenReturn(false);

      try (MockedStatic<PositionValidator> mockedStatic = mockStatic(
          PositionValidator.class)) {
        mockedStatic.when(() -> PositionValidator.isValidPosition(row, col))
            .thenReturn(true);
        mockedStatic.when(() -> PositionValidator.isValidPosition(toRow, toCol))
            .thenReturn(true);

        assertTrue(moveValidator.isValidPosition(row, col));
        assertTrue(moveValidator.canSelectPiece(row, col));
        assertTrue(moveValidator.isLegalMove(row, col, toRow, toCol));
        assertFalse(moveValidator.mustTake());
        assertTrue(moveValidator.isValidPosition(toRow, toCol));

        verify(boardState).getPiece(row, col);
        verify(moveService).canSelectPiece(row, col, boardState);
        verify(moveService).isLegalMove(toRow, toCol, col, row, pieceColor);
        verify(moveService).mustTake();
        mockedStatic.verify(() -> PositionValidator.isValidPosition(row, col));
        mockedStatic.verify(
            () -> PositionValidator.isValidPosition(toRow, toCol));
      }
    }
  }

  @Nested
  class ConstructorTests {

    @Test
    void shouldCreateMoveValidatorWithProvidedDependencies() {

      MoveService testMoveService = mock(MoveService.class);
      BoardState testBoardState = mock(BoardState.class);

      MoveValidator validator =
          new MoveValidator(testMoveService, testBoardState);

      assertNotNull(validator);

      validator.mustTake();
      verify(testMoveService).mustTake();
    }

    @Test
    void shouldHandleNullDependenciesGracefullyInConstructor() {

      assertDoesNotThrow(() -> new MoveValidator(null, null));
    }
  }
}