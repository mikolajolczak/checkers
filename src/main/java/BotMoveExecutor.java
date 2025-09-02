package checkers.src.main.java;

public class BotMoveExecutor {
  private final MoveExecutor moveExecutor;
  private final PromotionService promotionService;
  private final BoardState boardState;
  private final PlayerConfiguration playerConfig;

  public BotMoveExecutor(MoveExecutor moveExecutor,
                         PromotionService promotionService,
                         BoardState boardState,
                         PlayerConfiguration playerConfig) {
    this.moveExecutor = moveExecutor;
    this.promotionService = promotionService;
    this.boardState = boardState;
    this.playerConfig = playerConfig;
  }

  public void executeMove(BotDecision decision) {
    moveExecutor.applyMoveToBoard(decision, boardState, playerConfig);
    promotionService.promoteIfNeeded(decision.toRow(), decision.toCol(),
        playerConfig.getBotColor());
  }
}
