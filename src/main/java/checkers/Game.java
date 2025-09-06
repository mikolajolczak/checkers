package checkers;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * The main entry point for the Checkers game application.
 *
 * <p>This class is responsible for initializing and setting up the entire
 * game environment, including:
 * <ul>
 *   <li>Displaying a color selection dialog for the human player.</li>
 *   <li>Initializing the game board, pawns, and their starting positions.</li>
 *   <li>Setting up game services such as move validation, promotion
 *   handling, turn management, and AI bot logic.</li>
 *   <li>Registering event listeners for mouse and button interactions.</li>
 * </ul>
 *
 * <p>After the human player selects a color, the main game window becomes
 * visible. If the bot is assigned
 * to move first, its turn is executed automatically.
 *
 * <p>This class cannot be instantiated, as it only provides the
 * {@link #main(String[])} method.
 */
public final class Game {

  private Game() {
  }

  /**
   * The entry point of the Checkers game application.
   *
   * <p>This method initializes and sets up the game environment, including:
   * <ul>
   *   <li>The color selection frame for the player to choose their color.</li>
   *   <li>The game board, pieces, and their initial positions.</li>
   *   <li>All game services and controllers, including move validation,
   *       promotion handling, turn management, and bot AI.</li>
   *   <li>Event listeners for mouse input and button actions.</li>
   * </ul>
   *
   * <p>After the player selects a color, the main game window becomes visible,
   * and if the bot is set to move first, its turn is executed automatically.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(final String[] args) {
    JFrame colorChoiceFrame = new JFrame();
    colorChoiceFrame.setLocation(GameConstants.COLOR_CHOICE_X,
        GameConstants.COLOR_CHOICE_Y);
    colorChoiceFrame.setLayout(new FlowLayout());
    colorChoiceFrame.setSize(GameConstants.COLOR_CHOICE_WIDTH,
        GameConstants.COLOR_CHOICE_HEIGHT);
    colorChoiceFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    JLabel chooseColor = new JLabel("Choose your color");
    JButton redButton = new JButton("Red");
    JButton blackButton = new JButton("Black");

    colorChoiceFrame.add(chooseColor);
    colorChoiceFrame.add(redButton);
    colorChoiceFrame.add(blackButton);

    BoardState boardState = new BoardState();
    BoardInitializer.setUpPawns(boardState);
    SelectionState selectionState = new SelectionState();
    BoardPanel boardPanel = new BoardPanel();
    Frame boardFrame = new Frame(boardState, boardPanel);

    Runnable refreshBoardPanel = () ->
        boardPanel.setPiecesToDraw(
            BoardViewMapper.toPieceViews(boardState, selectionState));
    refreshBoardPanel.run();

    PromotionService promotionService = new PromotionService(boardState);

    PlayerConfig playerConfig = new PlayerConfig();
    TurnManager turnManager =
        new TurnManager(playerConfig, GameConstants.RED,
            GameConstants.RED_KING);

    UiController uiController = new UiController(boardFrame);
    uiController.setRefreshBoardPanel(refreshBoardPanel);

    MoveGenerator moveGenerator = new MoveGenerator(playerConfig);
    MoveService moveService =
        new MoveService(turnManager, boardState, moveGenerator);
    BotState botState = new BotState(boardState, playerConfig);
    BotAi bot = new BotAi(moveService);
    BotDecisionService botDecisionService =
        new BotDecisionService(bot, botState);
    BotMoveExecutor botMoveExecutor =
        new BotMoveExecutor(promotionService, boardState, playerConfig);
    BotUiHandler botUiHandler = new BotUiHandler(uiController, turnManager);
    BotController botController =
        new BotController(botDecisionService, botMoveExecutor, botUiHandler);

    CaptureExecutor captureExecutor = new CaptureExecutor(promotionService);
    TurnFlowManager turnFlowManager =
        new TurnFlowManager(turnManager, botController);
    CaptureHandler captureHandler =
        new CaptureHandler(captureExecutor, turnFlowManager, boardState);

    MovePerformer movePerformer =
        new MovePerformer(promotionService, boardState);
    MoveValidator moveValidator = new MoveValidator(moveService, boardState);
    MoveCoordinator moveCoordinator =
        new MoveCoordinator(movePerformer, moveValidator, uiController,
            turnManager, botController);

    MouseInputHandler mouseInputHandler =
        new MouseInputHandler(moveValidator, captureHandler, selectionState,
            uiController, moveCoordinator);
    ClickHandler clickHandler = new ClickHandler(mouseInputHandler);
    boardFrame.addBoardListener(clickHandler);

    boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    redButton.addActionListener(ignored -> {
      playerConfig.setBotColor(GameConstants.BLACK);
      playerConfig.setBotKingColor(GameConstants.BLACK_KING);
      playerConfig.setHumanColor(GameConstants.RED);
      playerConfig.setHumanKingColor(GameConstants.RED_KING);
      colorChoiceFrame.dispose();
      boardFrame.setVisible(true);
    });

    blackButton.addActionListener(ignored -> {
      playerConfig.setBotColor(GameConstants.RED);
      playerConfig.setBotKingColor(GameConstants.RED_KING);
      playerConfig.setHumanColor(GameConstants.BLACK);
      playerConfig.setHumanKingColor(GameConstants.BLACK_KING);
      colorChoiceFrame.dispose();
      boardFrame.setVisible(true);
      if (turnManager.isCurrentPlayerBot()) {
        botController.executeTurn();
      }
    });

    colorChoiceFrame.setVisible(true);
  }
}
