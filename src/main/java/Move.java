package checkers.src.main.java;

/**
 * Represents a move in a checkers game. Provides methods to check piece types,
 * legal moves, captures, and diagonals on the board.
 */
public class Move {
  private final BoardState boardState;
  /**
   * Constructs a Move object for the given frame.
   *
   */
  public Move(BoardState boardStateParam) {
    this.boardState = boardStateParam;
  }

  /**
   * Checks if the piece at the specified position is a red piece.
   *
   * @param column the column of the piece
   * @param row    the row of the piece
   * @return true if the piece is red, false otherwise
   */
  public boolean isItRed(final int column, final int row) {
    return boardState.getPiece(row, column) == GameConstants.RED;
  }

  /**
   * Checks if the piece at the specified position is a black piece.
   *
   * @param column the column of the piece
   * @param row    the row of the piece
   * @return true if the piece is black, false otherwise
   */
  public boolean isItBlack(final int column, final int row) {
    return boardState.getPiece(row, column) == GameConstants.BLACK;
  }

  /**
   * Checks if the piece at the specified position is a black king.
   *
   * @param column the column of the piece
   * @param row    the row of the piece
   * @return true if the piece is a black king, false otherwise
   */
  public boolean isItBlackKing(final int column, final int row) {
    return boardState.getPiece(row, column) == GameConstants.BLACK_KING;
  }

  /**
   * Checks if the piece at the specified position is a red king.
   *
   * @param column the column of the piece
   * @param row    the row of the piece
   * @return true if the piece is a red king, false otherwise
   */
  public boolean isItRedKing(final int column, final int row) {
    return boardState.getPiece(row, column) == GameConstants.RED_KING;
  }

  /**
   * Checks if the specified board cell is empty.
   *
   * @param column the column to check
   * @param row    the row to check
   * @return true if the cell is empty, false otherwise
   */
  public boolean isItEmpty(final int column, final int row) {
    return boardState.getPiece(row, column) == GameConstants.EMPTY;
  }

  /**
   * Checks if two positions are on the same diagonal.
   *
   * @param columnFirst  the column of the first position
   * @param rowFirst     the row of the first position
   * @param columnSecond the column of the second position
   * @param rowSecond    the row of the second position
   * @return true if the positions are on the same diagonal, false otherwise
   */
  public boolean isItOnTheSameDiagonal(final int columnFirst,
                                       final int rowFirst,
                                       final int columnSecond,
                                       final int rowSecond) {
    return
        Math.abs(rowSecond - columnSecond) == Math.abs(rowFirst - columnFirst)
            || rowFirst + columnFirst == columnSecond + rowSecond;
  }

