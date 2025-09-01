package checkers.src.main.java;

public class MoveExecutor {

  public void executeNormalMove(int fromRow, int fromCol, int toRow, int toCol, int color, BoardState board) {
    board.setPiece(fromRow, fromCol, GameConstants.EMPTY);
    board.setPiece(toRow, toCol, color);
  }

  public void executeCapture(int fromRow, int fromCol, int toRow, int toCol, int color, BoardState board) {
    board.setPiece(fromRow, fromCol, GameConstants.EMPTY);
    board.setPiece(toRow, toCol, color);

    int capturedRow = (fromRow + toRow) / 2;
    int capturedCol = (fromCol + toCol) / 2;
    board.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }

  public void executeQueenCapture(int fromRow, int fromCol, int toRow, int toCol, int color, BoardState board) {
    board.setPiece(fromRow, fromCol, GameConstants.EMPTY);
    board.setPiece(toRow, toCol, color);

    int rowDir = Integer.signum(toRow - fromRow);
    int colDir = Integer.signum(toCol - fromCol);

    int capturedRow = toRow - rowDir;
    int capturedCol = toCol - colDir;
    board.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }
}