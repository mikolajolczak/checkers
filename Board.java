package Checkers;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    static final int EMPTY = 0,RED = 1,BLACK = 2,REDKING = 3,BLACKKING=4;
    int[][] pieces = new int[8][8];
    public void setUpPawns(){
        for(int row = 0;row < 8;row++){
            for(int col = 0;col < 8;col++){
                if(row % 2 != col % 2){
                    if(row < 3) pieces[row][col] = BLACK;
                    else if(row > 4) pieces[row][col]= RED;
                    else pieces[row][col] = EMPTY;
                }else
                    pieces[row][col]=EMPTY;
            }
        }
    }
    int selectedRow = 8;
    int selectedColumn = 8;
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }
    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }
    public int getSelectedRow() {
        return selectedRow;
    }
    public int getSelectedColumn() {
        return selectedColumn;
    }

    int getValueOfPiece(int row, int col){
        return pieces[row][col];
    }
    public void setValueOfPiece(int row,int col,int color){
        this.pieces[row][col]=color;
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for(int row = 0;row < 8;row++){
            for(int col = 0;col < 8;col++){
                if(col == getSelectedColumn() && row == getSelectedRow()){
                    g.setColor(Color.DARK_GRAY);
                }else {
                    if (row % 2 == col % 2) g.setColor(Color.LIGHT_GRAY);
                    else g.setColor(Color.GRAY);
                }
                g.fillRect(col * 50,row * 50,50,50);
                switch(getValueOfPiece(row, col)) {
                    case RED:
                        g.setColor(Color.RED);
                        g.fillOval(5 + col * 50, 5 + row * 50, 40, 40);
                        break;
                    case BLACK:
                        g.setColor(Color.BLACK);
                        g.fillOval(5 + col * 50, 5 + row * 50, 40, 40);
                        break;
                    case REDKING:
                        g.setColor(Color.RED);
                        g.fillOval(5 + col * 50, 5 + row * 50, 40, 40);
                        g.setColor(Color.WHITE);
                        g.drawOval(15 + col * 50, 15 + row * 50, 20, 20);
                        break;
                    case BLACKKING:
                        g.setColor(Color.BLACK);
                        g.fillOval(5 + col * 50, 5 + row * 50, 40, 40);
                        g.setColor(Color.WHITE);
                        g.drawOval(15 + col * 50, 15 + row * 50, 20, 20);
                        break;
                }
            }
        }

    }

    public Board(){
        setUpPawns();
    }
}
