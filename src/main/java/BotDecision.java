package checkers.src.main.java;

public record BotDecision(int fromRow, int fromCol, int toRow, int toCol, int moveType) {
  public BotDecision(int[] moveArray) {
    this(moveArray[0], moveArray[1], moveArray[2], moveArray[3], moveArray[4]);
  }
}
