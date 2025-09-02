package checkers.src.main.java;

import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

  private final SquareRenderer squareRenderer = new SquareRenderer();
  private final PieceRenderer pieceRenderer = new PieceRenderer();
  private List<PieceView> piecesToDraw;

  public void setPiecesToDraw(List<PieceView> piecesToDraw) {
    this.piecesToDraw = piecesToDraw;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (piecesToDraw == null) {
      return;
    }

    for (PieceView piece : piecesToDraw) {
      squareRenderer.drawSquare(g, piece.getRow(), piece.getCol(),
          piece.isSelected(), GameConstants.SQUARE_SIZE);
      pieceRenderer.drawPiece(g, piece);
    }
  }
}