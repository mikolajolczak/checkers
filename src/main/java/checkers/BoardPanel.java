package checkers;

import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

  private List<PieceView> piecesToDraw;

  public void setPiecesToDraw(final List<PieceView> piecesToDrawParam) {
    this.piecesToDraw = piecesToDrawParam;
    repaint();
  }

  @Override
  protected void paintComponent(final Graphics g) {
    super.paintComponent(g);
    if (piecesToDraw == null) {
      return;
    }

    for (PieceView piece : piecesToDraw) {
      SquareRenderer.drawSquare(g, piece.row(), piece.col(),
          piece.selected(), GameConstants.SQUARE_SIZE);
      PieceRenderer.drawPiece(g, piece);
    }
  }
}
