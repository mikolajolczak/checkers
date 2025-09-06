package checkers;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveGeneratorTest {

  private static final int BOT_COLOR = GameConstants.BLACK;
  private static final int BOT_KING_COLOR = GameConstants.BLACK_KING;
  private static final int OPPONENT_COLOR = GameConstants.RED;
  private static final int EMPTY_CELL = GameConstants.EMPTY;
  @Mock
  private PlayerConfig playerConfig;
  @Mock
  private BoardState boardState;
  private MoveGenerator moveGenerator;

  @BeforeEach
  void setUp() {
    when(playerConfig.getBotColor()).thenReturn(BOT_COLOR);
    when(playerConfig.getBotKingColor()).thenReturn(BOT_KING_COLOR);
    moveGenerator = new MoveGenerator(playerConfig);
  }

  @Test
  void shouldReturnEmptyListWhenNoBotPieces() {

    mockBoardWithNoBotPieces();

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class)) {
      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(false);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      assertTrue(result.isEmpty());
    }
  }

  @Test
  void shouldGenerateCaptureMovesWhenMustTake() {

    mockBoardWithBotPiece(BOT_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<CaptureGenerator> captureGeneratorMock = mockStatic(
             CaptureGenerator.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(
             PieceRules.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(true);
      captureRulesMock.when(() -> CaptureRules.canCapture(0, 0, boardState))
          .thenReturn(true);
      pieceRulesMock.when(() -> PieceRules.isKing(BOT_COLOR)).thenReturn(false);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      captureGeneratorMock.verify(() -> CaptureGenerator.findRegularCaptures(
          eq(0), eq(0), eq(BOT_COLOR), ArgumentMatchers.any(), eq(boardState)));
      assertNotNull(result);
    }
  }

  @Test
  void shouldGenerateKingCaptureMovesWhenPieceIsKing() {

    mockBoardWithBotPiece(BOT_KING_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<CaptureGenerator> captureGeneratorMock = mockStatic(
             CaptureGenerator.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(
             PieceRules.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(true);
      captureRulesMock.when(() -> CaptureRules.canCapture(0, 0, boardState))
          .thenReturn(true);
      pieceRulesMock.when(() -> PieceRules.isKing(BOT_KING_COLOR))
          .thenReturn(true);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      captureGeneratorMock.verify(() -> CaptureGenerator.findKingCaptures(
          eq(0), eq(0), eq(BOT_KING_COLOR), ArgumentMatchers.any(),
          eq(boardState)));
      assertNotNull(result);
    }
  }

  @Test
  void shouldGenerateRegularMovesWhenMustTakeIsFalse() {

    mockBoardWithBotPiece(BOT_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<MoveRules> moveRulesMock = mockStatic(MoveRules.class);
         MockedStatic<RegularMoveGenerator> regularMoveGeneratorMock =
             mockStatic(
                 RegularMoveGenerator.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(
             PieceRules.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(false);
      moveRulesMock.when(() -> MoveRules.canMove(0, 0, boardState))
          .thenReturn(true);
      pieceRulesMock.when(() -> PieceRules.isKing(BOT_COLOR)).thenReturn(false);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      regularMoveGeneratorMock.verify(
          () -> RegularMoveGenerator.findRegularPieceMoves(
              eq(0), eq(0), eq(BOT_COLOR), ArgumentMatchers.any(),
              eq(boardState)));
      assertNotNull(result);
    }
  }

  @Test
  void shouldGenerateKingRegularMovesWhenPieceIsKing() {

    mockBoardWithBotPiece(BOT_KING_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<MoveRules> moveRulesMock = mockStatic(MoveRules.class);
         MockedStatic<KingMoveGenerator> kingMoveGeneratorMock = mockStatic(
             KingMoveGenerator.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(
             PieceRules.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(false);
      moveRulesMock.when(() -> MoveRules.canMove(0, 0, boardState))
          .thenReturn(true);
      pieceRulesMock.when(() -> PieceRules.isKing(BOT_KING_COLOR))
          .thenReturn(true);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      kingMoveGeneratorMock.verify(() -> KingMoveGenerator.findKingMoves(
          eq(0), eq(0), eq(BOT_KING_COLOR), ArgumentMatchers.any(),
          eq(boardState)));
      assertNotNull(result);
    }
  }

  @Test
  void shouldSkipOpponentPieces() {

    mockBoardWithOpponentPiece();

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<MoveRules> moveRulesMock = mockStatic(MoveRules.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(false);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      moveRulesMock.verifyNoInteractions();
      assertTrue(result.isEmpty());
    }
  }

  @Test
  void shouldSkipEmptyCells() {

    mockBoardWithEmptyCell();

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<MoveRules> moveRulesMock = mockStatic(MoveRules.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(false);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      moveRulesMock.verifyNoInteractions();
      assertTrue(result.isEmpty());
    }
  }

  @Test
  void shouldSkipBotPiecesThatCannotCaptureWhenMustTake() {

    mockBoardWithBotPiece(BOT_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<CaptureGenerator> captureGeneratorMock = mockStatic(
             CaptureGenerator.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(true);
      captureRulesMock.when(() -> CaptureRules.canCapture(0, 0, boardState))
          .thenReturn(false);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      captureGeneratorMock.verifyNoInteractions();
      assertTrue(result.isEmpty());
    }
  }

  @Test
  void shouldSkipBotPiecesThatCannotMoveWhenNoCaptureRequired() {

    mockBoardWithBotPiece(BOT_COLOR);

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<MoveRules> moveRulesMock = mockStatic(MoveRules.class);
         MockedStatic<RegularMoveGenerator> regularMoveGeneratorMock =
             mockStatic(
                 RegularMoveGenerator.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(false);
      moveRulesMock.when(() -> MoveRules.canMove(0, 0, boardState))
          .thenReturn(false);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      regularMoveGeneratorMock.verifyNoInteractions();
      assertTrue(result.isEmpty());
    }
  }

  @Test
  void shouldHandleMultipleBotPiecesOnBoard() {

    mockBoardWithMultipleBotPieces();

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class);
         MockedStatic<MoveRules> moveRulesMock = mockStatic(MoveRules.class);
         MockedStatic<RegularMoveGenerator> regularMoveGeneratorMock =
             mockStatic(
                 RegularMoveGenerator.class);
         MockedStatic<PieceRules> pieceRulesMock = mockStatic(
             PieceRules.class)) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(false);
      moveRulesMock.when(
              () -> MoveRules.canMove(anyInt(), anyInt(), eq(boardState)))
          .thenReturn(true);
      pieceRulesMock.when(() -> PieceRules.isKing(anyInt())).thenReturn(false);

      ArrayList<BotDecision> result =
          moveGenerator.getPossibleMoves(boardState);

      regularMoveGeneratorMock.verify(
          () -> RegularMoveGenerator.findRegularPieceMoves(
              eq(0), eq(0), eq(BOT_COLOR), ArgumentMatchers.any(),
              eq(boardState)));
      regularMoveGeneratorMock.verify(
          () -> RegularMoveGenerator.findRegularPieceMoves(
              eq(2), eq(2), eq(BOT_COLOR), ArgumentMatchers.any(),
              eq(boardState)));
      assertNotNull(result);
    }
  }

  @Test
  void shouldVerifyBoardSizeIteration() {

    mockBoardWithNoBotPieces();

    try (MockedStatic<CaptureRules> captureRulesMock = mockStatic(
        CaptureRules.class)
    ) {

      captureRulesMock.when(() -> CaptureRules.checkAllPiecesPossibleCaptures(
          BOT_COLOR, BOT_KING_COLOR, boardState)).thenReturn(false);

      moveGenerator.getPossibleMoves(boardState);

      verify(boardState, times(64)).getPiece(anyInt(), anyInt());
    }
  }


  private void mockBoardWithNoBotPieces() {

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        when(boardState.getPiece(row, col)).thenReturn(EMPTY_CELL);
      }
    }

  }

  private void mockBoardWithBotPiece(int pieceType) {

    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (r == 0 && c == 0) {
          when(boardState.getPiece(r, c)).thenReturn(pieceType);
        } else {
          when(boardState.getPiece(r, c)).thenReturn(EMPTY_CELL);
        }
      }
    }

  }

  private void mockBoardWithOpponentPiece() {

    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        if (r == 0 && c == 0) {
          when(boardState.getPiece(r, c)).thenReturn(
              MoveGeneratorTest.OPPONENT_COLOR);
        } else {
          when(boardState.getPiece(r, c)).thenReturn(EMPTY_CELL);
        }
      }

    }
  }

  private void mockBoardWithEmptyCell() {

    for (int r = 0; r < 8; r++) {
      for (int c = 0; c < 8; c++) {
        when(boardState.getPiece(r, c)).thenReturn(EMPTY_CELL);
      }
    }

  }

  private void mockBoardWithMultipleBotPieces() {

    for (int row = 0; row < 8; row++) {
      for (int col = 0; col < 8; col++) {
        if ((row == 0 && col == 0) || (row == 2 && col == 2)) {
          when(boardState.getPiece(row, col)).thenReturn(BOT_COLOR);
        } else {
          when(boardState.getPiece(row, col)).thenReturn(EMPTY_CELL);
        }
      }

    }
  }
}