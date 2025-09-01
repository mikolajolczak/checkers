package checkers.src.main.java;

public class PlayerConfiguration {
  private int humanColor;
  private int humanKingColor;
  private int botColor;
  private int botKingColor;


  public int getHumanColor() {
    return humanColor;
  }

  public int getHumanKingColor() {
    return humanKingColor;
  }

  public int getBotColor() {
    return botColor;
  }

  public int getBotKingColor() {
    return botKingColor;
  }

  public void setHumanColor(int humanColorParam) {
    humanColor = humanColorParam;
  }

  public void setHumanKingColor(int humanKingColorParam) {
    humanKingColor = humanKingColorParam;
  }

  public void setBotColor(int botColorParam) {
    botColor = botColorParam;
  }

  public void setBotKingColor(int botKingColorParam) {
    botKingColor = botKingColorParam;
  }
}