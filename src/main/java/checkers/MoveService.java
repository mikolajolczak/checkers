package checkers;

import java.util.ArrayList;

/**
 * Service class responsible for handling move-related logic in a checkers game.
 *
 * <p>This class provides methods to check whether a piece can be selected,
 * whether
 * a move is legal, if a capture is mandatory, and to generate all possible
 * moves
 * for a given board state. It acts as a bridge between the game rules, the
 * board
 * state, and the turn management system.
 * </p>
 *
 * <p>The primary responsibilities of this class include:
 * <ul>
 *   <li>Validating piece selection for the current player.</li>
 *   <li>Determining the legality of moves according to game rules.</li>
 *   <li>Checking mandatory capture rules.</li>
 *   <li>Providing possible moves for AI or move generation purposes.</li>
 * </ul>
 * </p>
 *
 * @param turnManager   the manager that tracks the current player's turn
 * @param boardState    the current state of the game board
 * @param moveGenerator the utility responsible for generating possible moves
 */
public record MoveService(TurnManager turnManager, BoardState boardState,
                          MoveGenerator moveGenerator) {
  /**
   * Checks whether a piece at the specified row and column can be selected
   * by the current player.
   *
   * @param row             the row index of the piece
   * @param col             the column index of the piece
   * @param boardStateParam the current state of the board
   * @return true if the piece belongs to the current player and has at least
   *     one legal move or capture, false otherwise
   */
  public boolean canSelectPiece(final int row, final int col,
                                final BoardState boardStateParam) {
    int value = boardStateParam.getPiece(row, col);
    boolean isCurrentPiece = value == turnManager.getCurrentColor()
        || value == turnManager.getCurrentKingColor();

    return isCurrentPiece && (MoveRules.canMove(col, row, boardStateParam)
        || CaptureRules.canCapture(col, row, boardStateParam));
  }

  /**
   * Determines whether a move from the initial position to the target position
   * is legal according to the game's rules.
   *
   * @param row             the target row index
   * @param col             the target column index
   * @param firstClickCol   the starting column index
   * @param firstClickRow   the starting row index
   * @param firstClickColor the color of the piece being moved
   * @return true if the move is legal, false otherwise
   */
  public boolean isLegalMove(final int row, final int col,
                             final int firstClickCol,
                             final int firstClickRow,
                             final int firstClickColor) {
    return MoveRules.isLegalMove(col, row, firstClickCol, firstClickRow,
        firstClickColor, boardState);
  }

  /**
   * Checks if the current player must perform a capture on this turn.
   *
   * @return true if a capture is mandatory, false otherwise
   */
  public boolean mustTake() {
    return CaptureRules.checkAllPiecesPossibleCaptures(
        turnManager.getCurrentColor(),
        turnManager.getCurrentKingColor(),
        boardState);
  }

  /**
   * Returns a list of all possible moves for the current board state,
   * typically used by the AI or for move generation.
   *
   * @param boardStateParam the board state to evaluate
   * @return an ArrayList of BotDecision objects representing possible moves
   */
  public ArrayList<BotDecision> getPossibleMoves(
      final BoardState boardStateParam) {
    return moveGenerator.getPossibleMoves(boardStateParam);
  }
}
