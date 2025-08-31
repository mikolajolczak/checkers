package checkers.src.main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Bot {
  private final BoardPanel board;
  private final BoardState boardState;
  private final Move move;
  private final BoardController boardController;
  private final ArrayList<int[]> coordinates = new ArrayList<>();
  private int[] bestMoves = new int[GameConstants.MOVE_ARRAY_LENGTH];


  public Bot(final BoardPanel boardParam, final Move moveParam,
             final BoardController boardControllerParam, final BoardState boardStateParam) {
    this.board = boardParam;
    this.move = moveParam;
    this.boardController = boardControllerParam;
    this.boardState = boardStateParam;
  }

  public boolean checkAllPiecesPossibleTakes(final int color,
                                             final int colorQueen,
                                             final int[][] boardParam) {
    boolean result = false;
    for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        if (canITake(col, row, boardParam)) {
          if (boardParam[row][col] == color
              || boardParam[row][col] == colorQueen) {
            result = true;
          }
        }
      }
    }
    return result;
  }

  public void analyze() {


    if (move.checkAllPiecesPossibleTakes(boardController.getBotsColor(),
        boardController.getBotsKingColor())) {

      for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          if (boardState.getPiece(row, col) == boardController.getBotsColor()
              || boardState.getPiece(row, col)
              == boardController.getBotsKingColor()) {

            if (move.canITake(col, row)) {
              int row1;
              int col1;
              switch (boardState.getPiece(row, col)) {
                case GameConstants.RED:
                  if (row >= 2) {
                    if (col != 0 && col != 1) {
                      if (move.legalTakeMove(col - 2, row - 2, col, row,
                          GameConstants.RED)) {
                        int[] array = {row, col, row - 2, col - 2, GameConstants.TAKE};
                        coordinates.add(array);
                      }
                    }
                    if (col != GameConstants.SECOND_LAST_INDEX && col != GameConstants.LAST_ROW_INDEX) {
                      if (move.legalTakeMove(col + 2, row - 2, col, row,
                          GameConstants.RED)) {
                        int[] array = {row, col, row - 2, col + 2, GameConstants.TAKE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                case GameConstants.BLACK:
                  if (row < GameConstants.SECOND_LAST_INDEX) {
                    if (col != 0 && col != 1) {
                      if (move.legalTakeMove(col - 2, row + 2, col, row,
                          GameConstants.BLACK)) {

                        int[] array = {row, col, row + 2, col - 2, GameConstants.TAKE};
                        coordinates.add(array);
                      }
                    }
                    if (col != GameConstants.SECOND_LAST_INDEX && col != GameConstants.LAST_ROW_INDEX) {
                      if (move.legalTakeMove(col + 2, row + 2, col, row,
                          GameConstants.BLACK)) {

                        int[] array = {row, col, row + 2, col + 2, GameConstants.TAKE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                case GameConstants.BLACK_KING:
                  for (row1 = row + 1, col1 = col + 1;
                       row1 < GameConstants.BOARD_SIZE && col1 < GameConstants.BOARD_SIZE;
                       row1++, col1++) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        GameConstants.BLACK_KING)) {
                      if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0;
                       row1--, col1--) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        GameConstants.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row - 1, col1 = col + 1;
                       row1 >= 0 && col1 < GameConstants.BOARD_SIZE;
                       row1--, col1++) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        GameConstants.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }


                  }
                  for (row1 = row + 1, col1 = col - 1;
                       row1 < GameConstants.BOARD_SIZE && col1 >= 0;
                       row1++, col1--) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        GameConstants.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  break;
                case GameConstants.RED_KING:

                  for (row1 = row + 1, col1 = col + 1;
                       row1 < GameConstants.BOARD_SIZE && col1 < GameConstants.BOARD_SIZE;
                       row1++, col1++) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        GameConstants.RED_KING)) {


                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0;
                       row1--, col1--) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        GameConstants.RED_KING)) {


                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row - 1, col1 = col + 1;
                       row1 >= 0 && col1 < GameConstants.BOARD_SIZE;
                       row1--, col1++) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        GameConstants.RED_KING)) {


                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row + 1, col1 = col - 1;
                       row1 < GameConstants.BOARD_SIZE && col1 >= 0;
                       row1++, col1--) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        GameConstants.RED_KING)) {


                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }
                  }
                default:
                  break;
              }
              break;
            }
          }
        }

      }
    } else {
      for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          if (boardState.getPiece(row, col) == boardController.getBotsColor()
              || boardState.getPiece(row, col)
              == boardController.getBotsKingColor()) {

            if (move.canIMove(col, row)) {
              int row1;
              int col1;
              switch (boardState.getPiece(row, col)) {

                case GameConstants.RED:
                  if (row >= 1) {
                    if (col != 0) {
                      if (move.isItLegalSecondClickMove(col - 1, row - 1, col,
                          row, GameConstants.RED)) {
                        int[] array = {row, col, row - 1, col - 1, GameConstants.MOVE};
                        coordinates.add(array);

                      }
                    }
                    if (col != GameConstants.LAST_ROW_INDEX) {
                      if (move.isItLegalSecondClickMove(col + 1, row - 1, col,
                          row, GameConstants.RED)) {
                        int[] array = {row, col, row - 1, col + 1, GameConstants.MOVE};
                        coordinates.add(array);

                      }
                    }
                  }
                  break;
                case GameConstants.BLACK:
                  if (row < GameConstants.LAST_ROW_INDEX) {
                    if (col != 0) {
                      if (move.isItLegalSecondClickMove(col - 1, row + 1, col,
                          row, GameConstants.BLACK)) {
                        int[] array = {row, col, row + 1, col - 1, GameConstants.MOVE};
                        coordinates.add(array);

                      }
                    }
                    if (col != GameConstants.LAST_ROW_INDEX) {
                      if (move.isItLegalSecondClickMove(col + 1, row + 1, col,
                          row, GameConstants.BLACK)) {
                        int[] array = {row, col, row + 1, col + 1, GameConstants.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                case GameConstants.RED_KING:

                  for (row1 = row + 1, col1 = col + 1;
                       row1 < GameConstants.BOARD_SIZE && col1 < GameConstants.BOARD_SIZE;
                       row1++, col1++) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        GameConstants.RED_KING)) {
                      if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0;
                       row1--, col1--) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        GameConstants.RED_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  for (row1 = row - 1, col1 = col + 1;
                       row1 >= 0 && col1 < GameConstants.BOARD_SIZE;
                       row1--, col1++) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        GameConstants.RED_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.MOVE};
                        coordinates.add(array);
                      }

                    }
                  }
                  for (row1 = row + 1, col1 = col - 1;
                       row1 < GameConstants.BOARD_SIZE && col1 >= 0;
                       row1++, col1--) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        GameConstants.RED_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                case GameConstants.BLACK_KING:
                  for (row1 = row + 1, col1 = col + 1;
                       row1 < GameConstants.BOARD_SIZE && col1 < GameConstants.BOARD_SIZE;
                       row1++, col1++) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        GameConstants.BLACK_KING)) {
                      if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0;
                       row1--, col1--) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        GameConstants.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  for (row1 = row - 1, col1 = col + 1;
                       row1 >= 0 && col1 < GameConstants.BOARD_SIZE;
                       row1--, col1++) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        GameConstants.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.MOVE};
                        coordinates.add(array);
                      }

                    }
                  }
                  for (row1 = row + 1, col1 = col - 1;
                       row1 < GameConstants.BOARD_SIZE && col1 >= 0;
                       row1++, col1--) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        GameConstants.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, GameConstants.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                default:
                  break;
              }
            }
          }
        }
      }
    }
  }

  public void simulate() {
    int sumMax = GameConstants.INITIAL_SUM_MAX;
    for (int[] array : coordinates) {
      int[][] localpieces = new int[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
      for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          localpieces[row][col] = boardState.getPiece(row,col);
        }
      }

      int sum = 0;
      if (array[GameConstants.MOVE_TYPE] == GameConstants.MOVE) {
        if (localpieces[array[0]][array[1]] == GameConstants.RED
            || localpieces[array[0]][array[1]] == GameConstants.BLACK) {
          localpieces[array[0]][array[1]] = GameConstants.EMPTY;
          localpieces[array[2]][array[GameConstants.TO_COL]] = boardController.getBotsColor();
        }
        if (localpieces[array[0]][array[1]] == GameConstants.RED_KING
            || localpieces[array[0]][array[1]] == GameConstants.BLACK_KING) {
          localpieces[array[0]][array[1]] = GameConstants.EMPTY;
          localpieces[array[2]][array[GameConstants.TO_COL]] =
              boardController.getBotsKingColor();
        }
      } else if (array[GameConstants.MOVE_TYPE] == GameConstants.TAKE) {
        take(array[0], array[1], array[2], array[GameConstants.TO_COL],
            boardController.getBotsColor(), localpieces);
      } else if (array[GameConstants.MOVE_TYPE] == GameConstants.QUEEN_TAKE) {
        queenTake(array[0], array[1], array[2], array[GameConstants.TO_COL],
            boardController.getBotsKingColor(), localpieces);
      }
      if (checkAllPiecesPossibleTakes(boardController.getPlayersColor(),
          boardController.getPlayersKingColor(), localpieces)) {
        if (localpieces[array[2]][array[GameConstants.TO_COL]]
            == boardController.getBotsColor()) {
          sum -= GameConstants.SCORE_PLAYER_THREAT;
        }
        if (localpieces[array[2]][array[GameConstants.TO_COL]]
            == boardController.getBotsKingColor()) {
          sum -= GameConstants.SCORE_PLAYER_THREAT_KING;
        }
      }
      if (checkAllPiecesPossibleTakes(boardController.getBotsColor(),
          boardController.getBotsKingColor(), localpieces)) {
        sum += GameConstants.SCORE_TAKE_POSSIBLE;
      }
      if (isChanceForQueen(boardController.getBotsColor(), localpieces,
          localpieces[array[2]][array[GameConstants.TO_COL]])) {
        sum += GameConstants.SCORE_CHANCE_FOR_QUEEN;
      }
      if (sum >= sumMax) {
        bestMoves = array;
        sumMax = sum;
      }


    }
  }

  public boolean isChanceForQueen(final int colorToCheck,
                                  final int[][] boardParam,
                                  final int typeOfFigure) {
    boolean check = false;
    if (typeOfFigure != GameConstants.BLACK_KING && typeOfFigure != GameConstants.RED_KING) {
      for (int col = 0; col < GameConstants.LAST_ROW_INDEX; col++) {
        if (boardParam[GameConstants.LAST_ROW_INDEX][col] == colorToCheck
            && colorToCheck == GameConstants.BLACK) {
          check = true;
        }
        if (boardParam[0][col] == colorToCheck && colorToCheck == GameConstants.RED) {
          check = true;
        }
      }
    }

    return check;
  }

  public void move() {

    int rowFirst = bestMoves[0];
    int colFirst = bestMoves[1];
    int rowSecond = bestMoves[2];
    int colSecond = bestMoves[GameConstants.TO_COL];

    if (bestMoves[GameConstants.MOVE_TYPE] == GameConstants.MOVE) {
      if (boardState.getPiece(rowFirst, colFirst)
          == boardController.getBotsColor()) {
        boardState.setPiece(rowFirst, colFirst, GameConstants.EMPTY);
        boardState.setPiece(rowSecond, colSecond, boardController.getBotsColor());

      } else if (boardState.getPiece(rowFirst, colFirst)
          == boardController.getBotsKingColor()) {
        boardState.setPiece(rowFirst, colFirst, GameConstants.EMPTY);
        boardState.setPiece(rowSecond, colSecond, boardController.getBotsKingColor());
      }
    } else if (bestMoves[GameConstants.MOVE_TYPE] == GameConstants.TAKE) {

      boardController.take(rowFirst, colFirst, rowSecond, colSecond,
          boardController.getBotsColor());
    } else if (bestMoves[GameConstants.MOVE_TYPE] == GameConstants.QUEEN_TAKE) {
      boardController.queenTake(rowFirst, colFirst, rowSecond, colSecond,
          boardController.getBotsKingColor());
    }
    if (rowSecond == GameConstants.LAST_ROW_INDEX
        && boardController.getBotsColor() == GameConstants.BLACK) {
      boardState.setPiece(rowSecond, colSecond, GameConstants.BLACK_KING);
    }
    if (rowSecond == 0 && boardController.getBotsColor() == GameConstants.RED) {
      boardState.setPiece(rowSecond, colSecond, GameConstants.RED_KING);
    }

    board.repaint();

    coordinates.clear();
    Arrays.fill(bestMoves, 0);
    boardController.getFrame().isGameFinished();
  }

  public void take(final int firstRow, final int firstColumn,
                   final int secondRow,
                   final int secondColumn, final int currentColor,
                   final int[][] boardParam) {
    boardParam[firstRow][firstColumn] = GameConstants.EMPTY;
    boardParam[secondRow][secondColumn] = currentColor;
    int rowBetween = (firstRow + secondRow) / 2;
    int colBetween = (firstColumn + secondColumn) / 2;
    boardParam[rowBetween][colBetween] = GameConstants.EMPTY;
  }

  public boolean canITake(final int column, final int row,
                          final int[][] boardParam) {
    boolean result = false;
    int i;
    int j;
    int colorofpiece = boardParam[row][column];
    switch (colorofpiece) {
      case GameConstants.RED:
        if (row >= 2) {
          if (column == GameConstants.LAST_ROW_INDEX || column == GameConstants.SECOND_LAST_INDEX) {
            if ((boardParam[row - 1][column - 1] == GameConstants.BLACK
                || boardParam[row - 1][column - 1] == GameConstants.BLACK_KING)
                && boardParam[row - 2][column - 2] == GameConstants.EMPTY) {
              result = true;
            }
          } else if (column == 0 || column == 1) {
            if ((boardParam[row - 1][column + 1] == GameConstants.BLACK
                || boardParam[row - 1][column + 1] == GameConstants.BLACK_KING)
                && boardParam[row - 2][column + 2] == GameConstants.EMPTY) {
              result = true;
            }
          } else if (((boardParam[row - 1][column + 1] == GameConstants.BLACK
              || boardParam[row - 1][column + 1] == GameConstants.BLACK_KING)
              && boardParam[row - 2][column + 2] == GameConstants.EMPTY) || (
              (boardParam[row - 1][column - 1] == GameConstants.BLACK
                  || boardParam[row - 1][column - 1] == GameConstants.BLACK_KING)
                  && boardParam[row - 2][column - 2] == GameConstants.EMPTY)) {
            result = true;
          }
        }
        break;
      case GameConstants.BLACK:
        if (row < GameConstants.SECOND_LAST_INDEX) {
          if (column == GameConstants.LAST_ROW_INDEX || column == GameConstants.SECOND_LAST_INDEX) {
            if ((boardParam[row + 1][column - 1] == GameConstants.RED
                || boardParam[row + 1][column - 1] == GameConstants.RED_KING)
                && boardParam[row + 2][column - 2] == GameConstants.EMPTY) {
              result = true;
            }
          } else if (column == 0 || column == 1) {
            if ((boardParam[row + 1][column + 1] == GameConstants.RED
                || boardParam[row + 1][column + 1] == GameConstants.RED_KING)
                && boardParam[row + 2][column + 2] == GameConstants.EMPTY) {
              result = true;
            }
          } else {
            if (((boardParam[row + 1][column + 1] == GameConstants.RED
                || boardParam[row + 1][column + 1] == GameConstants.RED_KING)
                && boardParam[row + 2][column + 2] == GameConstants.EMPTY) || (
                (boardParam[row + 1][column - 1] == GameConstants.RED
                    || boardParam[row + 1][column - 1] == GameConstants.RED_KING)
                    && boardParam[row + 2][column - 2] == GameConstants.EMPTY)) {
              result = true;
            }
          }
        }
        break;
      case GameConstants.BLACK_KING:
        for (i = row - 1, j = column - 1; i > 0 && j > 0; i--, j--) {
          if (boardParam[i][j] == GameConstants.RED_KING
              || boardParam[i][j] == GameConstants.RED) {
            if (boardParam[i - 1][j - 1] == GameConstants.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row + 1, j = column - 1; i < GameConstants.LAST_ROW_INDEX && j > 0;
             i++, j--) {
          if (boardParam[i][j] == GameConstants.RED_KING
              || boardParam[i][j] == GameConstants.RED) {
            if (boardParam[i + 1][j - 1] == GameConstants.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row - 1, j = column + 1; i > 0 && j < GameConstants.LAST_ROW_INDEX;
             i--, j++) {
          if (boardParam[i][j] == GameConstants.RED_KING
              || boardParam[i][j] == GameConstants.RED) {
            if (boardParam[i - 1][j + 1] == GameConstants.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row + 1, j = column + 1;
             i < GameConstants.LAST_ROW_INDEX && j < GameConstants.LAST_ROW_INDEX; i++, j++) {
          if (boardParam[i][j] == GameConstants.RED_KING
              || boardParam[i][j] == GameConstants.RED) {
            if (boardParam[i + 1][j + 1] == GameConstants.EMPTY) {
              result = true;
            }
          }
        }
        break;
      case GameConstants.RED_KING:
        for (i = row - 1, j = column - 1; i > 0 && j > 0; i--, j--) {
          if (boardParam[i][j] == GameConstants.BLACK_KING
              || boardParam[i][j] == GameConstants.BLACK) {
            if (boardParam[i - 1][j - 1] == GameConstants.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row + 1, j = column - 1; i < GameConstants.LAST_ROW_INDEX && j > 0;
             i++, j--) {
          if (boardParam[i][j] == GameConstants.BLACK_KING
              || boardParam[i][j] == GameConstants.BLACK) {
            if (boardParam[i + 1][j - 1] == GameConstants.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row - 1, j = column + 1; i > 0 && j < GameConstants.LAST_ROW_INDEX;
             i--, j++) {
          if (boardParam[i][j] == GameConstants.BLACK_KING
              || boardParam[i][j] == GameConstants.BLACK) {
            if (boardParam[i - 1][j + 1] == GameConstants.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row + 1, j = column + 1;
             i < GameConstants.LAST_ROW_INDEX && j < GameConstants.LAST_ROW_INDEX; i++, j++) {
          if (boardParam[i][j] == GameConstants.BLACK_KING
              || boardParam[i][j] == GameConstants.BLACK) {
            if (boardParam[i + 1][j + 1] == GameConstants.EMPTY) {
              result = true;
            }
          }
        }
        break;
      default:
        break;
    }
    return result;
  }

  public void queenTake(final int firstRow, final int firstColumn,
                        final int secondRow,
                        final int secondColumn, final int currentColor,
                        final int[][] boardParam) {
    boardParam[firstRow][firstColumn] = GameConstants.EMPTY;
    boardParam[secondRow][secondColumn] = currentColor;
    if (secondRow < firstRow && secondColumn < firstColumn) {
      boardParam[secondRow + 1][secondColumn + 1] = GameConstants.EMPTY;
    }

    if (secondRow > firstRow && secondColumn < firstColumn) {
      boardParam[secondRow - 1][secondColumn + 1] = GameConstants.EMPTY;
    }
    if (secondRow < firstRow && secondColumn > firstColumn) {
      boardParam[secondRow + 1][secondColumn - 1] = GameConstants.EMPTY;
    }
    if (secondRow > firstRow && secondColumn > firstColumn) {
      boardParam[secondRow - 1][secondColumn - 1] = GameConstants.EMPTY;
    }
  }
}
