package Checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class Frame extends JFrame {
    Board board = new Board();
    public void addBoardListener(MouseListener listenForClick){board.addMouseListener(listenForClick);}
    public Frame(){
        this.setSize(416,436);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.LIGHT_GRAY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(board);
    }

    public void makeAQueen (int rowFirst, int columnFirst, int rowSecond, int columnSecond) {
        if (board.pieces[rowFirst][columnFirst] == Board.RED && rowSecond == 0) {
            board.pieces[rowSecond][columnFirst] = Board.REDKING;
        } else if (board.pieces[rowFirst][columnFirst] == Board.BLACK && rowSecond == 7) {
            board.pieces[rowSecond][columnSecond] = Board.BLACKKING;
        }
    }

    public void isGameFinished () {
        int sumOfReds = 0;
        int sumofBlacks = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.pieces[row][col] == Board.RED || board.pieces[row][col] == Board.REDKING) {
                    sumOfReds ++;
                } else if (board.pieces[row][col] == Board.BLACK || board.pieces[row][col] == Board.BLACKKING) {
                    sumofBlacks ++;
                }
            }
        }
        if (sumOfReds <= 0) {
            JOptionPane.showMessageDialog(this,"Czarny zwyciężył!");
        } else if (sumofBlacks <= 0) {
            JOptionPane.showMessageDialog(this,"czerwony zwyciężył!");
        }
    }

}
