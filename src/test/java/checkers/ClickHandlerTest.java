package checkers;

import java.awt.event.MouseEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.intThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClickHandlerTest {

  @Mock
  private MouseInputHandler mouseInputHandler;

  @Mock
  private MouseEvent mouseEvent;

  private ClickHandler clickHandler;

  @BeforeEach
  void setUp() {
    clickHandler = new ClickHandler(mouseInputHandler);
  }

  @Test
  void constructor_shouldAcceptMouseInputHandler() {
    new ClickHandler(mouseInputHandler);
  }

  @Test
  void constructor_shouldAcceptNullMouseInputHandler() {

    new ClickHandler(null);
  }

  @Test
  void mouseReleased_shouldCalculateCorrectRowAndColumn_whenSquareSizeIs50() {

    int expectedSquareSize = 50;
    int mouseX = 125;
    int mouseY = 175;
    int expectedCol = mouseX / expectedSquareSize;
    int expectedRow = mouseY / expectedSquareSize;

    when(mouseEvent.getX()).thenReturn(mouseX);
    when(mouseEvent.getY()).thenReturn(mouseY);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler).handleMouseInput(expectedRow, expectedCol);
  }

  @Test
  void mouseReleased_shouldHandleZeroCoordinates() {

    when(mouseEvent.getX()).thenReturn(0);
    when(mouseEvent.getY()).thenReturn(0);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler).handleMouseInput(0, 0);
  }

  @Test
  void mouseReleased_shouldHandleTopLeftCorner() {

    when(mouseEvent.getX()).thenReturn(1);
    when(mouseEvent.getY()).thenReturn(1);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler).handleMouseInput(0, 0);
  }

  @Test
  void mouseReleased_shouldHandleLargeCoordinates() {

    int largeX = 500;
    int largeY = 600;
    when(mouseEvent.getX()).thenReturn(largeX);
    when(mouseEvent.getY()).thenReturn(largeY);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler).handleMouseInput(anyInt(), anyInt());
  }

  @Test
  void mouseReleased_shouldHandleNegativeCoordinates() {

    when(mouseEvent.getX()).thenReturn(-10);
    when(mouseEvent.getY()).thenReturn(-20);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler).handleMouseInput(anyInt(), anyInt());
  }

  @Test
  void mouseReleased_shouldCallHandleMouseInputExactlyOnce() {

    when(mouseEvent.getX()).thenReturn(100);
    when(mouseEvent.getY()).thenReturn(150);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler, times(1)).handleMouseInput(anyInt(), anyInt());
  }

  @Test
  void mouseReleased_shouldNotCallOtherMouseInputHandlerMethods() {

    when(mouseEvent.getX()).thenReturn(50);
    when(mouseEvent.getY()).thenReturn(75);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler, only()).handleMouseInput(anyInt(), anyInt());
  }

  @Test
  void mouseReleased_shouldHandleNullMouseInputHandler() {

    ClickHandler handlerWithNullDependency = new ClickHandler(null);
    when(mouseEvent.getX()).thenReturn(100);
    when(mouseEvent.getY()).thenReturn(100);

    try {
      handlerWithNullDependency.mouseReleased(mouseEvent);

      throw new AssertionError("NullPointerException should have been thrown");
    } catch (NullPointerException e) {
      System.err.println(e.getMessage());
    }
  }

  @Test
  void mouseReleased_shouldGetCoordinatesFromMouseEvent() {

    when(mouseEvent.getX()).thenReturn(200);
    when(mouseEvent.getY()).thenReturn(300);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseEvent).getX();
    verify(mouseEvent).getY();
  }

  @Test
  void mouseReleased_shouldWorkWithDifferentSquareSizes() {
    int testX = 240;
    int testY = 320;
    when(mouseEvent.getX()).thenReturn(testX);
    when(mouseEvent.getY()).thenReturn(testY);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler).handleMouseInput(anyInt(), anyInt());

    verify(mouseInputHandler).handleMouseInput(
        intThat(arg -> arg >= 0),
        intThat(arg -> arg >= 0)
    );
  }

  @Test
  void mouseReleased_shouldHandleBoundaryValues() {

    when(mouseEvent.getX()).thenReturn(Integer.MAX_VALUE);
    when(mouseEvent.getY()).thenReturn(Integer.MAX_VALUE);

    clickHandler.mouseReleased(mouseEvent);

    verify(mouseInputHandler).handleMouseInput(anyInt(), anyInt());
  }
}