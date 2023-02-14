package Checkers;

import java.util.ArrayList;
import java.util.Arrays;

public class Bot {
    private Board board;
    private Move move;
    private BoardController boardController; //stad ktory kolor ma ruch
    //    private ArrayList<Integer> coordinates = new ArrayList<Integer>(); // zapis jednego ruchu: [wiersz1, kolumna1, wiersz2, kolumna2, czyBicie(0/1)]
//    private ArrayList<Integer> bestMoves = new ArrayList<Integer>(); //lista z najlepszymi ruchami
    private int[] bestMoves = new int[5];
    private ArrayList<int[]> coordinates = new ArrayList<>();
    private final static int MOVE = 0;
    private final static int TAKE = 1;
    private final static int QUEENTAKE = 2;

    public Bot(Board board, Move move, BoardController boardController) {
        this.board = board;
        this.move = move;
        this.boardController = boardController;
    }

    public boolean checkAllPiecesPossibleTakes(int color, int colorQueen, int[][] board) {
        boolean result = false;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (canITake(col, row, board)) {
                    if (board[row][col] == color || board[row][col] == colorQueen)
                        result = true;
                }
            }
        }
        return result;
    }

    public void analyze() {
        // przejscie przez plansze, dodanie mozliwych wspolrzednych
        //dodawanie do coordinates
//        int[][] = board.pieces;
        if (move.checkAllPiecesPossibleTakes(boardController.botsColor, boardController.botsKingColor)) { //jesli trzeba zbic -> znalezienie wszystkich mozliwosci bicia
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board.getValueOfPiece(row, col) == boardController.botsColor || board.getValueOfPiece(row, col) == boardController.botsKingColor) { //jezeli to jest pionek bota
                        if (move.canITake(col, row)) { //na razie tylko dla zwyklych pionkow
                            switch (board.getValueOfPiece(row, col)) {
                                case Board.RED:
                                    if (row >= 2) {
                                        if (col != 0 && col != 1) {
                                            if (move.legalTakeMove(col - 2, row - 2, col, row, Board.RED)) {
                                                int[] array = {row, col, row - 2, col - 2, Bot.TAKE};
                                                coordinates.add(array);
                                            }
                                        }
                                        if (col != 6 && col != 7) {
                                            if (move.legalTakeMove(col + 2, row - 2, col, row, Board.RED)) {
                                                int[] array = {row, col, row - 2, col + 2, Bot.TAKE};
                                                coordinates.add(array);
                                            }
                                        }
                                    }
                                    break;
                                case Board.BLACK:
                                    if (row <= 5) {
                                        if (col != 0 && col != 1) {
                                            if (move.legalTakeMove(col - 2, row + 2, col, row, Board.BLACK)) {

                                                int[] array = {row, col, row + 2, col - 2, Bot.TAKE};
                                                coordinates.add(array);
                                            }
                                        }
                                        if (col != 6 && col != 7) {
                                            if (move.legalTakeMove(col + 2, row + 2, col, row, Board.BLACK)) {

                                                int[] array = {row, col, row + 2, col + 2, Bot.TAKE};
                                                coordinates.add(array);
                                            }
                                        }
                                    }
                                    break;
                                case Board.BLACKKING:
                                    int row1, col1;
                                    for (row1 = row + 1, col1 = col + 1; row1 <= 7 && col1 <= 7; row1++, col1++) { //prawy dolny skos
                                        if(move.legalTakeMove(col1,row1,col,row,Board.BLACKKING))
                                        //if ((board.getValueOfPiece(row1, col1) == Board.BLACKKING || board.getValueOfPiece(row1, col1) == Board.BLACK) && (row1 != 7 || col1 != 7) && (row1 != 6 || col1 != 6) && board.getValueOfPiece(row1 + 1, col1 + 1) == Board.EMPTY && board.getValueOfPiece(row1 - 1, col1 - 1) == Board.EMPTY) {
                                            if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.QUEENTAKE};
                                                coordinates.add(array);
                                            }
                                        //}
                                    }
                                    for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0; row1--, col1--) {// lewy górny skos
                                        if(move.legalTakeMove(col1,row1,col,row,Board.BLACKKING))
                                        //if ((board.getValueOfPiece(row1, col1) == Board.BLACKKING || board.getValueOfPiece(row1, col1) == Board.BLACK) && (row1 != 0 || col1 != 0) && (row1 != 1 || col1 != 1) && board.getValueOfPiece(row1 - 1, col1 - 1) == Board.EMPTY && board.getValueOfPiece(row1 + 1, col1 + 1) == Board.EMPTY) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.QUEENTAKE};
                                                coordinates.add(array);
                                            }
                                       // }
                                    }
                                    for (row1 = row - 1, col1 = col + 1; row1 >= 0 && col1 <= 7; row1--, col1++) {// prawy górny skos
                                        if(move.legalTakeMove(col1,row1,col,row,Board.BLACKKING))
                                        //if ((board.getValueOfPiece(row1, col1) == Board.BLACKKING || board.getValueOfPiece(row1, col1) == Board.BLACK) && (row1 != 0 || col1 != 7) && (row1 != 1 || col1 != 6) && board.getValueOfPiece(row1 - 1, col1 + 1) == Board.EMPTY && board.getValueOfPiece(row1 + 1, col1 - 1) == Board.EMPTY) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1, col1 , Bot.QUEENTAKE};
                                                coordinates.add(array);
                                            }

