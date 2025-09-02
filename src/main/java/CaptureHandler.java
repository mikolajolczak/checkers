package checkers.src.main.java;

public record CaptureHandler(CaptureExecutor executor, TurnFlowManager turnFlow,
                             BoardState boardState) {

  public void handleCapture(int fromRow, int fromCol, int toRow, int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    if (!CaptureValidator.isValidCapture(boardState, fromRow, fromCol, toRow,
        toCol, pieceColor)) {
      return;
    }
    executor.execute(boardState, fromRow, fromCol, toRow, toCol, pieceColor,
        turnFlow.turnManager());
    turnFlow.afterMove();
  }
}
