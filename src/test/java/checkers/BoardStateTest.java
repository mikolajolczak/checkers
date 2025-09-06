package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoardStateTest {

  private BoardState boardState;

  @Nested
  class DefaultConstructorTest {

    @Test
    void shouldCreateEmptyBoardWithCorrectSize() {
      boardState = new BoardState();

      assertNotNull(boardState.getPieces());
      assertEquals(GameConstants.BOARD_SIZE, boardState.getPieces().length);

      for (int i = 0; i < GameConstants.BOARD_SIZE; i++) {
        assertNotNull(boardState.getPiecesRows(i));
        assertEquals(GameConstants.BOARD_SIZE,
            boardState.getPiecesRows(i).length);
      }
    }

    @Test
    void shouldInitializeAllFieldsToZero() {
      boardState = new BoardState();

      for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          assertEquals(0, boardState.getPiece(row, col));
        }
      }
    }
  }

  @Nested
  class ParameterConstructorTest {

    @Test
    void shouldCreateCopyOfGivenBoard() {
      int[][] originalPieces = {
          {1, 0, 1, 0},
          {0, 1, 0, 1},
          {1, 0, 1, 0},
          {0, 1, 0, 1}
      };

      boardState = new BoardState(originalPieces);

      for (int i = 0; i < originalPieces.length; i++) {
        assertArrayEquals(originalPieces[i], boardState.getPiecesRows(i));
      }
    }

    @Test
    void shouldCreateIndependentCopy() {
      int[][] originalPieces = {
          {1, 0, 1, 0},
          {0, 1, 0, 1},
          {1, 0, 1, 0},
          {0, 1, 0, 1}
      };

      boardState = new BoardState(originalPieces);

      originalPieces[0][0] = 999;
      originalPieces[1] = new int[]{5, 5, 5, 5};

      assertEquals(1, boardState.getPiece(0, 0));
      assertEquals(1, boardState.getPiece(1, 1));
    }

    @Test
    void shouldHandleEmptyArray() {
      int[][] emptyPieces = {};

      boardState = new BoardState(emptyPieces);

      assertNotNull(boardState.getPieces());
      assertEquals(0, boardState.getPieces().length);
    }

    @Test
    void shouldHandleJaggedArray() {
      int[][] jaggedPieces = {
          {1, 2},
          {3, 4, 5},
          {6}
      };

      boardState = new BoardState(jaggedPieces);

      assertEquals(3, boardState.getPieces().length);
      assertEquals(2, boardState.getPiecesRows(0).length);
      assertEquals(3, boardState.getPiecesRows(1).length);
      assertEquals(1, boardState.getPiecesRows(2).length);

      assertEquals(1, boardState.getPiece(0, 0));
      assertEquals(5, boardState.getPiece(1, 2));
      assertEquals(6, boardState.getPiece(2, 0));
    }
  }

  @Nested
  class GetPieceTest {

    @BeforeEach
    void setUp() {
      int[][] testPieces = {
          {1, 2, 3},
          {4, 5, 6},
          {7, 8, 9}
      };
      boardState = new BoardState(testPieces);
    }

    @Test
    void shouldReturnCorrectValueForDifferentPositions() {
      assertEquals(1, boardState.getPiece(0, 0));
      assertEquals(5, boardState.getPiece(1, 1));
      assertEquals(9, boardState.getPiece(2, 2));
      assertEquals(6, boardState.getPiece(1, 2));
    }

    @Test
    void shouldThrowExceptionForInvalidRowIndex() {
      assertThrows(ArrayIndexOutOfBoundsException.class,
          () -> boardState.getPiece(-1, 0));
      assertThrows(ArrayIndexOutOfBoundsException.class,
          () -> boardState.getPiece(3, 0));
    }

    @Test
    void shouldThrowExceptionForInvalidColumnIndex() {
      assertThrows(ArrayIndexOutOfBoundsException.class,
          () -> boardState.getPiece(0, -1));
      assertThrows(ArrayIndexOutOfBoundsException.class,
          () -> boardState.getPiece(0, 3));
    }
  }

  @Nested
  class SetPieceTest {

    @BeforeEach
    void setUp() {
      boardState = new BoardState();
    }

    @Test
    void shouldSetValueAtSpecificPosition() {
      boardState.setPiece(0, 0, 1);
      boardState.setPiece(1, 1, 2);
      boardState.setPiece(7, 7, 3);

      assertEquals(1, boardState.getPiece(0, 0));
      assertEquals(2, boardState.getPiece(1, 1));
      assertEquals(3, boardState.getPiece(7, 7));
    }

    @Test
    void shouldOverwriteExistingValues() {
      boardState.setPiece(2, 2, 10);
      assertEquals(10, boardState.getPiece(2, 2));

      boardState.setPiece(2, 2, 20);
      assertEquals(20, boardState.getPiece(2, 2));
    }

    @Test
    void shouldHandleNegativeValues() {
      boardState.setPiece(0, 0, -1);
      boardState.setPiece(1, 1, -100);

      assertEquals(-1, boardState.getPiece(0, 0));
      assertEquals(-100, boardState.getPiece(1, 1));
    }

    @Test
    void shouldThrowExceptionForInvalidRowIndex() {
      assertThrows(ArrayIndexOutOfBoundsException.class,
          () -> boardState.setPiece(-1, 0, 1));
      assertThrows(ArrayIndexOutOfBoundsException.class,
          () -> boardState.setPiece(GameConstants.BOARD_SIZE, 0, 1));
    }

    @Test
    void shouldThrowExceptionForInvalidColumnIndex() {
      assertThrows(ArrayIndexOutOfBoundsException.class,
          () -> boardState.setPiece(0, -1, 1));
      assertThrows(ArrayIndexOutOfBoundsException.class,
          () -> boardState.setPiece(0, GameConstants.BOARD_SIZE, 1));
    }
  }

  @Nested
  class CopyTest {

    @BeforeEach
    void setUp() {
      int[][] testPieces = {
          {1, 2, 3, 4},
          {5, 6, 7, 8},
          {9, 10, 11, 12},
          {13, 14, 15, 16}
      };
      boardState = new BoardState(testPieces);
    }

    @Test
    void shouldCreateNewInstance() {
      BoardState copy = boardState.copy();

      assertNotNull(copy);
      assertNotSame(boardState, copy);
    }

    @Test
    void copyShouldHaveIdenticalValues() {
      BoardState copy = boardState.copy();

      for (int row = 0; row < 4; row++) {
        for (int col = 0; col < 4; col++) {
          assertEquals(boardState.getPiece(row, col), copy.getPiece(row, col));
        }
      }
    }

    @Test
    void copyShouldBeIndependent() {
      BoardState copy = boardState.copy();

      boardState.setPiece(0, 0, 999);

      assertEquals(1, copy.getPiece(0, 0));
      assertEquals(999, boardState.getPiece(0, 0));
    }

    @Test
    void copyOfCopyShouldWorkCorrectly() {
      BoardState copy1 = boardState.copy();
      BoardState copy2 = copy1.copy();

      assertNotSame(copy1, copy2);
      assertNotSame(boardState, copy2);

      copy1.setPiece(1, 1, 888);

      assertEquals(6, boardState.getPiece(1, 1));
      assertEquals(888, copy1.getPiece(1, 1));
      assertEquals(6, copy2.getPiece(1, 1));
    }

    @Test
    void copyOfEmptyBoardShouldWork() {
      BoardState emptyBoard = new BoardState();
      BoardState copy = emptyBoard.copy();

      assertNotSame(emptyBoard, copy);
      assertEquals(GameConstants.BOARD_SIZE, copy.getPieces().length);

      for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          assertEquals(0, copy.getPiece(row, col));
        }
      }
    }
  }

  @Nested
  class IntegrationTest {

    @Test
    void complexUsageScenario() {

      int[][] initialState =
          new int[GameConstants.BOARD_SIZE][GameConstants.BOARD_SIZE];
      initialState[0][1] = 1;
      initialState[0][3] = 1;
      initialState[7][0] = 2;
      initialState[7][2] = 2;

      boardState = new BoardState(initialState);

      assertEquals(1, boardState.getPiece(0, 1));
      assertEquals(1, boardState.getPiece(0, 3));
      assertEquals(2, boardState.getPiece(7, 0));
      assertEquals(2, boardState.getPiece(7, 2));
      assertEquals(0, boardState.getPiece(3, 3));

      boardState.setPiece(0, 1, 0);
      boardState.setPiece(1, 2, 1);

      BoardState copy = boardState.copy();

      boardState.setPiece(7, 0, 0);
      boardState.setPiece(6, 1, 2);

      assertEquals(0, copy.getPiece(0, 1));
      assertEquals(1, copy.getPiece(1, 2));
      assertEquals(2, copy.getPiece(7, 0));
      assertEquals(0, copy.getPiece(6, 1));

      assertEquals(0, boardState.getPiece(7, 0));
      assertEquals(2, boardState.getPiece(6, 1));
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test
    void shouldHandleLargeValues() {
      boardState = new BoardState();

      boardState.setPiece(0, 0, Integer.MAX_VALUE);
      boardState.setPiece(1, 1, Integer.MIN_VALUE);

      assertEquals(Integer.MAX_VALUE, boardState.getPiece(0, 0));
      assertEquals(Integer.MIN_VALUE, boardState.getPiece(1, 1));
    }

    @Test
    void multipleCopying() {
      int[][] testPieces = {{1, 2}, {3, 4}};
      boardState = new BoardState(testPieces);

      BoardState copy1 = boardState.copy();
      BoardState copy2 = copy1.copy();
      BoardState copy3 = copy2.copy();

      assertEquals(1, copy3.getPiece(0, 0));
      assertEquals(4, copy3.getPiece(1, 1));

      copy2.setPiece(0, 0, 999);

      assertEquals(1, boardState.getPiece(0, 0));
      assertEquals(1, copy1.getPiece(0, 0));
      assertEquals(999, copy2.getPiece(0, 0));
      assertEquals(1, copy3.getPiece(0, 0));
    }
  }
}