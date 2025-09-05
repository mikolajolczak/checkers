package checkers;

import java.awt.Color;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

public class Frame extends JFrame {

  private final BoardPanel board;
  private final GameEndEvaluator gameEndEvaluator;

  public Frame(final BoardState state, final BoardPanel boardParam) {
    if (state == null) {
      throw new NullPointerException("BoardState is null");
    }
    this.setSize(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
    this.setLocationRelativeTo(null);
    this.setBackground(Color.LIGHT_GRAY);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.board = boardParam;
    this.add(boardParam);
    this.gameEndEvaluator = new GameEndEvaluator(state, this);
  }

  public void addBoardListener(final MouseListener listenForClick) {
    board.addMouseListener(listenForClick);
  }

  public void checkGameFinished() {
    gameEndEvaluator.evaluateGameEnd();
  }
}

