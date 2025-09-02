package checkers;

public final class CaptureValidator {

  private CaptureValidator() {
  }

  public static boolean isValidCapture(final BoardState state,
                                       final int fromRow,
                                       final int fromCol, final int toRow,
                                       final int toCol,
                                       final int pieceColor) {
    return CaptureRules.isLegalCapture(toCol, toRow, fromCol, fromRow,
        pieceColor, state);
  }
}
