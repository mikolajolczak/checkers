package checkers.src.main.java;

import java.awt.Color;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Frame extends JFrame {


  private final BoardPanel board;

  private final BoardState state;

  public Frame(BoardState state, BoardPanel board) {
    this.setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
    this.setLocationRelativeTo(null);
    this.setBackground(Color.LIGHT_GRAY);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.board = board;
    this.state = state;
    this.add(board);
  }

  public void addBoardListener(final MouseListener listenForClick) {
    board.addMouseListener(listenForClick);
  }

  public void isGameFinished() {
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
        JOptionPane.showMessageDialog(this, "Black won!");
        this.dispose();
      }
      if (sumOfBlacks <= 0) {
        JOptionPane.showMessageDialog(this, "Red won!");
        this.dispose();
      }
    }

  }
}
