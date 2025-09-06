package checkers;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MoveServiceTest {

  private static final int CURRENT_PLAYER_COLOR = GameConstants.RED;
  private static final int CURRENT_KING_COLOR = GameConstants.RED_KING;
  private static final int OPPONENT_COLOR = GameConstants.BLACK;
  private static final int EMPTY_SQUARE = GameConstants.EMPTY;
  @Mock
  private TurnManager turnManager;
  @Mock
  private BoardState boardState;
  @Mock
  private MoveGenerator moveGenerator;
  private MoveService moveService;
  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
    moveService = new MoveService(turnManager, boardState, moveGenerator);

    when(turnManager.getCurrentColor()).thenReturn(CURRENT_PLAYER_COLOR);
    when(turnManager.getCurrentKingColor()).thenReturn(CURRENT_KING_COLOR);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void canSelectPiece_CurrentPlayerPieceCanMove_ReturnsTrue() {

    int row = 2, col = 3;
    when(boardState.getPiece(row, col)).thenReturn(CURRENT_PLAYER_COLOR);

    try (var moveRulesMock = mockStatic(MoveRules.class);
         var captureRulesMock = mockStatic(CaptureRules.class)) {

      moveRulesMock.when(() -> MoveRules.canMove(col, row, boardState))
          .thenReturn(true);
      captureRulesMock.when(() -> CaptureRules.canCapture(col, row, boardState))
          .thenReturn(false);

      boolean result = moveService.canSelectPiece(row, col, boardState);

      assertTrue(result);
    }
  }

  @Test
  void canSelectPiece_CurrentPlayerKingCanCapture_ReturnsTrue() {

    int row = 1, col = 4;
    when(boardState.getPiece(row, col)).thenReturn(CURRENT_KING_COLOR);

    try (var moveRulesMock = mockStatic(MoveRules.class);
         var captureRulesMock = mockStatic(CaptureRules.class)) {

      moveRulesMock.when(() -> MoveRules.canMove(col, row, boardState))
          .thenReturn(false);
      captureRulesMock.when(() -> CaptureRules.canCapture(col, row, boardState))
          .thenReturn(true);

      boolean result = moveService.canSelectPiece(row, col, boardState);

      assertTrue(result);
    }
  }

  @Test
  void canSelectPiece_OpponentPiece_ReturnsFalse() {

    int row = 5, col = 2;
    when(boardState.getPiece(row, col)).thenReturn(OPPONENT_COLOR);

    try (var moveRulesMock = mockStatic(MoveRules.class);
         var captureRulesMock = mockStatic(CaptureRules.class)) {

      moveRulesMock.when(() -> MoveRules.canMove(col, row, boardState))
          .thenReturn(true);
      captureRulesMock.when(() -> CaptureRules.canCapture(col, row, boardState))
          .thenReturn(true);

      boolean result = moveService.canSelectPiece(row, col, boardState);

      assertFalse(result);
    }
  }

  @Test
  void canSelectPiece_EmptySquare_ReturnsFalse() {

    int row = 3, col = 3;
    when(boardState.getPiece(row, col)).thenReturn(EMPTY_SQUARE);

    try (var moveRulesMock = mockStatic(MoveRules.class);
         var captureRulesMock = mockStatic(CaptureRules.class)) {

      moveRulesMock.when(() -> MoveRules.canMove(col, row, boardState))
          .thenReturn(true);
      captureRulesMock.when(() -> CaptureRules.canCapture(col, row, boardState))
          .thenReturn(true);

      boolean result = moveService.canSelectPiece(row, col, boardState);

      assertFalse(result);
    }
  }

  @Test
  void canSelectPiece_CurrentPlayerPieceCannotMoveOrCapture_ReturnsFalse() {

    int row = 0, col = 1;
    when(boardState.getPiece(row, col)).thenReturn(CURRENT_PLAYER_COLOR);

    try (var moveRulesMock = mockStatic(MoveRules.class);
         var captureRulesMock = mockStatic(CaptureRules.class)) {

      moveRulesMock.when(() -> MoveRules.canMove(col, row, boardState))
          .thenReturn(false);
      captureRulesMock.when(() -> CaptureRules.canCapture(col, row, boardState))
          .thenReturn(false);

      boolean result = moveService.canSelectPiece(row, col, boardState);

      assertFalse(result);
    }
  }

  @Test
  void isLegalMove_LegalMove_ReturnsTrue() {

    int row = 3, col = 4;
    int firstClickRow = 2, firstClickCol = 3;
    int firstClickColor = CURRENT_PLAYER_COLOR;

    try (var moveRulesMock = mockStatic(MoveRules.class)) {
      moveRulesMock.when(() -> MoveRules.isLegalMove(
              col, row, firstClickCol, firstClickRow, firstClickColor,
              boardState))
          .thenReturn(true);

      boolean result = moveService.isLegalMove(row, col, firstClickCol,
          firstClickRow, firstClickColor);

      assertTrue(result);
    }
  }

  @Test
  void isLegalMove_IllegalMove_ReturnsFalse() {

    int row = 5, col = 6;
    int firstClickRow = 1, firstClickCol = 2;
    int firstClickColor = CURRENT_PLAYER_COLOR;

    try (var moveRulesMock = mockStatic(MoveRules.class)) {
      moveRulesMock.when(() -> MoveRules.isLegalMove(
              col, row, firstClickCol, firstClickRow, firstClickColor,
              boardState))
          .thenReturn(false);

      boolean result = moveService.isLegalMove(row, col, firstClickCol,
          firstClickRow, firstClickColor);

      assertFalse(result);
    }
  }

  @Test
  void mustTake_MustTakeAvailable_ReturnsTrue() {

    try (var captureRulesMock = mockStatic(CaptureRules.class)) {
      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              CURRENT_PLAYER_COLOR, CURRENT_KING_COLOR, boardState))
          .thenReturn(true);

      boolean result = moveService.mustTake();

      assertTrue(result);
    }
  }

  @Test
  void mustTake_NoMandatoryCaptures_ReturnsFalse() {

    try (var captureRulesMock = mockStatic(CaptureRules.class)) {
      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              CURRENT_PLAYER_COLOR, CURRENT_KING_COLOR, boardState))
          .thenReturn(false);

      boolean result = moveService.mustTake();

      assertFalse(result);
    }
  }

  @Test
  void getPossibleMoves_ValidBoardState_ReturnsListOfMoves() {

    ArrayList<BotDecision> expectedMoves = new ArrayList<>();
    expectedMoves.add(new BotDecision(1, 2, 3, 4, GameConstants.MOVE));
    expectedMoves.add(new BotDecision(5, 6, 7, 0, GameConstants.MOVE));

    when(moveGenerator.getPossibleMoves(boardState)).thenReturn(expectedMoves);

    ArrayList<BotDecision> result = moveService.getPossibleMoves(boardState);

    assertNotNull(result);
    assertEquals(expectedMoves.size(), result.size());
    assertEquals(expectedMoves, result);
    verify(moveGenerator).getPossibleMoves(boardState);
  }

  @Test
  void getPossibleMoves_NoMovesAvailable_ReturnsEmptyList() {

    ArrayList<BotDecision> emptyMoves = new ArrayList<>();
    when(moveGenerator.getPossibleMoves(boardState)).thenReturn(emptyMoves);

    ArrayList<BotDecision> result = moveService.getPossibleMoves(boardState);

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(moveGenerator).getPossibleMoves(boardState);
  }

  @Test
  void isLegalMove_VerifyParametersPassedCorrectly() {

    int row = 7, col = 6;
    int firstClickRow = 5, firstClickCol = 4;
    int firstClickColor = CURRENT_KING_COLOR;

    try (var moveRulesMock = mockStatic(MoveRules.class)) {
      moveRulesMock.when(() -> MoveRules.isLegalMove(
              col, row, firstClickCol, firstClickRow, firstClickColor,
              boardState))
          .thenReturn(true);

      moveService.isLegalMove(row, col, firstClickCol, firstClickRow,
          firstClickColor);

      moveRulesMock.verify(() -> MoveRules.isLegalMove(
          col, row, firstClickCol, firstClickRow, firstClickColor, boardState));
    }
  }

  @Test
  void mustTake_VerifyParametersPassedCorrectly() {

    try (var captureRulesMock = mockStatic(CaptureRules.class)) {
      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
              CURRENT_PLAYER_COLOR, CURRENT_KING_COLOR, boardState))
          .thenReturn(false);

      moveService.mustTake();

      captureRulesMock.verify(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          CURRENT_PLAYER_COLOR, CURRENT_KING_COLOR, boardState));
    }
  }

  @Test
  void canSelectPiece_EdgeCaseCoordinates_HandledCorrectly() {

    int[][] edgeCases = {{0, 0}, {0, 7}, {7, 0}, {7, 7}};

    for (int[] coords : edgeCases) {
      int row = coords[0], col = coords[1];
      when(boardState.getPiece(row, col)).thenReturn(CURRENT_PLAYER_COLOR);

      try (var moveRulesMock = mockStatic(MoveRules.class);
           var captureRulesMock = mockStatic(CaptureRules.class)) {

        moveRulesMock.when(() -> MoveRules.canMove(col, row, boardState))
            .thenReturn(true);
        captureRulesMock.when(
                () -> CaptureRules.canCapture(col, row, boardState))
            .thenReturn(false);

        boolean result = moveService.canSelectPiece(row, col, boardState);

        assertTrue(result,
            "Should handle edge case at (" + row + ", " + col + ")");
      }
    }
  }

  @Test
  void canSelectPiece_BothRegularAndKingPieces_WorkCorrectly() {

    int row1 = 2, col1 = 1;
    when(boardState.getPiece(row1, col1)).thenReturn(CURRENT_PLAYER_COLOR);

    int row2 = 6, col2 = 5;
    when(boardState.getPiece(row2, col2)).thenReturn(CURRENT_KING_COLOR);

    try (var moveRulesMock = mockStatic(MoveRules.class);
         var captureRulesMock = mockStatic(CaptureRules.class)) {

      moveRulesMock.when(
              () -> MoveRules.canMove(anyInt(), anyInt(), eq(boardState)))
          .thenReturn(true);
      captureRulesMock.when(
              () -> CaptureRules.canCapture(anyInt(), anyInt(), eq(boardState)))
          .thenReturn(false);

      boolean result1 = moveService.canSelectPiece(row1, col1, boardState);
      boolean result2 = moveService.canSelectPiece(row2, col2, boardState);

      assertTrue(result1, "Regular piece should be selectable");
      assertTrue(result2, "King piece should be selectable");
    }
  }
}