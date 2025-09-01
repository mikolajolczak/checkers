package checkers.src.main.java;

public class MoveValidator {
  private final BoardState boardState;
  private final BoardController boardController;
  private final Move move;

  public MoveValidator(BoardState boardStateParam,
                       BoardController boardControllerParam, Move moveParam) {
    boardState = boardStateParam;
    boardController = boardControllerParam;
    move = moveParam;
  }

  public boolean canSelectPiece(int row, int col) {
    int value = boardState.getPiece(row, col);
    boolean isCurrentPiece = value == boardController.getCurrentColor()
        || value == boardController.getCurrentColorKing();
    return isCurrentPiece && (move.canIMove(col, row) || move.canITake(col,
        row));
  }
  public boolean isLegalNormalMove(int row, int col, int firstClickCol, int firstClickRow, int firstClickColor) {
    return move.isItLegalSecondClickMove(col, row, firstClickCol, firstClickRow,
        firstClickColor)
        && boardState.getPiece(row, col) == GameConstants.EMPTY;
  }
  public boolean mustTake() {
    return move.checkAllPiecesPossibleTakes(boardController.getCurrentColor(),
        boardController.getCurrentColorKing());
  }
}
