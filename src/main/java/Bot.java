package checkers.src.main.java;

import java.util.ArrayList;
import java.util.Arrays;

public class Bot {
  private final Board board;
  private final Move move;
  private final BoardController boardController;

  private static final int MOVE_ARRAY_LENGTH = 5;
  private static final int INITIAL_SUM_MAX = -100;
  private int[] bestMoves = new int[MOVE_ARRAY_LENGTH];
  private final ArrayList<int[]> coordinates = new ArrayList<>();
  private static final int MOVE = 0;
  private static final int TAKE = 1;
  private static final int QUEEN_TAKE = 2;
  private static final int LAST_ROW_INDEX = Board.BOARD_SIZE - 1;
  private static final int SECOND_LAST_INDEX = Board.BOARD_SIZE - 2;
  private static final int TO_COL = 3;
  private static final int MOVE_TYPE = 4;
  private static final int SCORE_PLAYER_THREAT = 20;
  private static final int SCORE_PLAYER_THREAT_KING = 30;
  private static final int SCORE_TAKE_POSSIBLE = 10;
  private static final int SCORE_CHANCE_FOR_QUEEN = 15;

  public Bot(final Board boardParam, final Move moveParam,
             final BoardController boardControllerParam) {
    this.board = boardParam;
    this.move = moveParam;
    this.boardController = boardControllerParam;
  }

