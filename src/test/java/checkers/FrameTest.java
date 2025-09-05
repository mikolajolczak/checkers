package checkers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FrameTest {

  @Mock
  private BoardState mockBoardState;

  private BoardPanel spyBoardPanel;
  private Frame frame;

  @BeforeEach
  void setUp() {

    spyBoardPanel = spy(new BoardPanel());
    frame = new Frame(mockBoardState, spyBoardPanel);
  }

  @Test
  void constructor_ShouldSetCorrectWindowSize() {
    Dimension expectedSize =
        new Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);
    assertEquals(expectedSize, frame.getSize());
  }

  @Test
  void constructor_ShouldSetLocationRelativeToNull() {
    assertDoesNotThrow(() -> new Frame(mockBoardState, spyBoardPanel));
  }

  @Test
  void constructor_ShouldSetCorrectBackground() {
    assertEquals(Color.LIGHT_GRAY, frame.getBackground());
  }

  @Test
  void constructor_ShouldSetCorrectDefaultCloseOperation() {
    assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
  }

  @Test
  void constructor_ShouldAddBoardPanelToFrame() {
    Component[] components = frame.getContentPane().getComponents();
    boolean boardPanelAdded = false;

    for (Component component : components) {
      if (component == spyBoardPanel) {
        boardPanelAdded = true;
        break;
      }
    }

    assertTrue(boardPanelAdded, "BoardPanel should be added to the frame");
  }

  @Test
  void constructor_WithNullBoardState_ShouldThrowNullPointerException() {
    assertThrows(NullPointerException.class,
        () -> new Frame(null, spyBoardPanel));
  }

  @Test
  void constructor_WithNullBoardPanel_ShouldThrowNullPointerException() {
    assertThrows(NullPointerException.class,
        () -> new Frame(mockBoardState, null));
  }

  @Test
  void addBoardListener_ShouldAddMouseListenerToBoard() {
    MouseListener mockMouseListener = mock(MouseListener.class);
    frame.addBoardListener(mockMouseListener);

    verify(spyBoardPanel).addMouseListener(mockMouseListener);
  }

  @Test
  void addBoardListener_WithNullListener_ShouldStillCallAddMouseListener() {
    frame.addBoardListener(null);
    verify(spyBoardPanel).addMouseListener(null);
  }

  @Test
  void addBoardListener_CalledMultipleTimes_ShouldAddAllListeners() {
    MouseListener listener1 = mock(MouseListener.class);
    MouseListener listener2 = mock(MouseListener.class);
    MouseListener listener3 = mock(MouseListener.class);

    frame.addBoardListener(listener1);
    frame.addBoardListener(listener2);
    frame.addBoardListener(listener3);

    verify(spyBoardPanel).addMouseListener(listener1);
    verify(spyBoardPanel).addMouseListener(listener2);
    verify(spyBoardPanel).addMouseListener(listener3);
  }

  @Test
  void checkGameFinished_ShouldCallGameEndEvaluatorEvaluateGameEnd() {
    try (
        var mockedGameEndEvaluator = mockConstruction(GameEndEvaluator.class)) {
      Frame testFrame = new Frame(mockBoardState, spyBoardPanel);
      testFrame.checkGameFinished();

      assertEquals(1, mockedGameEndEvaluator.constructed().size());
      GameEndEvaluator gameEndEvaluator =
          mockedGameEndEvaluator.constructed().getFirst();

      verify(gameEndEvaluator).evaluateGameEnd();
    }
  }

  @Test
  void checkGameFinished_CalledMultipleTimes_ShouldCallEvaluateGameEndEachTime() {
    try (
        var mockedGameEndEvaluator = mockConstruction(GameEndEvaluator.class)) {
      Frame testFrame = new Frame(mockBoardState, spyBoardPanel);

      testFrame.checkGameFinished();
      testFrame.checkGameFinished();
      testFrame.checkGameFinished();

      GameEndEvaluator gameEndEvaluator =
          mockedGameEndEvaluator.constructed().getFirst();
      verify(gameEndEvaluator, times(3)).evaluateGameEnd();
    }
  }

  @Test
  void gameEndEvaluator_ShouldBeCreatedWithCorrectParameters() {
    try (var mockedGameEndEvaluator = mockConstruction(GameEndEvaluator.class,
        (_, context) -> {
          assertEquals(2, context.arguments().size());
          assertSame(mockBoardState, context.arguments().get(0));
          assertInstanceOf(Frame.class, context.arguments().get(1));
        })) {

      new Frame(mockBoardState, spyBoardPanel);
      assertEquals(1, mockedGameEndEvaluator.constructed().size());
    }
  }

  @Test
  void frame_ShouldExtendJFrame() {
    assertInstanceOf(JFrame.class, frame, "Frame should extend JFrame");
  }

  @Test
  void frame_ShouldBeVisible_WhenSetVisible() {
    frame.setVisible(true);
    assertTrue(frame.isVisible());

    frame.setVisible(false);
    assertFalse(frame.isVisible());
  }

  @Test
  void frame_ShouldHandleResizing() {
    int newWidth = 1000;
    int newHeight = 800;

    frame.setSize(newWidth, newHeight);

    assertEquals(newWidth, frame.getWidth());
    assertEquals(newHeight, frame.getHeight());
  }
}
