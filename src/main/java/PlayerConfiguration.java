package checkers.src.main.java;

public class PlayerConfiguration {
  private int humanColor;
  private int humanKingColor;
  private int botColor;
  private int botKingColor;


  public int getHumanColor() {
    return humanColor;
  }

  public void setHumanColor(final int humanColorParam) {
    humanColor = humanColorParam;
  }

  public int getHumanKingColor() {
    return humanKingColor;
  }

  public void setHumanKingColor(final int humanKingColorParam) {
    humanKingColor = humanKingColorParam;
  }

  public int getBotColor() {
    return botColor;
  }

  public void setBotColor(final int botColorParam) {
    botColor = botColorParam;
  }

  public int getBotKingColor() {
    return botKingColor;
  }

  public void setBotKingColor(final int botKingColorParam) {
    botKingColor = botKingColorParam;
  }
}
