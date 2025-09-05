package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;


class SelectionStateTest {

  private SelectionState selectionState;

  @BeforeEach
  void setUp() {
    selectionState = new SelectionState();
  }

  @Nested
  class ConstructorTests {

    @Test
    void shouldInitializeWithDefaultValues() {

      assertEquals(GameConstants.BOARD_SIZE, selectionState.getSelectedRow(),
          "Initial selectedRow should be equal to BOARD_SIZE");
      assertEquals(GameConstants.BOARD_SIZE, selectionState.getSelectedColumn(),
          "Initial selectedColumn should be equal to BOARD_SIZE");
    }

    @Test
    void shouldCreateMultipleIndependentInstances() {

      SelectionState firstInstance = new SelectionState();
      SelectionState secondInstance = new SelectionState();

      firstInstance.setSelectedRow(3);
      firstInstance.setSelectedColumn(5);

      assertNotSame(firstInstance, secondInstance,
          "Instances should be different objects");
      assertEquals(GameConstants.BOARD_SIZE, secondInstance.getSelectedRow(),
          "Second instance should not be affected by changes to first "
              + "instance");
      assertEquals(GameConstants.BOARD_SIZE, secondInstance.getSelectedColumn(),
          "Second instance should not be affected by changes to first "
              + "instance");
    }
  }

  @Nested
  class RowOperationsTests {

    @Test
    void shouldGetInitialRowValue() {

      int actualRow = selectionState.getSelectedRow();

      assertEquals(GameConstants.BOARD_SIZE, actualRow,
          "Initial row should be BOARD_SIZE");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void shouldSetAndGetValidRowValues(int row) {

      selectionState.setSelectedRow(row);
      int actualRow = selectionState.getSelectedRow();

      assertEquals(row, actualRow,
          String.format("Row should be set to %d", row));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void shouldHandleNegativeRowValues(int negativeRow) {

      selectionState.setSelectedRow(negativeRow);
      int actualRow = selectionState.getSelectedRow();

      assertEquals(negativeRow, actualRow,
          String.format("Row should accept negative value %d", negativeRow));
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 100, 1000})
    void shouldHandleRowValuesBeyondBoardSize(int largeRow) {

      selectionState.setSelectedRow(largeRow);
      int actualRow = selectionState.getSelectedRow();

      assertEquals(largeRow, actualRow,
          String.format("Row should accept value %d beyond board size",
              largeRow));
    }

    @Test
    void shouldNotAffectColumnWhenSettingRow() {

      selectionState.setSelectedColumn(3);
      int originalColumn = selectionState.getSelectedColumn();

      selectionState.setSelectedRow(5);

      assertEquals(5, selectionState.getSelectedRow(), "Row should be updated");
      assertEquals(originalColumn, selectionState.getSelectedColumn(),
          "Column should remain unchanged when setting row");
    }
  }

  @Nested
  class ColumnOperationsTests {

    @Test
    void shouldGetInitialColumnValue() {

      int actualColumn = selectionState.getSelectedColumn();

      assertEquals(GameConstants.BOARD_SIZE, actualColumn,
          "Initial column should be BOARD_SIZE");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void shouldSetAndGetValidColumnValues(int column) {

      selectionState.setSelectedColumn(column);
      int actualColumn = selectionState.getSelectedColumn();

      assertEquals(column, actualColumn,
          String.format("Column should be set to %d", column));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -10, -100})
    void shouldHandleNegativeColumnValues(int negativeColumn) {

      selectionState.setSelectedColumn(negativeColumn);
      int actualColumn = selectionState.getSelectedColumn();

      assertEquals(negativeColumn, actualColumn,
          String.format("Column should accept negative value %d",
              negativeColumn));
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 9, 10, 100, 1000})
    void shouldHandleColumnValuesBeyondBoardSize(int largeColumn) {

      selectionState.setSelectedColumn(largeColumn);
      int actualColumn = selectionState.getSelectedColumn();

      assertEquals(largeColumn, actualColumn,
          String.format("Column should accept value %d beyond board size",
              largeColumn));
    }

    @Test
    void shouldNotAffectRowWhenSettingColumn() {

      selectionState.setSelectedRow(3);
      int originalRow = selectionState.getSelectedRow();

      selectionState.setSelectedColumn(5);

      assertEquals(5, selectionState.getSelectedColumn(),
          "Column should be updated");
      assertEquals(originalRow, selectionState.getSelectedRow(),
          "Row should remain unchanged when setting column");
    }
  }

  @Nested
  class IntegrationTests {