  public boolean checkAllPiecesPossibleTakes(final int color,
                                             final int colorQueen,
                                             final int[][] boardParam) {
    boolean result = false;
    for (int row = 0; row < Board.BOARD_SIZE; row++) {
      for (int col = 0; col < Board.BOARD_SIZE; col++) {
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

      for (int row = 0; row < Board.BOARD_SIZE; row++) {
        for (int col = 0; col < Board.BOARD_SIZE; col++) {
          if (board.getValueOfPiece(row, col) == boardController.getBotsColor()
              || board.getValueOfPiece(row, col)
              == boardController.getBotsKingColor()) {

            if (move.canITake(col, row)) {
              int row1;
              int col1;
              switch (board.getValueOfPiece(row, col)) {
                case Board.RED:
                  if (row >= 2) {
                    if (col != 0 && col != 1) {
                      if (move.legalTakeMove(col - 2, row - 2, col, row,
                          Board.RED)) {
                        int[] array = {row, col, row - 2, col - 2, Bot.TAKE};
                        coordinates.add(array);
                      }
                    }
                    if (col != SECOND_LAST_INDEX && col != LAST_ROW_INDEX) {
                      if (move.legalTakeMove(col + 2, row - 2, col, row,
                          Board.RED)) {
                        int[] array = {row, col, row - 2, col + 2, Bot.TAKE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                case Board.BLACK:
                  if (row < SECOND_LAST_INDEX) {
                    if (col != 0 && col != 1) {
                      if (move.legalTakeMove(col - 2, row + 2, col, row,
                          Board.BLACK)) {

                        int[] array = {row, col, row + 2, col - 2, Bot.TAKE};
                        coordinates.add(array);
                      }
                    }
                    if (col != SECOND_LAST_INDEX && col != LAST_ROW_INDEX) {
                      if (move.legalTakeMove(col + 2, row + 2, col, row,
                          Board.BLACK)) {

                        int[] array = {row, col, row + 2, col + 2, Bot.TAKE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                case Board.BLACK_KING:
                  for (row1 = row + 1, col1 = col + 1;
                       row1 < Board.BOARD_SIZE && col1 < Board.BOARD_SIZE;
                       row1++, col1++) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        Board.BLACK_KING)) {
                      if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0;
                       row1--, col1--) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        Board.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row - 1, col1 = col + 1;
                       row1 >= 0 && col1 < Board.BOARD_SIZE;
                       row1--, col1++) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        Board.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }


                  }
                  for (row1 = row + 1, col1 = col - 1;
                       row1 < Board.BOARD_SIZE && col1 >= 0;
                       row1++, col1--) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        Board.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  break;
                case Board.RED_KING:

                  for (row1 = row + 1, col1 = col + 1;
                       row1 < Board.BOARD_SIZE && col1 < Board.BOARD_SIZE;
                       row1++, col1++) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        Board.RED_KING)) {


                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0;
                       row1--, col1--) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        Board.RED_KING)) {


                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row - 1, col1 = col + 1;
                       row1 >= 0 && col1 < Board.BOARD_SIZE;
                       row1--, col1++) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        Board.RED_KING)) {


                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.QUEEN_TAKE};
                        coordinates.add(array);
                      }
                    }

                  }
                  for (row1 = row + 1, col1 = col - 1;
                       row1 < Board.BOARD_SIZE && col1 >= 0;
                       row1++, col1--) {
                    if (move.legalTakeMove(col1, row1, col, row,
                        Board.RED_KING)) {


                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.QUEEN_TAKE};
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
      for (int row = 0; row < Board.BOARD_SIZE; row++) {
        for (int col = 0; col < Board.BOARD_SIZE; col++) {
          if (board.getValueOfPiece(row, col) == boardController.getBotsColor()
              || board.getValueOfPiece(row, col)
              == boardController.getBotsKingColor()) {

            if (move.canIMove(col, row)) {
              int row1;
              int col1;
              switch (board.getValueOfPiece(row, col)) {

                case Board.RED:
                  if (row >= 1) {
                    if (col != 0) {
                      if (move.isItLegalSecondClickMove(col - 1, row - 1, col,
                          row, Board.RED)) {
                        int[] array = {row, col, row - 1, col - 1, Bot.MOVE};
                        coordinates.add(array);

                      }
                    }
                    if (col != LAST_ROW_INDEX) {
                      if (move.isItLegalSecondClickMove(col + 1, row - 1, col,
                          row, Board.RED)) {
                        int[] array = {row, col, row - 1, col + 1, Bot.MOVE};
                        coordinates.add(array);

                      }
                    }
                  }
                  break;
                case Board.BLACK:
                  if (row < LAST_ROW_INDEX) {
                    if (col != 0) {
                      if (move.isItLegalSecondClickMove(col - 1, row + 1, col,
                          row, Board.BLACK)) {
                        int[] array = {row, col, row + 1, col - 1, Bot.MOVE};
                        coordinates.add(array);

                      }
                    }
                    if (col != LAST_ROW_INDEX) {
                      if (move.isItLegalSecondClickMove(col + 1, row + 1, col,
                          row, Board.BLACK)) {
                        int[] array = {row, col, row + 1, col + 1, Bot.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                case Board.RED_KING:

                  for (row1 = row + 1, col1 = col + 1;
                       row1 < Board.BOARD_SIZE && col1 < Board.BOARD_SIZE;
                       row1++, col1++) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        Board.RED_KING)) {
                      if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0;
                       row1--, col1--) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        Board.RED_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  for (row1 = row - 1, col1 = col + 1;
                       row1 >= 0 && col1 < Board.BOARD_SIZE;
                       row1--, col1++) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        Board.RED_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.MOVE};
                        coordinates.add(array);
                      }

                    }
                  }
                  for (row1 = row + 1, col1 = col - 1;
                       row1 < Board.BOARD_SIZE && col1 >= 0;
                       row1++, col1--) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        Board.RED_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  break;
                case Board.BLACK_KING:
                  for (row1 = row + 1, col1 = col + 1;
                       row1 < Board.BOARD_SIZE && col1 < Board.BOARD_SIZE;
                       row1++, col1++) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        Board.BLACK_KING)) {
                      if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0;
                       row1--, col1--) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        Board.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.MOVE};
                        coordinates.add(array);
                      }
                    }
                  }
                  for (row1 = row - 1, col1 = col + 1;
                       row1 >= 0 && col1 < Board.BOARD_SIZE;
                       row1--, col1++) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        Board.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.MOVE};
                        coordinates.add(array);
                      }

                    }
                  }
                  for (row1 = row + 1, col1 = col - 1;
                       row1 < Board.BOARD_SIZE && col1 >= 0;
                       row1++, col1--) {
                    if (move.isItLegalSecondClickMove(col1, row1, col, row,
                        Board.BLACK_KING)) {
                      if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1,
                          row1)) {
                        int[] array = {row, col, row1, col1, Bot.MOVE};
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
    int sumMax = INITIAL_SUM_MAX;
    for (int[] array : coordinates) {
      int[][] localpieces = new int[Board.BOARD_SIZE][Board.BOARD_SIZE];
      for (int row = 0; row < Board.BOARD_SIZE; row++) {
        for (int col = 0; col < Board.BOARD_SIZE; col++) {
          localpieces[row][col] = board.getPieces()[row][col];
        }
      }

      int sum = 0;
      if (array[MOVE_TYPE] == Bot.MOVE) {
        if (localpieces[array[0]][array[1]] == Board.RED
            || localpieces[array[0]][array[1]] == Board.BLACK) {
          localpieces[array[0]][array[1]] = Board.EMPTY;
          localpieces[array[2]][array[TO_COL]] = boardController.getBotsColor();
        }
        if (localpieces[array[0]][array[1]] == Board.RED_KING
            || localpieces[array[0]][array[1]] == Board.BLACK_KING) {
          localpieces[array[0]][array[1]] = Board.EMPTY;
          localpieces[array[2]][array[TO_COL]] =
              boardController.getBotsKingColor();
        }
      } else if (array[MOVE_TYPE] == Bot.TAKE) {
        take(array[0], array[1], array[2], array[TO_COL],
            boardController.getBotsColor(), localpieces);
      } else if (array[MOVE_TYPE] == Bot.QUEEN_TAKE) {
        queenTake(array[0], array[1], array[2], array[TO_COL],
            boardController.getBotsKingColor(), localpieces);
      }
      if (checkAllPiecesPossibleTakes(boardController.getPlayersColor(),
          boardController.getPlayersKingColor(), localpieces)) {
        if (localpieces[array[2]][array[TO_COL]]
            == boardController.getBotsColor()) {
          sum -= SCORE_PLAYER_THREAT;
        }
        if (localpieces[array[2]][array[TO_COL]]
            == boardController.getBotsKingColor()) {
          sum -= SCORE_PLAYER_THREAT_KING;
        }
      }
      if (checkAllPiecesPossibleTakes(boardController.getBotsColor(),
          boardController.getBotsKingColor(), localpieces)) {
        sum += SCORE_TAKE_POSSIBLE;
      }
      if (isChanceForQueen(boardController.getBotsColor(), localpieces,
          localpieces[array[2]][array[TO_COL]])) {
        sum += SCORE_CHANCE_FOR_QUEEN;
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
    if (typeOfFigure != Board.BLACK_KING && typeOfFigure != Board.RED_KING) {
      for (int col = 0; col < LAST_ROW_INDEX; col++) {
        if (boardParam[LAST_ROW_INDEX][col] == colorToCheck
            && colorToCheck == Board.BLACK) {
          check = true;
        }
        if (boardParam[0][col] == colorToCheck && colorToCheck == Board.RED) {
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
    int colSecond = bestMoves[TO_COL];

    if (bestMoves[MOVE_TYPE] == Bot.MOVE) {
      if (board.getValueOfPiece(rowFirst, colFirst)
          == boardController.getBotsColor()) {
        board.getPieces()[rowFirst][colFirst] = Board.EMPTY;
        board.getPieces()[rowSecond][colSecond] =
            boardController.getBotsColor();

      } else if (board.getValueOfPiece(rowFirst, colFirst)
          == boardController.getBotsKingColor()) {
        board.getPieces()[rowFirst][colFirst] = Board.EMPTY;
        board.getPieces()[rowSecond][colSecond] =
            boardController.getBotsKingColor();
      }
    } else if (bestMoves[MOVE_TYPE] == Bot.TAKE) {

      boardController.take(rowFirst, colFirst, rowSecond, colSecond,
          boardController.getBotsColor());
    } else if (bestMoves[MOVE_TYPE] == Bot.QUEEN_TAKE) {
      boardController.queenTake(rowFirst, colFirst, rowSecond, colSecond,
          boardController.getBotsKingColor());
    }
    if (rowSecond == LAST_ROW_INDEX
        && boardController.getBotsColor() == Board.BLACK) {
      board.getPieces()[rowSecond][colSecond] = Board.BLACK_KING;
    }
    if (rowSecond == 0 && boardController.getBotsColor() == Board.RED) {
      board.getPieces()[rowSecond][colSecond] = Board.RED_KING;
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
    boardParam[firstRow][firstColumn] = Board.EMPTY;
    boardParam[secondRow][secondColumn] = currentColor;
    int rowBetween = (firstRow + secondRow) / 2;
    int colBetween = (firstColumn + secondColumn) / 2;
    boardParam[rowBetween][colBetween] = Board.EMPTY;
  }

  public boolean canITake(final int column, final int row,
                          final int[][] boardParam) {
    boolean result = false;
    int i;
    int j;
    int colorofpiece = boardParam[row][column];
    switch (colorofpiece) {
      case Board.RED:
        if (row >= 2) {
          if (column == LAST_ROW_INDEX || column == SECOND_LAST_INDEX) {
            if ((boardParam[row - 1][column - 1] == Board.BLACK
                || boardParam[row - 1][column - 1] == Board.BLACK_KING)
                && boardParam[row - 2][column - 2] == Board.EMPTY) {
              result = true;
            }
          } else if (column == 0 || column == 1) {
            if ((boardParam[row - 1][column + 1] == Board.BLACK
                || boardParam[row - 1][column + 1] == Board.BLACK_KING)
                && boardParam[row - 2][column + 2] == Board.EMPTY) {
              result = true;
            }
          } else if (((boardParam[row - 1][column + 1] == Board.BLACK
              || boardParam[row - 1][column + 1] == Board.BLACK_KING)
              && boardParam[row - 2][column + 2] == Board.EMPTY) || (
              (boardParam[row - 1][column - 1] == Board.BLACK
                  || boardParam[row - 1][column - 1] == Board.BLACK_KING)
                  && boardParam[row - 2][column - 2] == Board.EMPTY)) {
            result = true;
          }
        }
        break;
      case Board.BLACK:
        if (row < SECOND_LAST_INDEX) {
          if (column == LAST_ROW_INDEX || column == SECOND_LAST_INDEX) {
            if ((boardParam[row + 1][column - 1] == Board.RED
                || boardParam[row + 1][column - 1] == Board.RED_KING)
                && boardParam[row + 2][column - 2] == Board.EMPTY) {
              result = true;
            }
          } else if (column == 0 || column == 1) {
            if ((boardParam[row + 1][column + 1] == Board.RED
                || boardParam[row + 1][column + 1] == Board.RED_KING)
                && boardParam[row + 2][column + 2] == Board.EMPTY) {
              result = true;
            }
          } else {
            if (((boardParam[row + 1][column + 1] == Board.RED
                || boardParam[row + 1][column + 1] == Board.RED_KING)
                && boardParam[row + 2][column + 2] == Board.EMPTY) || (
                (boardParam[row + 1][column - 1] == Board.RED
                    || boardParam[row + 1][column - 1] == Board.RED_KING)
                    && boardParam[row + 2][column - 2] == Board.EMPTY)) {
              result = true;
            }
          }
        }
        break;
      case Board.BLACK_KING:
        for (i = row - 1, j = column - 1; i > 0 && j > 0; i--, j--) {
          if (boardParam[i][j] == Board.RED_KING
              || boardParam[i][j] == Board.RED) {
            if (boardParam[i - 1][j - 1] == Board.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row + 1, j = column - 1; i < LAST_ROW_INDEX && j > 0;
             i++, j--) {
          if (boardParam[i][j] == Board.RED_KING
              || boardParam[i][j] == Board.RED) {
            if (boardParam[i + 1][j - 1] == Board.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row - 1, j = column + 1; i > 0 && j < LAST_ROW_INDEX;
             i--, j++) {
          if (boardParam[i][j] == Board.RED_KING
              || boardParam[i][j] == Board.RED) {
            if (boardParam[i - 1][j + 1] == Board.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row + 1, j = column + 1;
             i < LAST_ROW_INDEX && j < LAST_ROW_INDEX; i++, j++) {
          if (boardParam[i][j] == Board.RED_KING
              || boardParam[i][j] == Board.RED) {
            if (boardParam[i + 1][j + 1] == Board.EMPTY) {
              result = true;
            }
          }
        }
        break;
      case Board.RED_KING:
        for (i = row - 1, j = column - 1; i > 0 && j > 0; i--, j--) {
          if (boardParam[i][j] == Board.BLACK_KING
              || boardParam[i][j] == Board.BLACK) {
            if (boardParam[i - 1][j - 1] == Board.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row + 1, j = column - 1; i < LAST_ROW_INDEX && j > 0;
             i++, j--) {
          if (boardParam[i][j] == Board.BLACK_KING
              || boardParam[i][j] == Board.BLACK) {
            if (boardParam[i + 1][j - 1] == Board.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row - 1, j = column + 1; i > 0 && j < LAST_ROW_INDEX;
             i--, j++) {
          if (boardParam[i][j] == Board.BLACK_KING
              || boardParam[i][j] == Board.BLACK) {
            if (boardParam[i - 1][j + 1] == Board.EMPTY) {
              result = true;
            }
          }
        }
        for (i = row + 1, j = column + 1;
             i < LAST_ROW_INDEX && j < LAST_ROW_INDEX; i++, j++) {
          if (boardParam[i][j] == Board.BLACK_KING
              || boardParam[i][j] == Board.BLACK) {
            if (boardParam[i + 1][j + 1] == Board.EMPTY) {
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
    boardParam[firstRow][firstColumn] = Board.EMPTY;
    boardParam[secondRow][secondColumn] = currentColor;
    if (secondRow < firstRow && secondColumn < firstColumn) {
      boardParam[secondRow + 1][secondColumn + 1] = Board.EMPTY;
    }

    if (secondRow > firstRow && secondColumn < firstColumn) {
      boardParam[secondRow - 1][secondColumn + 1] = Board.EMPTY;
    }
    if (secondRow < firstRow && secondColumn > firstColumn) {
      boardParam[secondRow + 1][secondColumn - 1] = Board.EMPTY;
    }
    if (secondRow > firstRow && secondColumn > firstColumn) {
      boardParam[secondRow - 1][secondColumn - 1] = Board.EMPTY;
    }
  }
}
