package checkers.src.main.java;

public class MoveExecutor {
  private final BoardState board;
  private final BoardController boardController;

  public MoveExecutor(BoardState boardParam,
                      BoardController boardControllerParam) {
    board = boardParam;
    boardController = boardControllerParam;
  }

  public void movePiece(int row, int col, int firstClickCol, int firstClickRow, int firstClickColor) {
    board.setPiece(firstClickRow, firstClickCol, GameConstants.EMPTY);
    board.setPiece(row, col, firstClickColor);
  }
  public void attemptNormalTake(int row, int col, int firstClickCol, int firstClickRow) {
    boardController.take(firstClickRow, firstClickCol, row, col,
        boardController.getCurrentColor());
  }

  public void attemptQueenTake(int row, int col, int firstClickCol, int firstClickRow) {
    boardController.queenTake(firstClickRow, firstClickCol, row, col,
        boardController.getCurrentColorKing());
  }
}
