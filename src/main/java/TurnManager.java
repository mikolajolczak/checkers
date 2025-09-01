package checkers.src.main.java;

public class TurnManager {
  private int currentColor;
  private int currentKingColor;
  private final PlayerConfiguration playerConfig;

  public TurnManager(PlayerConfiguration playerConfigParam, int currentColorParam, int  currentKingColorParam) {
    currentColor = currentColorParam;
    currentKingColor = currentKingColorParam;
    playerConfig = playerConfigParam;
  }

  public void switchTurn() {
    if (currentColor == GameConstants.RED) {
      currentColor = GameConstants.BLACK;
      currentKingColor = GameConstants.BLACK_KING;
    } else {
      currentColor = GameConstants.RED;
      currentKingColor = GameConstants.RED_KING;
    }
  }

  public boolean isCurrentPlayerBot() {
    return currentColor == playerConfig.getBotColor();
  }

  public int getCurrentColor() {
    return currentColor;
  }


  public int getCurrentKingColor() {
    return currentKingColor;
  }

}