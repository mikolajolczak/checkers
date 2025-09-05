package checkers;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameEndEvaluatorTest {

  private static final int BOARD_SIZE = GameConstants.BOARD_SIZE;
  @Mock
  private BoardState mockBoardState;
  @Mock
  private JFrame mockFrame;
  private GameEndEvaluator gameEndEvaluator;

  @BeforeEach
  void setUp() {
    gameEndEvaluator = new GameEndEvaluator(mockBoardState, mockFrame);
  }

  @Test
  void testEvaluateGameEnd_RedWins_WhenNoBlackPieces() {

    setupBoardWithOnlyRedPieces();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verify(() ->
          JOptionPane.showMessageDialog(eq(mockFrame), eq("Red won!")));
      verify(mockFrame).dispose();
    }
  }

  @Test
  void testEvaluateGameEnd_BlackWins_WhenNoRedPieces() {

    setupBoardWithOnlyBlackPieces();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)
    ) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verify(() ->
          JOptionPane.showMessageDialog(eq(mockFrame), eq("Black won!")));
      verify(mockFrame).dispose();
    }
  }

  @Test
  void testEvaluateGameEnd_NoGameEnd_WhenBothColorsPresent() {

    setupBoardWithBothColors();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)
    ) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verifyNoInteractions();
      verify(mockFrame, never()).dispose();
    }
  }

  @Test
  void testEvaluateGameEnd_EmptyBoard_NoWinner() {

    setupEmptyBoard();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)
    ) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verify(() ->
              JOptionPane.showMessageDialog(eq(mockFrame), any(String.class)),
          atLeastOnce());
      verify(mockFrame, atLeastOnce()).dispose();
    }
  }

  @Test
  void testEvaluateGameEnd_OnlyRedKings() {

    setupBoardWithOnlyRedKings();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)
    ) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verify(() ->
          JOptionPane.showMessageDialog(eq(mockFrame), eq("Red won!")));
      verify(mockFrame).dispose();
    }
  }

  @Test
  void testEvaluateGameEnd_OnlyBlackKings() {

    setupBoardWithOnlyBlackKings();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)
    ) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verify(() ->
          JOptionPane.showMessageDialog(eq(mockFrame), eq("Black won!")));
      verify(mockFrame).dispose();
    }
  }

  @Test
  void testEvaluateGameEnd_MixedPiecesAndKings() {

    setupBoardWithMixedPiecesAndKings();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)
    ) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verifyNoInteractions();
      verify(mockFrame, never()).dispose();
    }
  }

  @Test
  void testEvaluateGameEnd_SingleRedPiece() {

    setupBoardWithSingleRedPiece();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)
    ) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verify(() ->
          JOptionPane.showMessageDialog(eq(mockFrame), eq("Red won!")));
      verify(mockFrame).dispose();
    }
  }

  @Test
  void testEvaluateGameEnd_SingleBlackPiece() {

    setupBoardWithSingleBlackPiece();

    try (MockedStatic<JOptionPane> optionPaneMock = mockStatic(
        JOptionPane.class)
    ) {

      gameEndEvaluator.evaluateGameEnd();

      optionPaneMock.verify(() ->
          JOptionPane.showMessageDialog(eq(mockFrame), eq("Black won!")));
      verify(mockFrame).dispose();
    }
  }


  private void setupBoardWithOnlyRedPieces() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row < 2) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.RED);
        } else {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
        }
      }
    }
  }

  private void setupBoardWithOnlyBlackPieces() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row < 2) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.BLACK);
        } else {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
        }
      }
    }
  }

  private void setupBoardWithBothColors() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row < 2) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.RED);
        } else if (row >= 6) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.BLACK);
        } else {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
        }
      }
    }
  }

  private void setupEmptyBoard() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
      }
    }
  }

  private void setupBoardWithOnlyRedKings() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row < 2) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.RED_KING);
        } else {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
        }
      }
    }
  }

  private void setupBoardWithOnlyBlackKings() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row < 2) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.BLACK_KING);
        } else {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
        }
      }
    }
  }

  private void setupBoardWithMixedPiecesAndKings() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row == 0) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.RED);
        } else if (row == 1) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.RED_KING);
        } else if (row == 6) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.BLACK);
        } else if (row == 7) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.BLACK_KING);
        } else {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
        }
      }
    }
  }

  private void setupBoardWithSingleRedPiece() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row == 0 && col == 0) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.RED);
        } else {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
        }
      }
    }
  }

  private void setupBoardWithSingleBlackPiece() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int col = 0; col < BOARD_SIZE; col++) {
        if (row == 0 && col == 0) {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.BLACK);
        } else {
          when(mockBoardState.getPiece(row, col)).thenReturn(GameConstants.EMPTY);
        }
      }
    }
  }
}