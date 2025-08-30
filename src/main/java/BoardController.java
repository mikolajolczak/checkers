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
  private int botsColor = Board.BLACK;

  /**
   * The color used by the bot's king pieces.
   */
  private int botsKingColor = Board.BLACK_KING;

  /**
   * The color used by the player's normal pieces.
   */
  private int playersColor = Board.RED;

  /**
   * The color used by the player's king pieces.
   */
  private int playersKingColor = Board.RED_KING;

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
  private int currentColor = Board.BLACK;

  /**
   * The color of the current king piece.
   */
  private int currentColorKing = Board.BLACK_KING;

  /**
   * Delay in milliseconds before the bot makes a move.
   */
  private static final int BOT_MOVE_DELAY_MS = 500;

  /**
   * Size of each tile on the board in pixels.
   */
  private static final int TILE_SIZE = 50;

  /**
   * Index of the last row on the board.
   */
  private static final int LAST_ROW_INDEX = Board.BOARD_SIZE - 1;

  /**
   * Creates a new BoardController for a given frame and move handler.
   *
   * @param frameParam the game frame
   * @param moveParam  the move handler
   */
  public BoardController(final Frame frameParam, final Move moveParam) {
    this.frame = frameParam;
    this.move = moveParam;
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
    if (this.currentColorKing == Board.RED_KING) {
      this.currentColorKing = Board.BLACK_KING;
    } else {
      this.currentColorKing = Board.RED_KING;
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
    frame.getBoard().setSelectedColumn(Board.BOARD_SIZE);
    frame.getBoard().setSelectedRow(Board.BOARD_SIZE);
    frame.repaint();
  }

  /**
   * Toggles the current player and lets the bot move if it's their turn.
   */
  public void setCurrentColor() {
    if (currentColor == Board.RED) {
      this.currentColor = Board.BLACK;
    } else {
      this.currentColor = Board.RED;
    }

    if (currentColor == botsColor) {
      bot.analyze();
      bot.simulate();
      new Thread(() -> {
        try {
          Thread.sleep(BOT_MOVE_DELAY_MS);
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
    frame.getBoard().getPieces()[firstRow][firstColumn] = Board.EMPTY;
    frame.getBoard().getPieces()[secondRow][secondColumn] = currentColorParam;
    int rowBetween = (firstRow + secondRow) / 2;
    int colBetween = (firstColumn + secondColumn) / 2;
    frame.getBoard().getPieces()[rowBetween][colBetween] = Board.EMPTY;
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
    frame.getBoard().setValueOfPiece(firstRow, firstColumn, Board.EMPTY);
    frame.getBoard()
        .setValueOfPiece(secondRow, secondColumn, currentColorParam);

    if (secondRow < firstRow && secondColumn < firstColumn) {
      frame.getBoard()
          .setValueOfPiece(secondRow + 1, secondColumn + 1, Board.EMPTY);
    }
    if (secondRow > firstRow && secondColumn < firstColumn) {
      frame.getBoard()
          .setValueOfPiece(secondRow - 1, secondColumn + 1, Board.EMPTY);
    }
    if (secondRow < firstRow && secondColumn > firstColumn) {
      frame.getBoard()
          .setValueOfPiece(secondRow + 1, secondColumn - 1, Board.EMPTY);
    }
    if (secondRow > firstRow && secondColumn > firstColumn) {
      frame.getBoard()
          .setValueOfPiece(secondRow - 1, secondColumn - 1, Board.EMPTY);
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
    private int firstClickColumnNumber = Board.BOARD_SIZE;

    /**
     * Row of the first click.
     */
    private int firstClickRowNumber = Board.BOARD_SIZE;

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
      if (color == Board.RED && row == 0) {
        frame.getBoard().setValueOfPiece(row, col, Board.RED_KING);
      } else if (color == Board.BLACK && row == LAST_ROW_INDEX) {
        frame.getBoard().setValueOfPiece(row, col, Board.BLACK_KING);
      }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
      if (firstclick) {
        firstClickColumnNumber = e.getX() / TILE_SIZE;
        firstClickRowNumber = e.getY() / TILE_SIZE;
        if (move.canIMove(firstClickColumnNumber, firstClickRowNumber)
            || move.canITake(firstClickColumnNumber, firstClickRowNumber)) {
          int value = frame.getBoard().getValueOfPiece(firstClickRowNumber,
              firstClickColumnNumber);
          if (value == getCurrentColor() || value == getCurrentColorKing()) {
            frame.getBoard().setSelectedColumn(firstClickColumnNumber);
            frame.getBoard().setSelectedRow(firstClickRowNumber);
            frame.getBoard().repaint();
            colorOfFirstClick = value;
            firstclick = false;
          }
        }
      } else {
        clearChosenTile();
        int columnsecond = e.getX() / TILE_SIZE;
        int rowsecond = e.getY() / TILE_SIZE;

        if (!move.checkAllPiecesPossibleTakes(getCurrentColor(),
            getCurrentColorKing())) {
          if (move.isItLegalSecondClickMove(columnsecond, rowsecond,
              firstClickColumnNumber, firstClickRowNumber, colorOfFirstClick)) {
            if (frame.getBoard().getValueOfPiece(rowsecond, columnsecond)
                == Board.EMPTY) {
              frame.getBoard().setValueOfPiece(firstClickRowNumber,
                  firstClickColumnNumber, Board.EMPTY);
              frame.getBoard().setValueOfPiece(rowsecond, columnsecond,
                  colorOfFirstClick);
              promoteIfNeeded(rowsecond, columnsecond, colorOfFirstClick);
              setCurrentColor();
            }
          }
        } else {
          if (move.legalTakeMove(columnsecond, rowsecond,
              firstClickColumnNumber, firstClickRowNumber, colorOfFirstClick)) {
            if (colorOfFirstClick == Board.BLACK
                || colorOfFirstClick == Board.RED) {
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
