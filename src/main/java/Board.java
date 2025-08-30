package checkers.src.main.java;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Represents a checkers game board with pieces and selection state.
 * Responsible for drawing the board and managing piece positions.
 */
public class Board extends JPanel {

  /**
   * Empty square constant.
   */
  static final int EMPTY = 0;

  /**
   * Red piece constant.
   */
  static final int RED = 1;

  /**
   * Black piece constant.
   */
  static final int BLACK = 2;

  /**
   * Red king piece constant.
   */
  static final int RED_KING = 3;

  /**
   * Black king piece constant.
   */
  static final int BLACK_KING = 4;

  /**
   * Number of rows and columns on the board.
   */
  static final int BOARD_SIZE = 8;

  /**
   * Size of each square in pixels.
   */
  static final int SQUARE_SIZE = 50;

  /**
   * Padding inside a square for drawing a piece.
   */
  static final int PIECE_PADDING = 5;

  /**
   * Diameter of a piece in pixels.
   */
  static final int PIECE_SIZE = 40;

  /**
   * Padding for drawing the king marker.
   */
  static final int KING_MARKER_PADDING = 15;

  /**
   * Diameter of the king marker circle.
   */
  static final int KING_MARKER_SIZE = 20;

  /**
   * Number of starting rows filled with pieces for each player.
   */
  static final int NUM_STARTING_ROWS = 3;

  /**
   * 2D array representing the pieces on the board.
   */
  private final int[][] pieces = new int[BOARD_SIZE][BOARD_SIZE];

  /**
   * Returns the board's piece array.
   *
   * @return 2D array of piece constants.
   */
  public int[][] getPieces() {
    return pieces;
  }

  /**
   * Sets up the board with initial pieces for red and black.
   */
  public void setUpPawns() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row % 2 != col % 2) {
          if (row < NUM_STARTING_ROWS) {
            pieces[row][col] = BLACK;
          } else if (row >= BOARD_SIZE - NUM_STARTING_ROWS) {
            pieces[row][col] = RED;
          } else {
            pieces[row][col] = EMPTY;
          }
        } else {
          pieces[row][col] = EMPTY;
        }
      }
    }
  }

  /**
   * Currently selected row. Defaults to an invalid index.
   */
  private int selectedRow = BOARD_SIZE;

  /**
   * Currently selected column. Defaults to an invalid index.
   */
  private int selectedColumn = BOARD_SIZE;

  /**
   * Sets the currently selected row.
   *
   * @param selectedRowParam the row index to select (0-based)
   */
  public void setSelectedRow(final int selectedRowParam) {
    this.selectedRow = selectedRowParam;
  }

  /**
   * Sets the currently selected column.
   *
   * @param selectedColumnParam the column index to select (0-based)
   */
  public void setSelectedColumn(final int selectedColumnParam) {
    this.selectedColumn = selectedColumnParam;
  }

  /**
   * Returns the currently selected row.
   *
   * @return the index of the selected row (0-based), or BOARD_SIZE if none
   *     is selected
   */
  public int getSelectedRow() {
    return selectedRow;
  }

  /**
   * Returns the currently selected column.
   *
   * @return the index of the selected column (0-based), or BOARD_SIZE if
   *     none is selected
   */
  public int getSelectedColumn() {
    return selectedColumn;
  }

  /**
   * Returns the value of a piece at a given position.
   *
   * @param row the row index
   * @param col the column index
   * @return piece constant at the given position
   */
  int getValueOfPiece(final int row, final int col) {
    return pieces[row][col];
  }

  /**
   * Sets the value of a piece at a given position.
   *
   * @param row   the row index
   * @param col   the column index
   * @param color the piece constant
   */
  public void setValueOfPiece(final int row, final int col, final int color) {
    this.pieces[row][col] = color;
  }

  /**
   * Paints the checkers board and pieces.
   *
   * @param g the Graphics object
   */
  @Override
  public void paintComponent(final Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (col == getSelectedColumn() && row == getSelectedRow()) {
          g.setColor(Color.DARK_GRAY);
        } else {
          g.setColor((row % 2 == col % 2) ? Color.LIGHT_GRAY : Color.GRAY);
        }
        g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE,
            SQUARE_SIZE);

        switch (getValueOfPiece(row, col)) {
          case RED:
            g.setColor(Color.RED);
            g.fillOval(PIECE_PADDING + col * SQUARE_SIZE,
                PIECE_PADDING + row * SQUARE_SIZE, PIECE_SIZE, PIECE_SIZE);
            break;
          case BLACK:
            g.setColor(Color.BLACK);
            g.fillOval(PIECE_PADDING + col * SQUARE_SIZE,
                PIECE_PADDING + row * SQUARE_SIZE, PIECE_SIZE, PIECE_SIZE);
            break;
          case RED_KING:
            g.setColor(Color.RED);
            g.fillOval(PIECE_PADDING + col * SQUARE_SIZE,
                PIECE_PADDING + row * SQUARE_SIZE, PIECE_SIZE, PIECE_SIZE);
            g.setColor(Color.WHITE);
            g.drawOval(KING_MARKER_PADDING + col * SQUARE_SIZE,
                KING_MARKER_PADDING + row * SQUARE_SIZE, KING_MARKER_SIZE,
                KING_MARKER_SIZE);
            break;
          case BLACK_KING:
            g.setColor(Color.BLACK);
            g.fillOval(PIECE_PADDING + col * SQUARE_SIZE,
                PIECE_PADDING + row * SQUARE_SIZE, PIECE_SIZE, PIECE_SIZE);
            g.setColor(Color.WHITE);
            g.drawOval(KING_MARKER_PADDING + col * SQUARE_SIZE,
                KING_MARKER_PADDING + row * SQUARE_SIZE, KING_MARKER_SIZE,
                KING_MARKER_SIZE);
            break;
          default:
            break;
        }
      }
    }
  }

  /**
   * Constructor that initializes the board with pieces.
   */
  public Board() {
    setUpPawns();
  }
}
