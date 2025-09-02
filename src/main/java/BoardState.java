package checkers.src.main.java;

import java.util.ArrayList;
import java.util.List;

public class BoardState {
  public BoardState(int[][] pieces) {
    this.pieces = new int[pieces.length][];

    for (int i = 0; i < pieces.length; i++) {
      this.pieces[i] = pieces[i].clone();
    }
  }
  private final int[][] pieces;

  public BoardState() {
    pieces = new int[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
    setUpPawns();
  }
  public boolean isItRed(int col, int row) {
    return getPiece(row, col) == GameConstants.RED;
  }
  public boolean isItBlack(int col, int row) {
    return getPiece(row, col) == GameConstants.BLACK;
  }
  public boolean isItBlackKing(int col, int row) {
    return getPiece(row, col) == GameConstants.BLACK_KING;
  }
  public boolean isItRedKing(int col, int row) {
    return getPiece(row, col) == GameConstants.RED_KING;
  }
  public boolean isItEmpty(int col, int row) {
    return getPiece(row, col) == GameConstants.EMPTY;
  }
  public boolean isItKing(int color) {
    return color == GameConstants.BLACK_KING || color == GameConstants.RED_KING;
  }

  public BoardState copy() {
    return new BoardState(pieces);
  }

  public void setUpPawns() {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {

        if ((row + col) % 2 == 0) {
          pieces[row][col] = GameConstants.EMPTY;
          continue;
        }

        if (row < GameConstants.NUM_STARTING_ROWS) {
          pieces[row][col] = GameConstants.BLACK;
        } else if (row
            >= GameConstants.BOARD_SIZE - GameConstants.NUM_STARTING_ROWS) {
          pieces[row][col] = GameConstants.RED;
        } else {
          pieces[row][col] = GameConstants.EMPTY;
        }
      }
    }
  }

  public int getPiece(int row, int col) {
    return pieces[row][col];
  }

  public void setPiece(int row, int col, int piece) {
    pieces[row][col] = piece;
  }
  public List<PieceView> toPieceViews(SelectionState selectionState) {
    List<PieceView> pieces = new ArrayList<>();
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int type = getPiece(row, col);
        boolean selected = (row == selectionState.getSelectedRow() && col == selectionState.getSelectedColumn());
        pieces.add(new PieceView(row, col, type, selected));
      }
    }
    return pieces;
  }
}
