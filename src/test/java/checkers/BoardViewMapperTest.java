package checkers;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BoardViewMapperTest {

  private static final int BOARD_SIZE = GameConstants.BOARD_SIZE;
  @Mock
  private BoardState mockBoardState;
  @Mock
  private SelectionState mockSelectionState;
  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  private void setupMockBoardWithAllZeros() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        when(mockBoardState.getPiece(row, col)).thenReturn(0);
      }
    }
  }

  private void setupRemainingFieldsAsZero() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        when(mockBoardState.getPiece(row, col)).thenReturn(0);
      }
    }
  }

  private void setupMockSelectionWithNoSelection() {
    when(mockSelectionState.getSelectedRow()).thenReturn(-1);
    when(mockSelectionState.getSelectedColumn()).thenReturn(-1);
  }

  private PieceView findPieceView(List<PieceView> pieces, int row, int col) {
    return pieces.stream()
        .filter(p -> p.row() == row && p.col() == col)
        .findFirst()
        .orElseThrow(() -> new AssertionError(
            "PieceView not found at (" + row + "," + col + ")"));
  }

  @Nested
  class ConstructorTest {

    @Test
    void classShouldNotBeInstantiable() {
      var constructors = BoardViewMapper.class.getDeclaredConstructors();
      assertEquals(1, constructors.length);
      assertFalse(constructors[0].canAccess(null));

      constructors[0].setAccessible(true);
      assertDoesNotThrow(() -> constructors[0].newInstance());
    }
  }

  @Nested
  class ToPieceViewsTest {

    @Test
    void shouldReturnListOfCorrectSize() {

      setupMockBoardWithAllZeros();
      setupMockSelectionWithNoSelection();

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      assertNotNull(result);
      assertEquals(BOARD_SIZE * BOARD_SIZE, result.size());
    }

    @Test
    void shouldCreatePieceViewForEachBoardField() {

      setupMockBoardWithAllZeros();
      setupMockSelectionWithNoSelection();

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      int index = 0;
      for (int row = 0; row < BOARD_SIZE; row++) {
        for (int col = 0; col < BOARD_SIZE; col++) {
          PieceView pieceView = result.get(index);
          assertEquals(row, pieceView.row());
          assertEquals(col, pieceView.col());
          index++;
        }
      }
    }

    @Test
    void shouldCorrectlyMapPieceTypes() {
      setupRemainingFieldsAsZero();
      when(mockBoardState.getPiece(0, 0)).thenReturn(0);
      when(mockBoardState.getPiece(0, 1)).thenReturn(1);
      when(mockBoardState.getPiece(0, 2)).thenReturn(2);
      when(mockBoardState.getPiece(0, 3)).thenReturn(3);

      setupMockSelectionWithNoSelection();

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      assertEquals(0, result.get(0).type());
      assertEquals(1, result.get(1).type());
      assertEquals(2, result.get(2).type());
      assertEquals(3, result.get(3).type());
    }

    @Test
    void shouldCorrectlyMarkSelectedField() {

      setupMockBoardWithAllZeros();
      when(mockSelectionState.getSelectedRow()).thenReturn(2);
      when(mockSelectionState.getSelectedColumn()).thenReturn(3);

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      for (PieceView pieceView : result) {
        if (pieceView.row() == 2 && pieceView.col() == 3) {
          assertTrue(pieceView.selected());
        } else {
          assertFalse(pieceView.selected());
        }
      }
    }

    @Test
    void shouldHandleNoSelection() {

      setupMockBoardWithAllZeros();
      when(mockSelectionState.getSelectedRow()).thenReturn(-1);
      when(mockSelectionState.getSelectedColumn()).thenReturn(-1);

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      for (PieceView pieceView : result) {
        assertFalse(pieceView.selected());
      }
    }

    @Test
    void shouldHandleSelectionAtTopLeft() {

      setupMockBoardWithAllZeros();
      when(mockSelectionState.getSelectedRow()).thenReturn(0);
      when(mockSelectionState.getSelectedColumn()).thenReturn(0);

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      assertTrue(result.getFirst().selected());

      for (int i = 1; i < result.size(); i++) {
        assertFalse(result.get(i).selected());
      }
    }

    @Test
    void shouldHandleSelectionAtBottomRight() {

      setupMockBoardWithAllZeros();
      when(mockSelectionState.getSelectedRow()).thenReturn(BOARD_SIZE - 1);
      when(mockSelectionState.getSelectedColumn()).thenReturn(BOARD_SIZE - 1);

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      int lastIndex = result.size() - 1;
      assertTrue(result.get(lastIndex).selected());

      for (int i = 0; i < lastIndex; i++) {
        assertFalse(result.get(i).selected());
      }
    }

    @Test
    void shouldCallGetPieceForEachField() {

      setupMockBoardWithAllZeros();
      setupMockSelectionWithNoSelection();

      BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      for (int row = 0; row < BOARD_SIZE; row++) {
        for (int col = 0; col < BOARD_SIZE; col++) {
          verify(mockBoardState).getPiece(row, col);
        }
      }
    }

    @Test
    void shouldCallSelectionStateMethods() {

      setupMockBoardWithAllZeros();
      setupMockSelectionWithNoSelection();

      BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      verify(mockSelectionState,
          times(BOARD_SIZE * BOARD_SIZE)).getSelectedRow();
      verify(mockSelectionState,
          times(BOARD_SIZE * BOARD_SIZE)).getSelectedColumn();
    }

    @Test
    void shouldHandleDifferentPieceTypesAndSelection() {
      setupRemainingFieldsAsZero();
      when(mockBoardState.getPiece(1, 1)).thenReturn(1);
      when(mockBoardState.getPiece(2, 2)).thenReturn(2);
      when(mockBoardState.getPiece(3, 3)).thenReturn(3);

      when(mockSelectionState.getSelectedRow()).thenReturn(2);
      when(mockSelectionState.getSelectedColumn()).thenReturn(2);

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      PieceView piece11 = findPieceView(result, 1, 1);
      assertEquals(1, piece11.type());
      assertFalse(piece11.selected());

      PieceView piece22 = findPieceView(result, 2, 2);
      assertEquals(2, piece22.type());
      assertTrue(piece22.selected());

      PieceView piece33 = findPieceView(result, 3, 3);
      assertEquals(3, piece33.type());
      assertFalse(piece33.selected());
    }

    @Test
    void shouldHandleNegativePieceTypes() {
      setupRemainingFieldsAsZero();
      when(mockBoardState.getPiece(0, 0)).thenReturn(-1);
      when(mockBoardState.getPiece(0, 1)).thenReturn(-5);

      setupMockSelectionWithNoSelection();

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      assertEquals(-1, result.get(0).type());
      assertEquals(-5, result.get(1).type());
    }

    @Test
    void shouldMaintainCorrectOrderInList() {

      setupMockBoardWithAllZeros();
      setupMockSelectionWithNoSelection();

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      int expectedIndex = 0;
      for (int row = 0; row < BOARD_SIZE; row++) {
        for (int col = 0; col < BOARD_SIZE; col++) {
          PieceView pieceView = result.get(expectedIndex);
          assertEquals(row, pieceView.row());
          assertEquals(col, pieceView.col());
          expectedIndex++;
        }
      }
    }
  }

  @Nested
  class IntegrationTest {

    @Test
    void complexScenarioWithDifferentPiecesAndSelection() {
      setupRemainingFieldsAsZero();
      when(mockBoardState.getPiece(0, 1)).thenReturn(1);
      when(mockBoardState.getPiece(0, 3)).thenReturn(1);
      when(mockBoardState.getPiece(0, 5)).thenReturn(1);

      when(mockBoardState.getPiece(7, 0)).thenReturn(2);
      when(mockBoardState.getPiece(7, 2)).thenReturn(2);
      when(mockBoardState.getPiece(7, 4)).thenReturn(2);

      when(mockBoardState.getPiece(2, 2)).thenReturn(3);
      when(mockBoardState.getPiece(5, 5)).thenReturn(4);

      when(mockSelectionState.getSelectedRow()).thenReturn(2);
      when(mockSelectionState.getSelectedColumn()).thenReturn(2);

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      assertEquals(BOARD_SIZE * BOARD_SIZE, result.size());

      PieceView piece01 = findPieceView(result, 0, 1);
      assertEquals(1, piece01.type());
      assertFalse(piece01.selected());

      PieceView selectedPiece = findPieceView(result, 2, 2);
      assertEquals(3, selectedPiece.type());
      assertTrue(selectedPiece.selected());

      PieceView piece70 = findPieceView(result, 7, 0);
      assertEquals(2, piece70.type());
      assertFalse(piece70.selected());

      PieceView emptyPiece = findPieceView(result, 4, 4);
      assertEquals(0, emptyPiece.type());
      assertFalse(emptyPiece.selected());
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test
    void shouldHandleVeryLargePieceTypes() {
      setupRemainingFieldsAsZero();
      when(mockBoardState.getPiece(0, 0)).thenReturn(Integer.MAX_VALUE);
      when(mockBoardState.getPiece(0, 1)).thenReturn(Integer.MIN_VALUE);

      setupMockSelectionWithNoSelection();

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      assertEquals(Integer.MAX_VALUE, result.get(0).type());
      assertEquals(Integer.MIN_VALUE, result.get(1).type());
    }

    @Test
    void shouldHandleSelectionOutOfBounds() {

      setupMockBoardWithAllZeros();
      when(mockSelectionState.getSelectedRow()).thenReturn(BOARD_SIZE + 5);
      when(mockSelectionState.getSelectedColumn()).thenReturn(BOARD_SIZE + 10);

      List<PieceView> result =
          BoardViewMapper.toPieceViews(mockBoardState, mockSelectionState);

      for (PieceView pieceView : result) {
        assertFalse(pieceView.selected());
      }
    }
  }
}