package checkers;

/**
 * Utility class for validating capture moves in a checkers game.
 *
 * <p>This class provides a single static method to check whether a capture
 * move is legal according to the rules defined in {@link CaptureRules}.
 *
 * <p>This class cannot be instantiated.
 */
public final class CaptureValidator {

  private CaptureValidator() {
  }

  /**
   * Checks whether a capture move is valid for a given piece on the board.
   *
   * <p>This method delegates the validation logic to
   * {@link CaptureRules#isLegalCapture(int, int, int, int, int, BoardState)}.
   *
   * @param state      the current state of the board
   * @param fromRow    the row index of the piece to move
   * @param fromCol    the column index of the piece to move
   * @param toRow      the row index of the destination square
   * @param toCol      the column index of the destination square
   * @param pieceColor the color of the piece (e.g., 1 for white, 2 for black)
   * @return {@code true} if the capture move is legal according to the
   *     rules, {@code false} otherwise
   */
  public static boolean isValidCapture(final BoardState state,
                                       final int fromRow,
                                       final int fromCol, final int toRow,
                                       final int toCol,
                                       final int pieceColor) {
    return CaptureRules.isLegalCapture(toCol, toRow, fromCol, fromRow,
        pieceColor, state);
  }
}
