package checkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotDecisionServiceTest {

  @Mock
  private BotAi mockBot;

  @Mock
  private BotState mockBotState;

  @Mock
  private BotDecision mockBotDecision;

  private BotDecisionService botDecisionService;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    botDecisionService = new BotDecisionService(mockBot, mockBotState);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  class ConstructorTest {

    @Test
    void shouldCreateInstanceWithGivenDependencies() {

      BotAi bot = mock(BotAi.class);
      BotState state = mock(BotState.class);

      BotDecisionService service = new BotDecisionService(bot, state);

      assertNotNull(service);
      assertEquals(bot, service.bot());
      assertEquals(state, service.botState());
    }

    @Test
    void shouldAcceptNullBot() {

      BotState state = mock(BotState.class);

      BotDecisionService service = new BotDecisionService(null, state);

      assertNotNull(service);
      assertNull(service.bot());
      assertEquals(state, service.botState());
    }

    @Test
    void shouldAcceptNullBotState() {

      BotAi bot = mock(BotAi.class);

      BotDecisionService service = new BotDecisionService(bot, null);

      assertNotNull(service);
      assertEquals(bot, service.bot());
      assertNull(service.botState());
    }

    @Test
    void shouldAcceptBothParametersAsNull() {

      BotDecisionService service = new BotDecisionService(null, null);

      assertNotNull(service);
      assertNull(service.bot());
      assertNull(service.botState());
    }
  }

  @Nested
  class RecordAccessorTest {

    @Test
    void botShouldReturnProvidedBotAI() {

      assertSame(mockBot, botDecisionService.bot());
    }

    @Test
    void botStateShouldReturnProvidedBotState() {

      assertSame(mockBotState, botDecisionService.botState());
    }

  }

  @Nested
  class GetBotDecisionTest {

    @Test
    void shouldCallMakeMoveOnBotAIWithBotState() {

      when(mockBot.makeMove(mockBotState)).thenReturn(mockBotDecision);

      botDecisionService.getBotDecision();

      verify(mockBot).makeMove(mockBotState);
    }

    @Test
    void shouldReturnResultFromBotAIMakeMove() {

      when(mockBot.makeMove(mockBotState)).thenReturn(mockBotDecision);

      BotDecision result = botDecisionService.getBotDecision();

      assertSame(mockBotDecision, result);
    }

    @Test
    void shouldReturnNullWhenBotAIReturnsNull() {

      when(mockBot.makeMove(mockBotState)).thenReturn(null);

      BotDecision result = botDecisionService.getBotDecision();

      assertNull(result);
    }

    @Test
    void shouldCallMakeMoveExactlyOnce() {

      when(mockBot.makeMove(mockBotState)).thenReturn(mockBotDecision);

      botDecisionService.getBotDecision();

      verify(mockBot, times(1)).makeMove(mockBotState);
    }

    @Test
    void shouldPassExactSameBotStateToMakeMove() {

      when(mockBot.makeMove(any(BotState.class))).thenReturn(mockBotDecision);

      botDecisionService.getBotDecision();

      verify(mockBot).makeMove(same(mockBotState));
    }

    @Test
    void shouldBeCallableMultipleTimes() {

      BotDecision decision1 = mock(BotDecision.class);
      BotDecision decision2 = mock(BotDecision.class);
      BotDecision decision3 = mock(BotDecision.class);

      when(mockBot.makeMove(mockBotState))
          .thenReturn(decision1)
          .thenReturn(decision2)
          .thenReturn(decision3);

      BotDecision result1 = botDecisionService.getBotDecision();
      BotDecision result2 = botDecisionService.getBotDecision();
      BotDecision result3 = botDecisionService.getBotDecision();

      assertSame(decision1, result1);
      assertSame(decision2, result2);
      assertSame(decision3, result3);
      verify(mockBot, times(3)).makeMove(mockBotState);
    }

    @Test
    void shouldPropagateExceptionFromBotAI() {

      RuntimeException expectedException = new RuntimeException("BotAI error");
      when(mockBot.makeMove(mockBotState)).thenThrow(expectedException);

      RuntimeException thrownException = assertThrows(RuntimeException.class,
          () -> botDecisionService.getBotDecision());

      assertSame(expectedException, thrownException);
      verify(mockBot).makeMove(mockBotState);
    }

    @Test
    void shouldHandleDifferentExceptionTypesFromBotAI() {

      IllegalArgumentException expectedException =
          new IllegalArgumentException("Invalid state");
      when(mockBot.makeMove(mockBotState)).thenThrow(expectedException);

      IllegalArgumentException thrownException =
          assertThrows(IllegalArgumentException.class,
              () -> botDecisionService.getBotDecision());

      assertSame(expectedException, thrownException);
    }

    @Test
    void shouldHandleCheckedExceptionsWrappedInRuntimeException() {

      RuntimeException wrapperException =
          new RuntimeException("Wrapped exception",
              new InterruptedException("Thread interrupted"));
      when(mockBot.makeMove(mockBotState)).thenThrow(wrapperException);

      RuntimeException thrownException = assertThrows(RuntimeException.class,
          () -> botDecisionService.getBotDecision());

      assertSame(wrapperException, thrownException);
      assertInstanceOf(InterruptedException.class, thrownException.getCause());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenBotIsNull() {

      BotDecisionService serviceWithNullBot =
          new BotDecisionService(null, mockBotState);

      assertThrows(NullPointerException.class,
          serviceWithNullBot::getBotDecision);
    }

  }

  @Nested
  class IntegrationTest {

    @Test
    void fullFlowServiceToBotToDecision() {

      BotDecision expectedDecision = new BotDecision(2, 1, 3, 2, 1);
      when(mockBot.makeMove(mockBotState)).thenReturn(expectedDecision);

      BotDecision actualDecision = botDecisionService.getBotDecision();

      assertSame(expectedDecision, actualDecision);
      verify(mockBot).makeMove(mockBotState);

      assertEquals(expectedDecision.fromRow(), actualDecision.fromRow());
      assertEquals(expectedDecision.fromCol(), actualDecision.fromCol());
      assertEquals(expectedDecision.toRow(), actualDecision.toRow());
      assertEquals(expectedDecision.toCol(), actualDecision.toCol());
      assertEquals(expectedDecision.moveType(), actualDecision.moveType());
    }

    @Test
    void serviceShouldBehaveAsTransparentProxy() {

      BotDecision decision1 = new BotDecision(1, 1, 2, 2, 1);
      BotDecision decision2 = new BotDecision(3, 3, 4, 4, 2);

      when(mockBot.makeMove(mockBotState))
          .thenReturn(decision1)
          .thenReturn(decision2);

      BotDecision result1 = botDecisionService.getBotDecision();
      BotDecision result2 = botDecisionService.getBotDecision();

      assertSame(decision1, result1);
      assertSame(decision2, result2);
      assertNotEquals(result1, result2);
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test
    void shouldHandleMultipleCallsWithSameResults() {

      BotDecision consistentDecision = new BotDecision(5, 5, 6, 6, 1);
      when(mockBot.makeMove(mockBotState)).thenReturn(consistentDecision);

      BotDecision result1 = botDecisionService.getBotDecision();
      BotDecision result2 = botDecisionService.getBotDecision();
      BotDecision result3 = botDecisionService.getBotDecision();

      assertSame(consistentDecision, result1);
      assertSame(consistentDecision, result2);
      assertSame(consistentDecision, result3);
      assertEquals(result1, result2);
      assertEquals(result2, result3);
      verify(mockBot, times(3)).makeMove(mockBotState);
    }

    @Test
    void shouldHandleWhenBotAIReturnsDifferentDecisionsForSameState() {

      BotDecision decision1 = new BotDecision(1, 1, 2, 2, 1);
      BotDecision decision2 = new BotDecision(1, 1, 2, 0, 1);

      when(mockBot.makeMove(mockBotState))
          .thenReturn(decision1)
          .thenReturn(decision2);

      BotDecision result1 = botDecisionService.getBotDecision();
      BotDecision result2 = botDecisionService.getBotDecision();

      assertSame(decision1, result1);
      assertSame(decision2, result2);
      assertNotEquals(result1, result2);
    }

    @Test
    void shouldHandleVeryComplexBotDecision() {

      BotDecision complexDecision = new BotDecision(Integer.MAX_VALUE,
          Integer.MIN_VALUE, 0, -1, 999);
      when(mockBot.makeMove(mockBotState)).thenReturn(complexDecision);

      BotDecision result = botDecisionService.getBotDecision();

      assertSame(complexDecision, result);
      assertEquals(Integer.MAX_VALUE, result.fromRow());
      assertEquals(Integer.MIN_VALUE, result.fromCol());
      assertEquals(0, result.toRow());
      assertEquals(-1, result.toCol());
      assertEquals(999, result.moveType());
    }
  }

  @Nested
  class RecordEqualityTest {

    @Test
    void shouldBeEqualToAnotherServiceWithSameDependencies() {

      BotDecisionService service1 =
          new BotDecisionService(mockBot, mockBotState);
      BotDecisionService service2 =
          new BotDecisionService(mockBot, mockBotState);

      assertEquals(service1, service2);
      assertEquals(service1.hashCode(), service2.hashCode());
    }

    @Test
    void shouldNotBeEqualToServiceWithDifferentBot() {

      BotAi otherBot = mock(BotAi.class);
      BotDecisionService service1 =
          new BotDecisionService(mockBot, mockBotState);
      BotDecisionService service2 =
          new BotDecisionService(otherBot, mockBotState);

      assertNotEquals(service1, service2);
    }

    @Test
    void shouldNotBeEqualToServiceWithDifferentBotState() {

      BotState otherBotState = mock(BotState.class);
      BotDecisionService service1 =
          new BotDecisionService(mockBot, mockBotState);
      BotDecisionService service2 =
          new BotDecisionService(mockBot, otherBotState);

      assertNotEquals(service1, service2);
    }

    @Test
    void shouldNotBeEqualToNull() {

      assertNotEquals(null, botDecisionService);
    }


    @Test
    void shouldHaveCorrectToStringRepresentation() {

      String toString = botDecisionService.toString();

      assertNotNull(toString);
      assertTrue(toString.contains("BotDecisionService"));
      assertTrue(toString.contains("bot"));
      assertTrue(toString.contains("botState"));
    }

    @Test
    void shouldHaveConsistentHashCode() {

      int hash1 = botDecisionService.hashCode();
      int hash2 = botDecisionService.hashCode();

      assertEquals(hash1, hash2);
    }

    @Test
    void equalObjectsShouldHaveSameHashCode() {

      BotDecisionService service1 =
          new BotDecisionService(mockBot, mockBotState);
      BotDecisionService service2 =
          new BotDecisionService(mockBot, mockBotState);

      assertEquals(service1, service2);
      assertEquals(service1.hashCode(), service2.hashCode());
    }
  }

  @Nested
  class ImmutabilityTest {

    @Test
    void recordShouldBeImmutable() {

      BotAi originalBot = botDecisionService.bot();
      BotState originalBotState = botDecisionService.botState();

      for (int i = 0; i < 10; i++) {
        assertSame(originalBot, botDecisionService.bot());
        assertSame(originalBotState, botDecisionService.botState());
      }

      assertSame(originalBot, botDecisionService.bot());
      assertSame(originalBotState, botDecisionService.botState());
    }

    @Test
    void differentInstancesShouldMaintainTheirValuesIndependently() {

      BotAi otherBot = mock(BotAi.class);
      BotState otherBotState = mock(BotState.class);
      BotDecisionService otherService =
          new BotDecisionService(otherBot, otherBotState);

      assertSame(mockBot, botDecisionService.bot());
      assertSame(otherBot, otherService.bot());
      assertNotSame(botDecisionService.bot(), otherService.bot());

      assertSame(mockBotState, botDecisionService.botState());
      assertSame(otherBotState, otherService.botState());
      assertNotSame(botDecisionService.botState(), otherService.botState());
    }
  }

  @Nested
  class PerformanceTest {

    @Test
    void getBotDecisionShouldBeEfficientForMultipleCalls() {

      when(mockBot.makeMove(mockBotState)).thenReturn(mockBotDecision);
      int numberOfCalls = 1000;

      long startTime = System.nanoTime();
      for (int i = 0; i < numberOfCalls; i++) {
        botDecisionService.getBotDecision();
      }
      long endTime = System.nanoTime();

      long duration = endTime - startTime;

      assertTrue(duration > 0);
      verify(mockBot, times(numberOfCalls)).makeMove(mockBotState);
    }
  }
}