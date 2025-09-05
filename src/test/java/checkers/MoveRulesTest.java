package checkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class MoveRulesTest {

  @Mock
  private BoardState boardState;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  class CanMoveTests {

    @Test
    void shouldReturnTrueForRedPieceThatCanMove() {

      int col = 3, row = 2;
      when(boardState.getPiece(row, col)).thenReturn(GameConstants.RED);
      when(boardState.getPiece(row - 1, col + 1)).thenReturn(
          GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnFalseForRedPieceThatCannotMove() {

      int col = 3, row = 2;
      when(boardState.getPiece(row, col)).thenReturn(GameConstants.RED);
      when(boardState.getPiece(row - 1, col + 1)).thenReturn(
          GameConstants.BLACK);
      when(boardState.getPiece(row - 1, col - 1)).thenReturn(GameConstants.RED);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.BLACK))
            .thenReturn(false);
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.RED))
            .thenReturn(false);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnTrueForBlackPieceThatCanMove() {

      int col = 3, row = 2;
      when(boardState.getPiece(row, col)).thenReturn(GameConstants.BLACK);
      when(boardState.getPiece(row + 1, col - 1)).thenReturn(
          GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnFalseForBlackPieceThatCannotMove() {

      int col = 3, row = 2;
      when(boardState.getPiece(row, col)).thenReturn(GameConstants.BLACK);
      when(boardState.getPiece(row + 1, col + 1)).thenReturn(GameConstants.RED);
      when(boardState.getPiece(row + 1, col - 1)).thenReturn(
          GameConstants.BLACK);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.RED))
            .thenReturn(false);
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.BLACK))
            .thenReturn(false);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnTrueForKingPiece() {

      int col = 3, row = 2;
      int kingPiece = GameConstants.RED_KING;
      when(boardState.getPiece(row, col)).thenReturn(kingPiece);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isKing(kingPiece))
            .thenReturn(true);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldHandleRedPieceAtRightEdge() {

      int col = GameConstants.LAST_ROW_INDEX, row = 2;
      when(boardState.getPiece(row, col)).thenReturn(GameConstants.RED);
      when(boardState.getPiece(row - 1, col - 1)).thenReturn(
          GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldHandleRedPieceAtLeftEdge() {

      int col = 0, row = 2;
      when(boardState.getPiece(row, col)).thenReturn(GameConstants.RED);
      when(boardState.getPiece(row - 1, col + 1)).thenReturn(
          GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldHandleBlackPieceAtRightEdge() {

      int col = GameConstants.LAST_ROW_INDEX, row = 2;
      when(boardState.getPiece(row, col)).thenReturn(GameConstants.BLACK);
      when(boardState.getPiece(row + 1, col - 1)).thenReturn(
          GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldHandleBlackPieceAtLeftEdge() {

      int col = 0, row = 2;
      when(boardState.getPiece(row, col)).thenReturn(GameConstants.BLACK);
      when(boardState.getPiece(row + 1, col + 1)).thenReturn(
          GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result = MoveRules.canMove(col, row, boardState);

        assertTrue(result);
      }
    }
  }

  @Nested
  class IsLegalMoveTests {

    @Test
    void shouldReturnFalseWhenDestinationIsOccupied() {

      int c1 = 2, r1 = 2, c2 = 3, r2 = 1;
      int color = GameConstants.RED;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.BLACK);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.BLACK))
            .thenReturn(false);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnTrueForValidRedPieceMove() {

      int c1 = 2, r1 = 2, c2 = 3, r2 = 1;
      int color = GameConstants.RED;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnFalseForInvalidRedPieceMoveWrongDirection() {

      int c1 = 2, r1 = 2, c2 = 3, r2 = 3;
      int color = GameConstants.RED;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnFalseForInvalidRedPieceMoveTooFar() {

      int c1 = 2, r1 = 2, c2 = 4, r2 = 0;
      int color = GameConstants.RED;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnTrueForValidBlackPieceMove() {

      int c1 = 2, r1 = 2, c2 = 3, r2 = 3;
      int color = GameConstants.BLACK;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnFalseForInvalidBlackPieceMoveWrongDirection() {

      int c1 = 2, r1 = 2, c2 = 3, r2 = 1;
      int color = GameConstants.BLACK;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnTrueForValidRedKingMove() {

      int c1 = 2, r1 = 2, c2 = 4, r2 = 4;
      int color = GameConstants.RED_KING;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);
      when(boardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class);
           var positionValidatorMock = mockStatic(PositionValidator.class)) {

        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);
        positionValidatorMock.when(
                () -> PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2))
            .thenReturn(false);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnTrueForValidBlackKingMove() {

      int c1 = 4, r1 = 4, c2 = 2, r2 = 2;
      int color = GameConstants.BLACK_KING;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);
      when(boardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class);
           var positionValidatorMock = mockStatic(PositionValidator.class)) {

        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);
        positionValidatorMock.when(
                () -> PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2))
            .thenReturn(false);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnFalseForKingMoveNotOnDiagonal() {

      int c1 = 2, r1 = 2, c2 = 4, r2 = 3;
      int color = GameConstants.RED_KING;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class);
           var positionValidatorMock = mockStatic(PositionValidator.class)) {

        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);
        positionValidatorMock.when(
                () -> PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2))
            .thenReturn(true);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnFalseForKingMoveWithPieceInPath() {

      int c1 = 2, r1 = 2, c2 = 4, r2 = 4;
      int color = GameConstants.RED_KING;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);
      when(boardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);

      try (var pieceRulesMock = mockStatic(PieceRules.class);
           var positionValidatorMock = mockStatic(PositionValidator.class)) {

        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);
        positionValidatorMock.when(
                () -> PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2))
            .thenReturn(false);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnFalseForUnknownPieceType() {

      int c1 = 2, r1 = 2, c2 = 3, r2 = 1;
      int color = 999;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldHandleKingMoveWithMultiplePiecesInLongerPath() {

      int c1 = 1, r1 = 1, c2 = 5, r2 = 5;
      int color = GameConstants.BLACK_KING;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);
      when(boardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);
      when(boardState.getPiece(3, 3)).thenReturn(GameConstants.RED);
      when(boardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class);
           var positionValidatorMock = mockStatic(PositionValidator.class)) {

        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);
        positionValidatorMock.when(
                () -> PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2))
            .thenReturn(false);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertFalse(result);
      }
    }

    @Test
    void shouldHandleKingMoveBackwards() {

      int c1 = 4, r1 = 4, c2 = 2, r2 = 2;
      int color = GameConstants.RED_KING;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);
      when(boardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class);
           var positionValidatorMock = mockStatic(PositionValidator.class)) {

        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);
        positionValidatorMock.when(
                () -> PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2))
            .thenReturn(false);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertTrue(result);
      }
    }

    @Test
    void shouldHandleSingleStepKingMove() {

      int c1 = 3, r1 = 3, c2 = 4, r2 = 4;
      int color = GameConstants.RED_KING;
      when(boardState.getPiece(r2, c2)).thenReturn(GameConstants.EMPTY);

      try (var pieceRulesMock = mockStatic(PieceRules.class);
           var positionValidatorMock = mockStatic(PositionValidator.class)) {

        pieceRulesMock.when(() -> PieceRules.isEmpty(GameConstants.EMPTY))
            .thenReturn(true);
        positionValidatorMock.when(
                () -> PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2))
            .thenReturn(false);

        boolean result =
            MoveRules.isLegalMove(c2, r2, c1, r1, color, boardState);

        assertTrue(result);
      }
    }
  }
}