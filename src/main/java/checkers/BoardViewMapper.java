package checkers;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for mapping the current {@link BoardState}
 * and {@link SelectionState} into a collection of {@link PieceView}
 * objects.
 *
 * <p>This class provides helper methods for transforming internal
 * game state representations into view models suitable for rendering
 * the checkerboard in the user interface.
 * </p>
 *
 * <p>This class is not intended to be instantiated.
 * </p>
 */
public final class BoardViewMapper {

  private BoardViewMapper() {
  }

  /**
   * Converts the given {@link BoardState} and {@link SelectionState} into a
   * list
   * of {@link PieceView} objects representing each square on the board.
   *
   * @param boardState  the current state of the game board
   * @param selectState the current selection state
   * @return a list of {@link PieceView} objects for rendering
   */
  public static List<PieceView> toPieceViews(final BoardState boardState,
                                             final SelectionState selectState) {
    List<PieceView> pieces = new ArrayList<>();

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int type = boardState.getPiece(row, col);
        boolean selected = (row == selectState.getSelectedRow()
            & col == selectState.getSelectedColumn());
        pieces.add(new PieceView(row, col, type, selected));
      }
    }

    return pieces;
  }
}
