package checkers.src.main.java;

public record BotDecision(int fromRow, int fromCol, int toRow, int toCol, int moveType) {
  public BotDecision(BotDecision botDecision) {
    this(botDecision.fromRow, botDecision.fromCol, botDecision.toRow, botDecision.toCol, botDecision.moveType);
  }
}
