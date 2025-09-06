package checkers;

import java.awt.Color;
import java.awt.Graphics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PieceRendererTest {

  private static final Color TEST_COLOR = Color.RED;
  private static final int TEST_ROW = 2;
  private static final int TEST_COL = 3;
  @Mock
  private Graphics graphics;
  @Mock
  private PieceView pieceView;

  @BeforeEach
  void setUp() {

    when(pieceView.isEmpty()).thenReturn(false);
    when(pieceView.getColor()).thenReturn(TEST_COLOR);
    when(pieceView.row()).thenReturn(TEST_ROW);
    when(pieceView.col()).thenReturn(TEST_COL);
    when(pieceView.isKing()).thenReturn(false);
  }

  @Test
  void drawPiece_whenPieceIsNotEmpty_shouldSetColorAndDrawOval() {

    int expectedX =
        GameConstants.PIECE_PADDING + TEST_COL * GameConstants.SQUARE_SIZE;
    int expectedY =
        GameConstants.PIECE_PADDING + TEST_ROW * GameConstants.SQUARE_SIZE;

    PieceRenderer.drawPiece(graphics, pieceView);

    verify(graphics).setColor(TEST_COLOR);
    verify(graphics).fillOval(
        expectedX,
        expectedY,
        GameConstants.PIECE_SIZE,
        GameConstants.PIECE_SIZE
    );
  }

  @Test
  void drawPiece_whenPieceIsKing_shouldDrawKingMarker() {

    when(pieceView.isKing()).thenReturn(true);

    int expectedPieceX =
        GameConstants.PIECE_PADDING + TEST_COL * GameConstants.SQUARE_SIZE;
    int expectedPieceY =
        GameConstants.PIECE_PADDING + TEST_ROW * GameConstants.SQUARE_SIZE;
    int expectedKingX = GameConstants.KING_MARKER_PADDING
        + TEST_COL * GameConstants.SQUARE_SIZE;
    int expectedKingY = GameConstants.KING_MARKER_PADDING
        + TEST_ROW * GameConstants.SQUARE_SIZE;

    PieceRenderer.drawPiece(graphics, pieceView);

    verify(graphics).setColor(TEST_COLOR);
    verify(graphics).fillOval(
        expectedPieceX,
        expectedPieceY,
        GameConstants.PIECE_SIZE,
        GameConstants.PIECE_SIZE
    );

    verify(graphics).setColor(Color.WHITE);
    verify(graphics).drawOval(
        expectedKingX,
        expectedKingY,
        GameConstants.KING_MARKER_SIZE,
        GameConstants.KING_MARKER_SIZE
    );
  }

  @Test
  void drawPiece_whenPieceIsNotKing_shouldNotDrawKingMarker() {

    when(pieceView.isKing()).thenReturn(false);

    PieceRenderer.drawPiece(graphics, pieceView);

    verify(graphics).setColor(TEST_COLOR);
    verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());

    verify(graphics, never()).setColor(Color.WHITE);
    verify(graphics, never()).drawOval(anyInt(), anyInt(), anyInt(), anyInt());
  }

  @Test
  void drawPiece_shouldCalculateCorrectPositionForDifferentCoordinates() {

    when(pieceView.row()).thenReturn(0);
    when(pieceView.col()).thenReturn(0);

    int expectedX = GameConstants.PIECE_PADDING;
    int expectedY = GameConstants.PIECE_PADDING;

    PieceRenderer.drawPiece(graphics, pieceView);

    verify(graphics).fillOval(
        expectedX,
        expectedY,
        GameConstants.PIECE_SIZE,
        GameConstants.PIECE_SIZE
    );
  }

  @Test
  void drawPiece_shouldCalculateCorrectPositionForHighCoordinates() {

    int highRow = 7;
    int highCol = 7;
    when(pieceView.row()).thenReturn(highRow);
    when(pieceView.col()).thenReturn(highCol);

    int expectedX =
        GameConstants.PIECE_PADDING + highCol * GameConstants.SQUARE_SIZE;
    int expectedY =
        GameConstants.PIECE_PADDING + highRow * GameConstants.SQUARE_SIZE;

    PieceRenderer.drawPiece(graphics, pieceView);

    verify(graphics).fillOval(
        expectedX,
        expectedY,
        GameConstants.PIECE_SIZE,
        GameConstants.PIECE_SIZE
    );
  }

  @Test
  void drawPiece_withDifferentColors_shouldUseCorrectColor() {

    Color blackColor = Color.BLACK;
    when(pieceView.getColor()).thenReturn(blackColor);

    PieceRenderer.drawPiece(graphics, pieceView);

    verify(graphics).setColor(blackColor);
    verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());
  }

  @Test
  void drawPiece_whenKing_shouldCallSetColorTwice() {

    when(pieceView.isKing()).thenReturn(true);

    PieceRenderer.drawPiece(graphics, pieceView);

    verify(graphics, times(2)).setColor(any(Color.class));
    verify(graphics).setColor(TEST_COLOR);
    verify(graphics).setColor(Color.WHITE);
  }

  @Test
  void drawPiece_verifyMethodCallOrder() {

    when(pieceView.isKing()).thenReturn(true);

    PieceRenderer.drawPiece(graphics, pieceView);

    InOrder inOrder = inOrder(graphics);

    inOrder.verify(graphics).setColor(TEST_COLOR);

    inOrder.verify(graphics).fillOval(anyInt(), anyInt(), anyInt(), anyInt());

    inOrder.verify(graphics).setColor(Color.WHITE);

    inOrder.verify(graphics).drawOval(anyInt(), anyInt(), anyInt(), anyInt());
  }
}