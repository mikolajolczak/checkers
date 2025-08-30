package checkers.src.main.java;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * The Game class is the entry point for the Checkers game.
 * It handles the initial color selection for the player and
 * initializes the game board, controller, and bot.
 */
public final class Game {

  /**
   * X coordinate for the color choice frame location.
   */
  private static final int COLOR_CHOICE_X = 700;

  /**
   * Y coordinate for the color choice frame location.
   */
  private static final int COLOR_CHOICE_Y = 400;

  /**
   * Width of the color choice frame.
   */
  private static final int COLOR_CHOICE_WIDTH = 350;

  /**
   * Height of the color choice frame.
   */
  private static final int COLOR_CHOICE_HEIGHT = 90;

  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private Game() {
  }

  /**
   * Main method to start the Checkers game.
   * It displays a color selection frame for the player, sets up the game board,
   * initializes the controller, and starts the bot.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(final String[] args) {
    JFrame colorChoiceFrame = new JFrame();
    colorChoiceFrame.setLocation(COLOR_CHOICE_X, COLOR_CHOICE_Y);

    Frame boardFrame = new Frame();
    Move move = new Move(boardFrame);
    BoardController controller = new BoardController(boardFrame, move);

    JButton red = new JButton("Red");
    JButton black = new JButton("Black");
    JLabel chooseColor = new JLabel("Choose your color");

    colorChoiceFrame.setLayout(new FlowLayout());
    colorChoiceFrame.setSize(COLOR_CHOICE_WIDTH, COLOR_CHOICE_HEIGHT);
    colorChoiceFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    colorChoiceFrame.add(chooseColor);
    colorChoiceFrame.add(red);
    colorChoiceFrame.add(black);

    red.addActionListener(e -> {
      controller.setBotsColor(Board.BLACK);
      controller.setBotsKingColor(Board.BLACK_KING);
      controller.setPlayersColor(Board.RED);
      controller.setPlayersKingColor(Board.RED_KING);
      controller.setCurrentColor();
      controller.setCurrentColorKing();
      colorChoiceFrame.dispose();
      boardFrame.setVisible(true);
    });

    black.addActionListener(e -> {
      controller.setBotsColor(Board.RED);
      controller.setBotsKingColor(Board.RED_KING);
      controller.setPlayersColor(Board.BLACK);
      controller.setPlayersKingColor(Board.BLACK_KING);
      colorChoiceFrame.dispose();
      controller.setCurrentColorKing();
      boardFrame.setVisible(true);
      controller.setCurrentColor();
    });

    colorChoiceFrame.setVisible(true);

    Bot bot = new Bot(boardFrame.getBoard(), move, controller);
    controller.setBot(bot);
  }
}
