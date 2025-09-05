package checkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class PromotionEvaluatorTest {

  @Mock
  private BoardState boardState;

  @Mock
  private BotDecision botDecision;

  @Mock
  private PlayerConfiguration playerConfiguration;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  class EvaluatePromotionChanceTests {

    @Test
    void shouldReturnScoreChanceForQueenWhenPieceCanPromote() {

      int toRow = 3;
      int toCol = 4;
      int movedPiece = GameConstants.BLACK;
      int botColor = GameConstants.BLACK;

      when(botDecision.toRow()).thenReturn(toRow);
      when(botDecision.toCol()).thenReturn(toCol);
      when(boardState.getPiece(toRow, toCol)).thenReturn(movedPiece);
      when(playerConfiguration.getBotColor()).thenReturn(botColor);

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.evaluatePromotionChance(botDecision,
                    boardState, playerConfiguration))
            .thenCallRealMethod();
        mockedStatic.when(
                () -> PromotionEvaluator.canPromoteToQueen(boardState,
                    movedPiece,
                    botColor))
            .thenReturn(true);

        int result =
            PromotionEvaluator.evaluatePromotionChance(botDecision, boardState,
                playerConfiguration);

        assertEquals(GameConstants.SCORE_CHANCE_FOR_QUEEN, result);
      }
    }

    @Test
    void shouldReturnZeroWhenPieceCannotPromote() {

      int toRow = 3;
      int toCol = 4;
      int movedPiece = GameConstants.BLACK;
      int botColor = GameConstants.BLACK;

      when(botDecision.toRow()).thenReturn(toRow);
      when(botDecision.toCol()).thenReturn(toCol);
      when(boardState.getPiece(toRow, toCol)).thenReturn(movedPiece);
      when(playerConfiguration.getBotColor()).thenReturn(botColor);

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.evaluatePromotionChance(botDecision,
                    boardState, playerConfiguration))
            .thenCallRealMethod();
        mockedStatic.when(
                () -> PromotionEvaluator.canPromoteToQueen(boardState,
                    movedPiece,
                    botColor))
            .thenReturn(false);

        int result =
            PromotionEvaluator.evaluatePromotionChance(botDecision, boardState,
                playerConfiguration);

        assertEquals(0, result);
      }
    }
  }

  @Nested
  class CanPromoteToQueenTests {

    @Test
    void shouldReturnTrueWhenChanceForQueen() {

      int movedPiece = GameConstants.BLACK;
      int botColor = GameConstants.BLACK;

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.canPromoteToQueen(boardState,
                    movedPiece,
                    botColor))
            .thenCallRealMethod();
        mockedStatic.when(
                () -> PromotionEvaluator.isChanceForQueen(botColor, boardState,
                    movedPiece))
            .thenReturn(true);

        boolean result =
            PromotionEvaluator.canPromoteToQueen(boardState, movedPiece,
                botColor);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnFalseWhenNoChanceForQueen() {

      int movedPiece = GameConstants.BLACK;
      int botColor = GameConstants.BLACK;

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.canPromoteToQueen(boardState,
                    movedPiece,
                    botColor))
            .thenCallRealMethod();
        mockedStatic.when(
                () -> PromotionEvaluator.isChanceForQueen(botColor, boardState,
                    movedPiece))
            .thenReturn(false);

        boolean result =
            PromotionEvaluator.canPromoteToQueen(boardState, movedPiece,
                botColor);

        assertFalse(result);
      }
    }
  }

  @Nested
  class IsChanceForQueenTests {

    @Test
    void shouldReturnFalseWhenPieceIsKing() {

      int colorToCheck = GameConstants.BLACK;
      int kingPiece = GameConstants.BLACK_KING;

      try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(
          PieceRules.class);
           MockedStatic<PromotionEvaluator> promotionMock = mockStatic(
               PromotionEvaluator.class)) {

        pieceRulesMock.when(() -> PieceRules.isKing(kingPiece))
            .thenReturn(true);
        promotionMock.when(
                () -> PromotionEvaluator.isChanceForQueen(colorToCheck,
                    boardState,
                    kingPiece))
            .thenCallRealMethod();

        boolean result =
            PromotionEvaluator.isChanceForQueen(colorToCheck, boardState,
                kingPiece);

        assertFalse(result);
        pieceRulesMock.verify(() -> PieceRules.isKing(kingPiece));
      }
    }

    @Test
    void shouldReturnTrueWhenBlackPieceCanReachLastRow() {

      int colorToCheck = GameConstants.BLACK;
      int regularPiece = GameConstants.BLACK;

      try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(
          PieceRules.class);
           MockedStatic<PromotionEvaluator> promotionMock = mockStatic(
               PromotionEvaluator.class)) {

        pieceRulesMock.when(() -> PieceRules.isKing(regularPiece))
            .thenReturn(false);
        promotionMock.when(
                () -> PromotionEvaluator.isChanceForQueen(colorToCheck,
                    boardState,
                    regularPiece))
            .thenCallRealMethod();
        promotionMock.when(
                () -> PromotionEvaluator.getPromotionRow(colorToCheck))
            .thenReturn(GameConstants.LAST_ROW_INDEX);
        promotionMock.when(
                () -> PromotionEvaluator.hasPieceOnPromotionRow(boardState,
                    colorToCheck, GameConstants.LAST_ROW_INDEX))
            .thenReturn(true);

        boolean result =
            PromotionEvaluator.isChanceForQueen(colorToCheck, boardState,
                regularPiece);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnTrueWhenREDPieceCanReachFirstRow() {

      int colorToCheck = GameConstants.RED;
      int regularPiece = GameConstants.RED;

      try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(
          PieceRules.class);
           MockedStatic<PromotionEvaluator> promotionMock = mockStatic(
               PromotionEvaluator.class)) {

        pieceRulesMock.when(() -> PieceRules.isKing(regularPiece))
            .thenReturn(false);
        promotionMock.when(
                () -> PromotionEvaluator.isChanceForQueen(colorToCheck,
                    boardState,
                    regularPiece))
            .thenCallRealMethod();
        promotionMock.when(
                () -> PromotionEvaluator.getPromotionRow(colorToCheck))
            .thenReturn(0);
        promotionMock.when(
                () -> PromotionEvaluator.hasPieceOnPromotionRow(boardState,
                    colorToCheck, 0))
            .thenReturn(true);

        boolean result =
            PromotionEvaluator.isChanceForQueen(colorToCheck, boardState,
                regularPiece);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnFalseWhenPieceCannotReachPromotionRow() {

      int colorToCheck = GameConstants.BLACK;
      int regularPiece = GameConstants.BLACK;

      try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(
          PieceRules.class);
           MockedStatic<PromotionEvaluator> promotionMock = mockStatic(
               PromotionEvaluator.class)) {

        pieceRulesMock.when(() -> PieceRules.isKing(regularPiece))
            .thenReturn(false);
        promotionMock.when(
                () -> PromotionEvaluator.isChanceForQueen(colorToCheck,
                    boardState,
                    regularPiece))
            .thenCallRealMethod();
        promotionMock.when(
                () -> PromotionEvaluator.getPromotionRow(colorToCheck))
            .thenReturn(GameConstants.LAST_ROW_INDEX);
        promotionMock.when(
                () -> PromotionEvaluator.hasPieceOnPromotionRow(boardState,
                    colorToCheck, GameConstants.LAST_ROW_INDEX))
            .thenReturn(false);

        boolean result =
            PromotionEvaluator.isChanceForQueen(colorToCheck, boardState,
                regularPiece);

        assertFalse(result);
      }
    }
  }

  @Nested
  class GetPromotionRowTests {

    @Test
    void shouldReturnLastRowIndexForBlackColor() {

      int blackColor = GameConstants.BLACK;

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(() -> PromotionEvaluator.getPromotionRow(blackColor))
            .thenCallRealMethod();

        int result = PromotionEvaluator.getPromotionRow(blackColor);

        assertEquals(GameConstants.LAST_ROW_INDEX, result);
      }
    }

    @Test
    void shouldReturnZeroForREDColor() {

      int REDColor = GameConstants.RED;

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(() -> PromotionEvaluator.getPromotionRow(REDColor))
            .thenCallRealMethod();

        int result = PromotionEvaluator.getPromotionRow(REDColor);

        assertEquals(0, result);
      }
    }

    @Test
    void shouldReturnZeroForAnyColorOtherThanBlack() {

      int otherColor = 999;

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(() -> PromotionEvaluator.getPromotionRow(otherColor))
            .thenCallRealMethod();

        int result = PromotionEvaluator.getPromotionRow(otherColor);

        assertEquals(0, result);
      }
    }
  }

  @Nested
  class HasPieceOnPromotionRowTests {

    @Test
    void shouldReturnTrueWhenPieceExistsOnTargetRow() {

      int colorToCheck = GameConstants.BLACK;
      int targetRow = 7;

      when(boardState.getPiece(targetRow, 0)).thenReturn(GameConstants.EMPTY);
      when(boardState.getPiece(targetRow, 1)).thenReturn(GameConstants.EMPTY);
      when(boardState.getPiece(targetRow, 2)).thenReturn(GameConstants.BLACK);
      when(boardState.getPiece(targetRow, 3)).thenReturn(GameConstants.EMPTY);

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.hasPieceOnPromotionRow(boardState,
                    colorToCheck, targetRow))
            .thenCallRealMethod();

        boolean result =
            PromotionEvaluator.hasPieceOnPromotionRow(boardState, colorToCheck,
                targetRow);

        assertTrue(result);
      }
    }

    @Test
    void shouldReturnFalseWhenNoPieceExistsOnTargetRow() {

      int colorToCheck = GameConstants.BLACK;
      int targetRow = 7;

      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        when(boardState.getPiece(targetRow, col)).thenReturn(GameConstants.RED);
      }

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.hasPieceOnPromotionRow(boardState,
                    colorToCheck, targetRow))
            .thenCallRealMethod();

        boolean result =
            PromotionEvaluator.hasPieceOnPromotionRow(boardState, colorToCheck,
                targetRow);

        assertFalse(result);
      }
    }

    @Test
    void shouldReturnFalseWhenAllPositionsAreEmpty() {

      int colorToCheck = GameConstants.BLACK;
      int targetRow = 7;

      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        when(boardState.getPiece(targetRow, col)).thenReturn(
            GameConstants.EMPTY);
      }

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.hasPieceOnPromotionRow(boardState,
                    colorToCheck, targetRow))
            .thenCallRealMethod();

        boolean result =
            PromotionEvaluator.hasPieceOnPromotionRow(boardState, colorToCheck,
                targetRow);

        assertFalse(result);
      }
    }

    @Test
    void shouldCheckAllColumnsOnTargetRow() {

      int colorToCheck = GameConstants.BLACK;
      int targetRow = 7;

      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        when(boardState.getPiece(targetRow, col)).thenReturn(GameConstants.RED);
      }

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.hasPieceOnPromotionRow(boardState,
                    colorToCheck, targetRow))
            .thenCallRealMethod();

        boolean _ =
            PromotionEvaluator.hasPieceOnPromotionRow(boardState, colorToCheck,
                targetRow);

        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          verify(boardState).getPiece(targetRow, col);
        }
      }
    }

    @Test
    void shouldReturnTrueImmediatelyWhenFirstMatchingPieceFound() {

      int colorToCheck = GameConstants.BLACK;
      int targetRow = 7;

      when(boardState.getPiece(targetRow, 0)).thenReturn(GameConstants.BLACK);

      try (MockedStatic<PromotionEvaluator> mockedStatic = mockStatic(
          PromotionEvaluator.class)) {
        mockedStatic.when(
                () -> PromotionEvaluator.hasPieceOnPromotionRow(boardState,
                    colorToCheck, targetRow))
            .thenCallRealMethod();

        boolean result =
            PromotionEvaluator.hasPieceOnPromotionRow(boardState, colorToCheck,
                targetRow);

        assertTrue(result);
        verify(boardState, times(1)).getPiece(targetRow, 0);

        verify(boardState, never()).getPiece(targetRow, 1);
      }
    }
  }

  @Nested
  class ConstructorTests {

    @Test
    void shouldHavePrivateConstructorToPreventInstantiation() {

      try {
        java.lang.reflect.Constructor<PromotionEvaluator> constructor =
            PromotionEvaluator.class.getDeclaredConstructor();

        assertTrue(
            java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));

        constructor.setAccessible(true);
        assertThrows(java.lang.reflect.InvocationTargetException.class,
            constructor::newInstance);
      } catch (NoSuchMethodException e) {
        fail("Private constructor should exist");
      }
    }
  }

  @Nested
  class IntegrationTests {

    @Test
    void shouldEvaluatePromotionChanceForBlackPieceReachingLastRow() {

      int toRow = GameConstants.LAST_ROW_INDEX;
      int toCol = 3;
      int blackPiece = GameConstants.BLACK;

      when(botDecision.toRow()).thenReturn(toRow);
      when(botDecision.toCol()).thenReturn(toCol);
      when(boardState.getPiece(toRow, toCol)).thenReturn(blackPiece);
      when(playerConfiguration.getBotColor()).thenReturn(GameConstants.BLACK);
      when(boardState.getPiece(GameConstants.LAST_ROW_INDEX, toCol)).thenReturn(
          blackPiece);

      try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(
          PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isKing(blackPiece))
            .thenReturn(false);

        int result =
            PromotionEvaluator.evaluatePromotionChance(botDecision, boardState,
                playerConfiguration);

        assertEquals(GameConstants.SCORE_CHANCE_FOR_QUEEN, result);
      }
    }

    @Test
    void shouldEvaluatePromotionChanceForREDPieceReachingFirstRow() {

      int toRow = 0;
      int toCol = 3;
      int REDPiece = GameConstants.RED;

      when(botDecision.toRow()).thenReturn(toRow);
      when(botDecision.toCol()).thenReturn(toCol);
      when(boardState.getPiece(toRow, toCol)).thenReturn(REDPiece);
      when(playerConfiguration.getBotColor()).thenReturn(GameConstants.RED);
      when(boardState.getPiece(0, toCol)).thenReturn(REDPiece);

      try (MockedStatic<PieceRules> pieceRulesMock = mockStatic(
          PieceRules.class)) {
        pieceRulesMock.when(() -> PieceRules.isKing(REDPiece))
            .thenReturn(false);

        int result =
            PromotionEvaluator.evaluatePromotionChance(botDecision, boardState,
                playerConfiguration);

        assertEquals(GameConstants.SCORE_CHANCE_FOR_QUEEN, result);
      }
    }
  }
}