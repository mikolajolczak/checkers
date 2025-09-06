package checkers;

import java.awt.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PieceViewTest {

  @Test
  void constructor_ShouldCreateObjectWithCorrectValues() {

    int row = 3;
    int col = 4;
    int type = GameConstants.RED;
    boolean selected = true;

    PieceView piece = new PieceView(row, col, type, selected);

    assertEquals(row, piece.row());
    assertEquals(col, piece.col());
    assertEquals(type, piece.type());
    assertEquals(selected, piece.selected());
  }

  @Test
  void isEmpty_ShouldReturnTrue_WhenTypeIsEmpty() {

    PieceView emptyPiece = new PieceView(0, 0, GameConstants.EMPTY, false);

    assertTrue(emptyPiece.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(
      ints = {GameConstants.RED, GameConstants.BLACK, GameConstants.RED_KING,
          GameConstants.BLACK_KING})
  void isEmpty_ShouldReturnFalse_WhenTypeIsNotEmpty(int pieceType) {

    PieceView piece = new PieceView(0, 0, pieceType, false);

    assertFalse(piece.isEmpty());
  }

  @ParameterizedTest
  @ValueSource(ints = {GameConstants.RED_KING, GameConstants.BLACK_KING})
  void isKing_ShouldReturnTrue_WhenTypeIsKing(int kingType) {

    PieceView king = new PieceView(0, 0, kingType, false);

    assertTrue(king.isKing());
  }

  @ParameterizedTest
  @ValueSource(
      ints = {GameConstants.RED, GameConstants.BLACK, GameConstants.EMPTY})
  void isKing_ShouldReturnFalse_WhenTypeIsNotKing(int nonKingType) {

    PieceView piece = new PieceView(0, 0, nonKingType, false);

    assertFalse(piece.isKing());
  }

  @Test
  void getColor_ShouldReturnRed_WhenTypeIsRedPiece() {

    PieceView redPiece = new PieceView(0, 0, GameConstants.RED, false);

    Color color = redPiece.getColor();

    assertEquals(Color.RED, color);
  }

  @Test
  void getColor_ShouldReturnRed_WhenTypeIsRedKing() {

    PieceView redKing = new PieceView(0, 0, GameConstants.RED_KING, false);

    Color color = redKing.getColor();

    assertEquals(Color.RED, color);
  }

  @Test
  void getColor_ShouldReturnBlack_WhenTypeIsBlackPiece() {

    PieceView blackPiece = new PieceView(0, 0, GameConstants.BLACK, false);

    Color color = blackPiece.getColor();

    assertEquals(Color.BLACK, color);
  }

  @Test
  void getColor_ShouldReturnBlack_WhenTypeIsBlackKing() {

    PieceView blackKing = new PieceView(0, 0, GameConstants.BLACK_KING, false);

    Color color = blackKing.getColor();

    assertEquals(Color.BLACK, color);
  }

  @Test
  void getColor_ShouldReturnNull_WhenTypeIsEmpty() {

    PieceView emptyPiece = new PieceView(0, 0, GameConstants.EMPTY, false);

    Color color = emptyPiece.getColor();

    assertNull(color);
  }

  @ParameterizedTest
  @CsvSource({
      "0, 0, false",
      "7, 7, true",
      "3, 4, false",
      "2, 6, true"
  })
  void accessors_ShouldReturnCorrectValues(int row, int col, boolean selected) {

    PieceView piece = new PieceView(row, col, GameConstants.RED, selected);

    assertEquals(row, piece.row());
    assertEquals(col, piece.col());
    assertEquals(selected, piece.selected());
  }

  @Test
  void equals_ShouldReturnTrue_WhenObjectsHaveSameValues() {

    PieceView piece1 = new PieceView(3, 4, GameConstants.RED_KING, true);
    PieceView piece2 = new PieceView(3, 4, GameConstants.RED_KING, true);

    assertEquals(piece1, piece2);
    assertEquals(piece1.hashCode(), piece2.hashCode());
  }

  @Test
  void equals_ShouldReturnFalse_WhenObjectsHaveDifferentValues() {

    PieceView piece1 = new PieceView(3, 4, GameConstants.RED_KING, true);
    PieceView piece2 = new PieceView(3, 4, GameConstants.BLACK_KING, true);

    assertNotEquals(piece1, piece2);
  }

  @Test
  void toString_ShouldReturnReadableRepresentation() {

    PieceView piece = new PieceView(3, 4, GameConstants.RED_KING, true);

    String result = piece.toString();

    assertNotNull(result);
    assertTrue(result.contains("3"));
    assertTrue(result.contains("4"));
    assertTrue(result.contains("true"));
  }

  @Test
  void record_ShouldBeImmutable() {

    PieceView piece = new PieceView(3, 4, GameConstants.RED, false);

    assertEquals(3, piece.row());
    assertEquals(4, piece.col());
    assertEquals(GameConstants.RED, piece.type());
    assertFalse(piece.selected());
  }


  @Test
  void constructor_ShouldHandleNegativeRowAndCol() {

    int row = -1;
    int col = -5;

    PieceView piece = new PieceView(row, col, GameConstants.BLACK, true);

    assertEquals(row, piece.row());
    assertEquals(col, piece.col());
  }

  @Test
  void constructor_ShouldHandleLargeRowAndCol() {

    int row = Integer.MAX_VALUE;
    int col = Integer.MAX_VALUE;

    PieceView piece = new PieceView(row, col, GameConstants.EMPTY, false);

    assertEquals(row, piece.row());
    assertEquals(col, piece.col());
  }


  @Test
  void getColor_ShouldReturnNull_WhenTypeIsUndefined() {

    int undefinedType = 999;
    PieceView piece = new PieceView(0, 0, undefinedType, false);

    Color color = piece.getColor();

    assertNull(color);
  }
}