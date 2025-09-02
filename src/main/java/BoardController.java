package checkers.src.main.java;


/**
 * Controller class for managing the Checkers game board, handling player
 * and bot moves, and interacting with the game frame.
 */
public final class BoardController {
  private final TurnManager turnManager;
  private final MoveService moveService;
  private final MoveExecutor moveExecutor;
  private final UIController uiController;
  private final BoardState boardState;
  private final PromotionService promotionService;

  public TurnManager getTurnManager() {
    return turnManager;
  }

  public MoveService getMoveService() {
    return moveService;
  }

  public MoveExecutor getMoveExecutor() {
    return moveExecutor;
  }

  public UIController getUiController() {
    return uiController;
  }

  public BoardState getBoardState() {
    return boardState;
  }

  public PromotionService getPromotionService() {
    return promotionService;
  }

  public BoardController(TurnManager turnManagerParam,
                         MoveService moveServiceParam,
                         MoveExecutor moveExecutorParam,
                         UIController uiControllerParam,
                         BoardState boardStateParam,
                         PromotionService promotionServiceParam) {
    turnManager = turnManagerParam;
    moveService = moveServiceParam;
    moveExecutor = moveExecutorParam;
    uiController = uiControllerParam;
    boardState = boardStateParam;
    promotionService = promotionServiceParam;
  }
}