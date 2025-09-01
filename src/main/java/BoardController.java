package checkers.src.main.java;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Controller class for managing the Checkers game board, handling player
 * and bot moves, and interacting with the game frame.
 */
public final class BoardController {

  /**
   * The color used by the bot's normal pieces.
   */
  private int botsColor = GameConstants.BLACK;

  /**
   * The color used by the bot's king pieces.
   */
  private int botsKingColor = GameConstants.BLACK_KING;

  /**
   * The color used by the player's normal pieces.
   */
  private int playersColor = GameConstants.RED;

  /**
   * The color used by the player's king pieces.
   */
  private int playersKingColor = GameConstants.RED_KING;

  /**
   * Reference to the game frame.
   */
  private final Frame frame;

  /**
   * Reference to the Move logic handler.
   */
  private final Move move;

  /**
   * Reference to the bot player logic.
   */
  private Bot bot;

  /**
   * The color of the current player.
   */
  private int currentColor = GameConstants.BLACK;

  /**
   * The color of the current king piece.
   */
  private int currentColorKing = GameConstants.BLACK_KING;


  private final BoardState boardState;

  /**
   * Creates a new BoardController for a given frame and move handler.
   *
   * @param frameParam the game frame
   * @param moveParam  the move handler
   */
  public BoardController(final Frame frameParam, final Move moveParam,
                         final BoardState boardStateParam) {
    this.frame = frameParam;
    this.move = moveParam;
    this.boardState = boardStateParam;
    this.frame.addBoardListener(new BoardListener());
  }

  /**
   * @return the game frame
   */
  public Frame getFrame() {
    return frame;
  }

  /**
   * Toggles the current king color between red and black.
   */
  public void setCurrentColorKing() {
    if (this.currentColorKing == GameConstants.RED_KING) {
      this.currentColorKing = GameConstants.BLACK_KING;
    } else {
      this.currentColorKing = GameConstants.RED_KING;
    }
  }

  /**
   * @return the player's king color
   */
  public int getPlayersKingColor() {
    return playersKingColor;
  }

  /**
   * Sets the player's king color.
   *
   * @param playersKingColorParam the new king color for the player
   */
  public void setPlayersKingColor(final int playersKingColorParam) {
    this.playersKingColor = playersKingColorParam;
  }

  /**
   * @return the player's normal piece color
   */
  public int getPlayersColor() {
    return playersColor;
  }

  /**
   * Sets the player's normal piece color.
   *
   * @param playersColorParam the new color for the player
   */
  public void setPlayersColor(final int playersColorParam) {
    this.playersColor = playersColorParam;
  }

  /**
   * @return the bot's normal piece color
   */
  public int getBotsColor() {
    return botsColor;
  }

  /**
   * @return the bot's king piece color
   */
  public int getBotsKingColor() {
    return botsKingColor;
  }

  /**
   * Sets the bot's king piece color.
   *
   * @param botsKingColorParam the new king color for the bot
   */
  public void setBotsKingColor(final int botsKingColorParam) {
    this.botsKingColor = botsKingColorParam;
  }

  /**
   * Sets the bot's normal piece color.
   *
   * @param botsColorParam the new color for the bot
   */
  public void setBotsColor(final int botsColorParam) {
    this.botsColor = botsColorParam;
  }

  /**
   * @return the current king piece color
   */
  public int getCurrentColorKing() {
    return currentColorKing;
  }

  /**
   * Clears the currently selected tile on the board.
   */
  public void clearChosenTile() {
    boardState.setSelected(GameConstants.BOARD_SIZE, GameConstants.BOARD_SIZE);
    frame.repaint();
  }

  /**
   * Toggles the current player and lets the bot move if it's their turn.
   */
  public void setCurrentColor() {
    if (currentColor == GameConstants.RED) {
      this.currentColor = GameConstants.BLACK;
    } else {
      this.currentColor = GameConstants.RED;
    }

    if (currentColor == botsColor) {
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

      this.currentColor = playersColor;
      this.currentColorKing = playersKingColor;
    }
  }

  /**
   * Sets the bot logic.
   *
   * @param botParam the bot instance
   */
  public void setBot(final Bot botParam) {
    this.bot = botParam;
  }

  /**
   * @return the current player's color
   */
  public int getCurrentColor() {
    return currentColor;
  }

  /**
   * Performs a normal piece capture from one tile to another.
   *
   * @param firstRow          the row of the moving piece
   * @param firstColumn       the column of the moving piece
   * @param secondRow         the row of the destination
   * @param secondColumn      the column of the destination
   * @param currentColorParam the color of the moving piece
   */
  public void take(final int firstRow, final int firstColumn,
                   final int secondRow, final int secondColumn,
                   final int currentColorParam) {
    boardState.setPiece(firstRow, firstColumn, GameConstants.EMPTY);
    boardState.setPiece(secondRow, secondColumn, currentColorParam);
    int rowBetween = (firstRow + secondRow) / 2;
    int colBetween = (firstColumn + secondColumn) / 2;
    boardState.setPiece(rowBetween, colBetween, GameConstants.EMPTY);
  }

