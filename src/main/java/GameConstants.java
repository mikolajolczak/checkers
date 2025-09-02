package checkers.src.main.java;

public class GameConstants {
  public static final int EMPTY = 0;
  public static final int RED = 1;
  public static final int BLACK = 2;

  public static final int RED_KING = 3;

  public static final int BLACK_KING = 4;

  public static final int BOARD_SIZE = 8;

  public static final int SQUARE_SIZE = 50;

  public static final int PIECE_PADDING = 5;

  public static final int PIECE_SIZE = 40;

  public static final int KING_MARKER_PADDING = 15;

  public static final int KING_MARKER_SIZE = 20;

  public static final int NUM_STARTING_ROWS = 3;
  public static final int COLOR_CHOICE_X = 700;

  public static final int COLOR_CHOICE_Y = 400;

  public static final int COLOR_CHOICE_WIDTH = 350;

  public static final int COLOR_CHOICE_HEIGHT = 90;
  public static final int INITIAL_SUM_MAX = -100;


  public static final int MOVE = 0;
  public static final int TAKE = 1;
  public static final int QUEEN_TAKE = 2;
  public static final int LAST_ROW_INDEX = GameConstants.BOARD_SIZE - 1;
  public static final int SCORE_PLAYER_THREAT = 20;
  public static final int SCORE_PLAYER_THREAT_KING = 30;
  public static final int SCORE_TAKE_POSSIBLE = 10;
  public static final int SCORE_CHANCE_FOR_QUEEN = 15;
  public static final int WINDOW_WIDTH = 416;
  public static final int[][] DIRECTIONS =
      {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

  public static final int WINDOW_HEIGHT = 436;

  public static final int BOT_MOVE_DELAY_MS = 500;

}
