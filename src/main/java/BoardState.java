package checkers.src.main.java;

public class BoardState {

  private final int[][] pieces;

  public BoardState() {
    pieces = new int[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
  }

  public BoardState(int[][] pieces) {
    this.pieces = new int[pieces.length][];
    for (int i = 0; i < pieces.length; i++) {
      this.pieces[i] = pieces[i].clone();
    }
  }

  public int getPiece(int row, int col) {
    return pieces[row][col];
  }

  public void setPiece(int row, int col, int piece) {
    pieces[row][col] = piece;
  }

  public BoardState copy() {
    return new BoardState(pieces);
  }

}