  /**
   * Performs a king piece capture from one tile to another.
   *
   * @param firstRow          the row of the moving piece
   * @param firstColumn       the column of the moving piece
   * @param secondRow         the row of the destination
   * @param secondColumn      the column of the destination
   * @param currentColorParam the color of the moving piece
   */
  public void queenTake(final int firstRow, final int firstColumn,
                        final int secondRow, final int secondColumn,
                        final int currentColorParam) {
    boardState.setPiece(firstRow, firstColumn, GameConstants.EMPTY);
    boardState.setPiece(secondRow, secondColumn, currentColorParam);

    if (secondRow < firstRow && secondColumn < firstColumn) {
      boardState.setPiece(secondRow + 1, secondColumn + 1, GameConstants.EMPTY);
    }
    if (secondRow > firstRow && secondColumn < firstColumn) {
      boardState.setPiece(secondRow - 1, secondColumn + 1, GameConstants.EMPTY);
    }
    if (secondRow < firstRow && secondColumn > firstColumn) {
      boardState.setPiece(secondRow + 1, secondColumn - 1, GameConstants.EMPTY);
    }
    if (secondRow > firstRow && secondColumn > firstColumn) {
      boardState.setPiece(secondRow - 1, secondColumn - 1, GameConstants.EMPTY);
    }
  }

  /**
   * Inner class for handling mouse events on the game board.
   */
  private final class BoardListener implements MouseListener {

    /**
     * Tracks whether this is the first click.
     */
    private boolean firstclick = true;

    /**
     * Column of the first click.
     */
    private int firstClickColumnNumber = GameConstants.BOARD_SIZE;

    /**
     * Row of the first click.
     */
    private int firstClickRowNumber = GameConstants.BOARD_SIZE;

    /**
     * Color of the piece selected in the first click.
     */
    private int colorOfFirstClick;

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    private void promoteIfNeeded(final int row, final int col,
                                 final int color) {
      if (color == GameConstants.RED && row == 0) {
        boardState.setPiece(row, col, GameConstants.RED_KING);
      } else if (color == GameConstants.BLACK && row == GameConstants.LAST_ROW_INDEX) {
        boardState.setPiece(row, col, GameConstants.BLACK_KING);
      }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
      if (firstclick) {
        firstClickColumnNumber = e.getX() / GameConstants.SQUARE_SIZE;
        firstClickRowNumber = e.getY() / GameConstants.SQUARE_SIZE;
        if (move.canIMove(firstClickColumnNumber, firstClickRowNumber)
            || move.canITake(firstClickColumnNumber, firstClickRowNumber)) {
          int value = boardState.getPiece(firstClickRowNumber,
              firstClickColumnNumber);
          if (value == getCurrentColor() || value == getCurrentColorKing()) {
            boardState.setSelected(firstClickRowNumber, firstClickColumnNumber);
            frame.getBoard().repaint();
            colorOfFirstClick = value;
            firstclick = false;
          }
        }
      } else {
        clearChosenTile();
        int columnsecond = e.getX() / GameConstants.SQUARE_SIZE;
        int rowsecond = e.getY() / GameConstants.SQUARE_SIZE;

        if (!move.checkAllPiecesPossibleTakes(getCurrentColor(),
            getCurrentColorKing())) {
          if (move.isItLegalSecondClickMove(columnsecond, rowsecond,
              firstClickColumnNumber, firstClickRowNumber, colorOfFirstClick)) {
            if (boardState.getPiece(rowsecond, columnsecond)
                == GameConstants.EMPTY) {
              boardState.setPiece(firstClickRowNumber,
                  firstClickColumnNumber, GameConstants.EMPTY);
              boardState.setPiece(rowsecond, columnsecond,
                  colorOfFirstClick);
              promoteIfNeeded(rowsecond, columnsecond, colorOfFirstClick);
              setCurrentColor();
            }
          }
        } else {
          if (move.legalTakeMove(columnsecond, rowsecond,
              firstClickColumnNumber, firstClickRowNumber, colorOfFirstClick)) {
            if (colorOfFirstClick == GameConstants.BLACK
                || colorOfFirstClick == GameConstants.RED) {
              take(firstClickRowNumber, firstClickColumnNumber, rowsecond,
                  columnsecond, getCurrentColor());
            } else {
              queenTake(firstClickRowNumber, firstClickColumnNumber, rowsecond,
                  columnsecond, getCurrentColorKing());
            }
            promoteIfNeeded(rowsecond, columnsecond, colorOfFirstClick);
            setCurrentColor();
          }
        }
        firstclick = true;
        frame.getBoard().repaint();
      }
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }
  }
}
