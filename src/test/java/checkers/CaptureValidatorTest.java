package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;


class CaptureValidatorTest {

  private BoardState mockBoardState;

  @BeforeEach
  void setUp() {
    mockBoardState = mock(BoardState.class);
  }

  @Nested
  class ValidCaptureTests {

    @Test
    void shouldReturnTrueForValidCapture() {

      int fromRow = 2, fromCol = 1, toRow = 4, toCol = 3, pieceColor =
          GameConstants.RED;

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenReturn(true);

        boolean result =
            CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol,
                toRow, toCol, pieceColor);

        assertTrue(result);
        mockedCaptureRules.verify(
            () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol, fromRow,
                pieceColor, mockBoardState));
      }
    }

    @Test
    void shouldHandleValidCaptureForDifferentColors() {

      int fromRow = 5, fromCol = 4, toRow = 3, toCol = 2;
      int[] colors = {1, 2};

      for (int color : colors) {
        try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
            CaptureRules.class)) {
          mockedCaptureRules.when(
                  () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                      fromRow,
                      color, mockBoardState))
              .thenReturn(true);

          boolean result =
              CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol,
                  toRow, toCol, color);

          assertTrue(result, "Capture should be valid for color " + color);
        }
      }
    }

    @Test
    void shouldHandleEdgePositionsCorrectly() {

      int[][] edgePositions = {
          {0, 1, 2, 3},
          {7, 6, 5, 4},
          {3, 0, 5, 2},
          {4, 7, 2, 5}
      };

      for (int[] pos : edgePositions) {
        try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
            CaptureRules.class)) {
          mockedCaptureRules.when(
                  () -> CaptureRules.isLegalCapture(pos[3], pos[2], pos[1],
                      pos[0],
                      1, mockBoardState))
              .thenReturn(true);

          boolean result =
              CaptureValidator.isValidCapture(mockBoardState, pos[0], pos[1],
                  pos[2], pos[3], 1);

          assertTrue(result, "Edge position capture should be valid");
        }
      }
    }
  }

  @Nested
  class InvalidCaptureTests {

    @Test
    void shouldReturnFalseForInvalidCapture() {

      int fromRow = 2, fromCol = 1, toRow = 4, toCol = 3, pieceColor = 1;

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenReturn(false);

        boolean result =
            CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol,
                toRow, toCol, pieceColor);

        assertFalse(result);
        mockedCaptureRules.verify(
            () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol, fromRow,
                pieceColor, mockBoardState));
      }
    }

    @Test
    void shouldHandleInvalidMoveSamePosition() {

      int row = 3, col = 4, pieceColor = 1;

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(col, row, col, row,
                    pieceColor,
                    mockBoardState))
            .thenReturn(false);

        boolean result =
            CaptureValidator.isValidCapture(mockBoardState, row, col, row, col,
                pieceColor);

        assertFalse(result);
      }
    }

    @Test
    void shouldHandleInvalidPieceColors() {

      int fromRow = 2, fromCol = 1, toRow = 4, toCol = 3;
      int[] invalidColors = {0, -1, 3, 999};

      for (int color : invalidColors) {
        try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
            CaptureRules.class)) {
          mockedCaptureRules.when(
                  () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                      fromRow,
                      color, mockBoardState))
              .thenReturn(false);

          boolean result =
              CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol,
                  toRow, toCol, color);

          assertFalse(result,
              "Invalid color " + color + " should result in false");
        }
      }
    }
  }

  @Nested
  class BoundaryValueTests {

    @Test
    void shouldHandleMinimumValidCoordinates() {

      int fromRow = 0, fromCol = 0, toRow = 0, toCol = 0, pieceColor = 1;

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenReturn(true);

        boolean result =
            CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol,
                toRow, toCol, pieceColor);

        assertTrue(result);
      }
    }

    @Test
    void shouldHandleMaximumValidCoordinates() {

      int fromRow = 7, fromCol = 7, toRow = 7, toCol = 7, pieceColor = 2;

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenReturn(true);

        boolean result =
            CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol,
                toRow, toCol, pieceColor);

        assertTrue(result);
      }
    }

    @Test
    void shouldHandleNegativeCoordinates() {

      int fromRow = -1, fromCol = -1, toRow = 2, toCol = 3, pieceColor = 1;

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenReturn(false);

        boolean result =
            CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol,
                toRow, toCol, pieceColor);

        assertFalse(result);
      }
    }

    @Test
    void shouldHandleLargeCoordinates() {

      int fromRow = 100, fromCol = 50, toRow = 200, toCol = 150, pieceColor = 1;

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenReturn(false);

        boolean result =
            CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol,
                toRow, toCol, pieceColor);

        assertFalse(result);
      }
    }
  }

  @Nested
  class ParameterOrderTests {

    @Test
    void shouldPassParametersInCorrectOrder() {

      int fromRow = 1, fromCol = 2, toRow = 3, toCol = 4, pieceColor = 2;

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenReturn(true);

        CaptureValidator.isValidCapture(mockBoardState, fromRow, fromCol, toRow,
            toCol, pieceColor);

        mockedCaptureRules.verify(() -> CaptureRules.isLegalCapture(
            eq(toCol),
            eq(toRow),
            eq(fromCol),
            eq(fromRow),
            eq(pieceColor),
            eq(mockBoardState)
        ));
      }
    }

    @Test
    void shouldVerifyParameterMappingWithDifferentValues() {

      int[][] testCases = {
          {0, 1, 2, 3, 1},
          {5, 6, 7, 0, 2},
          {3, 2, 1, 4, 1}
      };

      for (int[] testCase : testCases) {
        try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
            CaptureRules.class)) {
          mockedCaptureRules.when(() -> CaptureRules.isLegalCapture(
                  testCase[3], testCase[2], testCase[1], testCase[0],
                  testCase[4],
                  mockBoardState))
              .thenReturn(true);

          CaptureValidator.isValidCapture(mockBoardState,
              testCase[0], testCase[1], testCase[2], testCase[3], testCase[4]);

          mockedCaptureRules.verify(() -> CaptureRules.isLegalCapture(
              testCase[3],
              testCase[2],
              testCase[1],
              testCase[0],
              testCase[4],
              mockBoardState
          ));
        }
      }
    }
  }

  @Nested
  class ExceptionHandlingTests {

    @Test
    void shouldPropagateExceptionsFromCaptureRules() {

      int fromRow = 2, fromCol = 1, toRow = 4, toCol = 3, pieceColor = 1;
      RuntimeException expectedException =
          new RuntimeException("Test exception");

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenThrow(expectedException);

        RuntimeException thrownException =
            assertThrows(RuntimeException.class, () ->
                CaptureValidator.isValidCapture(mockBoardState, fromRow,
                    fromCol, toRow, toCol, pieceColor)
            );

        assertEquals(expectedException, thrownException);
      }
    }

    @Test
    void shouldHandleIllegalArgumentExceptionFromCaptureRules() {

      int fromRow = 2, fromCol = 1, toRow = 4, toCol = 3, pieceColor = 1;
      IllegalArgumentException expectedException =
          new IllegalArgumentException("Invalid parameters");

      try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
          CaptureRules.class)) {
        mockedCaptureRules.when(
                () -> CaptureRules.isLegalCapture(toCol, toRow, fromCol,
                    fromRow,
                    pieceColor, mockBoardState))
            .thenThrow(expectedException);

        IllegalArgumentException thrownException =
            assertThrows(IllegalArgumentException.class, () ->
                CaptureValidator.isValidCapture(mockBoardState, fromRow,
                    fromCol, toRow, toCol, pieceColor)
            );

        assertEquals(expectedException, thrownException);
      }
    }
  }

  @Nested
  class IntegrationLikeTests {

    @Test
    void shouldWorkWithMultipleConsecutiveCalls() {

      int[][] moves = {
          {1, 2, 3, 4, 1, 1},
          {5, 6, 7, 0, 2, 0},
          {0, 1, 2, 3, 1, 1}
      };

      for (int[] move : moves) {
        try (MockedStatic<CaptureRules> mockedCaptureRules = mockStatic(
            CaptureRules.class)) {
          boolean expectedResult = move[5] == 1;
          mockedCaptureRules.when(() -> CaptureRules.isLegalCapture(
                  move[3], move[2], move[1], move[0], move[4], mockBoardState))
              .thenReturn(expectedResult);

          boolean result = CaptureValidator.isValidCapture(mockBoardState,
              move[0], move[1], move[2], move[3], move[4]);

          assertEquals(expectedResult, result,
              String.format(
                  "Move from (%d,%d) to (%d,%d) with color %d should return %b",
                  move[0], move[1], move[2], move[3], move[4], expectedResult));
        }
      }
    }
  }
}