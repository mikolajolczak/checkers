package checkers.src.main.java;

public final class CaptureHandler {
  private final CaptureValidator validator;
  private final CaptureExecutor executor;
  private final TurnFlowManager turnFlow;
  private final BoardState boardState;

  public CaptureHandler(CaptureValidator validator,
                        CaptureExecutor executor,
                        TurnFlowManager turnFlow,
                        BoardState boardState) {
    this.validator = validator;
    this.executor = executor;
    this.turnFlow = turnFlow;
    this.boardState = boardState;
  }

  public void handleCapture(int fromRow, int fromCol, int toRow, int toCol) {
    int pieceColor = boardState.getPiece(fromRow, fromCol);
    if (!validator.isValidCapture(boardState, fromRow, fromCol, toRow, toCol, pieceColor)) {
      return;
    }
    executor.execute(boardState, fromRow, fromCol, toRow, toCol, pieceColor, turnFlow.getTurnManager());
    turnFlow.afterMove();
  }
}
