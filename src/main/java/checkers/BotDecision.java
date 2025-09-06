package checkers;

/**
 * Represents a decision made by a bot during a checkers game.
 *
 * <p>A decision consists of the starting position, the target position,
 * and the type of move performed.
 * </p>
 *
 * @param fromRow  the row index of the piece before the move
 * @param fromCol  the column index of the piece before the move
 * @param toRow    the row index of the destination square
 * @param toCol    the column index of the destination square
 * @param moveType the type of the move (e.g., normal move, capture, etc.)
 */
public record BotDecision(int fromRow, int fromCol, int toRow, int toCol,
                          int moveType) {
}
