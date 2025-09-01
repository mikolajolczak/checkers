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
    colorChoiceFrame.setLocation(GameConstants.COLOR_CHOICE_X,
        GameConstants.COLOR_CHOICE_Y);
    BoardState boardState = new BoardState();
    BoardPanel boardPanel = new BoardPanel(boardState);
    Frame boardFrame = new Frame(boardState, boardPanel);

    Move move = new Move(boardState);
    PromotionService promotionService = new PromotionService(boardState);
    PlayerConfiguration playerConfiguration = new PlayerConfiguration();
    TurnManager turnManager = new TurnManager(playerConfiguration, GameConstants.RED, GameConstants.RED_KING);
    MoveService moveService = new MoveService(move, turnManager, boardState);
    MoveExecutor moveExecutor = new MoveExecutor();
    UIController uiController = new UIController(boardFrame);
    MoveEvaluator moveEvaluator = new MoveEvaluator(move,playerConfiguration,moveExecutor);
    MoveGenerator moveGenerator = new MoveGenerator(move, playerConfiguration);
    Bot bot = new Bot(boardState, moveGenerator, moveEvaluator);
    BoardController controller =
        new BoardController(playerConfiguration, bot, turnManager,
            moveService, moveExecutor, uiController, boardState,
            promotionService);

    BoardClickHandler clickHandler = new BoardClickHandler(controller, move);
    boardFrame.addBoardListener(clickHandler);
    JButton red = new JButton("Red");
    JButton black = new JButton("Black");
    JLabel chooseColor = new JLabel("Choose your color");

    colorChoiceFrame.setLayout(new FlowLayout());
    colorChoiceFrame.setSize(GameConstants.COLOR_CHOICE_WIDTH,
        GameConstants.COLOR_CHOICE_HEIGHT);
    colorChoiceFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    colorChoiceFrame.add(chooseColor);
    colorChoiceFrame.add(red);
    colorChoiceFrame.add(black);

    red.addActionListener(_ -> {
      playerConfiguration.setBotColor(GameConstants.BLACK);
      playerConfiguration.setBotKingColor(GameConstants.BLACK_KING);
      playerConfiguration.setHumanColor(GameConstants.RED);
      playerConfiguration.setHumanKingColor(GameConstants.RED_KING);
      colorChoiceFrame.dispose();
      boardFrame.setVisible(true);
    });

    black.addActionListener(_ -> {
      playerConfiguration.setBotColor(GameConstants.RED);
      playerConfiguration.setBotKingColor(GameConstants.RED_KING);
      playerConfiguration.setHumanColor(GameConstants.BLACK);
      playerConfiguration.setHumanKingColor(GameConstants.BLACK_KING);
      colorChoiceFrame.dispose();
      boardFrame.setVisible(true);
      if (turnManager.isCurrentPlayerBot()) {
        controller.executeBotTurn();
      }
    });

    colorChoiceFrame.setVisible(true);

  }
}
