package checkers.src.main.java;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class BoardClickHandler extends MouseAdapter {

  private final BoardController controller;
  private final Move move;
  private final BoardState boardState;
  private final Frame frame;

  private boolean firstClick = true;
  private int firstClickRow = GameConstants.BOARD_SIZE;
  private int firstClickCol = GameConstants.BOARD_SIZE;
  private int firstClickColor;

  public BoardClickHandler(BoardController controller, Move move, BoardState boardState, Frame frame) {
    this.controller = controller;
    this.move = move;
    this.boardState = boardState;
    this.frame = frame;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    int col = e.getX() / GameConstants.SQUARE_SIZE;
    int row = e.getY() / GameConstants.SQUARE_SIZE;

    if (firstClick) {
      if (move.canIMove(col, row) || move.canITake(col, row)) {
        int value = boardState.getPiece(row, col);
        if (value == controller.getCurrentColor() || value == controller.getCurrentColorKing()) {
          boardState.setSelected(row, col);
          frame.getBoard().repaint();
          firstClickRow = row;
          firstClickCol = col;
          firstClickColor = value;
          firstClick = false;
        }
      }
    } else {
      controller.clearChosenTile();

      if (!move.checkAllPiecesPossibleTakes(controller.getCurrentColor(),
          controller.getCurrentColorKing())) {
        if (move.isItLegalSecondClickMove(col, row, firstClickCol, firstClickRow, firstClickColor)) {
          if (boardState.getPiece(row, col) == GameConstants.EMPTY) {
            boardState.setPiece(firstClickRow, firstClickCol, GameConstants.EMPTY);
            boardState.setPiece(row, col, firstClickColor);
            promoteIfNeeded(row, col, firstClickColor);
            controller.setCurrentColor();
          }
        }
      } else {
        if (move.legalTakeMove(col, row, firstClickCol, firstClickRow, firstClickColor)) {
          if (firstClickColor == GameConstants.BLACK || firstClickColor == GameConstants.RED) {
            controller.take(firstClickRow, firstClickCol, row, col, controller.getCurrentColor());
          } else {
            controller.queenTake(firstClickRow, firstClickCol, row, col, controller.getCurrentColorKing());
          }
          promoteIfNeeded(row, col, firstClickColor);
          controller.setCurrentColor();
        }
      }

      firstClick = true;
      frame.getBoard().repaint();
    }
  }

  private void promoteIfNeeded(int row, int col, int color) {
    if (color == GameConstants.RED && row == 0) {
      boardState.setPiece(row, col, GameConstants.RED_KING);
    } else if (color == GameConstants.BLACK && row == GameConstants.LAST_ROW_INDEX) {
      boardState.setPiece(row, col, GameConstants.BLACK_KING);
    }
  }
}