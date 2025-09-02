package checkers.src.main.java;

public class CaptureValidator {
  private final CaptureRules captureRules;

  public CaptureValidator(CaptureRules captureRules) {
    this.captureRules = captureRules;
  }

  public boolean isValidCapture(BoardState state, int fromRow, int fromCol, int toRow, int toCol, int pieceColor) {
    return captureRules.isLegalCapture(toCol, toRow, fromCol, fromRow, pieceColor, state);
  }
}
