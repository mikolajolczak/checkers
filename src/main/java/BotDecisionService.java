package checkers.src.main.java;

public record BotDecisionService(Bot bot) {

  public BotDecision getBotDecision() {
    return bot.makeMove();
  }
}
