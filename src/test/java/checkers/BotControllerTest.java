package checkers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BotControllerTest {

  private static final long BOT_MOVE_DELAY_MS = GameConstants.BOT_MOVE_DELAY_MS;
  @Mock
  private BotDecisionService mockDecisionService;
  @Mock
  private BotMoveExecutor mockMoveExecutor;
  @Mock
  private BotUiHandler mockUIHandler;
  @Mock
  private BotDecision mockBotDecision;
  private BotController botController;
  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    botController =
        new BotController(mockDecisionService, mockMoveExecutor, mockUIHandler);

    when(mockDecisionService.getBotDecision()).thenReturn(mockBotDecision);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  class ConstructorTest {

    @Test
    void shouldCreateInstanceWithGivenDependencies() {

      BotDecisionService decisionService = mock(BotDecisionService.class);
      BotMoveExecutor moveExecutor = mock(BotMoveExecutor.class);
      BotUiHandler uiHandler = mock(BotUiHandler.class);

      BotController controller =
          new BotController(decisionService, moveExecutor, uiHandler);

      assertNotNull(controller);
      assertEquals(decisionService, controller.decisionService());
      assertEquals(moveExecutor, controller.moveExecutor());
      assertEquals(uiHandler, controller.uiHandler());
    }

    @Test
    void shouldAcceptNullDependencies() {

      BotController controller = new BotController(null, null, null);

      assertNotNull(controller);
      assertNull(controller.decisionService());
      assertNull(controller.moveExecutor());
      assertNull(controller.uiHandler());
    }
  }

  @Nested
  class RecordAccessorTest {

    @Test
    void shouldReturnProvidedDependencies() {

      assertSame(mockDecisionService, botController.decisionService());
      assertSame(mockMoveExecutor, botController.moveExecutor());
      assertSame(mockUIHandler, botController.uiHandler());
    }
  }

  @Nested
  class ExecuteTurnTest {

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldExecuteTurnInSeparateThread() throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(1);
      AtomicReference<String> threadName = new AtomicReference<>();

      doAnswer(_ -> {
        threadName.set(Thread.currentThread().getName());
        latch.countDown();
        return null;
      }).when(mockUIHandler).updateUiAndSwitchTurn();

      String mainThreadName = Thread.currentThread().getName();

      botController.executeTurn();

      assertTrue(latch.await(3, TimeUnit.SECONDS));
      assertNotEquals(mainThreadName, threadName.get());
    }

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldCallMethodsInCorrectOrder() throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(1);
      InOrder inOrder =
          inOrder(mockDecisionService, mockMoveExecutor, mockUIHandler);

      doAnswer(_ -> {
        latch.countDown();
        return null;
      }).when(mockUIHandler).updateUiAndSwitchTurn();

      botController.executeTurn();

      assertTrue(latch.await(3, TimeUnit.SECONDS));
      inOrder.verify(mockDecisionService).getBotDecision();
      inOrder.verify(mockMoveExecutor).executeMove(mockBotDecision);
      inOrder.verify(mockUIHandler).updateUiAndSwitchTurn();
    }

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldPassDecisionFromServiceToExecutor() throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(1);
      BotDecision specificDecision = mock(BotDecision.class);
      when(mockDecisionService.getBotDecision()).thenReturn(specificDecision);

      doAnswer(_ -> {
        latch.countDown();
        return null;
      }).when(mockUIHandler).updateUiAndSwitchTurn();

      botController.executeTurn();

      assertTrue(latch.await(3, TimeUnit.SECONDS));
      verify(mockMoveExecutor).executeMove(specificDecision);
    }

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldWaitBotMoveDelayBeforeExecution() throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(1);
      long startTime = System.currentTimeMillis();
      AtomicReference<Long> executionTime = new AtomicReference<>();

      doAnswer(_ -> {
        executionTime.set(System.currentTimeMillis());
        latch.countDown();
        return null;
      }).when(mockDecisionService).getBotDecision();

      botController.executeTurn();

      assertTrue(latch.await(3, TimeUnit.SECONDS));
      long actualDelay = executionTime.get() - startTime;
      assertTrue(actualDelay >= BOT_MOVE_DELAY_MS - 50);
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldHandleInterruptedExceptionDuringSleep()
        throws InterruptedException {

      CountDownLatch turnCompletedLatch = new CountDownLatch(1);
      CountDownLatch threadStartedLatch = new CountDownLatch(1);

      ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
      PrintStream originalErr = System.err;
      System.setErr(new PrintStream(errorStream));

      AtomicReference<Thread> workerThread = new AtomicReference<>();

      try {
        doAnswer(_ -> {

          workerThread.set(Thread.currentThread());
          threadStartedLatch.countDown();

          try {
            Thread.sleep(GameConstants.BOT_MOVE_DELAY_MS);
          } catch (InterruptedException e) {
            System.err.println("Thread was interrupted: " + e.getMessage());
          }

          turnCompletedLatch.countDown();
          return null;
        }).when(mockDecisionService).getBotDecision();

        botController.executeTurn();

        assertTrue(threadStartedLatch.await(1, TimeUnit.SECONDS),
            "Worker thread did not start in time");

        workerThread.get().interrupt();

        assertTrue(turnCompletedLatch.await(3, TimeUnit.SECONDS),
            "Turn did not complete in time");

        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Thread was interrupted:"),
            "Expected interruption message not found");

      } finally {

        System.setErr(originalErr);
      }
    }


    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldContinueExecutionDespiteInterruptedException()
        throws InterruptedException {

      CountDownLatch interruptLatch = new CountDownLatch(1);
      CountDownLatch completionLatch = new CountDownLatch(1);
      AtomicReference<Thread> workerThread = new AtomicReference<>();

      doAnswer(_ -> {
        workerThread.set(Thread.currentThread());
        interruptLatch.countDown();
        return mockBotDecision;
      }).when(mockDecisionService).getBotDecision();

      doAnswer(_ -> {
        completionLatch.countDown();
        return null;
      }).when(mockUIHandler).updateUiAndSwitchTurn();

      botController.executeTurn();

      assertTrue(interruptLatch.await(2, TimeUnit.SECONDS));
      if (workerThread.get() != null) {
        workerThread.get().interrupt();
      }

      assertTrue(completionLatch.await(3, TimeUnit.SECONDS));
      verify(mockDecisionService).getBotDecision();
      verify(mockMoveExecutor).executeMove(mockBotDecision);
      verify(mockUIHandler).updateUiAndSwitchTurn();
    }

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldHandleExceptionFromDecisionService()
        throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(1);
      RuntimeException expectedException =
          new RuntimeException("Decision service error");
      AtomicReference<Throwable> caughtException = new AtomicReference<>();

      when(mockDecisionService.getBotDecision()).thenThrow(expectedException);

      Thread.UncaughtExceptionHandler originalHandler =
          Thread.getDefaultUncaughtExceptionHandler();
      Thread.setDefaultUncaughtExceptionHandler((_, e) -> {
        caughtException.set(e);
        latch.countDown();
      });

      try {

        botController.executeTurn();

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertSame(expectedException, caughtException.get());
        verify(mockDecisionService).getBotDecision();
        verify(mockMoveExecutor, never()).executeMove(any());
        verify(mockUIHandler, never()).updateUiAndSwitchTurn();

      } finally {
        Thread.setDefaultUncaughtExceptionHandler(originalHandler);
      }
    }

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldHandleExceptionFromMoveExecutor() throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(1);
      RuntimeException expectedException =
          new RuntimeException("Move executor error");
      AtomicReference<Throwable> caughtException = new AtomicReference<>();

      doThrow(expectedException).when(mockMoveExecutor)
          .executeMove(mockBotDecision);

      Thread.UncaughtExceptionHandler originalHandler =
          Thread.getDefaultUncaughtExceptionHandler();
      Thread.setDefaultUncaughtExceptionHandler((_, e) -> {
        caughtException.set(e);
        latch.countDown();
      });

      try {

        botController.executeTurn();

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertSame(expectedException, caughtException.get());
        verify(mockDecisionService).getBotDecision();
        verify(mockMoveExecutor).executeMove(mockBotDecision);
        verify(mockUIHandler, never()).updateUiAndSwitchTurn();

      } finally {
        Thread.setDefaultUncaughtExceptionHandler(originalHandler);
      }
    }

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldHandleExceptionFromUIHandler() throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(1);
      RuntimeException expectedException =
          new RuntimeException("UI handler error");
      AtomicReference<Throwable> caughtException = new AtomicReference<>();

      doThrow(expectedException).when(mockUIHandler).updateUiAndSwitchTurn();

      Thread.UncaughtExceptionHandler originalHandler =
          Thread.getDefaultUncaughtExceptionHandler();
      Thread.setDefaultUncaughtExceptionHandler((_, e) -> {
        caughtException.set(e);
        latch.countDown();
      });

      try {

        botController.executeTurn();

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertSame(expectedException, caughtException.get());
        verify(mockDecisionService).getBotDecision();
        verify(mockMoveExecutor).executeMove(mockBotDecision);
        verify(mockUIHandler).updateUiAndSwitchTurn();

      } finally {
        Thread.setDefaultUncaughtExceptionHandler(originalHandler);
      }
    }

    @Test

    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void shouldBeCallableMultipleTimes() throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(3);

      doAnswer(_ -> {
        latch.countDown();
        return null;
      }).when(mockUIHandler).updateUiAndSwitchTurn();

      botController.executeTurn();
      botController.executeTurn();
      botController.executeTurn();

      assertTrue(latch.await(5, TimeUnit.SECONDS));
      verify(mockDecisionService, times(3)).getBotDecision();
      verify(mockMoveExecutor, times(3)).executeMove(mockBotDecision);
      verify(mockUIHandler, times(3)).updateUiAndSwitchTurn();
    }

    @Test

    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void differentCallsShouldRunInDifferentThreads()
        throws InterruptedException {

      CountDownLatch latch = new CountDownLatch(2);
      AtomicReference<String> thread1Name = new AtomicReference<>();
      AtomicReference<String> thread2Name = new AtomicReference<>();
      AtomicBoolean firstCall = new AtomicBoolean(true);

      doAnswer(_ -> {
        if (firstCall.getAndSet(false)) {
          thread1Name.set(Thread.currentThread().getName());
        } else {
          thread2Name.set(Thread.currentThread().getName());
        }
        latch.countDown();
        return null;
      }).when(mockUIHandler).updateUiAndSwitchTurn();

      botController.executeTurn();
      botController.executeTurn();

      assertTrue(latch.await(5, TimeUnit.SECONDS));
      assertNotNull(thread1Name.get());
      assertNotNull(thread2Name.get());
      assertNotEquals(thread1Name.get(), thread2Name.get());
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldHandleNullDecisionService() throws InterruptedException {

      BotController controllerWithNullService =
          new BotController(null, mockMoveExecutor, mockUIHandler);
      CountDownLatch latch = new CountDownLatch(1);
      AtomicReference<Throwable> caughtException = new AtomicReference<>();

      Thread.UncaughtExceptionHandler originalHandler =
          Thread.getDefaultUncaughtExceptionHandler();
      Thread.setDefaultUncaughtExceptionHandler((_, e) -> {
        caughtException.set(e);
        latch.countDown();
      });

      try {

        controllerWithNullService.executeTurn();

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertInstanceOf(NullPointerException.class, caughtException.get());

      } finally {
        Thread.setDefaultUncaughtExceptionHandler(originalHandler);
      }
    }

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldHandleNullMoveExecutor() throws InterruptedException {

      BotController controllerWithNullExecutor =
          new BotController(mockDecisionService, null, mockUIHandler);
      CountDownLatch latch = new CountDownLatch(1);
      AtomicReference<Throwable> caughtException = new AtomicReference<>();

      Thread.UncaughtExceptionHandler originalHandler =
          Thread.getDefaultUncaughtExceptionHandler();
      Thread.setDefaultUncaughtExceptionHandler((_, e) -> {
        caughtException.set(e);
        latch.countDown();
      });

      try {

        controllerWithNullExecutor.executeTurn();

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertInstanceOf(NullPointerException.class, caughtException.get());

      } finally {
        Thread.setDefaultUncaughtExceptionHandler(originalHandler);
      }
    }

    @Test

    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void shouldHandleNullUIHandler() throws InterruptedException {

      BotController controllerWithNullHandler =
          new BotController(mockDecisionService, mockMoveExecutor, null);
      CountDownLatch latch = new CountDownLatch(1);
      AtomicReference<Throwable> caughtException = new AtomicReference<>();

      Thread.UncaughtExceptionHandler originalHandler =
          Thread.getDefaultUncaughtExceptionHandler();
      Thread.setDefaultUncaughtExceptionHandler((_, e) -> {
        caughtException.set(e);
        latch.countDown();
      });

      try {

        controllerWithNullHandler.executeTurn();

        assertTrue(latch.await(3, TimeUnit.SECONDS));
        assertInstanceOf(NullPointerException.class, caughtException.get());

      } finally {
        Thread.setDefaultUncaughtExceptionHandler(originalHandler);
      }
    }
  }

  @Nested
  class RecordEqualityTest {

    @Test
    void shouldBeEqualToAnotherControllerWithSameDependencies() {

      BotController controller1 =
          new BotController(mockDecisionService, mockMoveExecutor,
              mockUIHandler);
      BotController controller2 =
          new BotController(mockDecisionService, mockMoveExecutor,
              mockUIHandler);

      assertEquals(controller1, controller2);
      assertEquals(controller1.hashCode(), controller2.hashCode());
    }

    @Test
    void shouldNotBeEqualToControllerWithDifferentDependencies() {

      BotDecisionService otherDecisionService = mock(BotDecisionService.class);
      BotController controller1 =
          new BotController(mockDecisionService, mockMoveExecutor,
              mockUIHandler);
      BotController controller2 =
          new BotController(otherDecisionService, mockMoveExecutor,
              mockUIHandler);

      assertNotEquals(controller1, controller2);
    }

    @Test
    void shouldHaveCorrectToStringRepresentation() {

      String toString = botController.toString();

      assertNotNull(toString);
      assertTrue(toString.contains("BotController"));
      assertTrue(toString.contains("decisionService"));
      assertTrue(toString.contains("moveExecutor"));
      assertTrue(toString.contains("uiHandler"));
    }
  }
}