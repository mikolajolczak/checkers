package checkers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Responsible for evaluating the end condition of a checkers game.
 *
 * <p>This class inspects the current {@link BoardState} to determine if one
 * of the players has won by having no remaining pieces for the opponent.
 * If the game has ended, it displays a message announcing the winner and
 * closes the game window.
 * </p>
 *
 * @param state the current state of the game board
 * @param frame the JFrame representing the game window
 */
public record GameEndEvaluator(BoardState state, JFrame frame) {
  /**
   * Evaluates the current state of the checkers game to determine if the
   * game has ended.
   *
   * <p>The method counts the number of red and black pieces on the board. If
   * either
   * player has no remaining pieces, it displays a message dialog announcing
   * the winner
   * and closes the game window.
   * </p>
   */
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
