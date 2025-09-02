package checkers.src.main.java;

public final class MoveExecutor {

  public static void executeNormalMove(int fromRow, int fromCol, int toRow, int toCol,
                                int color, BoardState board) {
    board.setPiece(fromRow, fromCol, GameConstants.EMPTY);
    board.setPiece(toRow, toCol, color);
  }

  public static void executeCapture(int fromRow, int fromCol, int toRow, int toCol,
                             int color, BoardState board) {
    executeNormalMove(fromRow, fromCol, toRow, toCol, color, board);

    int capturedRow = (fromRow + toRow) / 2;
    int capturedCol = (fromCol + toCol) / 2;
    board.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }

  public static void executeQueenCapture(int fromRow, int fromCol, int toRow,
                                  int toCol, int color, BoardState board) {
    executeNormalMove(fromRow, fromCol, toRow, toCol, color, board);

    int rowDir = Integer.signum(toRow - fromRow);
    int colDir = Integer.signum(toCol - fromCol);

    int capturedRow = toRow - rowDir;
    int capturedCol = toCol - colDir;
    board.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }

  public static  void applyMoveToBoard(BotDecision decision, BoardState boardState,
                               PlayerConfiguration playerConfiguration) {
    switch (decision.moveType()) {
      case GameConstants.MOVE:
        int color = boardState.getPiece(decision.fromRow(), decision.fromCol());
        executeNormalMove(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(), color, boardState);
        break;
      case GameConstants.TAKE:
        executeCapture(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(),
            playerConfiguration.getBotColor(), boardState);
        break;
      case GameConstants.QUEEN_TAKE:
        executeQueenCapture(decision.fromRow(), decision.fromCol(),
            decision.toRow(), decision.toCol(),
            playerConfiguration.getBotKingColor(), boardState);
        break;
    }
  }
}