  /**
   * Checks for any pieces on the left-top diagonal between two positions.
   *
   * @param columnFirst  starting column
   * @param rowFirst     starting row
   * @param columnSecond target column
   * @param rowSecond    target row
   * @return true if there is any piece between the positions, false otherwise
   */
  public boolean checkLeftTopDiagonalEmptySpaces(final int columnFirst,
                                                 final int rowFirst,
                                                 final int columnSecond,
                                                 final int rowSecond) {
    for (int i = rowFirst - 1, j = columnFirst - 1;
         i > rowSecond + 1 && j > columnSecond + 1; i--, j--) {
      if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks for any pieces on the left-bottom diagonal between two positions.
   *
   * @param columnFirst  starting column
   * @param rowFirst     starting row
   * @param columnSecond target column
   * @param rowSecond    target row
   * @return true if there is any piece between the positions, false otherwise
   */
  public boolean checkLeftBotDiagonalEmptySpaces(final int columnFirst,
                                                 final int rowFirst,
                                                 final int columnSecond,
                                                 final int rowSecond) {

    for (int i = rowFirst + 1, j = columnFirst - 1;
         i < rowSecond - 1 && j > columnSecond + 1; i++, j--) {
      if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks for any pieces on the right-top diagonal between two positions.
   *
   * @param columnFirst  starting column
   * @param rowFirst     starting row
   * @param columnSecond target column
   * @param rowSecond    target row
   * @return true if there is any piece between the positions, false otherwise
   */
  public boolean checkRightTopDiagonalEmptySpaces(final int columnFirst,
                                                  final int rowFirst,
                                                  final int columnSecond,
                                                  final int rowSecond) {
    for (int i = rowFirst - 1, j = columnFirst + 1;
         i > rowSecond + 1 && j < columnSecond - 1; i--, j++) {
      if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks for any pieces on the right-bottom diagonal between two positions.
   *
   * @param columnFirst  starting column
   * @param rowFirst     starting row
   * @param columnSecond target column
   * @param rowSecond    target row
   * @return true if there is any piece between the positions, false otherwise
   */
  public boolean checkRightBotDiagonalEmptySpaces(final int columnFirst,
                                                  final int rowFirst,
                                                  final int columnSecond,
                                                  final int rowSecond) {
    for (int i = rowFirst + 1, j = columnFirst + 1;
         i < rowSecond - 1 && j < columnSecond - 1; i++, j++) {
      if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if any pieces of the given color have possible captures.
   *
   * @param color      the color of the normal piece
   * @param colorQueen the color of the king piece
   * @return true if a capture is possible, false otherwise
   */
  public boolean checkAllPiecesPossibleTakes(int color, int colorQueen,
                                             BoardState boardStateParam) {
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        int piece = boardStateParam.getPiece(row, col);
        if ((piece == color || piece == colorQueen) && canITake(col, row,
                boardStateParam)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if a specific capture move is legal.
   *
   * @param columnsecond target column
   * @param rowsecond    target row
   * @param columnfirst  starting column
   * @param rowfirst     starting row
   * @param color        piece color
   * @return true if the capture is legal, false otherwise
   */
  public boolean legalTakeMove(final int columnsecond, final int rowsecond,
                               final int columnfirst,
                               final int rowfirst, final int color) {
    boolean result = true;
    if (isItEmpty(columnsecond, rowsecond) && isItOnTheSameDiagonal(columnfirst,
        rowfirst, columnsecond, rowsecond)) {
      switch (color) {
        case GameConstants.RED:
          if (isItBlack((columnsecond + columnfirst) / 2,
              (rowfirst + rowsecond) / 2) || isItBlackKing(
              (columnsecond + columnfirst) / 2,
              (rowfirst + rowsecond) / 2)) {
            if (Math.abs(columnsecond - columnfirst) != 2) {
              result = false;
            }
            if (Math.abs(rowfirst - rowsecond) != 2) {
              result = false;
            }
          } else {
            result = false;
          }
          break;
        case GameConstants.BLACK:
          if (isItRed((columnsecond + columnfirst) / 2,
              (rowsecond + rowfirst) / 2) || isItRedKing(
              (columnsecond + columnfirst) / 2,
              (rowsecond + rowfirst) / 2)) {
            if (Math.abs(columnsecond - columnfirst) != 2) {
              result = false;
            }
            if (Math.abs(rowsecond - rowfirst) != 2) {
              result = false;
            }
          } else {
            result = false;
          }
          break;
        case GameConstants.BLACK_KING:
          if (rowsecond < rowfirst && columnsecond < columnfirst) {
            if (checkLeftTopDiagonalEmptySpaces(columnfirst, rowfirst,
                columnsecond, rowsecond)) {
              result = false;
            }
            if (!(isItRedKing(columnsecond + 1, rowsecond + 1) || isItRed(
                columnsecond + 1, rowsecond + 1))) {
              result = false;
            }
          }
          if (rowsecond > rowfirst && columnsecond < columnfirst) {
            if (checkLeftBotDiagonalEmptySpaces(columnfirst, rowfirst,
                columnsecond, rowsecond)) {
              result = false;
            }
            if (!(isItRedKing(columnsecond + 1, rowsecond - 1) || isItRed(
                columnsecond + 1, rowsecond - 1))) {
              result = false;
            }
          }
          if (rowsecond < rowfirst && columnsecond > columnfirst) {
            if (checkRightTopDiagonalEmptySpaces(columnfirst, rowfirst,
                columnsecond, rowsecond)) {
              result = false;
            }
            if (!(isItRedKing(columnsecond - 1, rowsecond + 1) || isItRed(
                columnsecond - 1, rowsecond + 1))) {
              result = false;
            }
          }
          if (rowsecond > rowfirst && columnsecond > columnfirst) {
            if (checkRightBotDiagonalEmptySpaces(columnfirst, rowfirst,
                columnsecond, rowsecond)) {
              result = false;
            }
            if (!(isItRedKing(columnsecond - 1, rowsecond - 1) || isItRed(
                columnsecond - 1, rowsecond - 1))) {
              result = false;
            }
          }
          break;
        case GameConstants.RED_KING:
          if (rowsecond < rowfirst && columnsecond < columnfirst) {
            if (checkLeftTopDiagonalEmptySpaces(columnfirst, rowfirst,
                columnsecond, rowsecond)) {
              result = false;
            }
            if (!(isItBlackKing(columnsecond + 1, rowsecond + 1) || isItBlack(
                columnsecond + 1, rowsecond + 1))) {
              result = false;
            }
          }
          if (rowsecond > rowfirst && columnsecond < columnfirst) {
            if (checkLeftBotDiagonalEmptySpaces(columnfirst, rowfirst,
                columnsecond, rowsecond)) {
              result = false;
            }
            if (!(isItBlackKing(columnsecond + 1, rowsecond - 1) || isItBlack(
                columnsecond + 1, rowsecond - 1))) {
              result = false;
            }
          }
          if (rowsecond < rowfirst && columnsecond > columnfirst) {
            if (checkRightTopDiagonalEmptySpaces(columnfirst, rowfirst,
                columnsecond, rowsecond)) {
              result = false;
            }
            if (!(isItBlackKing(columnsecond - 1, rowsecond + 1) || isItBlack(
                columnsecond - 1, rowsecond + 1))) {
              result = false;
            }
          }
          if (rowsecond > rowfirst && columnsecond > columnfirst) {
            if (checkRightBotDiagonalEmptySpaces(columnfirst, rowfirst,
                columnsecond, rowsecond)) {
              result = false;
            }
            if (!(isItBlackKing(columnsecond - 1, rowsecond - 1) || isItBlack(
                columnsecond - 1, rowsecond - 1))) {
              result = false;
            }
          }
          break;
        default:
          break;
      }
    } else {
      result = false;
    }
    return result;
  }

  /**
   * Checks if a piece can move to any empty space (non-capture move).
   *
   * @param column the column of the piece
   * @param row    the row of the piece
   * @return true if the piece can move, false otherwise
   */
  public boolean canIMove(final int column, final int row) {
    boolean result = false;
    int colorofpiece = boardState.getPiece(row, column);
    switch (colorofpiece) {
      case GameConstants.RED:
        if (column != GameConstants.LAST_ROW_INDEX) {
          if (boardState.getPiece(row - 1, column + 1)
              == GameConstants.EMPTY) {
            result = true;
          }
        }
        if (column != 0) {
          if (boardState.getPiece(row - 1, column - 1)
              == GameConstants.EMPTY) {
            result = true;
          }
        }
        break;
      case GameConstants.BLACK:
        if (column != GameConstants.LAST_ROW_INDEX) {
          if (boardState.getPiece(row + 1, column + 1)
              == GameConstants.EMPTY) {
            result = true;
          }
        }
        if (column != 0) {
          if (boardState.getPiece(row + 1, column - 1)
              == GameConstants.EMPTY) {
            result = true;
          }
        }
        break;
      case GameConstants.BLACK_KING:
      case GameConstants.RED_KING:
        result = true;
      default:
        break;
    }
    return result;
  }

  /**
   * Checks if a piece can make a capture move.
   *
   * @param column the column of the piece
   * @param row    the row of the piece
   * @return true if a capture is possible, false otherwise
   */
  public boolean canITake(int column, int row, BoardState boardStateParam) {
    int piece = boardStateParam.getPiece(row, column);

    if (isKing(piece)) {
      return canKingTake(row, column, piece, boardStateParam);
    } else {
      return canRegularPieceTake(row, column, piece, boardStateParam);
    }
  }
  public boolean isKing(int piece) {
    return piece == GameConstants.BLACK_KING || piece == GameConstants.RED_KING;
  }
  public boolean canKingTake(int row, int col, int piece, BoardState boardStateParam) {
    int enemyColor1 = (piece == GameConstants.RED_KING) ? GameConstants.BLACK
        : GameConstants.RED;
    int enemyColor2 =
        (piece == GameConstants.RED_KING) ? GameConstants.BLACK_KING
            : GameConstants.RED_KING;

    for (int[] dir : GameConstants.DIRECTIONS) {
      for (int dist = 1; dist < GameConstants.BOARD_SIZE - 1; dist++) {
        int checkRow = row + dist * dir[0];
        int checkCol = col + dist * dir[1];

        if (!isValidPosition(checkRow, checkCol)) {
          break;
        }

        int checkPiece = boardStateParam.getPiece(checkRow, checkCol);
        if (checkPiece == enemyColor1 || checkPiece == enemyColor2) {

          int landRow = checkRow + dir[0];
          int landCol = checkCol + dir[1];

          if (isValidPosition(landRow, landCol) &&
              boardStateParam.getPiece(landRow, landCol) == GameConstants.EMPTY) {
            return true;
          }
          break;
        } else if (checkPiece != GameConstants.EMPTY) {
          break;
        }
      }
    }
    return false;
  }
  public boolean canRegularPieceTake(int row, int col, int piece,
                                      BoardState boardStateParam) {
    int direction = (piece == GameConstants.RED) ? -1 : 1;
    int enemyColor1 =
        (piece == GameConstants.RED) ? GameConstants.BLACK : GameConstants.RED;
    int enemyColor2 = (piece == GameConstants.RED) ? GameConstants.BLACK_KING
        : GameConstants.RED_KING;

    for (int colDir : new int[]{-1, 1}) {
      int jumpRow = row + 2 * direction;
      int jumpCol = col + 2 * colDir;
      int enemyRow = row + direction;
      int enemyCol = col + colDir;

      if (isValidPosition(jumpRow, jumpCol) && isValidPosition(enemyRow,
          enemyCol)) {
        int enemyPiece = boardStateParam.getPiece(enemyRow, enemyCol);
        if ((enemyPiece == enemyColor1 || enemyPiece == enemyColor2) &&
            boardStateParam.getPiece(jumpRow, jumpCol)== GameConstants.EMPTY) {
          return true;
        }
      }
    }
    return false;
  }
  public boolean isValidPosition(int row, int col) {
    return row >= 0 && row < GameConstants.BOARD_SIZE &&
        col >= 0 && col < GameConstants.BOARD_SIZE;
  }
  /**
   * Checks if the second click in a move is legal.
   *
   * @param columnsecond target column
   * @param rowsecond    target row
   * @param columnfirst  starting column
   * @param rowfirst     starting row
   * @param color        color of the piece
   * @return true if the second click is a legal move, false otherwise
   */
  public boolean isItLegalSecondClickMove(final int columnsecond,
                                          final int rowsecond,
                                          final int columnfirst,
                                          final int rowfirst,
                                          final int color) {
    boolean result = true;
    switch (color) {
      case GameConstants.RED_KING:
      case GameConstants.BLACK_KING:
        int i;
        int j;
        if (Math.abs(rowsecond - columnsecond) == Math.abs(
            rowfirst - columnfirst)
            || rowfirst + columnfirst == columnsecond + rowsecond) {
          if (rowsecond < rowfirst && columnsecond < columnfirst) {
            for (i = rowfirst - 1, j = columnfirst - 1;
                 i >= rowsecond && j >= columnsecond; i--, j--) {
              if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
                result = false;
              }
            }
          }
          if (rowsecond > rowfirst && columnsecond < columnfirst) {
            for (i = rowfirst + 1, j = columnfirst - 1;
                 i <= rowsecond && j >= columnsecond; i++, j--) {
              if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
                result = false;
              }
            }
          }
          if (rowsecond < rowfirst && columnsecond > columnfirst) {
            for (i = rowfirst - 1, j = columnfirst + 1;
                 i >= rowsecond && j <= columnsecond; i--, j++) {
              if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
                result = false;
              }
            }
          }
          if (rowsecond > rowfirst && columnsecond > columnfirst) {
            for (i = rowfirst + 1, j = columnfirst + 1;
                 i <= rowsecond && j <= columnsecond; i++, j++) {
              if (boardState.getPiece(i, j) != GameConstants.EMPTY) {
                result = false;
              }
            }
          }
          if (rowsecond == rowfirst || columnsecond == columnfirst) {
            result = false;
          }
        } else {
          result = false;
        }
        break;
      case GameConstants.RED:
        if (Math.abs(columnsecond - columnfirst) != 1) {
          result = false;
        }
        if (rowfirst - rowsecond != 1) {
          result = false;
        }
        break;
      case GameConstants.BLACK:
        if (Math.abs(columnsecond - columnfirst) != 1) {
          result = false;
        }
        if (rowsecond - rowfirst != 1) {
          result = false;
        }
        break;
      default:
        break;
    }
    if (boardState.getPiece(rowsecond,columnsecond) != GameConstants.EMPTY) {
      result = false;
    }
    return result;
  }
}
