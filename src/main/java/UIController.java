package checkers.src.main.java;

public class UIController {
  private final Frame frame;
  private Runnable refreshBoardPanel;

  public UIController(Frame frameParam) {
    frame = frameParam;
  }

  public void refreshBoard() {
    if (refreshBoardPanel != null) {
      refreshBoardPanel.run();
    }
  }

  public void setRefreshBoardPanel(Runnable refreshBoardPanelParam) {
    refreshBoardPanel = refreshBoardPanelParam;
  }

  public void checkGameEnd() {
    frame.isGameFinished();
  }
}