package checkers;

import java.util.ArrayList;

public record MoveService(TurnManager turnManager, BoardState boardState,
                          MoveGenerator moveGenerator) {

  public boolean canSelectPiece(final int row, final int col,
                                final BoardState boardStateParam) {
    int value = boardStateParam.getPiece(row, col);
    boolean isCurrentPiece = value == turnManager.getCurrentColor()
        || value == turnManager.getCurrentKingColor();

    return isCurrentPiece && (MoveRules.canMove(col, row, boardStateParam)
        || CaptureRules.canCapture(col, row, boardStateParam));
  }

  public boolean isLegalMove(final int row, final int col,
                             final int firstClickCol,
                             final int firstClickRow,
                             final int firstClickColor) {
    return MoveRules.isLegalMove(col, row, firstClickCol, firstClickRow,
        firstClickColor, boardState);
  }

  public boolean mustTake() {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        turnManager.getCurrentColor(),
        turnManager.getCurrentKingColor(),
        boardState);
  }

  public ArrayList<BotDecision> getPossibleMoves(
      final BoardState boardStateParam) {
    return moveGenerator.getPossibleMoves(boardStateParam);
  }
}