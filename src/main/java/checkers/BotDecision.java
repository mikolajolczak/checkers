package checkers;

public record BotDecision(int fromRow, int fromCol, int toRow, int toCol,
                          int moveType) {
}
