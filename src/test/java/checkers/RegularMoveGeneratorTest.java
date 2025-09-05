package checkers;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class RegularMoveGeneratorTest {

  private ArrayList<BotDecision> moves;
  private BoardState mockBoardState;

  @BeforeEach
  void setUp() {
    moves = new ArrayList<>();
    mockBoardState = mock(BoardState.class);
  }

  @Test
  void shouldHandleAllIllegalMoves() {

    int row = 3, col = 3;
    int piece = GameConstants.RED;

    try (MockedStatic<PositionValidator> positionValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

      positionValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);
      moveRules.when(
          () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(), anyInt(),
              anyInt(), any())).thenReturn(false);

      RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
          mockBoardState);

      assertEquals(0, moves.size());
    }
  }

  @Nested
  class RedPieceTests {

    @Test
    void shouldFindUpwardMovesForRedPiece() {

      int row = 5, col = 3;
      int piece = GameConstants.RED;

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class);
           MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(4, 2))
            .thenReturn(true);
        positionValidator.when(() -> PositionValidator.isValidPosition(4, 4))
            .thenReturn(true);

        moveRules.when(
                () -> MoveRules.isLegalMove(2, 4, 3, 5, piece, mockBoardState))
            .thenReturn(true);
        moveRules.when(
                () -> MoveRules.isLegalMove(4, 4, 3, 5, piece, mockBoardState))
            .thenReturn(true);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        assertEquals(2, moves.size());

        BotDecision move1 = moves.getFirst();
        assertEquals(5, move1.fromRow());
        assertEquals(3, move1.fromCol());
        assertEquals(4, move1.toRow());
        assertEquals(2, move1.toCol());
        assertEquals(GameConstants.MOVE, move1.moveType());

        BotDecision move2 = moves.get(1);
        assertEquals(5, move2.fromRow());
        assertEquals(3, move2.fromCol());
        assertEquals(4, move2.toRow());
        assertEquals(4, move2.toCol());
        assertEquals(GameConstants.MOVE, move2.moveType());
      }
    }

    @Test
    void shouldFindOnlyOneValidMoveWhenOtherIsBlockedForRedPiece() {

      int row = 3, col = 2;
      int piece = GameConstants.RED;

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class);
           MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(2, 1))
            .thenReturn(true);
        positionValidator.when(() -> PositionValidator.isValidPosition(2, 3))
            .thenReturn(true);

        moveRules.when(
                () -> MoveRules.isLegalMove(1, 2, 2, 3, piece, mockBoardState))
            .thenReturn(true);
        moveRules.when(
                () -> MoveRules.isLegalMove(3, 2, 2, 3, piece, mockBoardState))
            .thenReturn(false);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        assertEquals(1, moves.size());

        BotDecision move = moves.getFirst();
        assertEquals(3, move.fromRow());
        assertEquals(2, move.fromCol());
        assertEquals(2, move.toRow());
        assertEquals(1, move.toCol());
      }
    }

    @Test
    void shouldFindNoMovesWhenPositionsOutOfBoundsForRedPiece() {

      int row = 0, col = 1;
      int piece = GameConstants.RED;

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(-1, 0))
            .thenReturn(false);
        positionValidator.when(() -> PositionValidator.isValidPosition(-1, 2))
            .thenReturn(false);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        assertEquals(0, moves.size());
      }
    }
  }

  @Nested
  class BlackPieceTests {

    @Test
    void shouldFindDownwardMovesForBlackPiece() {

      int row = 2, col = 3;
      int piece = GameConstants.BLACK;

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class);
           MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(3, 2))
            .thenReturn(true);
        positionValidator.when(() -> PositionValidator.isValidPosition(3, 4))
            .thenReturn(true);

        moveRules.when(
                () -> MoveRules.isLegalMove(2, 3, 3, 2, piece, mockBoardState))
            .thenReturn(true);
        moveRules.when(
                () -> MoveRules.isLegalMove(4, 3, 3, 2, piece, mockBoardState))
            .thenReturn(true);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        assertEquals(2, moves.size());

        BotDecision move1 = moves.getFirst();
        assertEquals(2, move1.fromRow());
        assertEquals(3, move1.fromCol());
        assertEquals(3, move1.toRow());
        assertEquals(2, move1.toCol());

        BotDecision move2 = moves.get(1);
        assertEquals(2, move2.fromRow());
        assertEquals(3, move2.fromCol());
        assertEquals(3, move2.toRow());
        assertEquals(4, move2.toCol());
      }
    }

    @Test
    void shouldFindNoMovesWhenPositionsOutOfBoundsForBlackPiece() {

      int row = 7, col = 5;
      int piece = GameConstants.BLACK;

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(8, 4))
            .thenReturn(false);
        positionValidator.when(() -> PositionValidator.isValidPosition(8, 6))
            .thenReturn(false);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        assertEquals(0, moves.size());
      }
    }
  }

  @Nested
  class EdgeCaseTests {

    @Test
    void shouldWorkWithEmptyMovesList() {

      int row = 3, col = 3;
      int piece = GameConstants.RED;
      ArrayList<BotDecision> emptyMoves = new ArrayList<>();

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class);
           MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

        positionValidator.when(
                () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
            .thenReturn(true);
        moveRules.when(
            () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(), anyInt(),
                anyInt(), any())).thenReturn(true);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, emptyMoves,
            mockBoardState);

        assertEquals(2, emptyMoves.size());
      }
    }

    @Test
    void shouldAddMovesToExistingList() {

      int row = 4, col = 2;
      int piece = GameConstants.BLACK;
      moves.add(new BotDecision(0, 0, 1, 1, GameConstants.MOVE));

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class);
           MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(5, 1))
            .thenReturn(true);
        positionValidator.when(() -> PositionValidator.isValidPosition(5, 3))
            .thenReturn(true);

        moveRules.when(
                () -> MoveRules.isLegalMove(1, 5, 2, 4, piece, mockBoardState))
            .thenReturn(true);
        moveRules.when(
                () -> MoveRules.isLegalMove(3, 5, 2, 4, piece, mockBoardState))
            .thenReturn(false);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        assertEquals(2, moves.size());

        BotDecision firstMove = moves.getFirst();
        assertEquals(0, firstMove.fromRow());
        assertEquals(0, firstMove.fromCol());
      }
    }

    @Test
    void shouldCheckPositionsInCorrectOrder() {

      int row = 3, col = 3;
      int piece = GameConstants.RED;

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class);
           MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(2, 2))
            .thenReturn(true);
        positionValidator.when(() -> PositionValidator.isValidPosition(2, 4))
            .thenReturn(true);

        moveRules.when(
            () -> MoveRules.isLegalMove(anyInt(), anyInt(), anyInt(), anyInt(),
                anyInt(), any())).thenReturn(true);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        positionValidator.verify(() -> PositionValidator.isValidPosition(2, 2));
        positionValidator.verify(() -> PositionValidator.isValidPosition(2, 4));

        moveRules.verify(
            () -> MoveRules.isLegalMove(2, 2, 3, 3, piece, mockBoardState));
        moveRules.verify(
            () -> MoveRules.isLegalMove(4, 2, 3, 3, piece, mockBoardState));
      }
    }
  }

  @Nested
  class BoardEdgeTests {

    @Test
    void shouldHandlePieceOnLeftEdge() {

      int row = 3, col = 0;
      int piece = GameConstants.RED;

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class);
           MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(2, -1))
            .thenReturn(false);
        positionValidator.when(() -> PositionValidator.isValidPosition(2, 1))
            .thenReturn(true);

        moveRules.when(
                () -> MoveRules.isLegalMove(1, 2, 0, 3, piece, mockBoardState))
            .thenReturn(true);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        assertEquals(1, moves.size());
        BotDecision move = moves.getFirst();
        assertEquals(2, move.toRow());
        assertEquals(1, move.toCol());
      }
    }

    @Test
    void shouldHandlePieceOnRightEdge() {

      int row = 4, col = 7;
      int piece = GameConstants.BLACK;

      try (MockedStatic<PositionValidator> positionValidator = mockStatic(
          PositionValidator.class);
           MockedStatic<MoveRules> moveRules = mockStatic(MoveRules.class)) {

        positionValidator.when(() -> PositionValidator.isValidPosition(5, 6))
            .thenReturn(true);
        positionValidator.when(() -> PositionValidator.isValidPosition(5, 8))
            .thenReturn(false);

        moveRules.when(
                () -> MoveRules.isLegalMove(6, 5, 7, 4, piece, mockBoardState))
            .thenReturn(true);

        RegularMoveGenerator.findRegularPieceMoves(row, col, piece, moves,
            mockBoardState);

        assertEquals(1, moves.size());
        BotDecision move = moves.getFirst();
        assertEquals(5, move.toRow());
        assertEquals(6, move.toCol());
      }
    }
  }
}