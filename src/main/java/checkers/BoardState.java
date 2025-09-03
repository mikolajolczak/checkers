package checkers;

public record BoardState(int[][] pieces) {
  public BoardState() {
    this(new int[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE]);
  }

  public BoardState(final int[][] pieces) {
    this.pieces = new int[pieces.length][];
    for (int i = 0; i < pieces.length; i++) {
      this.pieces[i] = pieces[i].clone();
    }
  }

  public int getPiece(final int row, final int col) {
    return pieces[row][col];
  }

  public void setPiece(final int row, final int col, final int piece) {
    pieces[row][col] = piece;
  }

  public BoardState copy() {
    return new BoardState(pieces);
  }
  public int[][] getPieces() {
    return pieces;
  }
  public int[] getPiecesRows(int i){
    return pieces[i];
  }
}
