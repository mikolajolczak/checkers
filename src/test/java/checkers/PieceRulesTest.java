package checkers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PieceRulesTest {


  private static final int RED = GameConstants.RED;
  private static final int BLACK = GameConstants.BLACK;
  private static final int RED_KING = GameConstants.RED_KING;
  private static final int BLACK_KING = GameConstants.BLACK_KING;
  private static final int EMPTY = GameConstants.EMPTY;

  @Test
  void testIsRed_WhenPieceIsRed_ShouldReturnTrue() {
    assertTrue(PieceRules.isRed(RED));
  }

  @Test
  void testIsRed_WhenPieceIsNotRed_ShouldReturnFalse() {
    assertFalse(PieceRules.isRed(BLACK));
    assertFalse(PieceRules.isRed(RED_KING));
    assertFalse(PieceRules.isRed(BLACK_KING));
    assertFalse(PieceRules.isRed(EMPTY));
  }

  @Test
  void testIsBlack_WhenPieceIsBlack_ShouldReturnTrue() {
    assertTrue(PieceRules.isBlack(BLACK));
  }

  @Test
  void testIsBlack_WhenPieceIsNotBlack_ShouldReturnFalse() {
    assertFalse(PieceRules.isBlack(RED));
    assertFalse(PieceRules.isBlack(RED_KING));
    assertFalse(PieceRules.isBlack(BLACK_KING));
    assertFalse(PieceRules.isBlack(EMPTY));
  }

  @Test
  void testIsRedKing_WhenPieceIsRedKing_ShouldReturnTrue() {
    assertTrue(PieceRules.isRedKing(RED_KING));
  }

  @Test
  void testIsRedKing_WhenPieceIsNotRedKing_ShouldReturnFalse() {
    assertFalse(PieceRules.isRedKing(RED));
    assertFalse(PieceRules.isRedKing(BLACK));
    assertFalse(PieceRules.isRedKing(BLACK_KING));
    assertFalse(PieceRules.isRedKing(EMPTY));
  }

  @Test
  void testIsBlackKing_WhenPieceIsBlackKing_ShouldReturnTrue() {
    assertTrue(PieceRules.isBlackKing(BLACK_KING));
  }

  @Test
  void testIsBlackKing_WhenPieceIsNotBlackKing_ShouldReturnFalse() {
    assertFalse(PieceRules.isBlackKing(RED));
    assertFalse(PieceRules.isBlackKing(BLACK));
    assertFalse(PieceRules.isBlackKing(RED_KING));
    assertFalse(PieceRules.isBlackKing(EMPTY));
  }

  @Test
  void testIsKing_WhenPieceIsKing_ShouldReturnTrue() {
    assertTrue(PieceRules.isKing(RED_KING));
    assertTrue(PieceRules.isKing(BLACK_KING));
  }

  @Test
  void testIsKing_WhenPieceIsNotKing_ShouldReturnFalse() {
    assertFalse(PieceRules.isKing(RED));
    assertFalse(PieceRules.isKing(BLACK));
    assertFalse(PieceRules.isKing(EMPTY));
  }

  @Test
  void testIsEmpty_WhenPieceIsEmpty_ShouldReturnTrue() {
    assertTrue(PieceRules.isEmpty(EMPTY));
  }

  @Test
  void testIsEmpty_WhenPieceIsNotEmpty_ShouldReturnFalse() {
    assertFalse(PieceRules.isEmpty(RED));
    assertFalse(PieceRules.isEmpty(BLACK));
    assertFalse(PieceRules.isEmpty(RED_KING));
    assertFalse(PieceRules.isEmpty(BLACK_KING));
  }

  @Test
  void testEdgeCases_InvalidValues() {

    assertFalse(PieceRules.isRed(-1));
    assertFalse(PieceRules.isBlack(-1));
    assertFalse(PieceRules.isRedKing(-1));
    assertFalse(PieceRules.isBlackKing(-1));
    assertFalse(PieceRules.isKing(-1));
    assertFalse(PieceRules.isEmpty(-1));

    int invalidValue = 999;
    assertFalse(PieceRules.isRed(invalidValue));
    assertFalse(PieceRules.isBlack(invalidValue));
    assertFalse(PieceRules.isRedKing(invalidValue));
    assertFalse(PieceRules.isBlackKing(invalidValue));
    assertFalse(PieceRules.isKing(invalidValue));
    assertFalse(PieceRules.isEmpty(invalidValue));
  }

  @Test
  void testMutualExclusion() {

    assertTrue(PieceRules.isRed(RED));
    assertFalse(PieceRules.isBlack(RED));
    assertFalse(PieceRules.isKing(RED));
    assertFalse(PieceRules.isEmpty(RED));

    assertTrue(PieceRules.isBlack(BLACK));
    assertFalse(PieceRules.isRed(BLACK));
    assertFalse(PieceRules.isKing(BLACK));
    assertFalse(PieceRules.isEmpty(BLACK));

    assertTrue(PieceRules.isRedKing(RED_KING));
    assertTrue(PieceRules.isKing(RED_KING));
    assertFalse(PieceRules.isRed(RED_KING));
    assertFalse(PieceRules.isBlack(RED_KING));
    assertFalse(PieceRules.isBlackKing(RED_KING));
    assertFalse(PieceRules.isEmpty(RED_KING));

    assertTrue(PieceRules.isBlackKing(BLACK_KING));
    assertTrue(PieceRules.isKing(BLACK_KING));
    assertFalse(PieceRules.isRed(BLACK_KING));
    assertFalse(PieceRules.isBlack(BLACK_KING));
    assertFalse(PieceRules.isRedKing(BLACK_KING));
    assertFalse(PieceRules.isEmpty(BLACK_KING));

    assertTrue(PieceRules.isEmpty(EMPTY));
    assertFalse(PieceRules.isRed(EMPTY));
    assertFalse(PieceRules.isBlack(EMPTY));
    assertFalse(PieceRules.isRedKing(EMPTY));
    assertFalse(PieceRules.isBlackKing(EMPTY));
    assertFalse(PieceRules.isKing(EMPTY));
  }

  @Test
  void testCompleteness() {

    int[] allPieces = {RED, BLACK, RED_KING, BLACK_KING, EMPTY};

    for (int piece : allPieces) {
      boolean recognizedByAnyMethod =
          PieceRules.isRed(piece) ||
              PieceRules.isBlack(piece) ||
              PieceRules.isRedKing(piece) ||
              PieceRules.isBlackKing(piece) ||
              PieceRules.isEmpty(piece);

      assertTrue(recognizedByAnyMethod,
          "Piece value " + piece
              + " should be recognized by at least one method");
    }
  }
}