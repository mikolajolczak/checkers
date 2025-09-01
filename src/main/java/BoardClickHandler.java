package checkers.src.main.java;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class BoardClickHandler extends MouseAdapter {

  private final BoardController controller;
  private final Move move;
  private final BoardState boardState;
  private final Frame frame;
  private final MoveValidator moveValidator;
  private final MoveExecutor moveExecutor;
  private final PromotionService promotionService;

  private boolean firstClick = true;
  private int firstClickRow = GameConstants.BOARD_SIZE;
  private int firstClickCol = GameConstants.BOARD_SIZE;
  private int firstClickColor;

  public BoardClickHandler(BoardController controller, Move move,
                           BoardState boardState, Frame frame,
                           MoveValidator moveValidatorParam,
                           MoveExecutor moveExecutorParam,
                           PromotionService promotionServiceParam) {
    this.controller = controller;
    this.move = move;
    this.boardState = boardState;
    this.frame = frame;
    moveValidator = moveValidatorParam;
    moveExecutor = moveExecutorParam;
    promotionService = promotionServiceParam;
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
    if (!moveValidator.canSelectPiece(row, col)) {
      return;
    }

    firstClickRow = row;
    firstClickCol = col;
    firstClickColor = boardState.getPiece(row, col);
    boardState.setSelected(row, col);
    firstClick = false;
  }

  private void handleSecondClick(int row, int col) {
    controller.clearChosenTile();

    if (moveValidator.mustTake()) {
      handleTakeClick(row, col);
    } else {
      handleNormalClick(row, col);
    }

    firstClick = true;
  }

  private void handleNormalClick(int row, int col) {
    if (moveValidator.isLegalNormalMove(row, col, firstClickCol, firstClickRow, firstClickColor)) {
      moveExecutor.movePiece(row, col, firstClickCol, firstClickRow, firstClickColor);
      promotionService.promoteIfNeeded(row, col, firstClickColor);
      controller.setCurrentColor();
    }
  }

  private void handleTakeClick(int row, int col) {
    if (!move.legalTakeMove(col, row, firstClickCol, firstClickRow,
        firstClickColor)) {
      return;
    }

    if (promotionService.isQueen(firstClickColor)) {
      moveExecutor.attemptQueenTake(row, col, firstClickCol, firstClickRow);
    } else {
      moveExecutor.attemptNormalTake(row, col, firstClickCol, firstClickRow);
    }

    promotionService.promoteIfNeeded(row, col, firstClickColor);
    controller.setCurrentColor();
  }
}
