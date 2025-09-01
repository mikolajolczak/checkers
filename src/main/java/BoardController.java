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
  private final PromotionService promotionService;
  private final Move move;

  public BoardState getBoardState() {
    return boardState;
  }

  public PromotionService getPromotionService() {
    return promotionService;
  }

  public Move getMove() {
    return move;
  }

  private int currentColor = GameConstants.BLACK;
  private int currentColorKing = GameConstants.BLACK_KING;

  public BoardController(final Frame frameParam,
                         final BoardState boardStateParam,
                         PromotionService promotionServiceParam, Move moveParam) {
    this.frame = frameParam;
    this.boardState = boardStateParam;
    promotionService = promotionServiceParam;
    move = moveParam;
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
      frame.isGameFinished();
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
  public boolean canSelectPiece(int row, int col) {
    int value = boardState.getPiece(row, col);
    boolean isCurrentPiece = value == getCurrentColor()
        || value == getCurrentColorKing();
    return isCurrentPiece && (move.canIMove(col, row) || move.canITake(col,
        row, boardState));
  }
  public boolean isLegalNormalMove(int row, int col, int firstClickCol, int firstClickRow, int firstClickColor) {
    return move.isItLegalSecondClickMove(col, row, firstClickCol, firstClickRow,
        firstClickColor)
        && boardState.getPiece(row, col) == GameConstants.EMPTY;
  }
  public boolean mustTake() {
    return move.checkAllPiecesPossibleTakes(getCurrentColor(),
        getCurrentColorKing(), boardState);
  }
  public void take(int firstRow, int firstColumn, int secondRow,
                   int secondColumn,
                   int currentColor, BoardState boardState) {
    boardState.setPiece(firstRow, firstColumn,GameConstants.EMPTY);
    boardState.setPiece(secondRow, secondColumn,currentColor);

    int capturedRow = (firstRow + secondRow) / 2;
    int capturedCol = (firstColumn + secondColumn) / 2;
    boardState.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }

  public void queenTake(int firstRow, int firstColumn, int secondRow,
                        int secondColumn,
                        int currentColor, BoardState boardStateParam) {
    boardStateParam.setPiece(firstRow, firstColumn, GameConstants.EMPTY);
    boardStateParam.setPiece(secondRow, secondColumn, currentColor);

    int rowDir = Integer.signum(secondRow - firstRow);
    int colDir = Integer.signum(secondColumn - firstColumn);

    int capturedRow = secondRow - rowDir;
    int capturedCol = secondColumn - colDir;
    boardStateParam.setPiece(capturedRow, capturedCol, GameConstants.EMPTY);
  }
  public void movePiece(int row, int col, int firstClickCol, int firstClickRow, int firstClickColor) {
    boardState.setPiece(firstClickRow, firstClickCol, GameConstants.EMPTY);
    boardState.setPiece(row, col, firstClickColor);
  }
  public void attemptNormalTake(int row, int col, int firstClickCol, int firstClickRow) {
    take(firstClickRow, firstClickCol, row, col,
        getCurrentColor(), boardState);
  }

  public void attemptQueenTake(int row, int col, int firstClickCol, int firstClickRow) {
    queenTake(firstClickRow, firstClickCol, row, col,
        getCurrentColorKing(), boardState);
  }
}