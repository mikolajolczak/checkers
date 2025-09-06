package checkers;

import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

/**
 * A custom Swing {@link JPanel} responsible for rendering
 * the checkers board and its pieces.
 *
 * <p>This panel draws both the board squares and the pieces
 * currently in play, based on the list of {@link PieceView}
 * objects provided. It supports updating the displayed
 * pieces dynamically via {@link #setPiecesToDraw(List)}.
 * </p>
 */
public final class BoardPanel extends JPanel {
  /**
   * The list of pieces that should currently be rendered
   * on the board. May be {@code null} if no pieces are set.
   */
  private List<PieceView> piecesToDraw;

  /**
   * Updates the list of pieces to be rendered on the board
   * and triggers a repaint.
   *
   * @param piecesToDrawParam the list of {@link PieceView} objects
   *                          that should be displayed
   */
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
