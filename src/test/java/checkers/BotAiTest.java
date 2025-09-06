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

class BotAiTest {

  @Mock
  private MoveService mockMoveService;

  @Mock
  private BotState mockBotState;

  @Mock
  private BoardState mockBoardState;

  @Mock
  private PlayerConfig mockPlayerConfig;

  @Mock
  private BotDecision mockBotDecision1;

  @Mock
  private BotDecision mockBotDecision2;

  @Mock
  private BotDecision mockBotDecision3;

  @Mock
  private BotDecision mockBestDecision;

  private BotAi botAI;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    botAI = new BotAi(mockMoveService);
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

      BotAi bot = new BotAi(moveService);

      assertNotNull(bot);
      assertEquals(moveService, bot.moveService());
    }

    @Test
    void shouldAcceptNullMoveService() {

      BotAi bot = new BotAi(null);

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
      when(mockBotState.playerConfig()).thenReturn(
          mockPlayerConfig);
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
                possibleMoves, mockBoardState, mockPlayerConfig))
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
                possibleMoves, mockBoardState, mockPlayerConfig))
            .thenReturn(mockBestDecision);

        botAI.makeMove(mockBotState);

        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            possibleMoves, mockBoardState, mockPlayerConfig));
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
                possibleMoves, mockBoardState, mockPlayerConfig))
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
                emptyMoves, mockBoardState, mockPlayerConfig))
            .thenReturn(null);

        BotDecision result = botAI.makeMove(mockBotState);

        assertNull(result);
        verify(mockMoveService).getPossibleMoves(mockBoardState);
        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            emptyMoves, mockBoardState, mockPlayerConfig));
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
                singleMove, mockBoardState, mockPlayerConfig))
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
                multipleMoves, mockBoardState, mockPlayerConfig))
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
                eq(mockPlayerConfig)))
            .thenReturn(mockBestDecision);

        botAI.makeMove(mockBotState);

        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            same(possibleMoves), eq(mockBoardState),
            eq(mockPlayerConfig)));
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
                possibleMoves, mockBoardState, mockPlayerConfig))
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
                possibleMoves, mockBoardState, mockPlayerConfig))
            .thenReturn(mockBestDecision);

        botAI.makeMove(mockBotState);

        verify(mockBotState, times(2)).board();
        verify(mockBotState).playerConfig();
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
      when(mockBotState.playerConfig()).thenReturn(
          mockPlayerConfig);

      when(mockMoveService.getPossibleMoves(mockBoardState)).thenReturn(
          possibleMoves);

      try (MockedStatic<BestMoveSelector> mockedSelector = mockStatic(
          BestMoveSelector.class)) {
        mockedSelector.when(() -> BestMoveSelector.chooseBestMove(
                possibleMoves, mockBoardState, mockPlayerConfig))
            .thenReturn(mockBotDecision2);

        BotDecision result = botAI.makeMove(mockBotState);

        verify(mockBotState, times(2)).board();
        verify(mockMoveService).getPossibleMoves(mockBoardState);
        verify(mockBotState).playerConfig();
        mockedSelector.verify(() -> BestMoveSelector.chooseBestMove(
            possibleMoves, mockBoardState, mockPlayerConfig));

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
      when(mockBotState.playerConfig()).thenReturn(
          mockPlayerConfig);

      assertThrows(RuntimeException.class, () -> botAI.makeMove(mockBotState));
    }

    @Test
    void shouldHandleBotStateWithNullPlayerConfiguration() {

      when(mockBotState.board()).thenReturn(mockBoardState);
      when(mockBotState.playerConfig()).thenReturn(null);

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

      BotAi botWithNullService = new BotAi(null);

      assertThrows(NullPointerException.class,
          () -> botWithNullService.makeMove(mockBotState));
    }
  }

  @Nested
  class RecordEqualityTest {

    @Test
    void shouldBeEqualToAnotherBotAIWithSameMoveService() {

      BotAi botAi1 = new BotAi(mockMoveService);
      BotAi botAi2 = new BotAi(mockMoveService);

      assertEquals(botAi1, botAi2);
      assertEquals(botAi1.hashCode(), botAi2.hashCode());
    }

    @Test
    void shouldNotBeEqualToBotAIWithDifferentMoveService() {

      MoveService otherMoveService = mock(MoveService.class);
      BotAi botAi1 = new BotAi(mockMoveService);
      BotAi botAi2 = new BotAi(otherMoveService);

      assertNotEquals(botAi1, botAi2);
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