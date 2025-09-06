package checkers;

/**
 * Defines constant values used throughout the checkers game.
 *
 * <p>This class contains constants representing piece types, board dimensions,
 * UI element positions and sizes, scoring parameters, move types, and
 * directions for piece movement. It serves as a central reference for
 * all game-related configuration values.
 * </p>
 */
public final class GameConstants {
  /**
   * Represents an empty square on the board.
   */
  public static final int EMPTY = 0;
  /**
   * Represents a red piece.
   */
  public static final int RED = 1;
  /**
   * Represents a black piece.
   */
  public static final int BLACK = 2;
  /**
   * Represents a red king piece.
   */
  public static final int RED_KING = 3;
  /**
   * Represents a black king piece.
   */
  public static final int BLACK_KING = 4;
  /**
   * The size of the board (number of rows/columns).
   */
  public static final int BOARD_SIZE = 8;
  /**
   * The size of each square on the board, in pixels.
   */
  public static final int SQUARE_SIZE = 50;
  /**
   * Padding around a piece within a square, in pixels.
   */
  public static final int PIECE_PADDING = 5;
  /**
   * The diameter of a piece, in pixels.
   */
  public static final int PIECE_SIZE = 40;
  /**
   * Padding for the king marker, in pixels.
   */
  public static final int KING_MARKER_PADDING = 15;
  /**
   * Size of the king marker, in pixels.
   */
  public static final int KING_MARKER_SIZE = 20;
  /**
   * Number of rows filled with pieces at the start of the game.
   */
  public static final int NUM_STARTING_ROWS = 3;
  /**
   * X-coordinate for the color choice UI panel.
   */
  public static final int COLOR_CHOICE_X = 700;
  /**
   * Y-coordinate for the color choice UI panel.
   */
  public static final int COLOR_CHOICE_Y = 400;
  /**
   * Width of the color choice UI panel.
   */
  public static final int COLOR_CHOICE_WIDTH = 350;
  /**
   * Height of the color choice UI panel.
   */
  public static final int COLOR_CHOICE_HEIGHT = 90;
  /**
   * Initial maximum sum used for evaluation functions.
   */
  public static final int INITIAL_SUM_MAX = -100;
  /**
   * Indicates a standard move action.
   */
  public static final int MOVE = 0;
  /**
   * Indicates a capturing move.
   */
  public static final int TAKE = 1;
  /**
   * Indicates a capturing move performed by a king piece.
   */
  public static final int KING_TAKE = 2;
  /**
   * Index of the last row on the board.
   */
  public static final int LAST_ROW_INDEX = GameConstants.BOARD_SIZE - 1;
  /**
   * Score awarded when the player is threatened.
   */
  public static final int SCORE_PLAYER_THREAT = 20;
  /**
   * Score awarded when the player is threatened by a king.
   */
  public static final int SCORE_PLAYER_THREAT_KING = 30;
  /**
   * Score awarded when a capturing move is possible.
   */
  public static final int SCORE_TAKE_POSSIBLE = 10;
  /**
   * Score awarded for a chance to promote a piece to king.
   */
  public static final int SCORE_CHANCE_FOR_KING = 15;
  /**
   * Width of the game window, in pixels.
   */
  public static final int WINDOW_WIDTH = 416;
  /**
   * Movement directions for pieces (row and column offsets).
   */
  public static final int[][] DIRECTIONS = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
  /**
   * Height of the game window, in pixels.
   */
  public static final int WINDOW_HEIGHT = 436;
  /**
   * Delay for bot moves in milliseconds.
   */
  public static final int BOT_MOVE_DELAY_MS = 500;

  private GameConstants() {
  }
}
