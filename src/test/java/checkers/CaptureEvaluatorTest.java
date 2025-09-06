package checkers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class CaptureEvaluatorTest {

  final BoardState boardState = mock(BoardState.class);
  final PlayerConfig config = mock(PlayerConfig.class);

  @Nested
  class ConstructorTest {

    @Test
    void shouldHavePrivateConstructor() throws Exception {
      Constructor<CaptureEvaluator> constructor =
          CaptureEvaluator.class.getDeclaredConstructor();
      assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    void shouldBeFinalClass() {
      assertTrue(Modifier.isFinal(CaptureEvaluator.class.getModifiers()));
    }

    @Test
    void shouldInstantiateViaReflection() throws Exception {
      Constructor<CaptureEvaluator> constructor =
          CaptureEvaluator.class.getDeclaredConstructor();
      constructor.setAccessible(true);

      CaptureEvaluator instance = constructor.newInstance();
      assertNotNull(instance);

      CaptureEvaluator another = constructor.newInstance();
      assertNotSame(instance, another);
    }

    @Test
    void shouldHaveOnlyOneConstructor() {
      assertEquals(1, CaptureEvaluator.class.getDeclaredConstructors().length);
    }
  }

  @Nested
  class EvaluateBasicTest {

    @Test
    void shouldReturnScoreWhenCapturePossible() {
      when(config.getBotColor()).thenReturn(GameConstants.RED);
      when(config.getBotKingColor()).thenReturn(GameConstants.RED_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.RED, GameConstants.RED_KING, boardState))
            .thenReturn(true);

        int result =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
        assertEquals(GameConstants.SCORE_TAKE_POSSIBLE, result);
        mocked.verify(() -> CaptureRules.checkAllPiecesPossibleCaptures(
            GameConstants.RED, GameConstants.RED_KING, boardState));
      }
    }

    @Test
    void shouldReturnZeroWhenNoCapturePossible() {
      when(config.getBotColor()).thenReturn(GameConstants.BLACK);
      when(config.getBotKingColor()).thenReturn(GameConstants.BLACK_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.BLACK, GameConstants.BLACK_KING, boardState))
            .thenReturn(false);

        int result =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
        assertEquals(0, result);
      }
    }
  }

  @Nested
  class EvaluateEdgeCases {

    @Test
    void shouldHandleNullBoardState() {
      when(config.getBotColor()).thenReturn(GameConstants.RED);
      when(config.getBotKingColor()).thenReturn(GameConstants.RED_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.RED, GameConstants.RED_KING, null))
            .thenReturn(false);

        int result =
            CaptureEvaluator.evaluateCaptureOpportunities(null, config);
        assertEquals(0, result);
      }
    }

    @Test
    void shouldThrowWhenConfigIsNull() {
      assertThrows(NullPointerException.class,
          () -> CaptureEvaluator.evaluateCaptureOpportunities(boardState,
              null));
    }

    @Test
    void shouldHandleEmptyColor() {
      when(config.getBotColor()).thenReturn(GameConstants.EMPTY);
      when(config.getBotKingColor()).thenReturn(GameConstants.EMPTY);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.EMPTY, GameConstants.EMPTY, boardState))
            .thenReturn(true);

        int result =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
        assertEquals(GameConstants.SCORE_TAKE_POSSIBLE, result);
      }
    }

    @Test
    void shouldHandleExtremeIntValues() {
      when(config.getBotColor()).thenReturn(Integer.MAX_VALUE);
      when(config.getBotKingColor()).thenReturn(Integer.MIN_VALUE);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                Integer.MAX_VALUE, Integer.MIN_VALUE, boardState))
            .thenReturn(true);

        int result =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
        assertEquals(GameConstants.SCORE_TAKE_POSSIBLE, result);
      }
    }
  }

  @Nested
  class ExceptionHandlingTest {

    @Test
    void shouldPropagateIllegalStateException() {
      when(config.getBotColor()).thenReturn(GameConstants.RED);
      when(config.getBotKingColor()).thenReturn(GameConstants.RED_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.RED, GameConstants.RED_KING, boardState))
            .thenThrow(new IllegalStateException("boom"));

        assertThrows(IllegalStateException.class,
            () -> CaptureEvaluator.evaluateCaptureOpportunities(boardState,
                config));
      }
    }

    @Test
    void shouldPropagateRuntimeException() {
      when(config.getBotColor()).thenReturn(GameConstants.BLACK);
      when(config.getBotKingColor()).thenReturn(GameConstants.BLACK_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.BLACK, GameConstants.BLACK_KING, boardState))
            .thenThrow(new RuntimeException("runtime"));

        assertThrows(RuntimeException.class,
            () -> CaptureEvaluator.evaluateCaptureOpportunities(boardState,
                config));
      }
    }

    @Test
    void shouldPropagateError() {
      when(config.getBotColor()).thenReturn(GameConstants.RED);
      when(config.getBotKingColor()).thenReturn(GameConstants.RED_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.RED, GameConstants.RED_KING, boardState))
            .thenThrow(new AssertionError("error"));

        assertThrows(AssertionError.class,
            () -> CaptureEvaluator.evaluateCaptureOpportunities(boardState,
                config));
      }
    }
  }

  @Nested
  class ConsistencyTest {

    @Test
    void shouldReturnSameResultOnRepeatedCalls() {
      when(config.getBotColor()).thenReturn(GameConstants.RED);
      when(config.getBotKingColor()).thenReturn(GameConstants.RED_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.RED, GameConstants.RED_KING, boardState))
            .thenReturn(true);

        int first =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
        int second =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);

        assertEquals(first, second);
      }
    }

    @Test
    void shouldHandleMultipleSequentialResults() {
      when(config.getBotColor()).thenReturn(GameConstants.BLACK);
      when(config.getBotKingColor()).thenReturn(GameConstants.BLACK_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.BLACK, GameConstants.BLACK_KING, boardState))
            .thenReturn(true, false, true);

        int r1 =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
        int r2 =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
        int r3 =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);

        assertEquals(GameConstants.SCORE_TAKE_POSSIBLE, r1);
        assertEquals(0, r2);
        assertEquals(GameConstants.SCORE_TAKE_POSSIBLE, r3);
      }
    }

    @Test
    void shouldHandleRepeatedCallsInLoop() {
      when(config.getBotColor()).thenReturn(GameConstants.RED);
      when(config.getBotKingColor()).thenReturn(GameConstants.RED_KING);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.RED, GameConstants.RED_KING, boardState))
            .thenReturn(true);

        IntStream.range(0, 100).forEach(_ -> {
          int res =
              CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
          assertEquals(GameConstants.SCORE_TAKE_POSSIBLE, res);
        });
      }
    }
  }

  @Nested
  class IntegrationVerificationTest {

    @Test
    void shouldAlwaysPassCorrectArgumentsToCaptureRules() {
      when(config.getBotColor()).thenReturn(GameConstants.RED);
      when(config.getBotKingColor()).thenReturn(GameConstants.RED_KING);

      BoardState anotherBoard = mock(BoardState.class);

      try (var mocked = mockStatic(CaptureRules.class)) {
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.RED, GameConstants.RED_KING, boardState))
            .thenReturn(true);
        mocked.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
                GameConstants.RED, GameConstants.RED_KING, anotherBoard))
            .thenReturn(false);

        int r1 =
            CaptureEvaluator.evaluateCaptureOpportunities(boardState, config);
        int r2 =
            CaptureEvaluator.evaluateCaptureOpportunities(anotherBoard, config);

        assertEquals(GameConstants.SCORE_TAKE_POSSIBLE, r1);
        assertEquals(0, r2);

        mocked.verify(() -> CaptureRules.checkAllPiecesPossibleCaptures(
            GameConstants.RED, GameConstants.RED_KING, boardState));
        mocked.verify(() -> CaptureRules.checkAllPiecesPossibleCaptures(
            GameConstants.RED, GameConstants.RED_KING, anotherBoard));
      }
    }
  }
}
