package checkers;

import java.awt.Color;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

/**
 * Represents the main game window for a checkers game.
 *
 * <p>This frame contains the board panel where game pieces are displayed
 * and handles user interactions through mouse events. It also evaluates
 * the end of the game using a {@link GameEndEvaluator}.
 * </p>
 *
 * <p>Responsibilities of this class include initializing the window,
 * adding the board panel, registering mouse listeners for user input,
 * and checking for game termination conditions.
 * </p>
 */
public final class Frame extends JFrame {
  /**
   * The board panel where the game pieces are displayed.
   */
  private final BoardPanel board;
  /**
   * Evaluates whether the game has ended.
   */
  private final GameEndEvaluator gameEndEvaluator;

  /**
   * Constructs a new game frame with the specified board state and board panel.
   *
   * @param state      the current state of the board; must not be null
   * @param boardParam the panel displaying the board
   * @throws NullPointerException if the state is null
   */
  public Frame(final BoardState state, final BoardPanel boardParam) {
    if (state == null) {
      throw new NullPointerException("BoardState is null");
    }
    this.setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
    this.setLocationRelativeTo(null);
    this.setBackground(Color.LIGHT_GRAY);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.board = boardParam;
    this.add(boardParam);
    this.gameEndEvaluator = new GameEndEvaluator(state, this);
  }

  /**
   * Adds a mouse listener to the board panel to handle click events.
   *
   * @param listenForClick the mouse listener to add
   */
  public void addBoardListener(final MouseListener listenForClick) {
    board.addMouseListener(listenForClick);
  }

  /**
   * Checks whether the game has finished and evaluates the end of the game.
   */
  public void checkGameFinished() {
    gameEndEvaluator.evaluateGameEnd();
  }
}

