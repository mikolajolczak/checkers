package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnFlowManagerTest {

  @Mock
  private TurnManager turnManager;

  @Mock
  private BotController botController;

  private TurnFlowManager turnFlowManager;

  @BeforeEach
  void setUp() {
    turnFlowManager = new TurnFlowManager(turnManager, botController);
  }

  @Test
  void afterMove_shouldAlwaysSwitchTurn() {

    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    turnFlowManager.afterMove();

    verify(turnManager).switchTurn();
  }

  @Test
  void afterMove_shouldExecuteBotTurn_whenCurrentPlayerIsBot() {

    when(turnManager.isCurrentPlayerBot()).thenReturn(true);

    turnFlowManager.afterMove();

    verify(turnManager).switchTurn();
    verify(botController).executeTurn();
  }

  @Test
  void afterMove_shouldNotExecuteBotTurn_whenCurrentPlayerIsHuman() {

    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    turnFlowManager.afterMove();

    verify(turnManager).switchTurn();
    verify(botController, never()).executeTurn();
  }

  @Test
  void afterMove_shouldCallMethodsInCorrectOrder() {

    when(turnManager.isCurrentPlayerBot()).thenReturn(true);

    turnFlowManager.afterMove();

    var inOrder = inOrder(turnManager, botController);
    inOrder.verify(turnManager).switchTurn();
    inOrder.verify(turnManager).isCurrentPlayerBot();
    inOrder.verify(botController).executeTurn();
  }

  @Test
  void afterMove_shouldHandleMultipleCalls_whenBotPlays() {

    when(turnManager.isCurrentPlayerBot()).thenReturn(true);

    turnFlowManager.afterMove();
    turnFlowManager.afterMove();

    verify(turnManager, times(2)).switchTurn();
    verify(turnManager, times(2)).isCurrentPlayerBot();
    verify(botController, times(2)).executeTurn();
  }

  @Test
  void afterMove_shouldHandleMultipleCalls_whenHumanPlays() {

    when(turnManager.isCurrentPlayerBot()).thenReturn(false);

    turnFlowManager.afterMove();
    turnFlowManager.afterMove();

    verify(turnManager, times(2)).switchTurn();
    verify(turnManager, times(2)).isCurrentPlayerBot();
    verify(botController, never()).executeTurn();
  }

  @Test
  void afterMove_shouldHandleAlternatingTurns() {

    when(turnManager.isCurrentPlayerBot())
        .thenReturn(true)
        .thenReturn(false)
        .thenReturn(true);

    turnFlowManager.afterMove();
    turnFlowManager.afterMove();
    turnFlowManager.afterMove();

    verify(turnManager, times(3)).switchTurn();
    verify(turnManager, times(3)).isCurrentPlayerBot();
    verify(botController, times(2)).executeTurn();
  }

  @Test
  void constructor_shouldAcceptNullParameters() {

    new TurnFlowManager(null, null);
    new TurnFlowManager(turnManager, null);
    new TurnFlowManager(null, botController);
  }

  @Test
  void record_shouldProvideCorrectAccessorMethods() {

    assert turnFlowManager.turnManager() == turnManager;
    assert turnFlowManager.botController() == botController;
  }

  @Test
  void record_shouldImplementEqualsCorrectly() {

    TurnFlowManager other = new TurnFlowManager(turnManager, botController);
    TurnFlowManager different =
        new TurnFlowManager(mock(TurnManager.class), botController);

    assert turnFlowManager.equals(other);
    assert !turnFlowManager.equals(different);
  }

  @Test
  void record_shouldImplementHashCodeCorrectly() {

    TurnFlowManager other = new TurnFlowManager(turnManager, botController);

    assert turnFlowManager.hashCode() == other.hashCode();
  }

  @Test
  void record_shouldImplementToStringCorrectly() {

    String toString = turnFlowManager.toString();

    assert toString.contains("TurnFlowManager");
    assert toString.contains("turnManager");
    assert toString.contains("botController");
  }
}