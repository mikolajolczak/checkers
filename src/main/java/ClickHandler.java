package checkers.src.main.java;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class ClickHandler extends MouseAdapter {

  private final MouseInputHandler mouseInputHandler;

  public ClickHandler(MouseInputHandler mouseInputHandler) {
    this.mouseInputHandler = mouseInputHandler;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    int col = e.getX() / GameConstants.SQUARE_SIZE;
    int row = e.getY() / GameConstants.SQUARE_SIZE;

    mouseInputHandler.handleMouseInput(row, col);
  }
}