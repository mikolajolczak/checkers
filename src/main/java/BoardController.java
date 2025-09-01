package checkers.src.main.java;


/**
 * Controller class for managing the Checkers game board, handling player
 * and bot moves, and interacting with the game frame.
 */
public final class BoardController {

  private int botsColor = GameConstants.BLACK;
  private int botsKingColor = GameConstants.BLACK_KING;
  private int playersColor = GameConstants.RED;
  private int playersKingColor = GameConstants.RED_KING;

  private final Frame frame;
  private Bot bot;
  private final BoardState boardState;

  private int currentColor = GameConstants.BLACK;
  private int currentColorKing = GameConstants.BLACK_KING;

  public BoardController(final Frame frameParam,
                         final BoardState boardStateParam) {
    this.frame = frameParam;
    this.boardState = boardStateParam;
  }

  public Frame getFrame() {
    return frame;
  }

  public int getCurrentColor() {
    return currentColor;
  }

  public int getCurrentColorKing() {
    return currentColorKing;
  }

  public int getPlayersColor() {
    return playersColor;
  }

  public int getPlayersKingColor() {
    return playersKingColor;
  }

  public int getBotsColor() {
    return botsColor;
  }

  public int getBotsKingColor() {
    return botsKingColor;
  }

  public void setPlayersColor(final int color) {
    this.playersColor = color;
  }

  public void setPlayersKingColor(final int color) {
    this.playersKingColor = color;
  }

  public void setBotsColor(final int color) {
    this.botsColor = color;
  }

  public void setBotsKingColor(final int color) {
    this.botsKingColor = color;
  }

  public void setCurrentColorKing() {
    currentColorKing = (currentColorKing == GameConstants.RED_KING)
        ? GameConstants.BLACK_KING
        : GameConstants.RED_KING;
  }

  public void clearChosenTile() {
    boardState.setSelected(GameConstants.BOARD_SIZE, GameConstants.BOARD_SIZE);
    frame.repaint();
  }

  public void setBot(final Bot botParam) {
    this.bot = botParam;
  }

  public void setCurrentColor() {
    currentColor = (currentColor == GameConstants.RED)
        ? GameConstants.BLACK
        : GameConstants.RED;

    if (currentColor == botsColor && bot != null) {
      bot.analyze();
      bot.simulate();
      new Thread(() -> {
        try {
          Thread.sleep(GameConstants.BOT_MOVE_DELAY_MS);
        } catch (InterruptedException e) {
          System.err.println("Thread was interrupted: " + e.getMessage());
        }
        bot.move();
      }).start();

      currentColor = playersColor;
      currentColorKing = playersKingColor;
    }
  }

  public void take(int firstRow, int firstColumn, int secondRow,
                   int secondColumn, int color) {
    boardState.setPiece(firstRow, firstColumn, GameConstants.EMPTY);
    boardState.setPiece(secondRow, secondColumn, color);
    int rowBetween = (firstRow + secondRow) / 2;
    int colBetween = (firstColumn + secondColumn) / 2;
    boardState.setPiece(rowBetween, colBetween, GameConstants.EMPTY);
  }

  public void queenTake(int firstRow, int firstColumn, int secondRow,
                        int secondColumn, int color) {
    boardState.setPiece(firstRow, firstColumn, GameConstants.EMPTY);
    boardState.setPiece(secondRow, secondColumn, color);

    int dRow = secondRow - firstRow;
    int dCol = secondColumn - firstColumn;

    boardState.setPiece(secondRow - Integer.signum(dRow),
        secondColumn - Integer.signum(dCol), GameConstants.EMPTY);
  }
}