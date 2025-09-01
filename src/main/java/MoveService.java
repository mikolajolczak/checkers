package checkers.src.main.java;

public class MoveService {
  private final Move move;
  private final TurnManager turnManager;
  private final BoardState boardState;

  public MoveService(Move moveParam, TurnManager turnManagerParam,
                     BoardState boardStateParam) {
    move = moveParam;
    turnManager = turnManagerParam;
    boardState = boardStateParam;
  }

  public boolean canSelectPiece(int row, int col, int currentColor, int currentKingColor, BoardState boardStateParam) {
    int value = boardStateParam.getPiece(row, col);
    boolean isCurrentPiece = value == currentColor || value == currentKingColor;
    return isCurrentPiece && (move.canIMove(col, row) || move.canITake(col, row, boardStateParam));
  }

  public boolean isLegalMove(int row, int col, int firstClickCol, int firstClickRow, int firstClickColor) {
    return move.isItLegalSecondClickMove(col, row, firstClickCol, firstClickRow, firstClickColor);
  }

  public boolean mustTake() {
    return move.checkAllPiecesPossibleTakes(turnManager.getCurrentColor(), turnManager.getCurrentKingColor(), boardState);
  }
}
