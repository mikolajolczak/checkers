package checkers;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class KingMoveGeneratorTest {

  private static final int BOARD_SIZE = GameConstants.BOARD_SIZE;
  private static final int KING_PIECE = GameConstants.RED_KING;
  private static final int MOVE_TYPE = GameConstants.MOVE;
  private ArrayList<BotDecision> moves;
  private BoardState boardState;

  @BeforeEach
  void setUp() {
    moves = new ArrayList<>();
    boardState = mock(BoardState.class);
  }

  @Test
  void shouldFindKingMovesInAllDirections() {

    int startRow = 4, startCol = 4;

    try (
        MockedStatic<PositionValidator> positionValidator = mockStatic(
            PositionValidator.class);
        MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class);
        MockedStatic<DiagonalValidator> diagonalValidator = mockStatic(
            DiagonalValidator.class)) {

      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      moveRules.when(
              () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(),
                  anyInt(),
                  anyInt(), any()))
          .thenReturn(true);

      diagonalValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);

      KingMoveGenerator.findKingMoves(startRow, startCol, KING_PIECE, moves,
          boardState);

      assertEquals(28, moves.size());

      for (BotDecision move : moves) {
        assertEquals(startRow, move.fromRow());
        assertEquals(startCol, move.fromCol());
        assertEquals(MOVE_TYPE, move.moveType());
      }
    }
  }

  @Test
  void shouldStopWhenPositionIsOutOfBounds() {

    int startRow = 7, startCol = 7;

    try (
        MockedStatic<PositionValidator> positionValidator = mockStatic(
            PositionValidator.class);
        MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class);
        MockedStatic<DiagonalValidator> diagonalValidator = mockStatic(
            DiagonalValidator.class)) {

      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenAnswer(invocation -> {
            int row = invocation.getArgument(0);
            int col = invocation.getArgument(1);
            return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
          });

      moveRules.when(
              () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(),
                  anyInt(),
                  anyInt(), any()))
          .thenReturn(true);

      diagonalValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);

      KingMoveGenerator.findKingMoves(startRow, startCol, KING_PIECE, moves,
          boardState);

      assertTrue(moves.size() <= 7);
    }
  }

  @Test
  void shouldStopWhenMoveIsIllegal() {

    int startRow = 4, startCol = 4;

    try (
        MockedStatic<PositionValidator> positionValidator = mockStatic(
            PositionValidator.class);
        MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class);
        MockedStatic<DiagonalValidator> diagonalValidator = mockStatic(
            DiagonalValidator.class)) {

      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      moveRules.when(
              () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(),
                  anyInt(),
                  anyInt(), any()))
          .thenAnswer(invocation -> {
            int newRow = invocation.getArgument(1);
            int newCol = invocation.getArgument(0);

            return Math.abs(newRow - startRow) == 1
                && Math.abs(newCol - startCol) == 1;
          });

      diagonalValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);

      KingMoveGenerator.findKingMoves(startRow, startCol, KING_PIECE, moves,
          boardState);

      assertEquals(4, moves.size());
    }
  }

  @Test
  void shouldNotAddMoveWhenObstaclesExist() {

    int startRow = 4, startCol = 4;

    try (
        MockedStatic<PositionValidator> positionValidator = mockStatic(
            PositionValidator.class);
        MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class);
        MockedStatic<DiagonalValidator> diagonalValidator = mockStatic(
            DiagonalValidator.class)) {

      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      moveRules.when(
              () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(),
                  anyInt(),
                  anyInt(), any()))
          .thenReturn(true);

      diagonalValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(false);

      KingMoveGenerator.findKingMoves(startRow, startCol, KING_PIECE, moves,
          boardState);

      assertEquals(0, moves.size());
    }
  }

  @Test
  void shouldHandleCornerStartPosition() {

    int startRow = 0, startCol = 0;

    try (
        MockedStatic<PositionValidator> positionValidator = mockStatic(
            PositionValidator.class);
        MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class);
        MockedStatic<DiagonalValidator> diagonalValidator = mockStatic(
            DiagonalValidator.class)) {

      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenAnswer(invocation -> {
            int row = invocation.getArgument(0);
            int col = invocation.getArgument(1);
            return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
          });

      moveRules.when(
              () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(),
                  anyInt(),
                  anyInt(), any()))
          .thenReturn(true);

      diagonalValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);

      KingMoveGenerator.findKingMoves(startRow, startCol, KING_PIECE, moves,
          boardState);

      assertTrue(moves.size() <= 7);

      for (BotDecision move : moves) {
        assertTrue(move.toRow() > startRow);
        assertTrue(move.toCol() > startCol);
      }
    }
  }

  @Test
  void shouldHandleEmptyMovesList() {

    int startRow = 4, startCol = 4;
    ArrayList<BotDecision> emptyMoves = new ArrayList<>();

    try (
        MockedStatic<PositionValidator> positionValidator = mockStatic(
            PositionValidator.class);
        MockedStatic<MoveRules> _ = mockStatic(MoveRules.class);
        MockedStatic<DiagonalValidator> _ = mockStatic(
            DiagonalValidator.class)) {

      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(false);

      KingMoveGenerator.findKingMoves(startRow, startCol, KING_PIECE,
          emptyMoves, boardState);

      assertEquals(0, emptyMoves.size());
    }
  }

  @Test
  void shouldCheckAllDirectionsForKing() {

    int startRow = 3, startCol = 3;

    try (
        MockedStatic<PositionValidator> positionValidator = mockStatic(
            PositionValidator.class);
        MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class);
        MockedStatic<DiagonalValidator> diagonalValidator = mockStatic(
            DiagonalValidator.class)) {

      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);

      moveRules.when(
              () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(),
                  anyInt(),
                  anyInt(), any()))
          .thenReturn(true);

      diagonalValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);

      KingMoveGenerator.findKingMoves(startRow, startCol, KING_PIECE, moves,
          boardState);

      positionValidator.verify(
          () -> PositionValidator.isValidPosition(anyInt(), anyInt()),
          atLeast(4));
      moveRules.verify(
          () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(), anyInt(),
              anyInt(), any()), atLeast(4));
    }
  }

  @Test
  void shouldPassCorrectParametersToIsLegalMove() {

    int startRow = 2, startCol = 2;

    try (
        MockedStatic<PositionValidator> positionValidator = mockStatic(
            PositionValidator.class);
        MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class);
        MockedStatic<DiagonalValidator> diagonalValidator = mockStatic(
            DiagonalValidator.class)) {

      positionValidator.when(() -> PositionValidator.isValidPosition(3, 3))
          .thenReturn(true);
      positionValidator.when(() -> PositionValidator.isValidPosition(4, 4))
          .thenReturn(false);

      moveRules.when(
              () -> MoveRules.isLegalMove(3, 3, 2, 2, KING_PIECE, boardState))
          .thenReturn(true);

      diagonalValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(2, 2, 3, 3,
                  boardState))
          .thenReturn(true);

      KingMoveGenerator.findKingMoves(startRow, startCol, KING_PIECE, moves,
          boardState);

      moveRules.verify(
          () -> MoveRules.isLegalMove(3, 3, 2, 2, KING_PIECE, boardState));
      diagonalValidator.verify(
          () -> DiagonalValidator.hasObstaclesBetween(2, 2, 3, 3, boardState));
    }
  }
}