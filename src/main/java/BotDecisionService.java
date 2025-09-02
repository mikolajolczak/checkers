package checkers.src.main.java;

public record BotDecisionService(BotAI bot, BotState botState) {

  public BotDecision getBotDecision() {

    return bot.makeMove(botState);
  }
}
