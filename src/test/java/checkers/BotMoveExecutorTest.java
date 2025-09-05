package checkers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotMoveExecutorTest {

  private static final int RED = GameConstants.RED;
  private static final int BLACK = GameConstants.BLACK;
  private static final int RED_KING = GameConstants.RED_KING;
  private static final int BLACK_KING = GameConstants.BLACK_KING;
  final PromotionService promotionService = mock(PromotionService.class);
  final BoardState boardState = mock(BoardState.class);
  final PlayerConfiguration playerConfig = mock(PlayerConfiguration.class);
  final BotMoveExecutor executor =
      new BotMoveExecutor(promotionService, boardState, playerConfig);

  @Nested
  class ConstructorTest {

    @Test
    void shouldCreateInstanceWithGivenDependencies() {
      BotMoveExecutor localExecutor =
          new BotMoveExecutor(promotionService, boardState, playerConfig);

      assertNotNull(localExecutor);
      assertEquals(promotionService, localExecutor.promotionService());
      assertEquals(boardState, localExecutor.boardState());
      assertEquals(playerConfig, localExecutor.playerConfig());
    }

    @Test
    void shouldAcceptNullDependencies() {
      BotMoveExecutor localExecutor = new BotMoveExecutor(null, null, null);

      assertNull(localExecutor.promotionService());
      assertNull(localExecutor.boardState());
      assertNull(localExecutor.playerConfig());
    }
  }

  @Nested
  class AccessorMethodsTest {

    @Test
    void accessorsShouldReturnCorrectValues() {
      BotMoveExecutor localExecutor =
          new BotMoveExecutor(promotionService, boardState, playerConfig);

      assertEquals(promotionService, localExecutor.promotionService());
      assertEquals(boardState, localExecutor.boardState());
      assertEquals(playerConfig, localExecutor.playerConfig());
    }

    @Test
    void eachInstanceShouldMaintainItsOwnValues() {
      PromotionService ps2 = mock(PromotionService.class);
      BoardState bs2 = mock(BoardState.class);
      PlayerConfiguration pc2 = mock(PlayerConfiguration.class);

      BotMoveExecutor exec1 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);
      BotMoveExecutor exec2 = new BotMoveExecutor(ps2, bs2, pc2);

      assertNotEquals(exec1, exec2);
      assertEquals(promotionService, exec1.promotionService());
      assertEquals(ps2, exec2.promotionService());
    }
  }

  @Nested
  class EqualsTest {

    @Test
    void shouldBeEqualToAnotherWithSameDependencies() {
      BotMoveExecutor exec1 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);
      BotMoveExecutor exec2 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);

      assertEquals(exec1, exec2);
    }

    @Test
    void shouldNotBeEqualIfDifferentPromotionService() {
      PromotionService otherPs = mock(PromotionService.class);
      BotMoveExecutor exec1 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);
      BotMoveExecutor exec2 =
          new BotMoveExecutor(otherPs, boardState, playerConfig);

      assertNotEquals(exec1, exec2);
    }

    @Test
    void shouldNotBeEqualIfDifferentBoardState() {
      BoardState otherBs = mock(BoardState.class);
      BotMoveExecutor exec1 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);
      BotMoveExecutor exec2 =
          new BotMoveExecutor(promotionService, otherBs, playerConfig);

      assertNotEquals(exec1, exec2);
    }

    @Test
    void shouldNotBeEqualIfDifferentPlayerConfig() {
      PlayerConfiguration otherPc = mock(PlayerConfiguration.class);
      BotMoveExecutor exec1 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);
      BotMoveExecutor exec2 =
          new BotMoveExecutor(promotionService, boardState, otherPc);

      assertNotEquals(exec1, exec2);
    }
  }

  @Nested
  class HashCodeTest {

    @Test
    void shouldReturnSameHashCodeForEqualObjects() {
      BotMoveExecutor exec1 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);
      BotMoveExecutor exec2 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);

      assertEquals(exec1.hashCode(), exec2.hashCode());
    }

    @Test
    void shouldReturnConsistentHashCode() {
      BotMoveExecutor exec =
          new BotMoveExecutor(promotionService, boardState, playerConfig);

      int h1 = exec.hashCode();
      int h2 = exec.hashCode();

      assertEquals(h1, h2);
    }

    @Test
    void shouldReturnDifferentHashCodeForDifferentObjects() {
      PromotionService ps2 = mock(PromotionService.class);
      BotMoveExecutor exec1 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);
      BotMoveExecutor exec2 =
          new BotMoveExecutor(ps2, boardState, playerConfig);

      assertNotEquals(exec1.hashCode(), exec2.hashCode());
    }
  }

  @Nested
  class ToStringTest {

    @Test
    void shouldContainClassNameAndFieldNames() {
      BotMoveExecutor exec =
          new BotMoveExecutor(promotionService, boardState, playerConfig);

      String text = exec.toString();

      assertTrue(text.contains("BotMoveExecutor"));
      assertTrue(text.contains("promotionService"));
      assertTrue(text.contains("boardState"));
      assertTrue(text.contains("playerConfig"));
    }

    @Test
    void shouldNotBeNullOrEmpty() {
      BotMoveExecutor exec =
          new BotMoveExecutor(promotionService, boardState, playerConfig);

      String text = exec.toString();

      assertNotNull(text);
      assertFalse(text.isEmpty());
    }
  }

  @Nested
  class ExecuteMoveTest {

    @Test
    void shouldApplyMoveAndThenPromote() {
      BotDecision decision = new BotDecision(2, 1, 3, 2, 1);
      when(playerConfig.getBotColor()).thenReturn(RED);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(3, 2, RED);
      }
    }

    @ParameterizedTest
    @CsvSource({
        "0,0,1,1,1",
        "6,6,7,7,2",
        "3,4,4,5,3",
        "5,5,0,0,4"
    })
    void shouldCallPromotionWithCorrectCoordinatesAndColor(
        int fromRow, int fromCol, int toRow, int toCol, int color) {

      BotDecision decision = new BotDecision(fromRow, fromCol, toRow, toCol, 1);
      when(playerConfig.getBotColor()).thenReturn(color);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(toRow, toCol, color);
      }
    }

    @Test
    void shouldHandleVeryLargeCoordinates() {
      int max = Integer.MAX_VALUE;
      BotDecision decision = new BotDecision(max, max, max, max, max);
      when(playerConfig.getBotColor()).thenReturn(BLACK);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        assertDoesNotThrow(() -> executor.executeMove(decision));

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(max, max, BLACK);
      }
    }

    @Test
    void shouldHandleNegativeCoordinates() {
      BotDecision decision = new BotDecision(-1, -2, -3, -4, -5);
      when(playerConfig.getBotColor()).thenReturn(RED);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(-3, -4, RED);
      }
    }
  }

  @Nested
  class BusinessScenariosTest {

    @Test
    void shouldRepresentRegularRedMove() {
      BotDecision decision = new BotDecision(2, 1, 3, 2, 1);
      when(playerConfig.getBotColor()).thenReturn(RED);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(3, 2, RED);
      }
    }

    @Test
    void shouldRepresentCaptureBlackMove() {
      BotDecision decision = new BotDecision(5, 2, 3, 4, 2);
      when(playerConfig.getBotColor()).thenReturn(BLACK);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(3, 4, BLACK);
      }
    }

    @Test
    void shouldPromoteRedPieceToKingOnLastRow() {
      BotDecision decision = new BotDecision(6, 1, 7, 0, 1);
      when(playerConfig.getBotColor()).thenReturn(RED);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(7, 0, RED);
      }
    }

    @Test
    void shouldPromoteBlackPieceToKingOnFirstRow() {
      BotDecision decision = new BotDecision(1, 2, 0, 3, 1);
      when(playerConfig.getBotColor()).thenReturn(BLACK);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(0, 3, BLACK);
      }
    }

    @Test
    void shouldHandleAlreadyRedKingMove() {
      BotDecision decision = new BotDecision(4, 4, 5, 5, 3);
      when(playerConfig.getBotColor()).thenReturn(RED_KING);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(5, 5, RED_KING);
      }
    }

    @Test
    void shouldHandleAlreadyBlackKingMove() {
      BotDecision decision = new BotDecision(2, 2, 1, 1, 3);
      when(playerConfig.getBotColor()).thenReturn(BLACK_KING);

      try (MockedStatic<MoveExecutor> mocked = mockStatic(MoveExecutor.class)) {
        executor.executeMove(decision);

        mocked.verify(() -> MoveExecutor.applyMoveToBoard(decision, boardState,
            playerConfig));
        verify(promotionService).promoteIfNeeded(1, 1, BLACK_KING);
      }
    }

    @Test
    void shouldHandleMovesOnBoardEdges() {
      BotDecision topLeft = new BotDecision(0, 0, 1, 1, 1);
      BotDecision topRight = new BotDecision(0, 7, 1, 6, 1);
      BotDecision bottomLeft = new BotDecision(7, 0, 6, 1, 1);
      BotDecision bottomRight = new BotDecision(7, 7, 6, 6, 1);

      when(playerConfig.getBotColor()).thenReturn(RED);

      try (MockedStatic<MoveExecutor> _ = mockStatic(MoveExecutor.class)) {
        executor.executeMove(topLeft);
        executor.executeMove(topRight);
        executor.executeMove(bottomLeft);
        executor.executeMove(bottomRight);

        verify(promotionService).promoteIfNeeded(1, 1, RED);
        verify(promotionService).promoteIfNeeded(1, 6, RED);
        verify(promotionService).promoteIfNeeded(6, 1, RED);
        verify(promotionService).promoteIfNeeded(6, 6, RED);
      }
    }
  }

  @Nested
  class ImmutabilityTest {

    @Test
    void recordShouldBeImmutableMultipleCallsShouldReturnSameValues() {
      BotMoveExecutor exec =
          new BotMoveExecutor(promotionService, boardState, playerConfig);

      for (int i = 0; i < 5; i++) {
        assertEquals(promotionService, exec.promotionService());
        assertEquals(boardState, exec.boardState());
        assertEquals(playerConfig, exec.playerConfig());
      }
    }

    @Test
    void differentInstancesShouldRemainIndependent() {
      PromotionService ps2 = mock(PromotionService.class);
      BoardState bs2 = mock(BoardState.class);
      PlayerConfiguration pc2 = mock(PlayerConfiguration.class);

      BotMoveExecutor exec1 =
          new BotMoveExecutor(promotionService, boardState, playerConfig);
      BotMoveExecutor exec2 = new BotMoveExecutor(ps2, bs2, pc2);

      assertEquals(promotionService, exec1.promotionService());
      assertEquals(ps2, exec2.promotionService());

      assertEquals(boardState, exec1.boardState());
      assertEquals(bs2, exec2.boardState());

      assertEquals(playerConfig, exec1.playerConfig());
      assertEquals(pc2, exec2.playerConfig());
    }
  }
}
