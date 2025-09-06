package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class DiagonalValidatorTest {

  private BoardState mockBoardState;

  @BeforeEach
  void setUp() {
    mockBoardState = mock(BoardState.class);
  }

  @Test
  void isPathClearBetween_EmptyPath_ReturnsFalse() {

    int c1 = 0, r1 = 0, c2 = 4, r2 = 4, dc = 1, dr = 1;

    when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(() -> PositionValidator.isValidPosition(1, 1))
          .thenReturn(true);
      positionValidator.when(() -> PositionValidator.isValidPosition(2, 2))
          .thenReturn(true);
      positionValidator.when(() -> PositionValidator.isValidPosition(3, 3))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.isPathClearBetween(c1, r1, c2, r2, dc, dr,
              mockBoardState);

      assertFalse(result);
    }
  }

  @Test
  void isPathClearBetween_PieceOnPath_ReturnsTrue() {

    int c1 = 0, r1 = 0, c2 = 4, r2 = 4, dc = 1, dr = 1;

    when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(1);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(() -> PositionValidator.isValidPosition(1, 1))
          .thenReturn(true);
      positionValidator.when(() -> PositionValidator.isValidPosition(2, 2))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.isPathClearBetween(c1, r1, c2, r2, dc, dr,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void isPathClearBetween_InvalidPosition_ReturnsFalse() {

    int c1 = 0, r1 = 0, c2 = 4, r2 = 4, dc = 1, dr = 1;

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(() -> PositionValidator.isValidPosition(1, 1))
          .thenReturn(false);

      boolean result =
          DiagonalValidator.isPathClearBetween(c1, r1, c2, r2, dc, dr,
              mockBoardState);

      assertFalse(result);
    }
  }

  @Test
  void isPathClearBetween_CloseToDestination_ReturnsFalse() {

    int c1 = 0, r1 = 0, c2 = 2, r2 = 2, dc = 1, dr = 1;

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(() -> PositionValidator.isValidPosition(1, 1))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.isPathClearBetween(c1, r1, c2, r2, dc, dr,
              mockBoardState);

      assertFalse(result);
    }
  }

  @Test
  void diagonalHasPieces_NegativeDiagonal_WorksCorrectly() {

    int c1 = 4, r1 = 4, c2 = 0, r2 = 0, dc = -1, dr = -1;

    when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(1);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(() -> PositionValidator.isValidPosition(3, 3))
          .thenReturn(true);
      positionValidator.when(() -> PositionValidator.isValidPosition(2, 2))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.isPathClearBetween(c1, r1, c2, r2, dc, dr,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void hasObstaclesBetween_AdjacentPositions_ReturnsTrue() {

    int fromCol = 1, fromRow = 1, toCol = 2, toRow = 2;

    boolean result =
        DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
            mockBoardState);

    assertTrue(result);
  }

  @Test
  void hasObstaclesBetween_SamePosition_ReturnsTrue() {

    int fromCol = 1, fromRow = 1, toCol = 1, toRow = 1;

    boolean result =
        DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
            mockBoardState);

    assertTrue(result);
  }

  @Test
  void hasObstaclesBetween_ClearPath_ReturnsFalse() {

    int fromCol = 0, fromRow = 0, toCol = 4, toRow = 4;

    when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void hasObstaclesBetween_ObstaclesPresent_ReturnsTrue() {

    int fromCol = 0, fromRow = 0, toCol = 4, toRow = 4;

    when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(1);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
              mockBoardState);

      assertFalse(result);
    }
  }

  @Test
  void hasObstaclesBetween_VerticalUp_WorksCorrectly() {

    int fromCol = 2, fromRow = 0, toCol = 2, toRow = 4;

    when(mockBoardState.getPiece(2, 1)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 3)).thenReturn(GameConstants.EMPTY);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void hasObstaclesBetween_HorizontalRight_WorksCorrectly() {

    int fromCol = 0, fromRow = 2, toCol = 4, toRow = 2;

    when(mockBoardState.getPiece(1, 2)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(3, 2)).thenReturn(GameConstants.EMPTY);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void hasObstaclesBetween_AntiDiagonal_WorksCorrectly() {

    int fromCol = 0, fromRow = 4, toCol = 4, toRow = 0;

    when(mockBoardState.getPiece(1, 3)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(3, 1)).thenReturn(GameConstants.EMPTY);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void hasObstaclesBetween_VerticalMovement_CalculatesDirectionCorrectly() {

    int fromCol = 3, fromRow = 1, toCol = 3, toRow = 5;

    when(mockBoardState.getPiece(3, 2)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(3, 4)).thenReturn(GameConstants.EMPTY);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void hasObstaclesBetween_HorizontalMovement_CalculatesDirectionCorrectly() {

    int fromCol = 1, fromRow = 3, toCol = 5, toRow = 3;

    when(mockBoardState.getPiece(2, 3)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(4, 3)).thenReturn(GameConstants.EMPTY);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void hasObstaclesBetween_NegativeDirection_WorksCorrectly() {

    int fromCol = 4, fromRow = 4, toCol = 1, toRow = 1;

    when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);
    when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class)) {
      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      boolean result =
          DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Test
  void hasObstaclesBetween_RowDifferenceOne_ReturnsTrue() {

    int fromCol = 2, fromRow = 2, toCol = 4, toRow = 3;

    boolean result =
        DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
            mockBoardState);

    assertTrue(result);
  }

  @Test
  void hasObstaclesBetween_NegativeRowDifferenceOne_ReturnsTrue() {

    int fromCol = 2, fromRow = 3, toCol = 4, toRow = 2;

    boolean result =
        DiagonalValidator.hasObstaclesBetween(fromCol, fromRow, toCol, toRow,
            mockBoardState);

    assertTrue(result);
  }
}