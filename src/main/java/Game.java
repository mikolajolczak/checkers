package checkers.src.main.java;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public final class Game {

  private Game() {
  }

  public static void main(final String[] args) {
    JFrame colorChoiceFrame = new JFrame();
    colorChoiceFrame.setLocation(GameConstants.COLOR_CHOICE_X, GameConstants.COLOR_CHOICE_Y);
    colorChoiceFrame.setLayout(new FlowLayout());
    colorChoiceFrame.setSize(GameConstants.COLOR_CHOICE_WIDTH, GameConstants.COLOR_CHOICE_HEIGHT);
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
        boardPanel.setPiecesToDraw(BoardViewMapper.toPieceViews(boardState, selectionState));
    refreshBoardPanel.run();

    PromotionService promotionService = new PromotionService(boardState);

    PlayerConfiguration playerConfiguration = new PlayerConfiguration();
    TurnManager turnManager = new TurnManager(playerConfiguration, GameConstants.RED, GameConstants.RED_KING);

    UIController uiController = new UIController(boardFrame);
    uiController.setRefreshBoardPanel(refreshBoardPanel);

    MoveGenerator moveGenerator = new MoveGenerator(playerConfiguration);
    MoveService moveService = new MoveService(turnManager, boardState, moveGenerator);
    BotState botState = new BotState(boardState, playerConfiguration);
    BotAI bot = new BotAI(moveService);
    BotDecisionService botDecisionService = new BotDecisionService(bot, botState);
    BotMoveExecutor botMoveExecutor = new BotMoveExecutor(promotionService, boardState, playerConfiguration);
    BotUIHandler botUIHandler = new BotUIHandler(uiController, turnManager);
    BotController botController = new BotController(botDecisionService, botMoveExecutor, botUIHandler);

    CaptureExecutor captureExecutor = new CaptureExecutor(promotionService);
    TurnFlowManager turnFlowManager = new TurnFlowManager(turnManager, botController);
    CaptureHandler captureHandler = new CaptureHandler(captureExecutor, turnFlowManager, boardState);

    MovePerformer movePerformer = new MovePerformer(promotionService, boardState);
    MoveValidator moveValidator = new MoveValidator(moveService, boardState);
    MoveCoordinator moveCoordinator = new MoveCoordinator(movePerformer, moveValidator, uiController, turnManager, botController);

    MouseInputHandler mouseInputHandler = new MouseInputHandler(moveValidator, captureHandler, selectionState, uiController, moveCoordinator);
    ClickHandler clickHandler = new ClickHandler(mouseInputHandler);
    boardFrame.addBoardListener(clickHandler);

    boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    redButton.addActionListener(_ -> {
      playerConfiguration.setBotColor(GameConstants.BLACK);
      playerConfiguration.setBotKingColor(GameConstants.BLACK_KING);
      playerConfiguration.setHumanColor(GameConstants.RED);
      playerConfiguration.setHumanKingColor(GameConstants.RED_KING);
      colorChoiceFrame.dispose();
      boardFrame.setVisible(true);
    });

    blackButton.addActionListener(_ -> {
      playerConfiguration.setBotColor(GameConstants.RED);
      playerConfiguration.setBotKingColor(GameConstants.RED_KING);
      playerConfiguration.setHumanColor(GameConstants.BLACK);
      playerConfiguration.setHumanKingColor(GameConstants.BLACK_KING);
      colorChoiceFrame.dispose();
      boardFrame.setVisible(true);
      if (turnManager.isCurrentPlayerBot()) {
        botController.executeTurn();
      }
    });

    colorChoiceFrame.setVisible(true);
  }
}
