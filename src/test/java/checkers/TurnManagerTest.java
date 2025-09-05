package checkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


class TurnManagerTest {

  @Mock
  private PlayerConfiguration mockPlayerConfig;

  private TurnManager turnManager;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void constructor_InitializesWithRedColor() {

    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    assertEquals(GameConstants.RED, turnManager.getCurrentColor());
    assertEquals(GameConstants.RED_KING, turnManager.getCurrentKingColor());
  }

  @Test
  void constructor_InitializesWithBlackColor() {

    turnManager = new TurnManager(mockPlayerConfig, GameConstants.BLACK,
        GameConstants.BLACK_KING);

    assertEquals(GameConstants.BLACK, turnManager.getCurrentColor());
    assertEquals(GameConstants.BLACK_KING, turnManager.getCurrentKingColor());
  }

  @Test
  void switchTurn_ChangesFromRedToBlack() {

    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    turnManager.switchTurn();

    assertEquals(GameConstants.BLACK, turnManager.getCurrentColor());
    assertEquals(GameConstants.BLACK_KING, turnManager.getCurrentKingColor());
  }

  @Test
  void switchTurn_ChangesFromBlackToRed() {

    turnManager = new TurnManager(mockPlayerConfig, GameConstants.BLACK,
        GameConstants.BLACK_KING);

    turnManager.switchTurn();

    assertEquals(GameConstants.RED, turnManager.getCurrentColor());
    assertEquals(GameConstants.RED_KING, turnManager.getCurrentKingColor());
  }

  @Test
  void switchTurn_MultipleToggleReturnsToOriginal() {

    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    turnManager.switchTurn();
    assertEquals(GameConstants.BLACK, turnManager.getCurrentColor());
    assertEquals(GameConstants.BLACK_KING, turnManager.getCurrentKingColor());

    turnManager.switchTurn();
    assertEquals(GameConstants.RED, turnManager.getCurrentColor());
    assertEquals(GameConstants.RED_KING, turnManager.getCurrentKingColor());
  }

  @Test
  void isCurrentPlayerBot_ReturnsTrueWhenCurrentPlayerIsBotRed() {

    when(mockPlayerConfig.getBotColor()).thenReturn(GameConstants.RED);
    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    assertTrue(turnManager.isCurrentPlayerBot());
  }

  @Test
  void isCurrentPlayerBot_ReturnsTrueWhenCurrentPlayerIsBotBlack() {

    when(mockPlayerConfig.getBotColor()).thenReturn(GameConstants.BLACK);
    turnManager = new TurnManager(mockPlayerConfig, GameConstants.BLACK,
        GameConstants.BLACK_KING);

    assertTrue(turnManager.isCurrentPlayerBot());
  }

  @Test
  void isCurrentPlayerBot_ReturnsFalseWhenCurrentPlayerIsNotBot() {

    when(mockPlayerConfig.getBotColor()).thenReturn(GameConstants.BLACK);
    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    assertFalse(turnManager.isCurrentPlayerBot());
  }

  @Test
  void isCurrentPlayerBot_ChangesAfterSwitchTurn() {

    when(mockPlayerConfig.getBotColor()).thenReturn(GameConstants.BLACK);
    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    assertFalse(turnManager.isCurrentPlayerBot());

    turnManager.switchTurn();
    assertTrue(turnManager.isCurrentPlayerBot());

    turnManager.switchTurn();
    assertFalse(turnManager.isCurrentPlayerBot());
  }

  @Test
  void getCurrentColor_ReturnsCurrentColor() {

    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    assertEquals(GameConstants.RED, turnManager.getCurrentColor());

    turnManager.switchTurn();
    assertEquals(GameConstants.BLACK, turnManager.getCurrentColor());
  }

  @Test
  void getCurrentKingColor_ReturnsCurrentKingColor() {

    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    assertEquals(GameConstants.RED_KING, turnManager.getCurrentKingColor());

    turnManager.switchTurn();
    assertEquals(GameConstants.BLACK_KING, turnManager.getCurrentKingColor());
  }

  @Test
  void integrationTest_GameScenario() {

    when(mockPlayerConfig.getBotColor()).thenReturn(GameConstants.BLACK);
    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    assertEquals(GameConstants.RED, turnManager.getCurrentColor());
    assertEquals(GameConstants.RED_KING, turnManager.getCurrentKingColor());
    assertFalse(turnManager.isCurrentPlayerBot());

    turnManager.switchTurn();
    assertEquals(GameConstants.BLACK, turnManager.getCurrentColor());
    assertEquals(GameConstants.BLACK_KING, turnManager.getCurrentKingColor());
    assertTrue(turnManager.isCurrentPlayerBot());

    turnManager.switchTurn();
    assertEquals(GameConstants.RED, turnManager.getCurrentColor());
    assertEquals(GameConstants.RED_KING, turnManager.getCurrentKingColor());
    assertFalse(turnManager.isCurrentPlayerBot());
  }

  @Test
  void constructor_HandlesNullPlayerConfiguration() {

    turnManager =
        new TurnManager(null, GameConstants.RED, GameConstants.RED_KING);

    assertEquals(GameConstants.RED, turnManager.getCurrentColor());
    assertEquals(GameConstants.RED_KING, turnManager.getCurrentKingColor());

    assertThrows(NullPointerException.class,
        () -> turnManager.isCurrentPlayerBot());
  }

  @Test
  void playerConfig_IsOnlyCalledWhenNeeded() {

    turnManager = new TurnManager(mockPlayerConfig, GameConstants.RED,
        GameConstants.RED_KING);

    turnManager.switchTurn();
    int _ = turnManager.getCurrentColor();
    int _ = turnManager.getCurrentKingColor();

    verifyNoInteractions(mockPlayerConfig);

    turnManager.isCurrentPlayerBot();

    verify(mockPlayerConfig, times(1)).getBotColor();
  }
}