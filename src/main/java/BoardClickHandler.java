package checkers.src.main.java;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class BoardClickHandler extends MouseAdapter {

  private final BoardController controller;
  private final Move move;
  private boolean firstClick = true;
  private int firstClickRow = GameConstants.BOARD_SIZE;
  private int firstClickCol = GameConstants.BOARD_SIZE;
  private int firstClickColor;

  public BoardClickHandler(BoardController controller, Move moveParam) {
    this.controller = controller;
    move = moveParam;
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
    controller.getUiController().refreshBoard();
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
    controller.getBoardState().setSelected(GameConstants.BOARD_SIZE, GameConstants.BOARD_SIZE);

    if (controller.getMoveService().mustTake()) {
      handleTakeClick(row, col);
    } else {
      handleNormalClick(row, col);
    }

    firstClick = true;
  }

  private void handleNormalClick(int row, int col) {
    if (controller.getMoveService().isLegalMove(row, col, firstClickCol, firstClickRow, firstClickColor)
        && controller.getBoardState().getPiece(row, col) == GameConstants.EMPTY) {

      controller.getMoveExecutor().executeNormalMove(firstClickRow, firstClickCol, row, col, firstClickColor, controller.getBoardState());
      controller.getPromotionService().promoteIfNeeded(row, col, firstClickColor);
      controller.getTurnManager().switchTurn();

      if (controller.getTurnManager().isCurrentPlayerBot()) {
        controller.executeBotTurn();
      }
    }
  }

  private void handleTakeClick(int row, int col) {
    if (!move.legalTakeMove(col, row, firstClickCol, firstClickRow, firstClickColor)) {
      return;
    }

    if (controller.getPromotionService().isQueen(firstClickColor)) {
      controller.getMoveExecutor().executeQueenCapture(firstClickRow, firstClickCol, row, col,
          controller.getTurnManager().getCurrentKingColor(), controller.getBoardState());
    } else {
      controller.getMoveExecutor().executeCapture(firstClickRow, firstClickCol, row, col,
          controller.getTurnManager().getCurrentColor(), controller.getBoardState());
    }

    controller.getPromotionService().promoteIfNeeded(row, col, firstClickColor);
    controller.getTurnManager().switchTurn();

    if (controller.getTurnManager().isCurrentPlayerBot()) {
      controller.executeBotTurn();
    }
  }
}