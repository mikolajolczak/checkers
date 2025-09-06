package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CaptureRulesTest {

  private BoardState mockBoardState;

  @BeforeEach
  void setUp() {
    mockBoardState = mock(BoardState.class);
  }

  @Nested
  class CanCaptureTests {

    @Test
    void shouldReturnTrueWhenRegularPieceCanCapture() {

      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);

      boolean result = CaptureRules.canCapture(4, 4, mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenKingCanCapture() {

      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.RED_KING);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);

      boolean result = CaptureRules.canCapture(2, 2, mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenNoCaptureIsAvailable() {

      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(1, 3)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(3, 1)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.EMPTY);

      boolean result = CaptureRules.canCapture(2, 2, mockBoardState);

      assertFalse(result);
    }
  }

  @Nested
  class CanKingCaptureTests {

    @Test
    void shouldReturnTrueWhenRedKingCanCaptureBlackPiece() {

      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(5, 5)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

      boolean result = CaptureRules.canKingCapture(1, 1, GameConstants.RED_KING,
          mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenBlackKingCanCaptureRedKing() {

      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.RED_KING);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.canKingCapture(0, 0, GameConstants.BLACK_KING,
              mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenKingCannotCaptureBlockedLanding() {

      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);

      boolean result = CaptureRules.canKingCapture(0, 0, GameConstants.RED_KING,
          mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNoEnemyPiecesInRange() {

      when(mockBoardState.getPiece(anyInt(), anyInt())).thenReturn(
          GameConstants.EMPTY);

      boolean result = CaptureRules.canKingCapture(3, 3, GameConstants.RED_KING,
          mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPathBlockedBeforeEnemy() {

      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.EMPTY);

      boolean result = CaptureRules.canKingCapture(0, 0, GameConstants.RED_KING,
          mockBoardState);

      assertFalse(result);
    }
  }

  @Nested
  class CanRegularPieceCaptureTests {

    @Test
    void shouldReturnTrueWhenRedPieceCanCaptureBlackPiece() {

      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.canRegularPieceCapture(4, 4, GameConstants.RED,
              mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenBlackPieceCanCaptureRedKing() {

      when(mockBoardState.getPiece(5, 2)).thenReturn(GameConstants.RED_KING);
      when(mockBoardState.getPiece(6, 4)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.canRegularPieceCapture(4, 3, GameConstants.BLACK,
              mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenLandingSquareOccupied() {

      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.RED);

      boolean result =
          CaptureRules.canRegularPieceCapture(2, 2, GameConstants.RED,
              mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNoEnemyPieceToCapture() {

      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(3, 1)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(4, 0)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.canRegularPieceCapture(2, 2, GameConstants.RED,
              mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTryingToCaptureOwnPiece() {

      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.canRegularPieceCapture(2, 2, GameConstants.RED,
              mockBoardState);

      assertFalse(result);
    }
  }

  @Nested
  class IsLegalCaptureTests {

    @Test
    void shouldReturnTrueForLegalRedPieceCapture() {

      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);

      boolean result =
          CaptureRules.isLegalCapture(4, 4, 2, 2, GameConstants.RED,
              mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnTrueForLegalBlackPieceCapture() {

      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.RED_KING);

      boolean result =
          CaptureRules.isLegalCapture(4, 4, 2, 2, GameConstants.BLACK,
              mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenDestinationOccupied() {

      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.RED);

      boolean result =
          CaptureRules.isLegalCapture(4, 4, 2, 2, GameConstants.BLACK,
              mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnFalseForNonDiagonalMove() {

      when(mockBoardState.getPiece(4, 2)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.isLegalCapture(2, 4, 2, 2, GameConstants.RED,
              mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnFalseForInvalidPieceColor() {

      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.isLegalCapture(4, 4, 2, 2, 99, mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnTrueForLegalBlackKingCapture() {

      when(mockBoardState.getPiece(5, 5)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.isLegalCapture(5, 5, 2, 2, GameConstants.BLACK_KING,
              mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnTrueForLegalRedKingCapture() {

      when(mockBoardState.getPiece(5, 5)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.BLACK_KING);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.isLegalCapture(5, 5, 2, 2, GameConstants.RED_KING,
              mockBoardState);

      assertTrue(result);
    }
  }

  @Nested
  class CheckAllPiecesPossibleCapturesTests {

    @Test
    void shouldReturnTrueWhenRedPieceCanCapture() {
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.EMPTY);

      for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
          if ((r != 2 || c != 2) && (r != 3 || c != 3) && (r != 4 || c != 4)) {
            when(mockBoardState.getPiece(r, c)).thenReturn(GameConstants.EMPTY);
          }
        }
      }

      boolean result = CaptureRules.checkAllPiecesPossibleCaptures(
          GameConstants.RED, GameConstants.RED_KING, mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenRedKingCanCapture() {

      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.RED_KING);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.EMPTY);

      for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
          if ((r != 2 || c != 2) && (r != 3 || c != 3) && (r != 4 || c != 4)) {
            when(mockBoardState.getPiece(r, c)).thenReturn(GameConstants.EMPTY);
          }
        }
      }

      boolean result = CaptureRules.checkAllPiecesPossibleCaptures(
          GameConstants.RED, GameConstants.RED_KING, mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenNoPiecesCanCapture() {

      when(mockBoardState.getPiece(0, 0)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(7, 7)).thenReturn(GameConstants.BLACK);

      for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
          if ((r != 0 || c != 0) && (r != 7 || c != 7)) {
            when(mockBoardState.getPiece(r, c)).thenReturn(GameConstants.EMPTY);
          }
        }
      }

      boolean result = CaptureRules.checkAllPiecesPossibleCaptures(
          GameConstants.RED, GameConstants.RED_KING, mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenBoardHasNoPlayerPieces() {

      when(mockBoardState.getPiece(anyInt(), anyInt())).thenReturn(
          GameConstants.EMPTY);

      boolean result = CaptureRules.checkAllPiecesPossibleCaptures(
          GameConstants.BLACK, GameConstants.BLACK_KING, mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenBlackKingCanCapture() {

      when(mockBoardState.getPiece(1, 1)).thenReturn(GameConstants.BLACK_KING);
      when(mockBoardState.getPiece(2, 2)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

      for (int r = 0; r < 8; r++) {
        for (int c = 0; c < 8; c++) {
          if ((r != 1 || c != 1) && (r != 2 || c != 2) && (r != 3 || c != 3)) {
            when(mockBoardState.getPiece(r, c)).thenReturn(GameConstants.EMPTY);
          }
        }
      }

      boolean result = CaptureRules.checkAllPiecesPossibleCaptures(
          GameConstants.BLACK, GameConstants.BLACK_KING, mockBoardState);

      assertTrue(result);
    }
  }

  @Nested
  class EdgeCasesTests {

    @Test
    void shouldHandleBoardBoundariesCorrectly() {

      when(mockBoardState.getPiece(0, 0)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(anyInt(), anyInt())).thenReturn(
          GameConstants.EMPTY);

      boolean result = CaptureRules.canCapture(0, 0, mockBoardState);

      assertFalse(result);
    }

    @Test
    void shouldHandleMultipleCapturesScenario() {
      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.BLACK);
      when(mockBoardState.getPiece(4, 4)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(5, 5)).thenReturn(GameConstants.EMPTY);
      when(mockBoardState.getPiece(4, 2)).thenReturn(GameConstants.RED);
      when(mockBoardState.getPiece(5, 1)).thenReturn(GameConstants.EMPTY);

      boolean result = CaptureRules.canCapture(3, 3, mockBoardState);

      assertTrue(result);
    }

    @Test
    void shouldReturnFalseForSingleStepMove() {

      when(mockBoardState.getPiece(3, 3)).thenReturn(GameConstants.EMPTY);

      boolean result =
          CaptureRules.isLegalCapture(3, 3, 2, 2, GameConstants.RED,
              mockBoardState);

      assertFalse(result);
    }
  }
}