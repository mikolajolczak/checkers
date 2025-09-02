package checkers.src.main.java;

import java.awt.Color;

public record PieceView(int row, int col, int type, boolean selected) {

  public boolean isEmpty() {
    return type == GameConstants.EMPTY;
  }

  public boolean isKing() {
    return type == GameConstants.RED_KING || type == GameConstants.BLACK_KING;
  }

  public Color getColor() {
    return switch (type) {
      case GameConstants.RED, GameConstants.RED_KING -> Color.RED;
      case GameConstants.BLACK, GameConstants.BLACK_KING -> Color.BLACK;
      default -> null;
    };
  }

}
