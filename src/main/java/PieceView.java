package checkers.src.main.java;

import java.awt.Color;

public class PieceView {

  private final int row;
  private final int col;
  private final int type;
  private final boolean selected;

  public PieceView(int row, int col, int type, boolean selected) {
    this.row = row;
    this.col = col;
    this.type = type;
    this.selected = selected;
  }

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

  public boolean isSelected() {
    return selected;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

}
