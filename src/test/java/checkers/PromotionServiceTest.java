package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

  @Mock
  private BoardState boardState;

  private PromotionService promotionService;

  @BeforeEach
  void setUp() {
    promotionService = new PromotionService(boardState);
  }

  @Test
  void shouldPromoteRedPieceToRedKingWhenReachingRow0() {

    int row = 0;
    int col = 3;
    int color = GameConstants.RED;

    promotionService.promoteIfNeeded(row, col, color);

    verify(boardState).setPiece(row, col, GameConstants.RED_KING);
  }

  @Test
  void shouldPromoteBlackPieceToBlackKingWhenReachingLastRow() {

    int row = GameConstants.LAST_ROW_INDEX;
    int col = 2;
    int color = GameConstants.BLACK;

    promotionService.promoteIfNeeded(row, col, color);

    verify(boardState).setPiece(row, col, GameConstants.BLACK_KING);
  }

  @Test
  void shouldNotPromoteRedPieceWhenNotAtRow0() {

    int row = 1;
    int col = 3;
    int color = GameConstants.RED;

    promotionService.promoteIfNeeded(row, col, color);

    verify(boardState, never()).setPiece(anyInt(), anyInt(), anyInt());
  }

  @Test
  void shouldNotPromoteRedPieceWhenAtMiddleRow() {

    int row = 4;
    int col = 3;
    int color = GameConstants.RED;

    promotionService.promoteIfNeeded(row, col, color);

    verify(boardState, never()).setPiece(anyInt(), anyInt(), anyInt());
  }

  @Test
  void shouldNotPromoteRedPieceWhenAtLastRow() {

    int row = GameConstants.LAST_ROW_INDEX;
    int col = 3;
    int color = GameConstants.RED;

    promotionService.promoteIfNeeded(row, col, color);

    verify(boardState, never()).setPiece(anyInt(), anyInt(), anyInt());
  }

  @Test
  void shouldNotPromoteBlackPieceWhenNotAtLastRow() {

    int row = GameConstants.LAST_ROW_INDEX - 1;
    int col = 2;
    int color = GameConstants.BLACK;

    promotionService.promoteIfNeeded(row, col, color);

    verify(boardState, never()).setPiece(anyInt(), anyInt(), anyInt());
  }

  @Test
  void shouldNotPromoteBlackPieceWhenAtRow0() {

    int row = 0;
    int col = 2;
    int color = GameConstants.BLACK;

    promotionService.promoteIfNeeded(row, col, color);

    verify(boardState, never()).setPiece(anyInt(), anyInt(), anyInt());
  }

  @Test
  void shouldNotPromoteBlackPieceWhenAtMiddleRow() {

    int row = 3;
    int col = 2;
    int color = GameConstants.BLACK;

    promotionService.promoteIfNeeded(row, col, color);

    verify(boardState, never()).setPiece(anyInt(), anyInt(), anyInt());
  }

  @Test
  void shouldHandleEdgeCaseWithDifferentColumnPositionsForRedPromotion() {

    int row = 0;
    int[] columns = {0, 1, 5, 7};
    int color = GameConstants.RED;

    for (int col : columns) {
      reset(boardState);
      promotionService.promoteIfNeeded(row, col, color);
      verify(boardState).setPiece(row, col, GameConstants.RED_KING);
    }
  }

  @Test
  void shouldHandleEdgeCaseWithDifferentColumnPositionsForBlackPromotion() {

    int row = GameConstants.LAST_ROW_INDEX;
    int[] columns = {0, 2, 4, 6};
    int color = GameConstants.BLACK;

    for (int col : columns) {
      reset(boardState);
      promotionService.promoteIfNeeded(row, col, color);
      verify(boardState).setPiece(row, col, GameConstants.BLACK_KING);
    }
  }

  @Test
  void shouldNotPromoteWhenColorIsInvalid() {

    int row = 0;
    int col = 3;
    int invalidColor = -1;

    promotionService.promoteIfNeeded(row, col, invalidColor);

    verify(boardState, never()).setPiece(anyInt(), anyInt(), anyInt());
  }

  @Test
  void shouldVerifyBoardStateConstructorParameter() {

    BoardState testBoardState = mock(BoardState.class);

    PromotionService service = new PromotionService(testBoardState);

    service.promoteIfNeeded(0, 0, GameConstants.RED);
    verify(testBoardState).setPiece(0, 0, GameConstants.RED_KING);
  }
}