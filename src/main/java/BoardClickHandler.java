package checkers.src.main.java;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class BoardClickHandler extends MouseAdapter {

  private final BotController botController;
  private final Move move;
  private final BoardPanel panel;
  private boolean firstClick = true;
  private int firstClickRow = GameConstants.BOARD_SIZE;
  private int firstClickCol = GameConstants.BOARD_SIZE;
  private int firstClickColor;
  private final MoveService moveService;
  private final TurnManager turnManager;
  private final UIController uiController;
  private final BoardState boardState;
  private final MoveExecutor moveExecutor;
  private final PromotionService promotionService;

  public BoardClickHandler(BotController botControllerParam, Move moveParam,
                           BoardPanel panelParam, MoveService moveServiceParam,
                           TurnManager turnManagerParam,
                           UIController uiControllerParam,
                           BoardState boardStateParam,
                           MoveExecutor moveExecutorParam,
                           PromotionService promotionServiceParam) {
    botController = botControllerParam;
    move = moveParam;
    panel = panelParam;
    moveService = moveServiceParam;
    turnManager = turnManagerParam;
    uiController = uiControllerParam;
    boardState = boardStateParam;
    moveExecutor = moveExecutorParam;
    promotionService = promotionServiceParam;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    int col = e.getX() / GameConstants.SQUARE_SIZE;
    int row = e.getY() / GameConstants.SQUARE_SIZE;
    if (!move.isValidPosition(row, col)) return;
    if (firstClick) {
      handleFirstClick(row, col);
    } else {
      handleSecondClick(row, col);

    }
    uiController.refreshBoard();
  }

  private void handleFirstClick(int row, int col) {
    if (!moveService.canSelectPiece(row, col, boardState)) {
      return;
    }

    firstClickRow = row;
    firstClickCol = col;
    firstClickColor = boardState.getPiece(row, col);
    panel.setSelectedColumn(col);
    panel.setSelectedRow(row);

    firstClick = false;
  }

  private void handleSecondClick(int row, int col) {
    panel.setSelectedColumn(GameConstants.BOARD_SIZE);
    panel.setSelectedRow(GameConstants.BOARD_SIZE);
    if (moveService.mustTake()) {
      handleTakeClick(row, col);
    } else {
      handleNormalClick(row, col);
    }

    firstClick = true;
  }

  private void handleNormalClick(int row, int col) {
    if (moveService.isLegalMove(row, col, firstClickCol, firstClickRow, firstClickColor)
        && boardState.getPiece(row, col) == GameConstants.EMPTY) {

      moveExecutor.executeNormalMove(firstClickRow, firstClickCol, row, col, firstClickColor, boardState);
      promotionService.promoteIfNeeded(row, col, firstClickColor);
      turnManager.switchTurn();

      if (turnManager.isCurrentPlayerBot()) {
        botController.executeTurn();
      }
    }
  }

  private void handleTakeClick(int row, int col) {
    if (!move.legalTakeMove(col, row, firstClickCol, firstClickRow, firstClickColor)) {
      return;
    }

    if (boardState.isItKing(firstClickColor)) {
      moveExecutor.executeQueenCapture(firstClickRow, firstClickCol, row, col,
          turnManager.getCurrentKingColor(), boardState);
    } else {
      moveExecutor.executeCapture(firstClickRow, firstClickCol, row, col,
          turnManager.getCurrentColor(), boardState);
    }

    promotionService.promoteIfNeeded(row, col, firstClickColor);
    turnManager.switchTurn();

    if (turnManager.isCurrentPlayerBot()) {
      botController.executeTurn();
    }
  }
}