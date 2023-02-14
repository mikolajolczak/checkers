package Checkers;

public class Move {
    private Frame frame;
    public Move(Frame frame){
        this.frame = frame;
    }
    public boolean isItRed(int column,int row){
        if(frame.board.getValueOfPiece(row,column)==Board.RED){
            return true;
        }else{
            return false;
        }
    }

    public boolean isItBlack(int column,int row){
        if(frame.board.getValueOfPiece(row,column)==Board.BLACK){
            return true;
        }else{
            return false;
        }
    }
    public boolean isItBlackKing(int column,int row){
        if(frame.board.getValueOfPiece(row,column)==Board.BLACKKING){
            return true;
        }else{
            return false;
        }
    }
    public boolean isItRedKing(int column,int row){
        if(frame.board.getValueOfPiece(row,column)==Board.REDKING){
            return true;
        }else{
            return false;
        }
    }
    public boolean isItEmpty(int column,int row){
        if(frame.board.getValueOfPiece(row,column)==Board.EMPTY){
            return true;
        }else{
            return false;
        }
    }
    public boolean isItOnTheSameDiagonal(int columnFirst,int rowFirst, int columnSecond,int rowSecond){
        if(Math.abs(rowSecond - columnSecond) == Math.abs(rowFirst - columnFirst) || rowFirst + columnFirst == columnSecond + rowSecond){
            return true;
        }else{
            return false;
        }
    }
    public boolean checkLeftTopDiagonalEmptySpaces(int columnFirst,int rowFirst, int columnSecond,int rowSecond){
        int i,j;
        for (i = rowFirst - 1, j = columnFirst - 1; i > rowSecond + 1 && j > columnSecond + 1; i--, j--) {
            if (frame.board.getValueOfPiece(i, j) != Board.EMPTY) return true;
        }
        return false;
    }
    public boolean checkLeftBotDiagonalEmptySpaces(int columnFirst,int rowFirst, int columnSecond,int rowSecond){
        int i,j;
        for (i = rowFirst + 1, j = columnFirst - 1; i < rowSecond - 1 && j > columnSecond + 1; i++, j--) {
            if (frame.board.getValueOfPiece(i, j) != Board.EMPTY) return true;
        }
        return false;
    }
    public boolean checkRightTopDiagonalEmptySpaces(int columnFirst,int rowFirst, int columnSecond,int rowSecond){
        int i,j;
        for (i = rowFirst - 1, j = columnFirst + 1; i > rowSecond + 1 && j < columnSecond - 1; i--, j++) {
            if (frame.board.getValueOfPiece(i, j) != Board.EMPTY) return true;
        }
        return false;
    }
    public boolean checkRightBotDiagonalEmptySpaces(int columnFirst,int rowFirst, int columnSecond,int rowSecond){
        int i,j;
        for (i = rowFirst + 1, j = columnFirst + 1; i < rowSecond - 1 && j < columnSecond - 1; i++, j++) {
            if (frame.board.getValueOfPiece(i, j) != Board.EMPTY) return true;
        }
        return false;
    }
    public boolean checkAllPiecesPossibleTakes(int color,int colorQueen){
        boolean result = false;
        for(int row=0;row<8;row++){
            for(int col=0;col<8;col++){
                if(canITake(col,row)) {
                    if (frame.board.getValueOfPiece(row, col) == color || frame.board.getValueOfPiece(row, col) == colorQueen)
                        result = true;
                }
                }
        }
        return result;
    }
    public boolean legalTakeMove(int columnsecond,int rowsecond,int columnfirst,int rowfirst,int color){
        boolean result = true;
        if(isItEmpty(columnsecond,rowsecond) && isItOnTheSameDiagonal(columnfirst,rowfirst,columnsecond,rowsecond)) {
            switch (color) {
                case Board.RED:
                    if (isItBlack((columnsecond + columnfirst) / 2, (rowfirst + rowsecond) / 2) ||
                            isItBlackKing((columnsecond + columnfirst) / 2, (rowfirst + rowsecond) / 2)){
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
                case Board.BLACK:
                    if (isItRed((columnsecond + columnfirst) / 2, (rowsecond+rowfirst) / 2) ||
                            isItRedKing((columnsecond + columnfirst) / 2, (rowsecond+rowfirst) / 2)){
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
                case Board.BLACKKING:
                            if (rowsecond < rowfirst && columnsecond < columnfirst) {
                                if(checkLeftTopDiagonalEmptySpaces(columnfirst,rowfirst,columnsecond,rowsecond)){
                                    result=false;
                                }
                                if(!(isItRedKing(columnsecond+1,rowsecond+1) || isItRed(columnsecond+1,rowsecond+1))){
                                    result=false;
                                }
                            }
                            if (rowsecond > rowfirst && columnsecond < columnfirst) {
                                if(checkLeftBotDiagonalEmptySpaces(columnfirst,rowfirst,columnsecond,rowsecond)){
                                    result=false;
                                }
                                if(!(isItRedKing(columnsecond+1,rowsecond-1) || isItRed(columnsecond+1,rowsecond-1))){
                                    result=false;
                                }
                            }
                            if (rowsecond < rowfirst && columnsecond > columnfirst) {
                                if(checkRightTopDiagonalEmptySpaces(columnfirst,rowfirst,columnsecond,rowsecond)){
                                    result=false;
                                }
                                if(!(isItRedKing(columnsecond-1,rowsecond+1) || isItRed(columnsecond-1,rowsecond+1))){
                                    result=false;
                                }
                            }
                            if (rowsecond > rowfirst && columnsecond > columnfirst) {
                                if(checkRightBotDiagonalEmptySpaces(columnfirst,rowfirst,columnsecond,rowsecond)){
                                    result=false;
                                }
                                if(!(isItRedKing(columnsecond-1,rowsecond-1) || isItRed(columnsecond-1,rowsecond-1))){
                                    result=false;
                                }
                            }
                    break;
                case Board.REDKING:
                    if (rowsecond < rowfirst && columnsecond < columnfirst) {
                        if(checkLeftTopDiagonalEmptySpaces(columnfirst,rowfirst,columnsecond,rowsecond)){
                            result=false;
                        }
                        if(!(isItBlackKing(columnsecond+1,rowsecond+1) || isItBlack(columnsecond+1,rowsecond+1))){
                            result=false;
                        }
                    }
                    if (rowsecond > rowfirst && columnsecond < columnfirst) {
                        if(checkLeftBotDiagonalEmptySpaces(columnfirst,rowfirst,columnsecond,rowsecond)){
                            result=false;
                        }
                        if(!(isItBlackKing(columnsecond+1,rowsecond-1) || isItBlack(columnsecond+1,rowsecond-1))){
                            result=false;
                        }
                    }
                    if (rowsecond < rowfirst && columnsecond > columnfirst) {
                        if(checkRightTopDiagonalEmptySpaces(columnfirst,rowfirst,columnsecond,rowsecond)){
                            result=false;
                        }
                        if(!(isItBlackKing(columnsecond-1,rowsecond+1) || isItBlack(columnsecond-1,rowsecond+1))){
                            result=false;
                        }
                    }
                    if (rowsecond > rowfirst && columnsecond > columnfirst) {
                        if(checkRightBotDiagonalEmptySpaces(columnfirst,rowfirst,columnsecond,rowsecond)){
                            result=false;
                        }
                        if(!(isItBlackKing(columnsecond-1,rowsecond-1) || isItBlack(columnsecond-1,rowsecond-1))){
                            result=false;
                        }
                    }
                    break;
            }
        }else{
            result=false;
        }
        return result;
    }
    public boolean canIMove(int column, int row){
        boolean result=false;
        int colorofpiece = frame.board.getValueOfPiece(row,column);
        switch (colorofpiece){
            case Board.RED:
                if(column!=7){
                    if(frame.board.getValueOfPiece(row-1,column+1)==Board.EMPTY) {
                        result=true;
                    }
                }
                if(column!=0){
                    if(frame.board.getValueOfPiece(row-1,column-1)==Board.EMPTY) {
                        result=true;
                    }
                }
                break;
            case Board.BLACK:
                if(column!=7){
                    if(frame.board.getValueOfPiece(row+1,column+1)==Board.EMPTY) {
                        result=true;
                    }
                }
                if(column!=0){
                    if(frame.board.getValueOfPiece(row+1,column-1)==Board.EMPTY) {
                        result=true;
                    }
                }
                break;
            case Board.BLACKKING:
            case Board.REDKING:
                result=true;
        }
        return result;
    }
    public boolean canITake(int column,int row){
        boolean result=false;
        int i,j;
        int colorofpiece = frame.board.getValueOfPiece(row,column);
        switch (colorofpiece){
            case Board.RED:
                if(row>=2) {
                    if (column<6) {
                        if((isItBlackKing(column+1,row-1) || isItBlack(column+1,row-1)) && isItEmpty(column+2,row-2)){
                            result=true;
                        }
                    }
                    if (column>1) {
                        if((isItBlackKing(column-1,row-1) || isItBlack(column-1,row-1)) && isItEmpty(column-2,row-2)){
                            result=true;
                        }
                    }
                }
                break;
            case Board.BLACK:
                if(row<=5) {
                    if (column<6) {
                        if((isItRedKing(column+1,row+1) || isItRed(column+1,row+1)) && isItEmpty(column+2,row+2)){
                            result=true;
                        }
                    }
                    if (column>1) {
                        if((isItRedKing(column-1,row+1) || isItRed(column-1,row+1)) && isItEmpty(column-2,row+2)){
                            result=true;
                        }
                    }
                }
                break;
            case Board.BLACKKING:
                for(i=row-1,j=column-1;i>0 && j>0;i--,j--) {
                    if (!checkLeftTopDiagonalEmptySpaces(column, row, j-1, i-1)) {
                        if (frame.board.getValueOfPiece(i, j) == Board.REDKING || frame.board.getValueOfPiece(i, j) == Board.RED) {
                            if (frame.board.getValueOfPiece(i - 1, j - 1) == Board.EMPTY) {
                                result = true;
                            }
                        }
                    }
                }
                for(i=row+1,j=column-1;i<7 && j>0;i++,j--) {
                    if (!checkLeftBotDiagonalEmptySpaces(column, row, j-1, i+1)) {
                        if (frame.board.getValueOfPiece(i, j) == Board.REDKING || frame.board.getValueOfPiece(i, j) == Board.RED) {
                            if (frame.board.getValueOfPiece(i + 1, j - 1) == Board.EMPTY) {
                                result = true;
                            }
                        }
                    }
                }
                for(i=row-1,j=column+1;i>0 && j<7;i--,j++) {
                    if (!checkRightTopDiagonalEmptySpaces(column, row, j+1, i-1)) {
                        if (frame.board.getValueOfPiece(i, j) == Board.REDKING || frame.board.getValueOfPiece(i, j) == Board.RED) {
                            if (frame.board.getValueOfPiece(i - 1, j + 1) == Board.EMPTY) {
                                result = true;
                            }
                        }
                    }
                }
                for(i=row+1,j=column+1;i<7 && j<7;i++,j++) {
                    if (!checkRightBotDiagonalEmptySpaces(column, row, j+1, i+1)) {
                        if (frame.board.getValueOfPiece(i, j) == Board.REDKING || frame.board.getValueOfPiece(i, j) == Board.RED) {
                            if (frame.board.getValueOfPiece(i + 1, j + 1) == Board.EMPTY) {
                                result = true;
                            }
                        }
                    }
                }
                break;
            case Board.REDKING:
                for(i=row-1,j=column-1;i>0 && j>0;i--,j--){
                    if (!checkLeftTopDiagonalEmptySpaces(column, row, j-1, i-1)) {
                        if (frame.board.getValueOfPiece(i, j) == Board.BLACKKING || frame.board.getValueOfPiece(i, j) == Board.BLACK) {
                            if (frame.board.getValueOfPiece(i - 1, j - 1) == Board.EMPTY) {
                                result = true;
                            }
                        }
                    }
                }
                for(i=row+1,j=column-1;i<7 && j>0;i++,j--) {
                    if (!checkLeftBotDiagonalEmptySpaces(column, row, j - 1, i + 1)) {
                        if (frame.board.getValueOfPiece(i, j) == Board.BLACKKING || frame.board.getValueOfPiece(i, j) == Board.BLACK) {
                            if (frame.board.getValueOfPiece(i + 1, j - 1) == Board.EMPTY) {
                                result = true;
                            }
                        }
                    }
                }
                for(i=row-1,j=column+1;i>0 && j<7;i--,j++) {
                    if (!checkRightTopDiagonalEmptySpaces(column, row, j + 1, i - 1)) {
                        if (frame.board.getValueOfPiece(i, j) == Board.BLACKKING || frame.board.getValueOfPiece(i, j) == Board.BLACK) {
                            if (frame.board.getValueOfPiece(i - 1, j + 1) == Board.EMPTY) {
                                result = true;
                            }
                        }
                    }
                }
                for(i=row+1,j=column+1;i<7 && j<7;i++,j++) {
                    if (!checkRightBotDiagonalEmptySpaces(column, row, j + 1, i + 1)) {
                        if (frame.board.getValueOfPiece(i, j) == Board.BLACKKING || frame.board.getValueOfPiece(i, j) == Board.BLACK) {
                            if (frame.board.getValueOfPiece(i + 1, j + 1) == Board.EMPTY) {
                                result = true;
                            }
                        }
                    }
                }
                break;
        }
        return result;
    }
    public boolean isItLegalSecondClickMove(int columnsecond,int rowsecond,int columnfirst,int rowfirst,int color){
        boolean result = true;
        switch (color){
            case Board.REDKING:
            case Board.BLACKKING:
                int i,j;
                if(Math.abs(rowsecond-columnsecond)==Math.abs(rowfirst-columnfirst) || rowfirst+columnfirst == columnsecond+rowsecond) {
                    if (rowsecond < rowfirst && columnsecond < columnfirst)
                        for (i = rowfirst - 1, j = columnfirst - 1; i >= rowsecond && j >= columnsecond; i--, j--) {
                            if (frame.board.getValueOfPiece(i, j) != Board.EMPTY) result = false;
                        }
                    if (rowsecond > rowfirst && columnsecond < columnfirst)
                        for (i = rowfirst + 1, j = columnfirst - 1; i <= rowsecond && j >= columnsecond; i++, j--) {
                            if (frame.board.getValueOfPiece(i, j) != Board.EMPTY) result = false;
                        }
                    if (rowsecond < rowfirst && columnsecond > columnfirst)
                        for (i = rowfirst - 1, j = columnfirst + 1; i >= rowsecond && j <= columnsecond; i--, j++) {
                            if (frame.board.getValueOfPiece(i, j) != Board.EMPTY) result = false;
                        }
                    if (rowsecond > rowfirst && columnsecond > columnfirst)
                        for (i = rowfirst + 1, j = columnfirst + 1; i <= rowsecond && j <= columnsecond; i++, j++) {
                            if (frame.board.getValueOfPiece(i, j) != Board.EMPTY) result = false;
                        }
                    if (rowsecond == rowfirst || columnsecond == columnfirst) {
                        result = false;
                    }
                }else{
                    result=false;
                }
                break;
            case Board.RED:
                if(Math.abs(columnsecond-columnfirst)!=1){
                result=false;
            }
                if(rowfirst-rowsecond!=1){
                    result=false;
                }
                break;
            case Board.BLACK:
                if(Math.abs(columnsecond-columnfirst)!=1){
                    result=false;
                }
                if(rowsecond-rowfirst!=1){
                    result=false;
                }
                break;
        }
        if(frame.board.pieces[rowsecond][columnsecond]!=Board.EMPTY){result=false;}
        return result;
    }
}
