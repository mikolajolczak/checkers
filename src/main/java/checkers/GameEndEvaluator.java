package checkers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public record GameEndEvaluator(BoardState state, JFrame frame) {

  public void evaluateGameEnd() {
    int sumOfReds = 0;
    int sumOfBlacks = 0;

    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        if (state.getPiece(row, col) == GameConstants.RED
            || state.getPiece(row, col) == GameConstants.RED_KING) {
          sumOfReds++;
        } else if (state.getPiece(row, col) == GameConstants.BLACK
            || state.getPiece(row, col) == GameConstants.BLACK_KING) {
          sumOfBlacks++;
        }
      }
    }

    if (sumOfReds <= 0 || sumOfBlacks <= 0) {
      if (sumOfReds <= 0) {
        JOptionPane.showMessageDialog(frame, "Black won!");
        frame.dispose();
      }
      if (sumOfBlacks <= 0) {
        JOptionPane.showMessageDialog(frame, "Red won!");
        frame.dispose();
      }
    }
  }
}
