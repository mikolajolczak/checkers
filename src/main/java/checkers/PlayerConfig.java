package checkers;

/**
 * Configuration class for a checkers game player.
 *
 * <p>This class stores the colors used for both the human player and the bot,
 * including their standard pieces and king pieces. It provides getter and
 * setter methods to access and modify these colors.
 * </p>
 */
public class PlayerConfig {

  /**
   * Color of the human player's standard pieces.
   */
  private int humanColor;

  /**
   * Color of the human player's king pieces.
   */
  private int humanKingColor;

  /**
   * Color of the bot's standard pieces.
   */
  private int botColor;

  /**
   * Color of the bot's king pieces.
   */
  private int botKingColor;

  /**
   * Returns the color of the human player's standard pieces.
   *
   * @return the human player's color
   */
  public int getHumanColor() {
    return humanColor;
  }

  /**
   * Sets the color of the human player's standard pieces.
   *
   * @param humanColorParam the new color for the human player's pieces
   */
  public void setHumanColor(final int humanColorParam) {
    humanColor = humanColorParam;
  }

  /**
   * Returns the color of the human player's king pieces.
   *
   * @return the human king color
   */
  public int getHumanKingColor() {
    return humanKingColor;
  }

  /**
   * Sets the color of the human player's king pieces.
   *
   * @param humanKingColorParam the new color for the human king pieces
   */
  public void setHumanKingColor(final int humanKingColorParam) {
    humanKingColor = humanKingColorParam;
  }

  /**
   * Returns the color of the bot's standard pieces.
   *
   * @return the bot's color
   */
  public int getBotColor() {
    return botColor;
  }

  /**
   * Sets the color of the bot's standard pieces.
   *
   * @param botColorParam the new color for the bot's pieces
   */
  public void setBotColor(final int botColorParam) {
    botColor = botColorParam;
  }

  /**
   * Returns the color of the bot's king pieces.
   *
   * @return the bot king color
   */
  public int getBotKingColor() {
    return botKingColor;
  }

  /**
   * Sets the color of the bot's king pieces.
   *
   * @param botKingColorParam the new color for the bot king pieces
   */
  public void setBotKingColor(final int botKingColorParam) {
    botKingColor = botKingColorParam;
  }
}
