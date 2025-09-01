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

  public BoardClickHandler(BoardController controller, Move move,
                           BoardState boardState, Frame frame) {
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
      handleFirstClick(row, col);
    } else {
      handleSecondClick(row, col);
    }

    frame.getBoard().repaint();
  }

  private void handleFirstClick(int row, int col) {
    if (!canSelectPiece(row, col)) {
      return;
    }

    firstClickRow = row;
    firstClickCol = col;
    firstClickColor = boardState.getPiece(row, col);
    boardState.setSelected(row, col);
    firstClick = false;
  }

  private boolean canSelectPiece(int row, int col) {
    int value = boardState.getPiece(row, col);
    boolean isCurrentPiece = value == controller.getCurrentColor()
        || value == controller.getCurrentColorKing();
    return isCurrentPiece && (move.canIMove(col, row) || move.canITake(col,
        row));
  }

  private void handleSecondClick(int row, int col) {
    controller.clearChosenTile();

    if (mustTake()) {
      handleTakeClick(row, col);
    } else {
      handleNormalClick(row, col);
    }

    firstClick = true;
  }

  private boolean mustTake() {
    return move.checkAllPiecesPossibleTakes(controller.getCurrentColor(),
        controller.getCurrentColorKing());
  }

  private void handleNormalClick(int row, int col) {
    if (isLegalNormalMove(row, col)) {
      movePiece(row, col);
      promoteIfNeeded(row, col, firstClickColor);
      controller.setCurrentColor();
    }
  }

  private boolean isLegalNormalMove(int row, int col) {
    return move.isItLegalSecondClickMove(col, row, firstClickCol, firstClickRow,
        firstClickColor)
        && boardState.getPiece(row, col) == GameConstants.EMPTY;
  }

  private void movePiece(int row, int col) {
    boardState.setPiece(firstClickRow, firstClickCol, GameConstants.EMPTY);
    boardState.setPiece(row, col, firstClickColor);
  }

  private void handleTakeClick(int row, int col) {
    if (!move.legalTakeMove(col, row, firstClickCol, firstClickRow,
        firstClickColor)) {
      return;
    }

    if (isQueen(firstClickColor)) {
      attemptQueenTake(row, col);
    } else {
      attemptNormalTake(row, col);
    }

    promoteIfNeeded(row, col, firstClickColor);
    controller.setCurrentColor();
  }

  private boolean isQueen(int color) {
    return color == GameConstants.BLACK_KING || color == GameConstants.RED_KING;
  }

  private void attemptNormalTake(int row, int col) {
    controller.take(firstClickRow, firstClickCol, row, col,
        controller.getCurrentColor());
  }

  private void attemptQueenTake(int row, int col) {
    controller.queenTake(firstClickRow, firstClickCol, row, col,
        controller.getCurrentColorKing());
  }

  private void promoteIfNeeded(int row, int col, int color) {
    if (color == GameConstants.RED && row == 0) {
      boardState.setPiece(row, col, GameConstants.RED_KING);
    } else if (color == GameConstants.BLACK
        && row == GameConstants.LAST_ROW_INDEX) {
      boardState.setPiece(row, col, GameConstants.BLACK_KING);
    }
  }
}
