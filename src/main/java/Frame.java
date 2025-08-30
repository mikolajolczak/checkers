package checkers.src.main.java;

import java.awt.Color;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Main frame for the Checkers game.
 * Holds the board and manages basic game functionality.
 */
public class Frame extends JFrame {

  /**
   * Width of the window in pixels.
   */
  private static final int WINDOW_WIDTH = 416;

  /**
   * Height of the window in pixels.
   */
  private static final int WINDOW_HEIGHT = 436;

  /**
   * The main board for the game.
   */
  private final Board board = new Board();

  /**
   * Returns the board associated with this frame.
   *
   * @return the game board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Adds a mouse listener to the board.
   *
   * @param listenForClick the mouse listener to add
   */
  public void addBoardListener(final MouseListener listenForClick) {
    board.addMouseListener(listenForClick);
  }

  /**
   * Constructor: initializes the frame and adds the board.
   */
  public Frame() {
    this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    this.setLocationRelativeTo(null);
    this.setBackground(Color.LIGHT_GRAY);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(board);
  }

  /**
   * Checks if the game has finished and shows a message if a player has won.
   */
  public void isGameFinished() {
    int sumOfReds = 0;
    int sumOfBlacks = 0;
    for (int row = 0; row < Board.BOARD_SIZE; row++) {
      for (int col = 0; col < Board.BOARD_SIZE; col++) {
        if (board.getPieces()[row][col] == Board.RED
            || board.getPieces()[row][col] == Board.RED_KING) {
          sumOfReds++;
        } else if (board.getPieces()[row][col] == Board.BLACK
            || board.getPieces()[row][col] == Board.BLACK_KING) {
          sumOfBlacks++;
        }
      }
    }
    if (sumOfReds <= 0) {
      JOptionPane.showMessageDialog(this, "Black won!");
    } else if (sumOfBlacks <= 0) {
      JOptionPane.showMessageDialog(this, "Red won!");
    }
  }
}
