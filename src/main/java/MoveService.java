package checkers.src.main.java;

import java.util.ArrayList;

public class MoveService {

  private final MoveRules moveRules;
  private final CaptureRules captureRules;
  private final TurnManager turnManager;
  private final BoardState boardState;
  private final MoveGenerator moveGenerator;

  public MoveService(MoveRules moveRules, CaptureRules captureRules,
                     TurnManager turnManager, BoardState boardState,
                     MoveGenerator moveGenerator) {
    this.moveRules = moveRules;
    this.captureRules = captureRules;
    this.turnManager = turnManager;
    this.boardState = boardState;
    this.moveGenerator = moveGenerator;
  }

  public boolean canSelectPiece(int row, int col, BoardState boardStateParam) {
    int value = boardStateParam.getPiece(row, col);
    boolean isCurrentPiece = value == turnManager.getCurrentColor()
        || value == turnManager.getCurrentKingColor();

    return isCurrentPiece &&
        (moveRules.canMove(col, row, boardStateParam) ||
            captureRules.canCapture(col, row, boardStateParam));
  }

  public boolean isLegalMove(int row, int col, int firstClickCol,
                             int firstClickRow, int firstClickColor) {
    return moveRules.isLegalMove(col, row, firstClickCol, firstClickRow,
        firstClickColor, boardState);
  }

  public boolean mustTake() {
    return captureRules.checkAllPiecesPossibleCaptures(
        turnManager.getCurrentColor(),
        turnManager.getCurrentKingColor(),
        boardState);
  }

  public ArrayList<BotDecision> getPossibleMoves(BoardState boardState) {
    return moveGenerator.getPossibleMoves(boardState);
  }
}