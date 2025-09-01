package checkers.src.main.java;

public class UIController {
  private final Frame frame;

  public UIController(Frame frameParam) {
    frame = frameParam;
  }

  public void refreshBoard() {
    frame.getBoard().repaint();
  }
  public void checkGameEnd() {
    frame.isGameFinished();
  }
}