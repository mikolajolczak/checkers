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
    colorChoiceFrame.setLocation(GameConstants.COLOR_CHOICE_X, GameConstants.COLOR_CHOICE_Y);
    BoardState boardState = new BoardState();
    BoardPanel boardPanel = new BoardPanel(boardState);
    Frame boardFrame = new Frame(boardState, boardPanel);
    Move move = new Move(boardState);
    BoardController controller = new BoardController(boardFrame, move, boardState);

    JButton red = new JButton("Red");
    JButton black = new JButton("Black");
    JLabel chooseColor = new JLabel("Choose your color");

    colorChoiceFrame.setLayout(new FlowLayout());
    colorChoiceFrame.setSize(GameConstants.COLOR_CHOICE_WIDTH, GameConstants.COLOR_CHOICE_HEIGHT);
    colorChoiceFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    colorChoiceFrame.add(chooseColor);
    colorChoiceFrame.add(red);
    colorChoiceFrame.add(black);

    red.addActionListener(e -> {
      controller.setBotsColor(GameConstants.BLACK);
      controller.setBotsKingColor(GameConstants.BLACK_KING);
      controller.setPlayersColor(GameConstants.RED);
      controller.setPlayersKingColor(GameConstants.RED_KING);
      controller.setCurrentColor();
      controller.setCurrentColorKing();
      colorChoiceFrame.dispose();
      boardFrame.setVisible(true);
    });

    black.addActionListener(e -> {
      controller.setBotsColor(GameConstants.RED);
      controller.setBotsKingColor(GameConstants.RED_KING);
      controller.setPlayersColor(GameConstants.BLACK);
      controller.setPlayersKingColor(GameConstants.BLACK_KING);
      colorChoiceFrame.dispose();
      controller.setCurrentColorKing();
      boardFrame.setVisible(true);
      controller.setCurrentColor();
    });

    colorChoiceFrame.setVisible(true);

    Bot bot = new Bot(boardFrame.getBoard(), move, controller, boardState);
    controller.setBot(bot);
  }
}
