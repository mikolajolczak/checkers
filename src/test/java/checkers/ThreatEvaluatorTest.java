package checkers;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThreatEvaluatorTest {

  private static final int TEST_ROW = 3;
  private static final int TEST_COL = 4;
  private static final int REGULAR_PIECE = GameConstants.RED;
  private static final int KING_PIECE = GameConstants.RED_KING;
  private static final int HUMAN_COLOR = GameConstants.BLACK;
  private static final int HUMAN_KING_COLOR = GameConstants.BLACK_KING;
  @Mock
  private BotDecision mockDecision;
  @Mock
  private BoardState mockBoardState;
  @Mock
  private PlayerConfiguration mockPlayerConfiguration;

  @Test
  void shouldReturnZeroWhenPlayerCannotCaptureAfterMove() {

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class)) {
      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState))
          .thenReturn(false);

      int result = ThreatEvaluator.evaluatePlayerThreats(
          mockDecision, mockBoardState, mockPlayerConfiguration);

      assertEquals(0, result);

      verify(mockBoardState, never()).getPiece(anyInt(), anyInt());
    }
  }

  @Test
  void shouldReturnNegativeKingThreatScoreWhenMovedPieceIsKingAndPlayerCanCapture() {

    when(mockBoardState.getPiece(TEST_ROW, TEST_COL)).thenReturn(KING_PIECE);
    when(mockDecision.toRow()).thenReturn(TEST_ROW);
    when(mockDecision.toCol()).thenReturn(TEST_COL);
    when(mockPlayerConfiguration.getHumanColor()).thenReturn(HUMAN_COLOR);
    when(mockPlayerConfiguration.getHumanKingColor()).thenReturn(
        HUMAN_KING_COLOR);
    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class)
    ) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState))
          .thenReturn(true);

      pieceRulesMock.when(() -> PieceRules.isKing(KING_PIECE)).thenReturn(true);

      int result = ThreatEvaluator.evaluatePlayerThreats(
          mockDecision, mockBoardState, mockPlayerConfiguration);

      assertEquals(-GameConstants.SCORE_PLAYER_THREAT_KING, result);

      verify(mockBoardState).getPiece(TEST_ROW, TEST_COL);
      captureRulesMock.verify(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState));
      pieceRulesMock.verify(() -> PieceRules.isKing(KING_PIECE));
    }
  }

  @Test
  void shouldReturnNegativeRegularThreatScoreWhenMovedPieceIsNotKingAndPlayerCanCapture() {

    when(mockBoardState.getPiece(TEST_ROW, TEST_COL)).thenReturn(REGULAR_PIECE);
    when(mockDecision.toRow()).thenReturn(TEST_ROW);
    when(mockDecision.toCol()).thenReturn(TEST_COL);
    when(mockPlayerConfiguration.getHumanColor()).thenReturn(HUMAN_COLOR);
    when(mockPlayerConfiguration.getHumanKingColor()).thenReturn(
        HUMAN_KING_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class)
    ) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState))
          .thenReturn(true);

      pieceRulesMock.when(() -> PieceRules.isKing(REGULAR_PIECE))
          .thenReturn(false);

      int result = ThreatEvaluator.evaluatePlayerThreats(
          mockDecision, mockBoardState, mockPlayerConfiguration);

      assertEquals(-GameConstants.SCORE_PLAYER_THREAT, result);

      verify(mockBoardState).getPiece(TEST_ROW, TEST_COL);
      captureRulesMock.verify(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState));
      pieceRulesMock.verify(() -> PieceRules.isKing(REGULAR_PIECE));
    }
  }

  @Test
  void shouldHandleEdgeCaseWithZeroCoordinates() {

    when(mockDecision.toRow()).thenReturn(0);
    when(mockDecision.toCol()).thenReturn(0);
    when(mockBoardState.getPiece(0, 0)).thenReturn(REGULAR_PIECE);
    when(mockPlayerConfiguration.getHumanColor()).thenReturn(HUMAN_COLOR);
    when(mockPlayerConfiguration.getHumanKingColor()).thenReturn(
        HUMAN_KING_COLOR);
    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class)
    ) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState))
          .thenReturn(true);

      pieceRulesMock.when(() -> PieceRules.isKing(REGULAR_PIECE))
          .thenReturn(false);

      int result = ThreatEvaluator.evaluatePlayerThreats(
          mockDecision, mockBoardState, mockPlayerConfiguration);

      assertEquals(-GameConstants.SCORE_PLAYER_THREAT, result);
      verify(mockBoardState).getPiece(0, 0);
    }
  }

  @Test
  void shouldHandleCaseWithMaximumBoardCoordinates() {

    final int maxRow = 7;
    final int maxCol = 7;
    when(mockDecision.toRow()).thenReturn(maxRow);
    when(mockDecision.toCol()).thenReturn(maxCol);
    when(mockBoardState.getPiece(maxRow, maxCol)).thenReturn(KING_PIECE);
    when(mockPlayerConfiguration.getHumanColor()).thenReturn(HUMAN_COLOR);
    when(mockPlayerConfiguration.getHumanKingColor()).thenReturn(
        HUMAN_KING_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class)
    ) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState))
          .thenReturn(true);

      pieceRulesMock.when(() -> PieceRules.isKing(KING_PIECE)).thenReturn(true);

      int result = ThreatEvaluator.evaluatePlayerThreats(
          mockDecision, mockBoardState, mockPlayerConfiguration);

      assertEquals(-GameConstants.SCORE_PLAYER_THREAT_KING, result);
      verify(mockBoardState).getPiece(maxRow, maxCol);
    }
  }

  @Test
  void shouldVerifyCorrectMethodCallSequenceWhenPlayerCanCapture() {

    when(mockBoardState.getPiece(TEST_ROW, TEST_COL)).thenReturn(REGULAR_PIECE);
    when(mockDecision.toRow()).thenReturn(TEST_ROW);
    when(mockDecision.toCol()).thenReturn(TEST_COL);
    when(mockPlayerConfiguration.getHumanColor()).thenReturn(HUMAN_COLOR);
    when(mockPlayerConfiguration.getHumanKingColor()).thenReturn(
        HUMAN_KING_COLOR);
    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class)
    ) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState))
          .thenReturn(true);

      pieceRulesMock.when(() -> PieceRules.isKing(REGULAR_PIECE))
          .thenReturn(false);

      ThreatEvaluator.evaluatePlayerThreats(mockDecision, mockBoardState,
          mockPlayerConfiguration);

      var inOrder =
          inOrder(mockDecision, mockPlayerConfiguration, mockBoardState);
      int _ = inOrder.verify(mockPlayerConfiguration).getHumanColor();
      int _ = inOrder.verify(mockPlayerConfiguration).getHumanKingColor();
      int _ = inOrder.verify(mockDecision).toRow();
      int _ = inOrder.verify(mockDecision).toCol();
      int _ = inOrder.verify(mockBoardState).getPiece(TEST_ROW, TEST_COL);
    }
  }

  @Test
  void shouldHandleNullDecisionGracefully() {

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class)) {
      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState))
          .thenReturn(false);

      assertThrows(NullPointerException.class, () ->
          ThreatEvaluator.evaluatePlayerThreats(null, mockBoardState,
              mockPlayerConfiguration));
    }
  }

  @Test
  void shouldHandleNullBoardStateGracefully() {
    when(mockPlayerConfiguration.getHumanColor()).thenReturn(HUMAN_COLOR);
    when(mockPlayerConfiguration.getHumanKingColor()).thenReturn(
        HUMAN_KING_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class)) {
      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, null))
          .thenThrow(new NullPointerException("BoardState cannot be null"));

      assertThrows(NullPointerException.class, () ->
          ThreatEvaluator.evaluatePlayerThreats(mockDecision, null,
              mockPlayerConfiguration));
    }
  }

  @Test
  void shouldHandleNullPlayerConfigurationGracefully() {

    assertThrows(NullPointerException.class, () ->
        ThreatEvaluator.evaluatePlayerThreats(mockDecision, mockBoardState,
            null));
  }

  @Test
  void shouldVerifyThatThreatEvaluatorConstructorIsPrivate() {

    assertThrows(InvocationTargetException.class, () -> {
      var constructor = ThreatEvaluator.class.getDeclaredConstructor();
      assertFalse(constructor.canAccess(null));
      constructor.setAccessible(true);
      constructor.newInstance();
    });
  }

  @Test
  void shouldHandleDifferentPieceValuesCorrectly() {
    when(mockDecision.toRow()).thenReturn(TEST_ROW);
    when(mockDecision.toCol()).thenReturn(TEST_COL);
    when(mockPlayerConfiguration.getHumanColor()).thenReturn(HUMAN_COLOR);
    when(mockPlayerConfiguration.getHumanKingColor()).thenReturn(
        HUMAN_KING_COLOR);

    int unusualPieceValue = 999;
    when(mockBoardState.getPiece(TEST_ROW, TEST_COL)).thenReturn(
        unusualPieceValue);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(PieceRules.class)
    ) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              HUMAN_COLOR, HUMAN_KING_COLOR, mockBoardState))
          .thenReturn(true);

      pieceRulesMock.when(() -> PieceRules.isKing(unusualPieceValue))
          .thenReturn(false);

      int result = ThreatEvaluator.evaluatePlayerThreats(
          mockDecision, mockBoardState, mockPlayerConfiguration);

      assertEquals(-GameConstants.SCORE_PLAYER_THREAT, result);
      pieceRulesMock.verify(() -> PieceRules.isKing(unusualPieceValue));
    }
  }

  @Test
  void shouldWorkCorrectlyWithDifferentPlayerColors() {

    int differentHumanColor = 5;
    int differentHumanKingColor = 10;
    when(mockPlayerConfiguration.getHumanColor()).thenReturn(
        differentHumanColor);
    when(mockPlayerConfiguration.getHumanKingColor()).thenReturn(
        differentHumanKingColor);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class)) {
      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              differentHumanColor, differentHumanKingColor, mockBoardState))
          .thenReturn(false);

      int result = ThreatEvaluator.evaluatePlayerThreats(
          mockDecision, mockBoardState, mockPlayerConfiguration);

      assertEquals(0, result);
      captureRulesMock.verify(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          differentHumanColor, differentHumanKingColor, mockBoardState));
    }
  }
}