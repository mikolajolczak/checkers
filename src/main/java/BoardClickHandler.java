package checkers.src.main.java;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class BoardClickHandler extends MouseAdapter {

  private final BoardController controller;


  private final Frame frame;

  private boolean firstClick = true;
  private int firstClickRow = GameConstants.BOARD_SIZE;
  private int firstClickCol = GameConstants.BOARD_SIZE;
  private int firstClickColor;

  public BoardClickHandler(BoardController controller,
                           Frame frame) {
    this.controller = controller;
    this.frame = frame;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    int col = e.getX() / GameConstants.SQUARE_SIZE;
    int row = e.getY() / GameConstants.SQUARE_SIZE;

    if (firstClick) {
      handleFirstClick(row, col);
    } else {
      handleSecondClick(row, col);
    }

    frame.getBoard().repaint();
  }

  private void handleFirstClick(int row, int col) {
    if (!controller.canSelectPiece(row, col)) {
      return;
    }

    firstClickRow = row;
    firstClickCol = col;
    firstClickColor = controller.getBoardState().getPiece(row, col);
    controller.getBoardState().setSelected(row, col);
    firstClick = false;
  }

  private void handleSecondClick(int row, int col) {
    controller.clearChosenTile();

    if (controller.mustTake()) {
      handleTakeClick(row, col);
    } else {
      handleNormalClick(row, col);
    }

    firstClick = true;
  }

  private void handleNormalClick(int row, int col) {
    if (controller.isLegalNormalMove(row, col, firstClickCol, firstClickRow, firstClickColor)) {
      controller.movePiece(row, col, firstClickCol, firstClickRow, firstClickColor);
      controller.getPromotionService().promoteIfNeeded(row, col, firstClickColor);
      controller.setCurrentColor();
    }
  }

  private void handleTakeClick(int row, int col) {
    if (!controller.getMove().legalTakeMove(col, row, firstClickCol, firstClickRow,
        firstClickColor)) {
      return;
    }

    if (controller.getPromotionService().isQueen(firstClickColor)) {
      controller.attemptQueenTake(row, col, firstClickCol, firstClickRow);
    } else {
      controller.attemptNormalTake(row, col, firstClickCol, firstClickRow);
    }

    controller.getPromotionService().promoteIfNeeded(row, col, firstClickColor);
    controller.setCurrentColor();
  }
}
