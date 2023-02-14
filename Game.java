package Checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
    public static void main(String[] args) {
        int playersColor;
        //int botsColor;
        JFrame colorChoiceFrame = new JFrame();
        colorChoiceFrame.setLocation(700, 400);

        Frame boardFrame = new Frame();
        Move move = new Move(boardFrame);
        BoardController controller = new BoardController(boardFrame,move);
        JButton red = new JButton("Red");
        JButton black = new JButton("Black");
        JLabel chooseColor = new JLabel("Choose your color");
        colorChoiceFrame.setLayout(new FlowLayout());
        colorChoiceFrame.setSize(350, 90);
        colorChoiceFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        boardFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        colorChoiceFrame.add(chooseColor);
        colorChoiceFrame.add(red);
        colorChoiceFrame.add(black);
        red.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.botsColor= Board.BLACK;
                controller.botsKingColor= Board.BLACKKING;
                controller.playersColor = Board.RED;
                controller.playersKingColor = Board.REDKING;
                controller.setCurrentColor();
                controller.setCurrentColorKing();
                colorChoiceFrame.dispose();
                boardFrame.setVisible(true);

            }
        });
        black.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.botsColor= Board.RED;
                controller.botsKingColor= Board.REDKING;
                controller.playersColor = Board.BLACK;
                controller.playersKingColor = Board.BLACKKING;
                colorChoiceFrame.dispose();
                controller.setCurrentColorKing();
                boardFrame.setVisible(true);
                controller.setCurrentColor();
            }
        });

        colorChoiceFrame.setVisible(true);
        Bot bot = new Bot(boardFrame.board, move, controller);
        controller.setBot(bot);

//        Bot bot = new Bot(controller.botsColor);
    }
}
