package checkers;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class CaptureGeneratorTest {

  private ArrayList<BotDecision> moves;
  private BoardState mockBoardState;

  @BeforeEach
  void setUp() {
    moves = new ArrayList<>();
    mockBoardState = mock(BoardState.class);
  }

  @Test
  void testFindRegularCaptures_RedPiece_ValidCaptureLeft() {
    int row = 4, col = 4;
    int piece = GameConstants.RED;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<CaptureRules> captureRules = mockStatic(
             CaptureRules.class)) {

      posValidator.when(() -> PositionValidator.isValidPosition(2, 2))
          .thenReturn(true);
      posValidator.when(() -> PositionValidator.isValidPosition(2, 6))
          .thenReturn(true);

      captureRules.when(
              () -> CaptureRules.isLegalCapture(2, 2, 4, 4, piece,
                  mockBoardState))
          .thenReturn(true);
      captureRules.when(
              () -> CaptureRules.isLegalCapture(6, 2, 4, 4, piece,
                  mockBoardState))
          .thenReturn(false);

      CaptureGenerator.findRegularCaptures(row, col, piece, moves,
          mockBoardState);

      assertEquals(1, moves.size());
      BotDecision decision = moves.getFirst();
      assertEquals(row, decision.fromRow());
      assertEquals(col, decision.fromCol());
      assertEquals(2, decision.toRow());
      assertEquals(2, decision.toCol());
      assertEquals(GameConstants.TAKE, decision.moveType());
    }
  }

  @Test
  void testFindRegularCaptures_RedPiece_ValidCaptureRight() {
    int row = 4, col = 4;
    int piece = GameConstants.RED;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<CaptureRules> captureRules = mockStatic(
             CaptureRules.class)) {

      posValidator.when(() -> PositionValidator.isValidPosition(2, 2))
          .thenReturn(true);
      posValidator.when(() -> PositionValidator.isValidPosition(2, 6))
          .thenReturn(true);

      captureRules.when(
              () -> CaptureRules.isLegalCapture(2, 2, 4, 4, piece,
                  mockBoardState))
          .thenReturn(false);
      captureRules.when(
              () -> CaptureRules.isLegalCapture(6, 2, 4, 4, piece,
                  mockBoardState))
          .thenReturn(true);

      CaptureGenerator.findRegularCaptures(row, col, piece, moves,
          mockBoardState);

      assertEquals(1, moves.size());
      BotDecision decision = moves.getFirst();
      assertEquals(2, decision.toRow());
      assertEquals(6, decision.toCol());
    }
  }

  @Test
  void testFindRegularCaptures_BlackPiece_DownwardDirection() {
    int row = 2, col = 2;
    int piece = GameConstants.BLACK;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<CaptureRules> captureRules = mockStatic(
             CaptureRules.class)) {

      posValidator.when(() -> PositionValidator.isValidPosition(4, 0))
          .thenReturn(true);
      posValidator.when(() -> PositionValidator.isValidPosition(4, 4))
          .thenReturn(true);

      captureRules.when(
              () -> CaptureRules.isLegalCapture(0, 4, 2, 2, piece,
                  mockBoardState))
          .thenReturn(true);
      captureRules.when(
              () -> CaptureRules.isLegalCapture(4, 4, 2, 2, piece,
                  mockBoardState))
          .thenReturn(true);

      CaptureGenerator.findRegularCaptures(row, col, piece, moves,
          mockBoardState);

      assertEquals(2, moves.size());
    }
  }

  @Test
  void testFindRegularCaptures_NoCaptures_InvalidPositions() {
    int row = 0, col = 0;
    int piece = GameConstants.RED_KING;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class)) {
      posValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(false);

      CaptureGenerator.findRegularCaptures(row, col, piece, moves,
          mockBoardState);

      assertEquals(0, moves.size());
    }
  }

  @Test
  void testFindRegularCaptures_NoCaptures_IllegalCaptures() {
    int row = 4, col = 4;
    int piece = GameConstants.RED_KING;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<CaptureRules> captureRules = mockStatic(
             CaptureRules.class)) {

      posValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);
      captureRules.when(
              () -> CaptureRules.isLegalCapture(anyInt(), anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(false);

      CaptureGenerator.findRegularCaptures(row, col, piece, moves,
          mockBoardState);

      assertEquals(0, moves.size());
    }
  }

  @Test
  void testFindRegularCaptures_BothCapturesPossible() {
    int row = 4, col = 4;
    int piece = GameConstants.RED_KING;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<CaptureRules> captureRules = mockStatic(
             CaptureRules.class)) {

      posValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);
      captureRules.when(
              () -> CaptureRules.isLegalCapture(anyInt(), anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);

      CaptureGenerator.findRegularCaptures(row, col, piece, moves,
          mockBoardState);

      assertEquals(2, moves.size());
    }
  }

  @Test
  void testFindKingCaptures_MultipleValidCaptures() {
    int row = 4, col = 4, piece = GameConstants.RED_KING;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<CaptureRules> captureRules = mockStatic(
             CaptureRules.class);
         MockedStatic<DiagonalValidator> diagValidator = mockStatic(
             DiagonalValidator.class)) {

      posValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);
      captureRules.when(
              () -> CaptureRules.isLegalCapture(anyInt(), anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);
      diagValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);

      CaptureGenerator.findKingCaptures(row, col, piece, moves,
          mockBoardState);

      assertTrue(moves.size() >= 2);
      assertTrue(moves.stream()
          .allMatch(move -> move.moveType() == GameConstants.QUEEN_TAKE));

    }
  }

  @Test
  void testFindKingCaptures_InvalidPositions() {
    int row = 7, col = 7, piece = GameConstants.RED_KING;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class)
    ) {

      posValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(false);

      CaptureGenerator.findKingCaptures(row, col, piece, moves, mockBoardState);

      assertEquals(0, moves.size());
    }
  }

  @Test
  void testFindKingCaptures_IllegalCaptures() {
    int row = 4, col = 4, piece = GameConstants.RED_KING;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<CaptureRules> captureRules = mockStatic(
             CaptureRules.class)
    ) {

      posValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);
      captureRules.when(
              () -> CaptureRules.isLegalCapture(anyInt(), anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(false);

      CaptureGenerator.findKingCaptures(row, col, piece, moves, mockBoardState);

      assertEquals(0, moves.size());
    }
  }

  @Test
  void testFindKingCaptures_NoObstaclesBetween() {
    int row = 4, col = 4, piece = GameConstants.RED_KING;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class);
         MockedStatic<CaptureRules> captureRules = mockStatic(
             CaptureRules.class);
         MockedStatic<DiagonalValidator> diagValidator = mockStatic(
             DiagonalValidator.class)
    ) {

      posValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(true);
      captureRules.when(
              () -> CaptureRules.isLegalCapture(anyInt(), anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(true);
      diagValidator.when(
              () -> DiagonalValidator.hasObstaclesBetween(anyInt(), anyInt(),
                  anyInt(), anyInt(), any()))
          .thenReturn(false);

      CaptureGenerator.findKingCaptures(row, col, piece, moves, mockBoardState);

      assertEquals(0, moves.size());
    }
  }

  @Test
  void testFindKingCaptures_BreakOnFirstInvalidPosition() {
    int row = 6, col = 6, piece = GameConstants.RED_KING;

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class)
    ) {

      posValidator.when(() -> PositionValidator.isValidPosition(7, 7))
          .thenReturn(true);
      posValidator.when(() -> PositionValidator.isValidPosition(8, 8))
          .thenReturn(false);

      CaptureGenerator.findKingCaptures(row, col, piece, moves, mockBoardState);

      posValidator.verify(() -> PositionValidator.isValidPosition(7, 7),
          times(1));
      posValidator.verify(() -> PositionValidator.isValidPosition(8, 8),
          times(1));
      posValidator.verify(() -> PositionValidator.isValidPosition(9, 9),
          never());
    }
  }


  @Test
  void testPrivateConstructor() {

    assertThrows(Exception.class, () -> {
      var constructor = CaptureGenerator.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      constructor.newInstance();
    });
  }

  @Test
  void testEmptyMovesListInitially() {
    assertTrue(moves.isEmpty());

    try (MockedStatic<PositionValidator> posValidator = mockStatic(
        PositionValidator.class)) {
      posValidator.when(
              () -> PositionValidator.isValidPosition(anyInt(), anyInt()))
          .thenReturn(false);

      CaptureGenerator.findRegularCaptures(0, 0, GameConstants.RED_KING, moves,
          mockBoardState);

      assertTrue(moves.isEmpty());
    }
  }

  @Test
  void testNullBoardState() {
    assertDoesNotThrow(
        () -> CaptureGenerator.findRegularCaptures(0, 0, GameConstants.RED,
            moves, null));
  }

  @Test
  void testNullMovesList() {
    assertThrows(NullPointerException.class,
        () -> CaptureGenerator.findRegularCaptures(0, 0, GameConstants.RED,
            null, mockBoardState));
  }
}