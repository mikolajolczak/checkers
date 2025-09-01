package checkers.src.main.java;

public class GameConstants {
  /**
   * Empty square constant.
   */
  public static final int EMPTY = 0;
  /**
   * Red piece constant.
   */
  public static final int RED = 1;
  /**
   * Black piece constant.
   */
  public static final int BLACK = 2;

  /**
   * Red king piece constant.
   */
  public static final int RED_KING = 3;

  /**
   * Black king piece constant.
   */
  public static final int BLACK_KING = 4;

  /**
   * Number of rows and columns on the board.
   */
  public static final int BOARD_SIZE = 8;

  /**
   * Size of each square in pixels.
   */
  public static final int SQUARE_SIZE = 50;

  /**
   * Padding inside a square for drawing a piece.
   */
  public static final int PIECE_PADDING = 5;

  /**
   * Diameter of a piece in pixels.
   */
  public static final int PIECE_SIZE = 40;

  /**
   * Padding for drawing the king marker.
   */
  public static final int KING_MARKER_PADDING = 15;

  /**
   * Diameter of the king marker circle.
   */
  public static final int KING_MARKER_SIZE = 20;

  /**
   * Number of starting rows filled with pieces for each player.
   */
  public static final int NUM_STARTING_ROWS = 3;
  /**
   * X coordinate for the color choice frame location.
   */
  public static final int COLOR_CHOICE_X = 700;

  /**
   * Y coordinate for the color choice frame location.
   */
  public static final int COLOR_CHOICE_Y = 400;

  /**
   * Width of the color choice frame.
   */
  public static final int COLOR_CHOICE_WIDTH = 350;

  /**
   * Height of the color choice frame.
   */
  public static final int COLOR_CHOICE_HEIGHT = 90;
  public static final int MOVE_ARRAY_LENGTH = 5;
  public static final int INITIAL_SUM_MAX = -100;


  public static final int MOVE = 0;
  public static final int TAKE = 1;
  public static final int QUEEN_TAKE = 2;
  public static final int LAST_ROW_INDEX = GameConstants.BOARD_SIZE - 1;
  public static final int SECOND_LAST_INDEX = GameConstants.BOARD_SIZE - 2;
  public static final int TO_COL = 3;
  public static final int MOVE_TYPE = 4;
  public static final int SCORE_PLAYER_THREAT = 20;
  public static final int SCORE_PLAYER_THREAT_KING = 30;
  public static final int SCORE_TAKE_POSSIBLE = 10;
  public static final int SCORE_CHANCE_FOR_QUEEN = 15;
  /**
   * Width of the window in pixels.
   */
  public static final int WINDOW_WIDTH = 416;

  /**
   * Height of the window in pixels.
   */
  public static final int WINDOW_HEIGHT = 436;
  /**
   * Maximum row offset used in move calculations.
   */
  public static final int MAX_ROW_OFFSET = GameConstants.BOARD_SIZE - 3;
  /**
   * Maximum column offset used in move calculations.
   */
  public static final int MAX_COLUMN_OFFSET = GameConstants.BOARD_SIZE - 2;
  public static final int BOT_MOVE_DELAY_MS = 500;

}
