package checkers;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotAITest {

  @Mock
  private MoveService mockMoveService;

  @Mock
  private BotState mockBotState;

  @Mock
  private BoardState mockBoardState;

  @Mock
  private PlayerConfiguration mockPlayerConfiguration;

  @Mock
  private BotDecision mockBotDecision1;

  @Mock
  private BotDecision mockBotDecision2;

  @Mock
  private BotDecision mockBotDecision3;

  @Mock
  private BotDecision mockBestDecision;

  private BotAI botAI;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    botAI = new BotAI(mockMoveService);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  class ConstructorTest {

    @Test
    void shouldCreateInstanceWithGivenMoveService() {

      MoveService moveService = mock(MoveService.class);

      BotAI bot = new BotAI(moveService);

      assertNotNull(bot);
      assertEquals(moveService, bot.moveService());
    }

    @Test
    void shouldAcceptNullMoveService() {

      BotAI bot = new BotAI(null);

      assertNotNull(bot);
      assertNull(bot.moveService());
    }
  }

  @Nested
  class MoveServiceAccessorTest {

    @Test
    void shouldReturnProvidedMoveService() {

      MoveService retrievedService = botAI.moveService();

      assertSame(mockMoveService, retrievedService);
    }
  }

  @Nested
  class MakeMoveTest {

    @BeforeEach
    void setupBotState() {
      when(mockBotState.board()).thenReturn(mockBoardState);
      when(mockBotState.playerConfiguration()).thenReturn(
          mockPlayerConfiguration);
    }

    @Test
    void shouldCallGetPossibleMovesWithBoard() {

      ArrayList<BotDecision> possibleMoves = new ArrayList<>();
      possibleMoves.add(mockBotDecision1);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                possibleMoves, mockBoardState, mockPlayerConfiguration))
            .thenReturn(mockBestDecision);

        botAI.makeMove(mockBotState);

        verify(mockMoveService).getPossibleMoves(mockBoardState);
      }
    }

    @Test
    void shouldCallChooseBestMoveWithCorrectParameters() {

      ArrayList<BotDecision> possibleMoves = new ArrayList<>();
      possibleMoves.add(mockBotDecision1);
      possibleMoves.add(mockBotDecision2);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                possibleMoves, mockBoardState, mockPlayerConfiguration))
            .thenReturn(mockBestDecision);

        botAI.makeMove(mockBotState);

        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            possibleMoves, mockBoardState, mockPlayerConfiguration));
      }
    }

    @Test
    void shouldReturnBestMoveSelectedByBestMoveSelector() {

      ArrayList<BotDecision> possibleMoves = new ArrayList<>();
      possibleMoves.add(mockBotDecision1);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                possibleMoves, mockBoardState, mockPlayerConfiguration))
            .thenReturn(mockBestDecision);

        BotDecision result = botAI.makeMove(mockBotState);

        assertSame(mockBestDecision, result);
      }
    }

    @Test
    void shouldHandleEmptyPossibleMovesList() {

      ArrayList<BotDecision> emptyMoves = new ArrayList<>();
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          emptyMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                emptyMoves, mockBoardState, mockPlayerConfiguration))
            .thenReturn(null);

        BotDecision result = botAI.makeMove(mockBotState);

        assertNull(result);
        verify(mockMoveService).getPossibleMoves(mockBoardState);
        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            emptyMoves, mockBoardState, mockPlayerConfiguration));
      }
    }

    @Test
    void shouldHandleSinglePossibleMove() {

      ArrayList<BotDecision> singleMove = new ArrayList<>();
      singleMove.add(mockBotDecision1);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          singleMove);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                singleMove, mockBoardState, mockPlayerConfiguration))
            .thenReturn(mockBotDecision1);

        BotDecision result = botAI.makeMove(mockBotState);

        assertSame(mockBotDecision1, result);
      }
    }

    @Test
    void shouldHandleMultiplePossibleMoves() {

      ArrayList<BotDecision> multipleMoves = new ArrayList<>();
      multipleMoves.add(mockBotDecision1);
      multipleMoves.add(mockBotDecision2);
      multipleMoves.add(mockBotDecision3);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          multipleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                multipleMoves, mockBoardState, mockPlayerConfiguration))
            .thenReturn(mockBotDecision2);

        BotDecision result = botAI.makeMove(mockBotState);

        assertSame(mockBotDecision2, result);
      }
    }

    @Test
    void shouldPassExactSameListToBestMoveSelector() {

      ArrayList<BotDecision> possibleMoves = new ArrayList<>();
      possibleMoves.add(mockBotDecision1);
      possibleMoves.add(mockBotDecision2);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                (ArrayList<BotDecision>) ArgumentMatchers.<BotDecision>anyList(),
                eq(mockBoardState),
                eq(mockPlayerConfiguration)))
            .thenReturn(mockBestDecision);

        botAI.makeMove(mockBotState);

        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            same(possibleMoves), eq(mockBoardState),
            eq(mockPlayerConfiguration)));
      }
    }

    @Test
    void shouldThrowExceptionWhenMoveServiceThrowsException() {

      RuntimeException expectedException =
          new RuntimeException("MoveService error");
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenThrow(
          expectedException);

      RuntimeException thrownException = assertThrows(RuntimeException.class,
          () -> botAI.makeMove(mockBotState));

      assertSame(expectedException, thrownException);
    }

    @Test
    void shouldThrowExceptionWhenBestMoveSelectorThrowsException() {

      ArrayList<BotDecision> possibleMoves = new ArrayList<>();
      possibleMoves.add(mockBotDecision1);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      RuntimeException expectedException =
          new RuntimeException("BestMoveSelector error");

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                possibleMoves, mockBoardState, mockPlayerConfiguration))
            .thenThrow(expectedException);

        RuntimeException thrownException = assertThrows(RuntimeException.class,
            () -> botAI.makeMove(mockBotState));

        assertSame(expectedException, thrownException);
      }
    }

    @Test
    void shouldCallBoardAndPlayerConfigurationOnBotState() {

      ArrayList<BotDecision> possibleMoves = new ArrayList<>();
      possibleMoves.add(mockBotDecision1);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                possibleMoves, mockBoardState, mockPlayerConfiguration))
            .thenReturn(mockBestDecision);

        botAI.makeMove(mockBotState);

        verify(mockBotState, times(2)).board();
        verify(mockBotState).playerConfiguration();
      }
    }
  }

  @Nested
  class IntegrationTest {

    @Test
    void fullDecisionMakingFlow() {
      ArrayList<BotDecision> possibleMoves = new ArrayList<>();
      possibleMoves.add(mockBotDecision1);
      possibleMoves.add(mockBotDecision2);
      possibleMoves.add(mockBotDecision3);

      when(mockBotState.board()).thenReturn(mockBoardState);
      when(mockBotState.playerConfiguration()).thenReturn(
          mockPlayerConfiguration);

      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                possibleMoves, mockBoardState, mockPlayerConfiguration))
            .thenReturn(mockBotDecision2);

        BotDecision result = botAI.makeMove(mockBotState);

        verify(mockBotState, times(2)).board();
        verify(mockMoveService).getPossibleMoves(mockBoardState);
        verify(mockBotState).playerConfiguration();
        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            possibleMoves, mockBoardState, mockPlayerConfiguration));

        assertSame(mockBotDecision2, result);
      }
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test
    @SuppressWarnings("DataFlowIssue")
    void shouldHandleNullBotState() {

      assertThrows(NullPointerException.class, () -> botAI.makeMove(null));
    }

    @Test
    void shouldHandleBotStateWithNullBoard() {

      when(mockBotState.board()).thenReturn(null);
      when(mockBotState.playerConfiguration()).thenReturn(
          mockPlayerConfiguration);

      assertThrows(RuntimeException.class, () -> botAI.makeMove(mockBotState));
    }

    @Test
    void shouldHandleBotStateWithNullPlayerConfiguration() {

      when(mockBotState.board()).thenReturn(mockBoardState);
      when(mockBotState.playerConfiguration()).thenReturn(null);

      ArrayList<BotDecision> possibleMoves = new ArrayList<>();
      possibleMoves.add(mockBotDecision1);
      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                possibleMoves, mockBoardState, null))
            .thenReturn(mockBestDecision);

        BotDecision result = botAI.makeMove(mockBotState);

        assertSame(mockBestDecision, result);
        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            possibleMoves, mockBoardState, null));
      }
    }

    @Test
    void shouldHandleNullMoveService() {

      BotAI botWithNullService = new BotAI(null);

      assertThrows(NullPointerException.class,
          () -> botWithNullService.makeMove(mockBotState));
    }
  }

  @Nested
  class RecordEqualityTest {

    @Test
    void shouldBeEqualToAnotherBotAIWithSameMoveService() {

      BotAI botAI1 = new BotAI(mockMoveService);
      BotAI botAI2 = new BotAI(mockMoveService);

      assertEquals(botAI1, botAI2);
      assertEquals(botAI1.hashCode(), botAI2.hashCode());
    }

    @Test
    void shouldNotBeEqualToBotAIWithDifferentMoveService() {

      MoveService otherMoveService = mock(MoveService.class);
      BotAI botAI1 = new BotAI(mockMoveService);
      BotAI botAI2 = new BotAI(otherMoveService);

      assertNotEquals(botAI1, botAI2);
    }

    @Test
    void shouldHaveCorrectToStringRepresentation() {

      String toString = botAI.toString();

      assertNotNull(toString);
      assertTrue(toString.contains("BotAI"));
      assertTrue(toString.contains("moveService"));
    }
  }
}