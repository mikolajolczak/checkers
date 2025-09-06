package checkers;

/**
 * Manages the turn order and player states in a checkers game.
 *
 * <p>This class keeps track of which player's turn it is, including the
 * color of their regular pieces and kings. It also provides functionality
 * to switch turns and determine if the current player is controlled by a bot.
 * </p>
 */
public final class TurnManager {

  /**
   * Configuration containing information about the players, including
   * which color is controlled by a bot.
   */
  private final PlayerConfig playerConfig;

  /**
   * The color of the current player.
   */
  private int currentColor;

  /**
   * The color representing the king of the current player.
   */
  private int currentKingColor;

  /**
   * Constructs a TurnManager with the specified player configuration,
   * initial player color, and initial king color.
   *
   * @param playerConfigParam     Configuration of the players.
   * @param currentColorParam     The starting color of the current player.
   * @param currentKingColorParam The starting king color of the current player.
   */
  public TurnManager(final PlayerConfig playerConfigParam,
                     final int currentColorParam,
                     final int currentKingColorParam) {
    currentColor = currentColorParam;
    currentKingColor = currentKingColorParam;
    playerConfig = playerConfigParam;
  }

  /**
   * Switches the turn to the next player. Updates both the regular piece
   * color and the king color for the new current player.
   */
  public void switchTurn() {
    if (currentColor == GameConstants.RED) {
      currentColor = GameConstants.BLACK;
      currentKingColor = GameConstants.BLACK_KING;
    } else {
      currentColor = GameConstants.RED;
      currentKingColor = GameConstants.RED_KING;
    }
  }

  /**
   * Determines if the current player is controlled by a bot.
   *
   * @return {@code true} if the current player is a bot; {@code false}
   *     otherwise.
   */
  public boolean isCurrentPlayerBot() {
    return currentColor == playerConfig.getBotColor();
  }

  /**
   * Returns the color of the current player.
   *
   * @return The current player's color.
   */
  public int getCurrentColor() {
    return currentColor;
  }

  /**
   * Returns the king color of the current player.
   *
   * @return The current player's king color.
   */
  public int getCurrentKingColor() {
    return currentKingColor;
  }

}
