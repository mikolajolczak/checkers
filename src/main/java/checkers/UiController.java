package checkers;

/**
 * The {@code UiController} class manages the user interface interactions
 * for the checkers game. It provides methods to refresh the board panel
 * and to check whether the game has ended, delegating these actions to
 * the associated {@link Frame}.
 */
public final class UiController {
  /**
   * The main frame of the checkers game.
   */
  private final Frame frame;

  /**
   * A {@link Runnable} used to refresh the board panel.
   */
  private Runnable refreshBoardPanel;

  /**
   * Constructs a {@code UIController} with the specified frame.
   *
   * @param frameParam the frame associated with the UI controller
   */
  public UiController(final Frame frameParam) {
    frame = frameParam;
  }

  /**
   * Refreshes the board panel by executing the {@code refreshBoardPanel}
   * {@link Runnable},
   * if it has been set.
   */
  public void refreshBoard() {
    if (refreshBoardPanel != null) {
      refreshBoardPanel.run();
    }
  }

  /**
   * Sets the {@link Runnable} that will be used to refresh the board panel.
   *
   * @param refreshBoardPanelParam the {@link Runnable} to refresh the board
   *                               panel
   */
  public void setRefreshBoardPanel(final Runnable refreshBoardPanelParam) {
    refreshBoardPanel = refreshBoardPanelParam;
  }

  /**
   * Checks whether the game has ended by delegating to the associated
   * {@link Frame}.
   */
  public void checkGameEnd() {
    frame.checkGameFinished();
  }
}
