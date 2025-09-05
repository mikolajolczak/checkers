package checkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UIControllerTest {

  @Mock
  private Frame mockFrame;

  @Mock
  private Runnable mockRefreshBoardPanel;

  private UIController uiController;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    uiController = new UIController(mockFrame);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void constructor_ShouldCreateUIControllerWithGivenFrame() {

    UIController controller = new UIController(mockFrame);

    assertNotNull(controller);
  }

  @Test
  void constructor_ShouldAcceptNullFrame() {

    assertDoesNotThrow(() -> new UIController(null));
  }

  @Test
  void refreshBoard_ShouldCallRefreshBoardPanelRun_WhenCallbackIsSet() {

    uiController.setRefreshBoardPanel(mockRefreshBoardPanel);

    uiController.refreshBoard();

    verify(mockRefreshBoardPanel, times(1)).run();
  }

  @Test
  void refreshBoard_ShouldNotThrowException_WhenRefreshBoardPanelIsNull() {

    assertDoesNotThrow(() -> uiController.refreshBoard());
  }

  @Test
  void refreshBoard_CanBeCalledMultipleTimes() {

    uiController.setRefreshBoardPanel(mockRefreshBoardPanel);

    uiController.refreshBoard();
    uiController.refreshBoard();
    uiController.refreshBoard();

    verify(mockRefreshBoardPanel, times(3)).run();
  }

  @Test
  void setRefreshBoardPanel_ShouldSetCallback() {

    uiController.setRefreshBoardPanel(mockRefreshBoardPanel);
    uiController.refreshBoard();

    verify(mockRefreshBoardPanel, times(1)).run();
  }

  @Test
  void setRefreshBoardPanel_ShouldAcceptNull() {

    uiController.setRefreshBoardPanel(mockRefreshBoardPanel);

    uiController.setRefreshBoardPanel(null);

    assertDoesNotThrow(() -> uiController.refreshBoard());
    verify(mockRefreshBoardPanel, never()).run();
  }

  @Test
  void setRefreshBoardPanel_ShouldReplaceOldCallback() {

    Runnable oldCallback = mock(Runnable.class);
    uiController.setRefreshBoardPanel(oldCallback);

    uiController.setRefreshBoardPanel(mockRefreshBoardPanel);
    uiController.refreshBoard();

    verify(oldCallback, never()).run();
    verify(mockRefreshBoardPanel, times(1)).run();
  }

  @Test
  void checkGameEnd_ShouldCallFrameCheckGameFinished() {

    uiController.checkGameEnd();

    verify(mockFrame, times(1)).checkGameFinished();
  }

  @Test
  void checkGameEnd_CanBeCalledMultipleTimes() {

    uiController.checkGameEnd();
    uiController.checkGameEnd();
    uiController.checkGameEnd();

    verify(mockFrame, times(3)).checkGameFinished();
  }

  @Test
  void checkGameEnd_ShouldThrowException_WhenFrameIsNull() {

    UIController controllerWithNullFrame = new UIController(null);

    assertThrows(NullPointerException.class,
        controllerWithNullFrame::checkGameEnd);
  }

  @Test
  void integrationTest_SetCallbackAndRefreshBoard() {

    boolean[] callbackExecuted = {false};
    Runnable testCallback = () -> callbackExecuted[0] = true;

    uiController.setRefreshBoardPanel(testCallback);
    uiController.refreshBoard();

    assertTrue(callbackExecuted[0], "Callback should be executed");
  }

  @Test
  void integrationTest_CheckGameEndAndRefreshBoard() {

    uiController.setRefreshBoardPanel(mockRefreshBoardPanel);

    uiController.checkGameEnd();
    uiController.refreshBoard();

    verify(mockFrame, times(1)).checkGameFinished();
    verify(mockRefreshBoardPanel, times(1)).run();
  }
}