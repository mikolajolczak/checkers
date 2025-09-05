package checkers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class MoveExecutorTest {

  @Mock
  private BoardState mockBoard;

  @Mock
  private PlayerConfiguration mockPlayerConfig;

  @Mock
  private BotDecision mockDecision;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void testExecuteNormalMove() {

    int fromRow = 2, fromCol = 1;
    int toRow = 3, toCol = 2;
    int color = GameConstants.RED;

    MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol, color,
        mockBoard);

    verify(mockBoard).setPiece(fromRow, fromCol, GameConstants.EMPTY);
    verify(mockBoard).setPiece(toRow, toCol, color);
    verifyNoMoreInteractions(mockBoard);
  }

  @Test
  void testExecuteNormalMoveWithDifferentColors() {

    int fromRow = 5, fromCol = 4;
    int toRow = 4, toCol = 3;

    MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
        GameConstants.RED, mockBoard);
    verify(mockBoard).setPiece(fromRow, fromCol, GameConstants.EMPTY);
    verify(mockBoard).setPiece(toRow, toCol, GameConstants.RED);

    reset(mockBoard);

    MoveExecutor.executeNormalMove(fromRow, fromCol, toRow, toCol,
        GameConstants.BLACK, mockBoard);
    verify(mockBoard).setPiece(fromRow, fromCol, GameConstants.EMPTY);
    verify(mockBoard).setPiece(toRow, toCol, GameConstants.BLACK);
  }

  @Test
  void testExecuteCapture() {

    int fromRow = 1, fromCol = 0;
    int toRow = 3, toCol = 2;
    int color = GameConstants.RED;
    int expectedCapturedRow = 2;
    int expectedCapturedCol = 1;

    MoveExecutor.executeCapture(fromRow, fromCol, toRow, toCol, color,
        mockBoard);

    verify(mockBoard).setPiece(fromRow, fromCol, GameConstants.EMPTY);
    verify(mockBoard).setPiece(toRow, toCol, color);
    verify(mockBoard).setPiece(expectedCapturedRow, expectedCapturedCol,
        GameConstants.EMPTY);
    verifyNoMoreInteractions(mockBoard);
  }

  @Test
  void testExecuteCaptureDiagonal() {

    MoveExecutor.executeCapture(5, 2, 3, 4, GameConstants.BLACK, mockBoard);
    verify(mockBoard).setPiece(4, 3, GameConstants.EMPTY);

    reset(mockBoard);

    MoveExecutor.executeCapture(2, 5, 4, 3, GameConstants.BLACK, mockBoard);
    verify(mockBoard).setPiece(3, 4, GameConstants.EMPTY);
  }

  @Test
  void testExecuteQueenCapture() {

    int fromRow = 0, fromCol = 1;
    int toRow = 4, toCol = 5;
    int color = GameConstants.RED_KING;

    int rowDir = Integer.signum(toRow - fromRow);
    int colDir = Integer.signum(toCol - fromCol);
    int expectedCapturedRow = toRow - rowDir;
    int expectedCapturedCol = toCol - colDir;

    MoveExecutor.executeQueenCapture(fromRow, fromCol, toRow, toCol, color,
        mockBoard);

    verify(mockBoard).setPiece(fromRow, fromCol, GameConstants.EMPTY);
    verify(mockBoard).setPiece(toRow, toCol, color);
    verify(mockBoard).setPiece(expectedCapturedRow, expectedCapturedCol,
        GameConstants.EMPTY);
    verifyNoMoreInteractions(mockBoard);
  }

  @Test
  void testExecuteQueenCaptureDirections() {
    int color = GameConstants.BLACK_KING;

    MoveExecutor.executeQueenCapture(5, 0, 2, 3, color, mockBoard);
    verify(mockBoard).setPiece(3, 2, GameConstants.EMPTY);

    reset(mockBoard);

    MoveExecutor.executeQueenCapture(1, 6, 4, 3, color, mockBoard);
    verify(mockBoard).setPiece(3, 4, GameConstants.EMPTY);

    reset(mockBoard);

    MoveExecutor.executeQueenCapture(1, 1, 5, 5, color, mockBoard);
    verify(mockBoard).setPiece(4, 4, GameConstants.EMPTY);

    reset(mockBoard);

    MoveExecutor.executeQueenCapture(6, 5, 3, 2, color, mockBoard);
    verify(mockBoard).setPiece(4, 3, GameConstants.EMPTY);
  }

  @Test
  void testApplyMoveToBoardNormalMove() {

    when(mockDecision.moveType()).thenReturn(GameConstants.MOVE);
    when(mockDecision.fromRow()).thenReturn(2);
    when(mockDecision.fromCol()).thenReturn(1);
    when(mockDecision.toRow()).thenReturn(3);
    when(mockDecision.toCol()).thenReturn(2);
    when(mockBoard.getPiece(2, 1)).thenReturn(GameConstants.RED);

    MoveExecutor.applyMoveToBoard(mockDecision, mockBoard, mockPlayerConfig);

    verify(mockBoard).getPiece(2, 1);
    verify(mockBoard).setPiece(2, 1, GameConstants.EMPTY);
    verify(mockBoard).setPiece(3, 2, GameConstants.RED);
  }

  @Test
  void testApplyMoveToBoardCapture() {

    when(mockDecision.moveType()).thenReturn(GameConstants.TAKE);
    when(mockDecision.fromRow()).thenReturn(1);
    when(mockDecision.fromCol()).thenReturn(0);
    when(mockDecision.toRow()).thenReturn(3);
    when(mockDecision.toCol()).thenReturn(2);
    when(mockPlayerConfig.getBotColor()).thenReturn(GameConstants.BLACK);

    MoveExecutor.applyMoveToBoard(mockDecision, mockBoard, mockPlayerConfig);

    verify(mockBoard).setPiece(1, 0, GameConstants.EMPTY);
    verify(mockBoard).setPiece(3, 2, GameConstants.BLACK);
    verify(mockBoard).setPiece(2, 1, GameConstants.EMPTY);
  }

  @Test
  void testApplyMoveToBoardQueenCapture() {

    when(mockDecision.moveType()).thenReturn(GameConstants.QUEEN_TAKE);
    when(mockDecision.fromRow()).thenReturn(0);
    when(mockDecision.fromCol()).thenReturn(1);
    when(mockDecision.toRow()).thenReturn(3);
    when(mockDecision.toCol()).thenReturn(4);
    when(mockPlayerConfig.getBotKingColor()).thenReturn(GameConstants.RED_KING);

    MoveExecutor.applyMoveToBoard(mockDecision, mockBoard, mockPlayerConfig);

    verify(mockBoard).setPiece(0, 1, GameConstants.EMPTY);
    verify(mockBoard).setPiece(3, 4, GameConstants.RED_KING);
    verify(mockBoard).setPiece(2, 3, GameConstants.EMPTY);
  }

  @Test
  void testApplyMoveToBoardUnknownMoveType() {

    when(mockDecision.moveType()).thenReturn(999);

    MoveExecutor.applyMoveToBoard(mockDecision, mockBoard, mockPlayerConfig);

    verifyNoInteractions(mockBoard);
    verifyNoInteractions(mockPlayerConfig);
  }

  @Test
  void testApplyMoveToBoardEdgeCoordinates() {

    when(mockDecision.moveType()).thenReturn(GameConstants.MOVE);
    when(mockDecision.fromRow()).thenReturn(0);
    when(mockDecision.fromCol()).thenReturn(0);
    when(mockDecision.toRow()).thenReturn(7);
    when(mockDecision.toCol()).thenReturn(7);
    when(mockBoard.getPiece(0, 0)).thenReturn(GameConstants.BLACK_KING);

    MoveExecutor.applyMoveToBoard(mockDecision, mockBoard, mockPlayerConfig);

    verify(mockBoard).getPiece(0, 0);
    verify(mockBoard).setPiece(0, 0, GameConstants.EMPTY);
    verify(mockBoard).setPiece(7, 7, GameConstants.BLACK_KING);
  }

  @Test
  void testMultipleConsecutiveMoves() {

    MoveExecutor.executeNormalMove(2, 1, 3, 2, GameConstants.RED, mockBoard);

    MoveExecutor.executeCapture(5, 4, 3, 2, GameConstants.BLACK, mockBoard);

    MoveExecutor.executeQueenCapture(0, 7, 4, 3, GameConstants.RED_KING,
        mockBoard);

    verify(mockBoard, times(5)).setPiece(anyInt(), anyInt(),
        eq(GameConstants.EMPTY));
    verify(mockBoard, times(8)).setPiece(anyInt(), anyInt(), anyInt());
  }

  @Test
  void testExecuteCaptureCalculation() {

    MoveExecutor.executeCapture(1, 0, 3, 2, GameConstants.RED, mockBoard);
    verify(mockBoard).setPiece(2, 1, GameConstants.EMPTY);

    reset(mockBoard);

    MoveExecutor.executeCapture(5, 6, 3, 4, GameConstants.BLACK, mockBoard);
    verify(mockBoard).setPiece(4, 5, GameConstants.EMPTY);
  }
}