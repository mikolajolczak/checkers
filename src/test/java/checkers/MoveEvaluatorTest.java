package checkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MoveEvaluatorTest {

  private MockedStatic<MoveExecutor> moveExecutorMock;
  private MockedStatic<ThreatEvaluator> threatEvaluatorMock;
  private MockedStatic<CaptureEvaluator> captureEvaluatorMock;
  private MockedStatic<PromotionEvaluator> promotionEvaluatorMock;

  private BotDecision testDecision;
  private BoardState testBoardState;
  private PlayerConfig testPlayerConfig;

  @BeforeEach
  void setUp() {

    moveExecutorMock = mockStatic(MoveExecutor.class);
    threatEvaluatorMock = mockStatic(ThreatEvaluator.class);
    captureEvaluatorMock = mockStatic(CaptureEvaluator.class);
    promotionEvaluatorMock = mockStatic(PromotionEvaluator.class);

    testDecision = mock(BotDecision.class);
    testBoardState = mock(BoardState.class);
    testPlayerConfig = mock(PlayerConfig.class);
  }

  @AfterEach
  void tearDown() {

    moveExecutorMock.close();
    threatEvaluatorMock.close();
    captureEvaluatorMock.close();
    promotionEvaluatorMock.close();
  }

  @Test
  void shouldEvaluateMoveWithPositiveScores() {

    int threatScore = 50;
    int captureScore = 30;
    int promotionScore = 20;
    int expectedTotalScore = threatScore + captureScore + promotionScore;

    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(testDecision, testBoardState,
                testPlayerConfig))
        .thenReturn(threatScore);

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
                testPlayerConfig))
        .thenReturn(captureScore);

    promotionEvaluatorMock.when(() ->
            PromotionEvaluator.evaluatePromotionChance(testDecision,
                testBoardState,
                testPlayerConfig))
        .thenReturn(promotionScore);

    int actualScore = MoveEvaluator.evaluateMove(testDecision, testBoardState,
        testPlayerConfig);

    assertEquals(expectedTotalScore, actualScore);

    moveExecutorMock.verify(() ->
        MoveExecutor.applyMoveToBoard(testDecision, testBoardState,
            testPlayerConfig), times(1));
    threatEvaluatorMock.verify(() ->
        ThreatEvaluator.evaluatePlayerThreats(testDecision, testBoardState,
            testPlayerConfig), times(1));
    captureEvaluatorMock.verify(() ->
        CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
            testPlayerConfig), times(1));
    promotionEvaluatorMock.verify(() ->
        PromotionEvaluator.evaluatePromotionChance(testDecision, testBoardState,
            testPlayerConfig), times(1));
  }

  @Test
  void shouldEvaluateMoveWithNegativeScores() {

    int threatScore = -25;
    int captureScore = -15;
    int promotionScore = -10;
    int expectedTotalScore = threatScore + captureScore + promotionScore;

    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(testDecision, testBoardState,
                testPlayerConfig))
        .thenReturn(threatScore);

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
                testPlayerConfig))
        .thenReturn(captureScore);

    promotionEvaluatorMock.when(() ->
            PromotionEvaluator.evaluatePromotionChance(testDecision,
                testBoardState,
                testPlayerConfig))
        .thenReturn(promotionScore);

    int actualScore = MoveEvaluator.evaluateMove(testDecision, testBoardState,
        testPlayerConfig);

    assertEquals(expectedTotalScore, actualScore);
  }

  @Test
  void shouldEvaluateMoveWithMixedScores() {

    int threatScore = 40;
    int captureScore = -20;
    int promotionScore = 15;
    int expectedTotalScore = threatScore + captureScore + promotionScore;

    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(testDecision, testBoardState,
                testPlayerConfig))
        .thenReturn(threatScore);

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
                testPlayerConfig))
        .thenReturn(captureScore);

    promotionEvaluatorMock.when(() ->
            PromotionEvaluator.evaluatePromotionChance(testDecision,
                testBoardState,
                testPlayerConfig))
        .thenReturn(promotionScore);

    int actualScore = MoveEvaluator.evaluateMove(testDecision, testBoardState,
        testPlayerConfig);

    assertEquals(expectedTotalScore, actualScore);
  }

  @Test
  void shouldEvaluateMoveWithZeroScores() {

    int threatScore = 0;
    int captureScore = 0;
    int promotionScore = 0;
    int expectedTotalScore = 0;

    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(testDecision, testBoardState,
                testPlayerConfig))
        .thenReturn(threatScore);

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
                testPlayerConfig))
        .thenReturn(captureScore);

    promotionEvaluatorMock.when(() ->
            PromotionEvaluator.evaluatePromotionChance(testDecision,
                testBoardState,
                testPlayerConfig))
        .thenReturn(promotionScore);

    int actualScore = MoveEvaluator.evaluateMove(testDecision, testBoardState,
        testPlayerConfig);

    assertEquals(expectedTotalScore, actualScore);
  }

  @Test
  void shouldHandleMaximumIntegerValues() {

    int threatScore = Integer.MAX_VALUE / 3;
    int captureScore = Integer.MAX_VALUE / 3;
    int promotionScore = Integer.MAX_VALUE / 3;
    int expectedTotalScore = threatScore + captureScore + promotionScore;

    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(testDecision, testBoardState,
                testPlayerConfig))
        .thenReturn(threatScore);

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
                testPlayerConfig))
        .thenReturn(captureScore);

    promotionEvaluatorMock.when(() ->
            PromotionEvaluator.evaluatePromotionChance(testDecision,
                testBoardState,
                testPlayerConfig))
        .thenReturn(promotionScore);

    int actualScore = MoveEvaluator.evaluateMove(testDecision, testBoardState,
        testPlayerConfig);

    assertEquals(expectedTotalScore, actualScore);
  }

  @Test
  void shouldHandleMinimumIntegerValues() {

    int threatScore = Integer.MIN_VALUE / 3;
    int captureScore = Integer.MIN_VALUE / 3;
    int promotionScore = Integer.MIN_VALUE / 3;
    int expectedTotalScore = threatScore + captureScore + promotionScore;

    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(testDecision, testBoardState,
                testPlayerConfig))
        .thenReturn(threatScore);

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
                testPlayerConfig))
        .thenReturn(captureScore);

    promotionEvaluatorMock.when(() ->
            PromotionEvaluator.evaluatePromotionChance(testDecision,
                testBoardState,
                testPlayerConfig))
        .thenReturn(promotionScore);

    int actualScore = MoveEvaluator.evaluateMove(testDecision, testBoardState,
        testPlayerConfig);

    assertEquals(expectedTotalScore, actualScore);
  }

  @Test
  void shouldVerifyCorrectOrderOfOperations() {

    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(any(), any(), any()))
        .thenReturn(10);

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(any(), any()))
        .thenReturn(20);

    promotionEvaluatorMock.when(() ->
            PromotionEvaluator.evaluatePromotionChance(any(), any(), any()))
        .thenReturn(30);

    MoveEvaluator.evaluateMove(testDecision, testBoardState,
        testPlayerConfig);

    moveExecutorMock.verify(
        () -> MoveExecutor.applyMoveToBoard(testDecision, testBoardState,
            testPlayerConfig));
    threatEvaluatorMock.verify(
        () -> ThreatEvaluator.evaluatePlayerThreats(testDecision,
            testBoardState, testPlayerConfig));
    captureEvaluatorMock.verify(
        () -> CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
            testPlayerConfig));
    promotionEvaluatorMock.verify(
        () -> PromotionEvaluator.evaluatePromotionChance(testDecision,
            testBoardState, testPlayerConfig));

  }

  @Test
  void shouldHandleNullParametersGracefully() {

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(null, null))
        .thenReturn(0);

    assertDoesNotThrow(() ->
        MoveEvaluator.evaluateMove(null, null, null));
  }

  @Test
  void shouldPropagateExceptionsFromMoveExecutor() {

    RuntimeException expectedException =
        new RuntimeException("MoveExecutor failed");
    moveExecutorMock.when(() ->
            MoveExecutor.applyMoveToBoard(any(), any(), any()))
        .thenThrow(expectedException);

    RuntimeException actualException =
        assertThrows(RuntimeException.class, () ->
            MoveEvaluator.evaluateMove(testDecision, testBoardState,
                testPlayerConfig));

    assertEquals(expectedException, actualException);
  }

  @Test
  void shouldPropagateExceptionsFromThreatEvaluator() {

    RuntimeException expectedException =
        new RuntimeException("ThreatEvaluator failed");
    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(any(), any(), any()))
        .thenThrow(expectedException);

    RuntimeException actualException =
        assertThrows(RuntimeException.class, () ->
            MoveEvaluator.evaluateMove(testDecision, testBoardState,
                testPlayerConfig));

    assertEquals(expectedException, actualException);
  }

  @Test
  void shouldVerifyMoveExecutorIsCalledBeforeEvaluators() {

    threatEvaluatorMock.when(() ->
            ThreatEvaluator.evaluatePlayerThreats(any(), any(), any()))
        .thenReturn(5);

    captureEvaluatorMock.when(() ->
            CaptureEvaluator.evaluateCaptureOpportunities(any(), any()))
        .thenReturn(5);

    promotionEvaluatorMock.when(() ->
            PromotionEvaluator.evaluatePromotionChance(any(), any(), any()))
        .thenReturn(5);

    MoveEvaluator.evaluateMove(testDecision, testBoardState,
        testPlayerConfig);

    moveExecutorMock.verify(() ->
        MoveExecutor.applyMoveToBoard(testDecision, testBoardState,
            testPlayerConfig));

    threatEvaluatorMock.verify(() ->
        ThreatEvaluator.evaluatePlayerThreats(testDecision, testBoardState,
            testPlayerConfig));
    captureEvaluatorMock.verify(() ->
        CaptureEvaluator.evaluateCaptureOpportunities(testBoardState,
            testPlayerConfig));
    promotionEvaluatorMock.verify(() ->
        PromotionEvaluator.evaluatePromotionChance(testDecision, testBoardState,
            testPlayerConfig));
  }
}