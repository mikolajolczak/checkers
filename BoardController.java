package Checkers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoardController {
    public int botsColor = Board.BLACK;
    public int botsKingColor = Board.BLACKKING;
    public int playersColor = Board.RED;
    public int playersKingColor = Board.REDKING;
    public Frame frame;
    private Move move;
    private Bot bot; //dodac do konstruktora
    private int currentColor = Board.BLACK;
    private int currentColorKing = Board.BLACKKING;

    public void setCurrentColorKing(){
        if(this.currentColorKing==Board.REDKING){
            this.currentColorKing=Board.BLACKKING;
        }else{
            this.currentColorKing=Board.REDKING;
        }
    }
    public int getCurrentColorKing() {
        return currentColorKing;
    }

    public void clearChosenTile(){
        frame.board.setSelectedColumn(8);
        frame.board.setSelectedRow(8);
        frame.repaint();
    }
    public void setCurrentColor(){
        if(currentColor==Board.RED){
            this.currentColor=Board.BLACK;
        }else{
            this.currentColor=Board.RED;
        }
        if(currentColor == botsColor){
            bot.analyze();
            bot.simulate();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bot.move();
                }
            }).start();

            //bot oblicza, wykonuje ruch
            this.currentColor = playersColor;
            this.currentColorKing = playersKingColor;
        }
    }

    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public BoardController(Frame frame, Move move){
        this.frame = frame;
        this.move = move;
        this.frame.addBoardListener(new BoardListener());
    }
    public void take(int firstRow, int firstColumn, int secondRow, int secondColumn, int currentColor){
        frame.board.pieces[firstRow][firstColumn] = Board.EMPTY;
        frame.board.pieces[secondRow][secondColumn] = currentColor;
        int rowBetween = (firstRow+secondRow)/2;
        int colBetween = (firstColumn+secondColumn)/2;
        frame.board.pieces[rowBetween][colBetween] = Board.EMPTY;
    }
    public void queenTake(int firstRow, int firstColumn, int secondRow, int secondColumn, int currentColor){
        frame.board.setValueOfPiece(firstRow,firstColumn,Board.EMPTY);
        frame.board.setValueOfPiece(secondRow,secondColumn,currentColor);
        if(secondRow<firstRow && secondColumn<firstColumn){
            frame.board.setValueOfPiece(secondRow+1,secondColumn+1,Board.EMPTY);
        }

        if(secondRow>firstRow && secondColumn<firstColumn)
            frame.board.setValueOfPiece(secondRow-1,secondColumn+1,Board.EMPTY);
        if(secondRow<firstRow && secondColumn>firstColumn)
            frame.board.setValueOfPiece(secondRow+1,secondColumn-1,Board.EMPTY);
        if(secondRow>firstRow && secondColumn>firstColumn)
            frame.board.setValueOfPiece(secondRow-1,secondColumn-1,Board.EMPTY);
    }
    class BoardListener implements MouseListener{
        public boolean firstclick = true;
        int firstClickColumnNumber = 8;
        int firstClickRowNumber = 8;
        int columnsecond;
        int rowsecond;
        int colorOfFirstClick;
        int rowchaintake=8;
        int columnchaintake=8;
        boolean chaintake=false;
        @Override
        public void mouseClicked(MouseEvent e) {

        }


        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(firstclick){
                firstClickColumnNumber = e.getX()/50;
                firstClickRowNumber = e.getY()/50;
                if(move.canIMove(firstClickColumnNumber, firstClickRowNumber) || move.canITake(firstClickColumnNumber,firstClickRowNumber)) {
                    if(frame.board.getValueOfPiece(firstClickRowNumber,firstClickColumnNumber)==getCurrentColor() ||
                            frame.board.getValueOfPiece(firstClickRowNumber,firstClickColumnNumber)==getCurrentColorKing()){
                            frame.board.setSelectedColumn(firstClickColumnNumber);
                            frame.board.setSelectedRow(firstClickRowNumber);
                            frame.board.repaint();
                            colorOfFirstClick = frame.board.getValueOfPiece(firstClickRowNumber,firstClickColumnNumber);
                            firstclick=!firstclick;
                    }
                }
            }else {
                clearChosenTile();
                columnsecond = e.getX() / 50;
                rowsecond = e.getY() / 50;
                if (!move.checkAllPiecesPossibleTakes(getCurrentColor(),getCurrentColorKing())) {
                    if (move.isItLegalSecondClickMove(columnsecond, rowsecond, firstClickColumnNumber, firstClickRowNumber, colorOfFirstClick)) {
                        if (frame.board.getValueOfPiece(rowsecond,columnsecond) == Board.EMPTY) {
                            frame.board.setValueOfPiece(firstClickRowNumber,firstClickColumnNumber,Board.EMPTY);
                            frame.board.setValueOfPiece(rowsecond,columnsecond,colorOfFirstClick);
                            if(colorOfFirstClick==Board.RED && rowsecond==0){
                                frame.board.setValueOfPiece(rowsecond,columnsecond,Board.REDKING);
                            }
                            if(colorOfFirstClick==Board.BLACK && rowsecond==7){
                                frame.board.setValueOfPiece(rowsecond,columnsecond,Board.BLACKKING);
                            }
                            setCurrentColor();
                        }
                    }
                }else{
                    if(move.legalTakeMove(columnsecond, rowsecond, firstClickColumnNumber, firstClickRowNumber, colorOfFirstClick)){
                        if(colorOfFirstClick==Board.BLACK || colorOfFirstClick==Board.RED)
                        take(firstClickRowNumber,firstClickColumnNumber,rowsecond,columnsecond,getCurrentColor());
                        else
                            queenTake(firstClickRowNumber,firstClickColumnNumber,rowsecond,columnsecond,getCurrentColorKing());
                        if(colorOfFirstClick==Board.RED && rowsecond==0){
                            frame.board.setValueOfPiece(rowsecond,columnsecond,Board.REDKING);
                        }
                        if(colorOfFirstClick==Board.BLACK && rowsecond==7){
                            frame.board.setValueOfPiece(rowsecond,columnsecond,Board.BLACKKING);
                        }
                        setCurrentColor();
                    }

                }
                //frame.isGameFinished();
                firstclick = !firstclick;
                frame.board.repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