//                                        }
                                    }
                                    for (row1 = row + 1, col1 = col - 1; row1 <= 7 && col1 >= 0; row1++, col1--) {// lewy dolny skos
                                        if(move.legalTakeMove(col1,row1,col,row,Board.BLACKKING))
                                        //if ((board.getValueOfPiece(row1, col1) == Board.BLACKKING || board.getValueOfPiece(row1, col1) == Board.BLACK) && (row1 != 7 || col1 != 0) && (row1 != 6 || col1 != 1) && board.getValueOfPiece(row1 + 1, col1 - 1) == Board.EMPTY && board.getValueOfPiece(row1 - 1, col1 + 1) == Board.EMPTY) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.QUEENTAKE};
                                                coordinates.add(array);
                                            }
                                        //}
                                    }
                                    break;
                                case Board.REDKING:
                                    //int row1, col1;
                                    for (row1 = row + 1, col1 = col + 1; row1 <= 7 && col1 <= 7; row1++, col1++) { //prawy dolny skos
                                         if (move.legalTakeMove(col1, row1, col, row, Board.REDKING)) {
                                        //if ((board.getValueOfPiece(row1, col1) == Board.REDKING || board.getValueOfPiece(row1, col1) == Board.RED) && (row1 != 7 || col1 != 7) && (row1 != 6 || col1 != 6) && board.getValueOfPiece(row1 + 1, col1 + 1) == Board.EMPTY && board.getValueOfPiece(row1 - 1, col1 - 1) == Board.EMPTY) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.QUEENTAKE};
                                                coordinates.add(array);
                                            }
                                        }
                                        //}
                                    }
                                    for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0; row1--, col1--) {// lewy górny skos
                                        if (move.legalTakeMove(col1, row1, col, row, Board.REDKING)) {
                                        //if ((board.getValueOfPiece(row1, col1) == Board.REDKING || board.getValueOfPiece(row1, col1) == Board.RED) && (row1 != 0 || col1 != 0) && (row1 != 1 || col1 != 1) && board.getValueOfPiece(row1 - 1, col1 - 1) == Board.EMPTY && board.getValueOfPiece(row1 + 1, col1 + 1) == Board.EMPTY) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.QUEENTAKE};
                                                coordinates.add(array);
                                            }
                                        }
                                        //}
                                    }
                                    for (row1 = row - 1, col1 = col + 1; row1 >=0 && col1 <= 7; row1--, col1++) {// prawy górny skos
                                        if (move.legalTakeMove(col1, row1, col, row, Board.REDKING)) {
                                        //if ((board.getValueOfPiece(row1, col1) == Board.REDKING || board.getValueOfPiece(row1, col1) == Board.RED) && (row1 != 0 || col1 != 7) && (row1 != 1 || col1 != 6) && board.getValueOfPiece(row1 - 1, col1 + 1) == Board.EMPTY && board.getValueOfPiece(row1 - 1, col1 - 1) == Board.EMPTY) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1, Bot.QUEENTAKE};
                                                coordinates.add(array);
                                            }
                                        }
                                        //}
                                    }
                                    for (row1 = row + 1, col1 = col - 1; row1 <= 7 && col1 >= 0; row1++, col1--) {// lewy dolny skos
                                         if (move.legalTakeMove(col1, row1, col, row, Board.REDKING)) {
                                        //if ((board.getValueOfPiece(row1, col1) == Board.REDKING || board.getValueOfPiece(row1, col1) == Board.RED) && (row1 != 7 || col1 != 0) && (row1 != 6 || col1 != 1) && board.getValueOfPiece(row1 + 1, col1 - 1) == Board.EMPTY && board.getValueOfPiece(row1 - 1, col1 + 1) == Board.EMPTY) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.QUEENTAKE};
                                                coordinates.add(array);
                                            }
                                        }
                                    }
                                    // }
                            }
                            break;
                        }
                    }
                }

            }
        } else { //znalezienie wszystkich mozliwosci zwyklego ruchu
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board.getValueOfPiece(row, col) == boardController.botsColor || board.getValueOfPiece(row, col) == boardController.botsKingColor) { //jezeli to jest pionek bota
                        if (move.canIMove(col, row)) {
                            switch (board.getValueOfPiece(row, col)) {
                                case Board.RED:
                                    if (row >= 1) {
                                        if (col != 0) {
                                            if (move.isItLegalSecondClickMove(col - 1, row - 1, col, row, Board.RED)) {
                                                int[] array = {row, col, row - 1, col - 1, Bot.MOVE};
                                                coordinates.add(array);

                                            }
                                        }
                                        if (col != 7) {
                                            if (move.isItLegalSecondClickMove(col + 1, row - 1, col, row, Board.RED)) {
                                                int[] array = {row, col, row - 1, col + 1, Bot.MOVE};
                                                coordinates.add(array);

                                            }
                                        }
                                    }
                                    break;
                                case Board.BLACK:
                                    if (row <= 6) {
                                        if (col != 0) {
                                            if (move.isItLegalSecondClickMove(col - 1, row + 1, col, row, Board.BLACK)) {
                                                int[] array = {row, col, row + 1, col - 1, Bot.MOVE};
                                                coordinates.add(array);

                                            }
                                        }
                                        if (col != 7) {
                                            if (move.isItLegalSecondClickMove(col + 1, row + 1, col, row, Board.BLACK)) {
                                                int[] array = {row, col, row + 1, col + 1, Bot.MOVE};
                                                coordinates.add(array);
                                            }
                                        }
                                    }
                                    break;
                                case Board.REDKING:
                                    int row1, col1;
                                    for (row1 = row + 1, col1 = col + 1; row1 <= 7 && col1 <=7; row1++, col1++) { //prawy dolny skos
                                        if(move.isItLegalSecondClickMove(col1,row1,col,row,Board.REDKING)){
                                            if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.MOVE};
                                                coordinates.add(array);
                                            }
                                        }
                                    }
                                    for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0; row1--, col1--) {// lewy górny skos
                                        if(move.isItLegalSecondClickMove(col1,row1,col,row,Board.REDKING)){
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.MOVE};
                                                coordinates.add(array);
                                            }
                                        }
                                    }
                                    for (row1 = row - 1, col1 = col + 1; row1 >= 0 && col1 <= 7; row1--, col1++) {// prawy górny skos
                                        if (move.isItLegalSecondClickMove(col1, row1, col, row, Board.REDKING)) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.MOVE};
                                                coordinates.add(array);
                                            }

                                        }
                                    }
                                    for (row1 = row + 1, col1 = col - 1; row1 <= 7 && col1 >= 0; row1++, col1--) {// lewy dolny skos
                                        if(move.isItLegalSecondClickMove(col1,row1,col,row,Board.REDKING))
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.MOVE};
                                                coordinates.add(array);
                                            }
                                        }
                                    break;
                                case Board.BLACKKING:
                                    for (row1 = row + 1, col1 = col + 1; row1 <= 7 && col1 <= 7; row1++, col1++) { //prawy dolny skos
                                        if(move.isItLegalSecondClickMove(col1,row1,col,row,Board.BLACKKING)){
                                            if (!move.checkRightBotDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.MOVE};
                                                coordinates.add(array);
                                            }
                                        }
                                    }
                                    for (row1 = row - 1, col1 = col - 1; row1 >= 0 && col1 >= 0; row1--, col1--) {// lewy górny skos
                                        if(move.isItLegalSecondClickMove(col1,row1,col,row,Board.BLACKKING)){
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.MOVE};
                                                coordinates.add(array);
                                            }
                                        }
                                    }
                                    for (row1 = row - 1, col1 = col + 1; row1 >= 0 && col1 <= 7; row1--, col1++) {// prawy górny skos
                                        if (move.isItLegalSecondClickMove(col1, row1, col, row, Board.BLACKKING)) {
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.MOVE};
                                                coordinates.add(array);
                                            }

                                        }
                                    }
                                    for (row1 = row + 1, col1 = col - 1; row1 <= 7 && col1 >= 0; row1++, col1--) {// lewy dolny skos
                                        if(move.isItLegalSecondClickMove(col1,row1,col,row,Board.BLACKKING))
                                            if (!move.checkRightTopDiagonalEmptySpaces(col, row, col1, row1)) {
                                                int[] array = {row, col, row1 , col1 , Bot.MOVE};
                                                coordinates.add(array);
                                            }
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void simulate() {
        int sumMax = -100;
        for (var array : coordinates) {
            int[][] localpieces = new int[8][8];
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    localpieces[row][col] = board.pieces[row][col];
                }
            }
            //czy ruch jest biciem
            int sum = 0;
            if (array[4] == Bot.MOVE) {
                if(localpieces[array[0]][array[1]] == Board.RED || localpieces[array[0]][array[1]] == Board.BLACK) {
                    localpieces[array[0]][array[1]] = Board.EMPTY;
                    localpieces[array[2]][array[3]] = boardController.botsColor;
                }
                if(localpieces[array[0]][array[1]] == Board.REDKING || localpieces[array[0]][array[1]] == Board.BLACKKING){
                    localpieces[array[0]][array[1]] = Board.EMPTY;
                    localpieces[array[2]][array[3]] = boardController.botsKingColor;
                }
            } else if (array[4] == Bot.TAKE) { //bicie dla pionkow
                take(array[0], array[1], array[2], array[3], boardController.botsColor, localpieces);
            } else if (array[4] == Bot.QUEENTAKE) {
                queenTake(array[0], array[1], array[2], array[3], boardController.botsKingColor, localpieces);
            }
            if (checkAllPiecesPossibleTakes(boardController.playersColor, boardController.playersKingColor, localpieces)) {
                if(localpieces[array[2]][array[3]] == boardController.botsColor) {
                    sum -= 20;
                }
                if(localpieces[array[2]][array[3]] == boardController.botsKingColor){
                    sum -= 30;
                }
            }
            if (checkAllPiecesPossibleTakes(boardController.botsColor, boardController.botsKingColor, localpieces)) {
                sum += 10;
            }
            if (isChanceForQueen(boardController.botsColor, localpieces, localpieces[array[2]][array[3]])) {
                sum += 15;
            }
            if (sum >= sumMax) {
                bestMoves = array;
                sumMax = sum;
            }
            //metody sprawdzajace czy przeciwnik bota ma mozliwosc bicia albo bot - jesli tak to suma - albo + 1 [najplytsza mozliwosc]

        }
    }

    public boolean isChanceForQueen(int colorToCheck, int[][] board, int typeOfFigure) {
        boolean check = false;
        if(typeOfFigure != Board.BLACKKING && typeOfFigure != Board.REDKING) {
            for (int col = 0; col < 7; col++) {
                if (board[7][col] == colorToCheck && colorToCheck == Board.BLACK) {
                    check = true;
                }
                if (board[0][col] == colorToCheck && colorToCheck == Board.RED) {
                    check = true;
                }
            }
        }
        //sprawdzenie czy jest ryzyko/szansa zdobycia damy
        return check;
    }

    public void move() {

        int rowFirst = bestMoves[0];
        int colFirst = bestMoves[1];
        int rowSecond = bestMoves[2];
        int colSecond = bestMoves[3];
        //ruch
        if (bestMoves[4] == Bot.MOVE) {
            if (board.getValueOfPiece(rowFirst, colFirst) == boardController.botsColor) {
                board.pieces[rowFirst][colFirst] = Board.EMPTY;
                board.pieces[rowSecond][colSecond] = boardController.botsColor;

            } else if (board.getValueOfPiece(rowFirst, colFirst) == boardController.botsKingColor) {
                board.pieces[rowFirst][colFirst] = Board.EMPTY;
                board.pieces[rowSecond][colSecond] = boardController.botsKingColor;
            }
        } else if (bestMoves[4] == Bot.TAKE) {

            boardController.take(rowFirst, colFirst, rowSecond, colSecond, boardController.botsColor);
        } else if (bestMoves[4] == Bot.QUEENTAKE) {
            boardController.queenTake(rowFirst, colFirst, rowSecond, colSecond, boardController.botsKingColor);
        }
        if (rowSecond == 7 && boardController.botsColor==Board.BLACK) {
            board.pieces[rowSecond][colSecond] = Board.BLACKKING;
        }
        if (rowSecond == 0 && boardController.botsColor==Board.RED) {
            board.pieces[rowSecond][colSecond] = Board.REDKING;
        }

        board.repaint();

        coordinates.clear(); //reset listy mozliwych ruchow
        Arrays.fill(bestMoves, 0);
        boardController.frame.isGameFinished();
    }

    public void take(int firstRow, int firstColumn, int secondRow, int secondColumn, int currentColor, int[][] board) {
        board[firstRow][firstColumn] = Board.EMPTY;
        board[secondRow][secondColumn] = currentColor;
        int rowBetween = (firstRow + secondRow) / 2;
        int colBetween = (firstColumn + secondColumn) / 2;
        board[rowBetween][colBetween] = Board.EMPTY;
    }

    public boolean canIMove(int column, int row, int[][] board) {
        boolean result = false;
        int colorofpiece = board[row][column];
        switch (colorofpiece) {
            case Board.RED:
                if (column == 7) {
                    if (board[row - 1][column - 1] == Board.EMPTY) {
                        result = true;
                    }
                } else if (column == 0) {
                    if (board[row - 1][column + 1] == Board.EMPTY) {
                        result = true;
                    }
                } else {
                    if (board[row - 1][column + 1] == Board.EMPTY || board[row - 1][column - 1] == Board.EMPTY) {
                        result = true;
                    }
                }
                break;
            case Board.BLACK:
                if (column == 7) {
                    if (board[row + 1][column - 1] == Board.EMPTY) {
                        result = true;
                    }
                } else if (column == 0) {
                    if (board[row + 1][column + 1] == Board.EMPTY) {
                        result = true;
                    }
                } else {
                    if (board[row + 1][column + 1] == Board.EMPTY || board[row + 1][column - 1] == Board.EMPTY) {
                        result = true;
                    }
                }
                break;
            case Board.BLACKKING:
            case Board.REDKING:
                result = true;
        }
        return result;
    }

    public boolean canITake(int column, int row, int[][] board) {
        boolean result = false;
        int i, j;
        int colorofpiece = board[row][column];
        switch (colorofpiece) {
            case Board.RED:
                if (row >= 2) {
                    if (column == 7 || column == 6) {
                        if ((board[row - 1][column - 1] == Board.BLACK || board[row - 1][column - 1] == Board.BLACKKING) && board[row - 2][column - 2] == Board.EMPTY) {
                            result = true;
                        }
                    } else if (column == 0 || column == 1) {
                        if ((board[row - 1][column + 1] == Board.BLACK || board[row - 1][column + 1] == Board.BLACKKING)
                                && board[row - 2][column + 2] == Board.EMPTY) {
                            result = true;
                        }
                    } else if (((board[row - 1][column + 1] == Board.BLACK || board[row - 1][column + 1] == Board.BLACKKING) && board[row - 2][column + 2] == Board.EMPTY) || ((board[row - 1][column - 1] == Board.BLACK || board[row - 1][column - 1] == Board.BLACKKING) && board[row - 2][column - 2] == Board.EMPTY)) {
                        result = true;
                    }
                }
                break;
            case Board.BLACK:
                if (row <= 5) {
                    if (column == 7 || column == 6) {
                        if ((board[row + 1][column - 1] == Board.RED || board[row + 1][column - 1] == Board.REDKING) && board[row + 2][column - 2] == Board.EMPTY) {
                            result = true;
                        }
                    } else if (column == 0 || column == 1) {
                        if ((board[row + 1][column + 1] == Board.RED || board[row + 1][column + 1] == Board.REDKING) && board[row + 2][column + 2] == Board.EMPTY) {
                            result = true;
                        }
                    } else {
                        if (((board[row + 1][column + 1] == Board.RED || board[row + 1][column + 1] == Board.REDKING) && board[row + 2][column + 2] == Board.EMPTY) || ((board[row + 1][column - 1] == Board.RED || board[row + 1][column - 1] == Board.REDKING) && board[row + 2][column - 2] == Board.EMPTY)) {
                            result = true;
                        }
                    }
                }
                break;
            case Board.BLACKKING:
                for (i = row - 1, j = column - 1; i > 0 && j > 0; i--, j--) {
                    if (board[i][j] == Board.REDKING || board[i][j] == Board.RED) {
                        if (board[i - 1][j - 1] == Board.EMPTY) {
                            result = true;
                        }
                    }
                }
                for (i = row + 1, j = column - 1; i < 7 && j > 0; i++, j--) {
                    if (board[i][j] == Board.REDKING || board[i][j] == Board.RED) {
                        if (board[i + 1][j - 1] == Board.EMPTY) {
                            result = true;
                        }
                    }
                }
                for (i = row - 1, j = column + 1; i > 0 && j < 7; i--, j++) {
                    if (board[i][j] == Board.REDKING || board[i][j] == Board.RED) {
                        if (board[i - 1][j + 1] == Board.EMPTY) {
                            result = true;
                        }
                    }
                }
                for (i = row + 1, j = column + 1; i < 7 && j < 7; i++, j++) {
                    if (board[i][j] == Board.REDKING || board[i][j] == Board.RED) {
                        if (board[i + 1][j + 1] == Board.EMPTY) {
                            result = true;
                        }
                    }
                }
                break;
            case Board.REDKING:
                for (i = row - 1, j = column - 1; i > 0 && j > 0; i--, j--) {
                    if (board[i][j] == Board.BLACKKING || board[i][j] == Board.BLACK) {
                        if (board[i - 1][j - 1] == Board.EMPTY) {
                            result = true;
                        }
                    }
                }
                for (i = row + 1, j = column - 1; i < 7 && j > 0; i++, j--) {
                    if (board[i][j] == Board.BLACKKING || board[i][j] == Board.BLACK) {
                        if (board[i + 1][j - 1] == Board.EMPTY) {
                            result = true;
                        }
                    }
                }
                for (i = row - 1, j = column + 1; i > 0 && j < 7; i--, j++) {
                    if (board[i][j] == Board.BLACKKING || board[i][j] == Board.BLACK) {
                        if (board[i - 1][j + 1] == Board.EMPTY) {
                            result = true;
                        }
                    }
                }
                for (i = row + 1, j = column + 1; i < 7 && j < 7; i++, j++) {
                    if (board[i][j] == Board.BLACKKING || board[i][j] == Board.BLACK) {
                        if (board[i + 1][j + 1] == Board.EMPTY) {
                            result = true;
                        }
                    }
                }
                break;
        }
        return result;
    }

    public void queenTake(int firstRow, int firstColumn, int secondRow, int secondColumn, int currentColor, int[][] board) {
        board[firstRow][firstColumn] = Board.EMPTY;
        board[secondRow][secondColumn] = currentColor;
        if (secondRow < firstRow && secondColumn < firstColumn) {
            board[secondRow + 1][secondColumn + 1] = Board.EMPTY;
        }

        if (secondRow > firstRow && secondColumn < firstColumn)
            board[secondRow - 1][secondColumn + 1] = Board.EMPTY;
        if (secondRow < firstRow && secondColumn > firstColumn)
            board[secondRow + 1][secondColumn - 1] = Board.EMPTY;
        if (secondRow > firstRow && secondColumn > firstColumn)
            board[secondRow - 1][secondColumn - 1] = Board.EMPTY;
    }
}
