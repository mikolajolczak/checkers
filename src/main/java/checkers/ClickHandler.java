package checkers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A mouse click handler for the checkers board.
 *
 * <p>This class listens for mouse release events on the board and delegates
 * the processing of the mouse input to a {@link MouseInputHandler}.
 * Each click is converted to a board row and column based on the
 * predefined square size.
 * </p>
 */
public final class ClickHandler extends MouseAdapter {
  /**
   * The handler responsible for processing mouse input on the board.
   */
  private final MouseInputHandler mouseInputHandler;

  /**
   * Constructs a ClickHandler with the specified MouseInputHandler.
   *
   * @param mouseInputHandlerParam the handler to process mouse input
   */
  public ClickHandler(final MouseInputHandler mouseInputHandlerParam) {
    this.mouseInputHandler = mouseInputHandlerParam;
  }

  @Override
  public void mouseReleased(final MouseEvent e) {
    int col = e.getX() / GameConstants.SQUARE_SIZE;
    int row = e.getY() / GameConstants.SQUARE_SIZE;

    mouseInputHandler.handleMouseInput(row, col);
  }
}
