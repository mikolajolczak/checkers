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
    colorChoiceFrame.setLocation(GameConstants.COLOR_CHOICE_X,
        GameConstants.COLOR_CHOICE_Y);
    BoardState boardState = new BoardState();
    SelectionState selectionState = new SelectionState();
    BoardPanel boardPanel = new BoardPanel();
    Frame boardFrame = new Frame(boardState, boardPanel);
    Runnable refreshBoardPanel = () ->{
      boardPanel.setPiecesToDraw(boardState.toPieceViews(selectionState));
    };
    refreshBoardPanel.run();
    PromotionService promotionService = new PromotionService(boardState);
    Move move = new Move(boardState);
    PlayerConfiguration playerConfiguration = new PlayerConfiguration();
    TurnManager turnManager = new TurnManager(playerConfiguration, GameConstants.RED, GameConstants.RED_KING);

    MoveExecutor moveExecutor = new MoveExecutor();
    UIController uiController = new UIController(boardFrame);
    uiController.setRefreshBoardPanel(refreshBoardPanel);
    MoveEvaluator moveEvaluator = new MoveEvaluator(move,playerConfiguration,moveExecutor);
    MoveGenerator moveGenerator = new MoveGenerator(move, playerConfiguration, boardState);
    MoveService moveService = new MoveService(move, turnManager, boardState, moveGenerator);
    Bot bot = new Bot(boardState, moveService, moveEvaluator);
    BotDecisionService botDecisionService = new BotDecisionService(bot);
    BotMoveExecutor botMoveExecutor = new BotMoveExecutor(moveExecutor, promotionService, boardState, playerConfiguration);
    BotUIHandler botUIHandler = new BotUIHandler(uiController, turnManager);
    BotController botController = new BotController(botDecisionService, botMoveExecutor, botUIHandler);
    CaptureHandler captureHandler = new CaptureHandler(moveExecutor, promotionService, turnManager, boardState, botController, move);
    MoveHandler moveHandler = new MoveHandler(moveExecutor, promotionService, turnManager, boardState, botController, moveService, uiController);
    ClickHandler clickHandler = new ClickHandler(moveHandler,
        captureHandler, selectionState);
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
        botController.executeTurn();
      }
    });

    colorChoiceFrame.setVisible(true);

  }
}
