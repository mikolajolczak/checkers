package checkers.src.main.java;

public record BotMoveExecutor(PromotionService promotionService,
                              BoardState boardState,
                              PlayerConfiguration playerConfig) {

  public void executeMove(final BotDecision decision) {
    MoveExecutor.applyMoveToBoard(decision, boardState, playerConfig);
    promotionService.promoteIfNeeded(decision.toRow(), decision.toCol(),
        playerConfig.getBotColor());
  }
}
