package checkers.src.main.java;

public final  class CaptureValidator {

  public static boolean isValidCapture(BoardState state, int fromRow, int fromCol, int toRow, int toCol, int pieceColor) {
    return CaptureRules.isLegalCapture(toCol, toRow, fromCol, fromRow, pieceColor, state);
  }
}
