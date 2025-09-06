package checkers;

import java.awt.Color;
import java.awt.Graphics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class SquareRendererTest {

  @Mock
  private Graphics graphics;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void shouldDrawSelectedSquareWithDarkGrayColor() {

    int row = 0;
    int col = 0;
    boolean selected = true;
    int squareSize = 50;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.DARK_GRAY);
    verify(graphics).fillRect(0, 0, 50, 50);
  }

  @Test
  void shouldDrawLightGraySquareWhenRowAndColHaveSameParity() {

    int row = 0;
    int col = 0;
    boolean selected = false;
    int squareSize = 50;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.LIGHT_GRAY);
    verify(graphics).fillRect(0, 0, 50, 50);
  }

  @Test
  void shouldDrawLightGraySquareWhenBothRowAndColAreOdd() {

    int row = 1;
    int col = 1;
    boolean selected = false;
    int squareSize = 50;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.LIGHT_GRAY);
    verify(graphics).fillRect(50, 50, 50, 50);
  }

  @Test
  void shouldDrawGraySquareWhenRowEvenAndColOdd() {

    int row = 0;
    int col = 1;
    boolean selected = false;
    int squareSize = 50;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.GRAY);
    verify(graphics).fillRect(50, 0, 50, 50);
  }

  @Test
  void shouldDrawGraySquareWhenRowOddAndColEven() {

    int row = 1;
    int col = 0;
    boolean selected = false;
    int squareSize = 50;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.GRAY);
    verify(graphics).fillRect(0, 50, 50, 50);
  }

  @Test
  void shouldCalculateCorrectPositionForDifferentSquareSizes() {

    int row = 2;
    int col = 3;
    boolean selected = false;
    int squareSize = 75;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).fillRect(225, 150, 75, 75);
  }

  @Test
  void shouldHandleLargeCoordinatesCorrectly() {

    int row = 7;
    int col = 7;
    boolean selected = false;
    int squareSize = 100;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.LIGHT_GRAY);
    verify(graphics).fillRect(700, 700, 100, 100);
  }

  @Test
  void shouldPrioritizeSelectionColorOverParityBasedColor() {

    int row1 = 0, col1 = 0;
    int row2 = 0, col2 = 1;
    boolean selected = true;
    int squareSize = 50;

    SquareRenderer.drawSquare(graphics, row1, col1, selected, squareSize);
    SquareRenderer.drawSquare(graphics, row2, col2, selected, squareSize);

    verify(graphics, times(2)).setColor(Color.DARK_GRAY);
    verify(graphics).fillRect(0, 0, 50, 50);
    verify(graphics).fillRect(50, 0, 50, 50);
  }

  @Test
  void shouldHandleZeroCoordinates() {

    int row = 0;
    int col = 0;
    boolean selected = false;
    int squareSize = 30;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.LIGHT_GRAY);
    verify(graphics).fillRect(0, 0, 30, 30);
  }

  @Test
  void shouldHandleMinimumSquareSize() {

    int row = 1;
    int col = 1;
    boolean selected = false;
    int squareSize = 1;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.LIGHT_GRAY);
    verify(graphics).fillRect(1, 1, 1, 1);
  }

  @Test
  void shouldCallGraphicsMethodsInCorrectOrder() {

    int row = 0;
    int col = 1;
    boolean selected = false;
    int squareSize = 50;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    var inOrder = inOrder(graphics);
    inOrder.verify(graphics).setColor(Color.GRAY);
    inOrder.verify(graphics).fillRect(50, 0, 50, 50);
  }

  @Test
  void shouldHandleNegativeCoordinatesCorrectly() {

    int row = -1;
    int col = -1;
    boolean selected = false;
    int squareSize = 50;

    SquareRenderer.drawSquare(graphics, row, col, selected, squareSize);

    verify(graphics).setColor(Color.LIGHT_GRAY);
    verify(graphics).fillRect(-50, -50, 50, 50);
  }
}