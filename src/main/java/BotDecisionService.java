package checkers.src.main.java;

public class BotDecisionService {
  private final Bot bot;

  public BotDecisionService(Bot bot) {
    this.bot = bot;
  }

  public BotDecision getBotDecision() {
    return bot.makeMove();
  }
}
