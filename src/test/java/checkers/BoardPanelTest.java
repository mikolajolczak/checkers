package checkers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BoardPanelTest {

  private BoardPanel boardPanel;

  @Mock
  private Graphics mockGraphics;

  @Mock
  private PieceView mockPiece1;

  @Mock
  private PieceView mockPiece2;

  @Mock
  private PieceView mockPiece3;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    boardPanel = new BoardPanel();

    when(mockGraphics.create()).thenReturn(mockGraphics);
    when(
        mockGraphics.create(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(
        mockGraphics);
    doNothing().when(mockGraphics).dispose();
    doNothing().when(mockGraphics).translate(anyInt(), anyInt());
    doNothing().when(mockGraphics)
        .setClip(anyInt(), anyInt(), anyInt(), anyInt());
    when(mockGraphics.getClipBounds()).thenReturn(
        new java.awt.Rectangle(0, 0, 800, 600));
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void shouldExtendJPanel() {

    assertInstanceOf(JPanel.class, boardPanel,
        "BoardPanel should extend JPanel");
  }

  @Test
  void shouldSetPiecesToDrawAndTriggerRepaint() {

    List<PieceView> pieces = Arrays.asList(mockPiece1, mockPiece2);
    BoardPanel spyPanel = spy(boardPanel);

    spyPanel.setPiecesToDraw(pieces);

    verify(spyPanel, times(1)).repaint();
  }

  @Test
  void shouldAcceptNullListOfPieces() {

    assertDoesNotThrow(() -> boardPanel.setPiecesToDraw(null),
        "Should accept null list without throwing exception");
  }

  @Test
  void shouldAcceptEmptyListOfPieces() {

    List<PieceView> emptyList = new ArrayList<>();

    assertDoesNotThrow(() -> boardPanel.setPiecesToDraw(emptyList),
        "Should accept empty list without throwing exception");
  }

  @Test
  void shouldHandlePaintComponentWithNullPiecesList() {

    boardPanel.setPiecesToDraw(null);

    when(mockGraphics.create()).thenReturn(mockGraphics);
    doNothing().when(mockGraphics).dispose();

    try (MockedStatic<SquareRenderer> squareRenderer = mockStatic(
        SquareRenderer.class);
         MockedStatic<PieceRenderer> pieceRenderer = mockStatic(
             PieceRenderer.class)) {

      boardPanel.paintComponent(mockGraphics);

      squareRenderer.verifyNoInteractions();
      pieceRenderer.verifyNoInteractions();
    }
  }

  @Test
  void shouldHandlePaintComponentWithEmptyPiecesList() {

    List<PieceView> emptyList = new ArrayList<>();
    boardPanel.setPiecesToDraw(emptyList);

    when(mockGraphics.create()).thenReturn(mockGraphics);
    doNothing().when(mockGraphics).dispose();

    try (MockedStatic<SquareRenderer> squareRenderer = mockStatic(
        SquareRenderer.class);
         MockedStatic<PieceRenderer> pieceRenderer = mockStatic(
             PieceRenderer.class)) {

      boardPanel.paintComponent(mockGraphics);

      squareRenderer.verifyNoInteractions();
      pieceRenderer.verifyNoInteractions();
    }
  }

  @Test
  void shouldDrawSinglePieceCorrectly() {

    when(mockPiece1.row()).thenReturn(3);
    when(mockPiece1.col()).thenReturn(4);
    when(mockPiece1.selected()).thenReturn(true);

    List<PieceView> pieces = List.of(mockPiece1);
    boardPanel.setPiecesToDraw(pieces);

    try (MockedStatic<SquareRenderer> squareRenderer = mockStatic(
        SquareRenderer.class);
         MockedStatic<PieceRenderer> pieceRenderer = mockStatic(
             PieceRenderer.class);
         MockedStatic<GameConstants> _ = mockStatic(
             GameConstants.class)) {

      assertDoesNotThrow(() -> boardPanel.paintComponent(mockGraphics));

      squareRenderer.verify(() ->
          SquareRenderer.drawSquare(mockGraphics, 3, 4, true, 50), times(1));
      pieceRenderer.verify(() ->
          PieceRenderer.drawPiece(mockGraphics, mockPiece1), times(1));
    }
  }

  @Test
  void shouldDrawMultiplePiecesCorrectly() {

    when(mockPiece1.row()).thenReturn(1);
    when(mockPiece1.col()).thenReturn(2);
    when(mockPiece1.selected()).thenReturn(false);

    when(mockPiece2.row()).thenReturn(5);
    when(mockPiece2.col()).thenReturn(6);
    when(mockPiece2.selected()).thenReturn(true);

    when(mockPiece3.row()).thenReturn(7);
    when(mockPiece3.col()).thenReturn(0);
    when(mockPiece3.selected()).thenReturn(false);

    List<PieceView> pieces = Arrays.asList(mockPiece1, mockPiece2, mockPiece3);
    boardPanel.setPiecesToDraw(pieces);

    try (MockedStatic<SquareRenderer> squareRenderer = mockStatic(
        SquareRenderer.class);
         MockedStatic<PieceRenderer> pieceRenderer = mockStatic(
             PieceRenderer.class);
         MockedStatic<GameConstants> _ = mockStatic(
             GameConstants.class)) {

      assertDoesNotThrow(() -> boardPanel.paintComponent(mockGraphics));

      squareRenderer.verify(() ->
          SquareRenderer.drawSquare(mockGraphics, 1, 2, false, 50), times(1));
      squareRenderer.verify(() ->
          SquareRenderer.drawSquare(mockGraphics, 5, 6, true, 50), times(1));
      squareRenderer.verify(() ->
          SquareRenderer.drawSquare(mockGraphics, 7, 0, false, 50), times(1));

      pieceRenderer.verify(() ->
          PieceRenderer.drawPiece(mockGraphics, mockPiece1), times(1));
      pieceRenderer.verify(() ->
          PieceRenderer.drawPiece(mockGraphics, mockPiece2), times(1));
      pieceRenderer.verify(() ->
          PieceRenderer.drawPiece(mockGraphics, mockPiece3), times(1));
    }
  }

  @Test
  void shouldCallSuperPaintComponent() {

    BoardPanel spyPanel = spy(boardPanel);
    List<PieceView> pieces = List.of(mockPiece1);
    spyPanel.setPiecesToDraw(pieces);

    when(mockPiece1.row()).thenReturn(0);
    when(mockPiece1.col()).thenReturn(0);
    when(mockPiece1.selected()).thenReturn(false);

    try (MockedStatic<SquareRenderer> squareRenderer = mockStatic(
        SquareRenderer.class);
         MockedStatic<PieceRenderer> _ = mockStatic(
             PieceRenderer.class);
         MockedStatic<GameConstants> _ = mockStatic(
             GameConstants.class)) {

      assertDoesNotThrow(() -> spyPanel.paintComponent(mockGraphics));

      squareRenderer.verify(() ->
              SquareRenderer.drawSquare(any(Graphics.class), anyInt(), anyInt(),
                  anyBoolean(), anyInt()),
          times(1));
    }
  }

  @Test
  void shouldHandlePiecesWithEdgeCaseCoordinates() {

    when(mockPiece1.row()).thenReturn(0);
    when(mockPiece1.col()).thenReturn(0);
    when(mockPiece1.selected()).thenReturn(false);

    when(mockPiece2.row()).thenReturn(7);
    when(mockPiece2.col()).thenReturn(7);
    when(mockPiece2.selected()).thenReturn(true);

    List<PieceView> pieces = Arrays.asList(mockPiece1, mockPiece2);
    boardPanel.setPiecesToDraw(pieces);

    try (MockedStatic<SquareRenderer> squareRenderer = mockStatic(
        SquareRenderer.class);
         MockedStatic<PieceRenderer> _ = mockStatic(
             PieceRenderer.class);
         MockedStatic<GameConstants> _ = mockStatic(
             GameConstants.class)) {

      assertDoesNotThrow(() -> boardPanel.paintComponent(mockGraphics),
          "Should handle edge case coordinates without throwing exception");

      squareRenderer.verify(() ->
          SquareRenderer.drawSquare(mockGraphics, 0, 0, false, 50), times(1));
      squareRenderer.verify(() ->
          SquareRenderer.drawSquare(mockGraphics, 7, 7, true, 50), times(1));
    }
  }

  @Test
  void shouldUpdatePiecesListCorrectlyOnMultipleCalls() {

    List<PieceView> firstList = List.of(mockPiece1);
    List<PieceView> secondList = Arrays.asList(mockPiece2, mockPiece3);

    when(mockPiece2.row()).thenReturn(2);
    when(mockPiece2.col()).thenReturn(3);
    when(mockPiece2.selected()).thenReturn(false);

    when(mockPiece3.row()).thenReturn(4);
    when(mockPiece3.col()).thenReturn(5);
    when(mockPiece3.selected()).thenReturn(true);

    try (MockedStatic<SquareRenderer> _ = mockStatic(SquareRenderer.class);
         MockedStatic<PieceRenderer> pieceRenderer = mockStatic(
             PieceRenderer.class)) {

      boardPanel.setPiecesToDraw(firstList);
      boardPanel.setPiecesToDraw(secondList);
      assertDoesNotThrow(() -> boardPanel.paintComponent(mockGraphics));

      pieceRenderer.verify(() ->
          PieceRenderer.drawPiece(mockGraphics, mockPiece1), never());
      pieceRenderer.verify(() ->
          PieceRenderer.drawPiece(mockGraphics, mockPiece2), times(1));
      pieceRenderer.verify(() ->
          PieceRenderer.drawPiece(mockGraphics, mockPiece3), times(1));
    }
  }
}