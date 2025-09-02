package checkers.src.main.java;

import java.util.ArrayList;
import java.util.List;

public class BoardViewMapper {

  public static List<PieceView> toPieceViews(BoardState boardState,
                                             SelectionState selectionState) {
    List<PieceView> pieces = new ArrayList<>();

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int type = boardState.getPiece(row, col);
        boolean selected = (row == selectionState.getSelectedRow()
            && col == selectionState.getSelectedColumn());
        pieces.add(new PieceView(row, col, type, selected));
      }
    }

    return pieces;
  }
}
