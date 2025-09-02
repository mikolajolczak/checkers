package checkers.src.main.java;

public class PieceView {
  public final int row;
  public final int col;
  public final int type;
  public final boolean selected;

  public PieceView(int row, int col, int type, boolean selected) {
    this.row = row;
    this.col = col;
    this.type = type;
    this.selected = selected;
  }
}