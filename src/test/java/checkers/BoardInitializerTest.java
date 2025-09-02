package checkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BoardInitializerTest {

  @Mock
  private BoardState mockBoardState;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Nested
  class PawnSetupTests {

    @Test
    void shouldPlaceBlackPiecesCorrectly() {

      BoardInitializer.setUpPawns(mockBoardState);

      for (int row = 0; row < GameConstants.NUM_STARTING_ROWS; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          if ((row + col) % 2 == 1) {
            verify(mockBoardState).setPiece(row, col, GameConstants.BLACK);
          }
        }
      }
    }

    @Test
    void shouldPlaceRedPiecesCorrectly() {

      BoardInitializer.setUpPawns(mockBoardState);

      int redStartRow =
          GameConstants.BOARD_SIZE - GameConstants.NUM_STARTING_ROWS;
      for (int row = redStartRow; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          if ((row + col) % 2 == 1) {
            verify(mockBoardState).setPiece(row, col, GameConstants.RED);
          }
        }
      }
    }

    @Test
    void shouldPlaceEmptyOnLightSquares() {

      BoardInitializer.setUpPawns(mockBoardState);

      for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          if ((row + col) % 2 == 0) {
            verify(mockBoardState).setPiece(row, col, GameConstants.EMPTY);
          }
        }
      }
    }

    @Test
    void shouldLeaveMiddleRowsEmpty() {

      BoardInitializer.setUpPawns(mockBoardState);

      for (int row = GameConstants.NUM_STARTING_ROWS;
           row < GameConstants.BOARD_SIZE - GameConstants.NUM_STARTING_ROWS;
           row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          if ((row + col) % 2 == 1) {
            verify(mockBoardState).setPiece(row, col, GameConstants.EMPTY);
          }
        }
      }
    }
  }

  @Nested
  class ConstantsVerificationTests {

    @Test
    void shouldUseCorrectGameConstants() {

      BoardInitializer.setUpPawns(mockBoardState);

      int totalCalls = GameConstants.BOARD_SIZE * GameConstants.BOARD_SIZE;
      verify(mockBoardState, times(totalCalls)).setPiece(anyInt(), anyInt(),
          anyInt());

      verify(mockBoardState, atLeast(1)).setPiece(anyInt(), anyInt(),
          eq(GameConstants.EMPTY));
      verify(mockBoardState, atLeast(1)).setPiece(anyInt(), anyInt(),
          eq(GameConstants.BLACK));
      verify(mockBoardState, atLeast(1)).setPiece(anyInt(), anyInt(),
          eq(GameConstants.RED));
    }

    @Test
    void shouldVerifyConstantValues() {

      org.junit.jupiter.api.Assertions.assertEquals(0, GameConstants.EMPTY);
      org.junit.jupiter.api.Assertions.assertEquals(1, GameConstants.RED);
      org.junit.jupiter.api.Assertions.assertEquals(2, GameConstants.BLACK);
      org.junit.jupiter.api.Assertions.assertEquals(8,
          GameConstants.BOARD_SIZE);
      org.junit.jupiter.api.Assertions.assertEquals(3,
          GameConstants.NUM_STARTING_ROWS);
    }
  }

  @Nested
  class BoundaryConditionsTests {

    @Test
    void shouldHandleCornerPositions() {

      BoardInitializer.setUpPawns(mockBoardState);

      verify(mockBoardState).setPiece(0, 0, GameConstants.EMPTY);
      verify(mockBoardState).setPiece(0, 1, GameConstants.BLACK);
      verify(mockBoardState).setPiece(7, 6, GameConstants.RED);
      verify(mockBoardState).setPiece(7, 7, GameConstants.EMPTY);
    }

    @Test
    void shouldHandleTransitionRows() {

      BoardInitializer.setUpPawns(mockBoardState);

      verify(mockBoardState).setPiece(2, 0, GameConstants.EMPTY);
      verify(mockBoardState).setPiece(2, 1, GameConstants.BLACK);
      verify(mockBoardState).setPiece(2, 2, GameConstants.EMPTY);
      verify(mockBoardState).setPiece(2, 3, GameConstants.BLACK);

      verify(mockBoardState).setPiece(3, 0, GameConstants.EMPTY);
      verify(mockBoardState).setPiece(3, 1, GameConstants.EMPTY);
      verify(mockBoardState).setPiece(3, 2, GameConstants.EMPTY);
      verify(mockBoardState).setPiece(3, 3, GameConstants.EMPTY);

      verify(mockBoardState).setPiece(5, 0, GameConstants.RED);
      verify(mockBoardState).setPiece(5, 1, GameConstants.EMPTY);
      verify(mockBoardState).setPiece(5, 2, GameConstants.RED);
      verify(mockBoardState).setPiece(5, 3, GameConstants.EMPTY);
    }

    @Test
    void shouldHandleFirstAndLastRows() {

      BoardInitializer.setUpPawns(mockBoardState);

      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        if ((col) % 2 == 1) {
          verify(mockBoardState).setPiece(0, col, GameConstants.BLACK);
        } else {
          verify(mockBoardState).setPiece(0, col, GameConstants.EMPTY);
        }
      }

      int lastRow = GameConstants.BOARD_SIZE - 1;
      for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
        if ((lastRow + col) % 2 == 1) {
          verify(mockBoardState).setPiece(lastRow, col, GameConstants.RED);
        } else {
          verify(mockBoardState).setPiece(lastRow, col, GameConstants.EMPTY);
        }
      }
    }
  }

  @Nested
  class IntegrationTests {

    @Test
    void shouldCallSetPieceForEveryPosition() {

      BoardInitializer.setUpPawns(mockBoardState);

      int expectedCalls = GameConstants.BOARD_SIZE * GameConstants.BOARD_SIZE;
      verify(mockBoardState, times(expectedCalls)).setPiece(anyInt(), anyInt(),
          anyInt());

      for (int row = 0; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          verify(mockBoardState, times(1)).setPiece(eq(row), eq(col), anyInt());
        }
      }
    }

    @Test
    void shouldMaintainCorrectPieceDistribution() {

      BoardInitializer.setUpPawns(mockBoardState);

      int piecesPerColor =
          GameConstants.NUM_STARTING_ROWS * (GameConstants.BOARD_SIZE / 2);

      verify(mockBoardState, times(piecesPerColor)).setPiece(anyInt(), anyInt(),
          eq(GameConstants.BLACK));
      verify(mockBoardState, times(piecesPerColor)).setPiece(anyInt(), anyInt(),
          eq(GameConstants.RED));

      int totalSquares = GameConstants.BOARD_SIZE * GameConstants.BOARD_SIZE;
      int emptySquares = totalSquares - (2 * piecesPerColor);
      verify(mockBoardState, times(emptySquares)).setPiece(anyInt(), anyInt(),
          eq(GameConstants.EMPTY));
    }

    @Test
    void shouldPlacePiecesOnlyOnDarkSquares() {
      BoardInitializer.setUpPawns(mockBoardState);

      for (int row = 0; row < GameConstants.NUM_STARTING_ROWS; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          if ((row + col) % 2 == 1) {
            verify(mockBoardState).setPiece(row, col, GameConstants.BLACK);
          } else {
            verify(mockBoardState).setPiece(row, col, GameConstants.EMPTY);
          }
        }
      }

      int redStartRow =
          GameConstants.BOARD_SIZE - GameConstants.NUM_STARTING_ROWS;
      for (int row = redStartRow; row < GameConstants.BOARD_SIZE; row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          if ((row + col) % 2 == 1) {
            verify(mockBoardState).setPiece(row, col, GameConstants.RED);
          } else {
            verify(mockBoardState).setPiece(row, col, GameConstants.EMPTY);
          }
        }
      }

      for (int row = GameConstants.NUM_STARTING_ROWS; row < redStartRow;
           row++) {
        for (int col = 0; col < GameConstants.BOARD_SIZE; col++) {
          verify(mockBoardState).setPiece(row, col, GameConstants.EMPTY);
        }
      }
    }
  }


  @Nested
  class ErrorHandlingTests {

    @Test
    void shouldHandleNullBoardState() {

      org.junit.jupiter.api.Assertions.assertThrows(
          NullPointerException.class,
          () -> BoardInitializer.setUpPawns(null)
      );
    }

    @Test
    void shouldHandleBoardStateExceptions() {

      doThrow(new RuntimeException("Board error"))
          .when(mockBoardState).setPiece(0, 0, GameConstants.EMPTY);

      org.junit.jupiter.api.Assertions.assertThrows(
          RuntimeException.class,
          () -> BoardInitializer.setUpPawns(mockBoardState)
      );
    }

    @Test
    void shouldNotUseInvalidPieceValues() {

      BoardInitializer.setUpPawns(mockBoardState);

      verify(mockBoardState, never()).setPiece(anyInt(), anyInt(),
          eq(GameConstants.RED_KING));
      verify(mockBoardState, never()).setPiece(anyInt(), anyInt(),
          eq(GameConstants.BLACK_KING));
      verify(mockBoardState, never()).setPiece(anyInt(), anyInt(), eq(-1));
      verify(mockBoardState, never()).setPiece(anyInt(), anyInt(), eq(99));
    }
  }
}