    @Test
    void shouldHandleMultipleOperationsInSequence() {

      selectionState.setSelectedRow(2);
      selectionState.setSelectedColumn(4);
      assertEquals(2, selectionState.getSelectedRow(),
          "Row should be 2 after first set");
      assertEquals(4, selectionState.getSelectedColumn(),
          "Column should be 4 after first set");

      selectionState.setSelectedRow(6);
      assertEquals(6, selectionState.getSelectedRow(),
          "Row should be 6 after second set");
      assertEquals(4, selectionState.getSelectedColumn(),
          "Column should remain 4");

      selectionState.setSelectedColumn(1);
      assertEquals(6, selectionState.getSelectedRow(), "Row should remain 6");
      assertEquals(1, selectionState.getSelectedColumn(),
          "Column should be 1 after second set");
    }

    @Test
    void shouldHandleSettingSameValuesMultipleTimes() {

      int row = 3;
      int column = 5;

      selectionState.setSelectedRow(row);
      selectionState.setSelectedColumn(column);
      selectionState.setSelectedRow(row);
      selectionState.setSelectedColumn(column);

      assertEquals(row, selectionState.getSelectedRow(),
          "Row should maintain value after multiple identical sets");
      assertEquals(column, selectionState.getSelectedColumn(),
          "Column should maintain value after multiple identical sets");
    }

    @Test
    void shouldHandleBoundaryValues() {

      selectionState.setSelectedRow(0);
      selectionState.setSelectedColumn(0);
      assertEquals(0, selectionState.getSelectedRow(), "Should handle row = 0");
      assertEquals(0, selectionState.getSelectedColumn(),
          "Should handle column = 0");

      selectionState.setSelectedRow(7);
      selectionState.setSelectedColumn(7);
      assertEquals(7, selectionState.getSelectedRow(), "Should handle row = 7");
      assertEquals(7, selectionState.getSelectedColumn(),
          "Should handle column = 7");

      selectionState.setSelectedRow(GameConstants.BOARD_SIZE);
      selectionState.setSelectedColumn(GameConstants.BOARD_SIZE);
      assertEquals(GameConstants.BOARD_SIZE, selectionState.getSelectedRow(),
          "Should handle row = BOARD_SIZE");
      assertEquals(GameConstants.BOARD_SIZE, selectionState.getSelectedColumn(),
          "Should handle column = BOARD_SIZE");
    }

    @Test
    void shouldMaintainStateConsistencyDuringComplexOperations() {

      int[] rowSequence = {0, 3, 7, 2, 5, 1, 6, 4};
      int[] columnSequence = {7, 1, 4, 6, 0, 3, 2, 5};

      for (int i = 0; i < rowSequence.length; i++) {
        selectionState.setSelectedRow(rowSequence[i]);
        selectionState.setSelectedColumn(columnSequence[i]);

        assertEquals(rowSequence[i], selectionState.getSelectedRow(),
            String.format("Row should be %d at iteration %d", rowSequence[i],
                i));
        assertEquals(columnSequence[i], selectionState.getSelectedColumn(),
            String.format("Column should be %d at iteration %d",
                columnSequence[i], i));
      }
    }
  }

  @Nested
  class EdgeCasesTests {

    @Test
    void shouldHandleIntegerMaxValue() {

      selectionState.setSelectedRow(Integer.MAX_VALUE);
      selectionState.setSelectedColumn(Integer.MAX_VALUE);

      assertEquals(Integer.MAX_VALUE, selectionState.getSelectedRow(),
          "Should handle Integer.MAX_VALUE for row");
      assertEquals(Integer.MAX_VALUE, selectionState.getSelectedColumn(),
          "Should handle Integer.MAX_VALUE for column");
    }

    @Test
    void shouldHandleIntegerMinValue() {

      selectionState.setSelectedRow(Integer.MIN_VALUE);
      selectionState.setSelectedColumn(Integer.MIN_VALUE);

      assertEquals(Integer.MIN_VALUE, selectionState.getSelectedRow(),
          "Should handle Integer.MIN_VALUE for row");
      assertEquals(Integer.MIN_VALUE, selectionState.getSelectedColumn(),
          "Should handle Integer.MIN_VALUE for column");
    }

    @Test
    void shouldHandleZeroValues() {

      selectionState.setSelectedRow(0);
      selectionState.setSelectedColumn(0);

      assertEquals(0, selectionState.getSelectedRow(),
          "Should handle zero for row");
      assertEquals(0, selectionState.getSelectedColumn(),
          "Should handle zero for column");
    }
  }
}