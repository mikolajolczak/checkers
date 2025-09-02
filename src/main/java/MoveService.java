package checkers.src.main.java;

import java.util.ArrayList;

public record MoveService(TurnManager turnManager, BoardState boardState,
                          MoveGenerator moveGenerator) {

  public boolean canSelectPiece(int row, int col, BoardState boardStateParam) {
    int value = boardStateParam.getPiece(row, col);
    boolean isCurrentPiece = value == turnManager.getCurrentColor()
        || value == turnManager.getCurrentKingColor();

    return isCurrentPiece &&
        (MoveRules.canMove(col, row, boardStateParam) ||
            CaptureRules.canCapture(col, row, boardStateParam));
  }

  public boolean isLegalMove(int row, int col, int firstClickCol,
                             int firstClickRow, int firstClickColor) {
    return MoveRules.isLegalMove(col, row, firstClickCol, firstClickRow,
        firstClickColor, boardState);
  }

  public boolean mustTake() {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        turnManager.getCurrentColor(),
        turnManager.getCurrentKingColor(),
        boardState);
  }

  public ArrayList<BotDecision> getPossibleMoves(BoardState boardState) {
    return moveGenerator.getPossibleMoves(boardState);
  }
}