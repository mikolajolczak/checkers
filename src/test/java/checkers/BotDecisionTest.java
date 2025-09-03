package checkers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BotDecisionTest {

  @Nested
  class ConstructorTest {

    @Test
    void shouldCreateInstanceWithGivenValues() {

      int fromRow = 2, fromCol = 1, toRow = 3, toCol = 2, moveType = 1;

      BotDecision decision =
          new BotDecision(fromRow, fromCol, toRow, toCol, moveType);

      assertNotNull(decision);
      assertEquals(fromRow, decision.fromRow());
      assertEquals(fromCol, decision.fromCol());
      assertEquals(toRow, decision.toRow());
      assertEquals(toCol, decision.toCol());
      assertEquals(moveType, decision.moveType());
    }

    @Test
    void shouldAcceptZeroValues() {

      BotDecision decision = new BotDecision(0, 0, 0, 0, 0);

      assertEquals(0, decision.fromRow());
      assertEquals(0, decision.fromCol());
      assertEquals(0, decision.toRow());
      assertEquals(0, decision.toCol());
      assertEquals(0, decision.moveType());
    }

    @ParameterizedTest

    @ValueSource(ints = {-1, -5, -10, -100})
    void shouldAcceptNegativeValues(int negativeValue) {

      BotDecision decision = new BotDecision(negativeValue, negativeValue,
          negativeValue, negativeValue, negativeValue);

      assertEquals(negativeValue, decision.fromRow());
      assertEquals(negativeValue, decision.fromCol());
      assertEquals(negativeValue, decision.toRow());
      assertEquals(negativeValue, decision.toCol());
      assertEquals(negativeValue, decision.moveType());
    }

    @Test
    void shouldAcceptVeryLargeValues() {

      int maxValue = Integer.MAX_VALUE;
      int minValue = Integer.MIN_VALUE;

      BotDecision decisionMax =
          new BotDecision(maxValue, maxValue, maxValue, maxValue, maxValue);
      BotDecision decisionMin =
          new BotDecision(minValue, minValue, minValue, minValue, minValue);

      assertEquals(maxValue, decisionMax.fromRow());
      assertEquals(maxValue, decisionMax.fromCol());
      assertEquals(maxValue, decisionMax.toRow());
      assertEquals(maxValue, decisionMax.toCol());
      assertEquals(maxValue, decisionMax.moveType());

      assertEquals(minValue, decisionMin.fromRow());
      assertEquals(minValue, decisionMin.fromCol());
      assertEquals(minValue, decisionMin.toRow());
      assertEquals(minValue, decisionMin.toCol());
      assertEquals(minValue, decisionMin.moveType());
    }
  }

  @Nested
  class AccessorMethodsTest {

    @Test
    void fromRowShouldReturnCorrectValue() {

      BotDecision decision = new BotDecision(5, 3, 4, 2, 1);

      assertEquals(5, decision.fromRow());
    }

    @Test
    void fromColShouldReturnCorrectValue() {

      BotDecision decision = new BotDecision(5, 3, 4, 2, 1);

      assertEquals(3, decision.fromCol());
    }

    @Test
    void toRowShouldReturnCorrectValue() {

      BotDecision decision = new BotDecision(5, 3, 4, 2, 1);

      assertEquals(4, decision.toRow());
    }

    @Test
    void toColShouldReturnCorrectValue() {

      BotDecision decision = new BotDecision(5, 3, 4, 2, 1);

      assertEquals(2, decision.toCol());
    }

    @Test
    void moveTypeShouldReturnCorrectValue() {

      BotDecision decision = new BotDecision(5, 3, 4, 2, 1);

      assertEquals(1, decision.moveType());
    }

    @Test
    void allAccessorsShouldReturnIndependentValues() {

      BotDecision decision = new BotDecision(1, 2, 3, 4, 5);

      assertEquals(1, decision.fromRow());
      assertEquals(2, decision.fromCol());
      assertEquals(3, decision.toRow());
      assertEquals(4, decision.toCol());
      assertEquals(5, decision.moveType());

      assertNotEquals(decision.fromRow(), decision.fromCol());
      assertNotEquals(decision.fromRow(), decision.toRow());
      assertNotEquals(decision.fromRow(), decision.toCol());
      assertNotEquals(decision.fromRow(), decision.moveType());
    }
  }

  @Nested
  class EqualsTest {

    @Test
    void shouldBeEqualToAnotherBotDecisionWithSameValues() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(1, 2, 3, 4, 5);

      assertEquals(decision1, decision2);
      assertEquals(decision2, decision1);
    }

    @Test
    void shouldNotBeEqualToBotDecisionWithDifferentFromRow() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(999, 2, 3, 4, 5);

      assertNotEquals(decision1, decision2);
    }

    @Test
    void shouldNotBeEqualToBotDecisionWithDifferentFromCol() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(1, 999, 3, 4, 5);

      assertNotEquals(decision1, decision2);
    }

    @Test
    void shouldNotBeEqualToBotDecisionWithDifferentToRow() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(1, 2, 999, 4, 5);

      assertNotEquals(decision1, decision2);
    }

    @Test
    void shouldNotBeEqualToBotDecisionWithDifferentToCol() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(1, 2, 3, 999, 5);

      assertNotEquals(decision1, decision2);
    }

    @Test
    void shouldNotBeEqualToBotDecisionWithDifferentMoveType() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(1, 2, 3, 4, 999);

      assertNotEquals(decision1, decision2);
    }

    @Test
    void shouldNotBeEqualToNull() {

      BotDecision decision = new BotDecision(1, 2, 3, 4, 5);

      assertNotEquals(null, decision);
    }
  }

  @Nested
  class HashCodeTest {

    @Test
    void shouldReturnSameHashCodeForEqualObjects() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(1, 2, 3, 4, 5);

      assertEquals(decision1.hashCode(), decision2.hashCode());
    }

    @Test
    void shouldReturnConsistentHashCode() {

      BotDecision decision = new BotDecision(1, 2, 3, 4, 5);

      int hash1 = decision.hashCode();
      int hash2 = decision.hashCode();

      assertEquals(hash1, hash2);
    }

    @Test
    void shouldReturnDifferentHashCodeForDifferentObjects() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(5, 4, 3, 2, 1);

      assertNotEquals(decision1.hashCode(), decision2.hashCode());
    }

    @ParameterizedTest

    @CsvSource({
        "0, 0, 0, 0, 0",
        "2147483647, 2147483647, 2147483647, 2147483647, 2147483647",
        "-2147483648, -2147483648, -2147483648, -2147483648, -2147483648",
        "1, -1, 1, -1, 1"
    })
    void shouldGenerateHashCodeForBoundaryValues(int fromRow, int fromCol,
                                                 int toRow, int toCol,
                                                 int moveType) {

      BotDecision decision =
          new BotDecision(fromRow, fromCol, toRow, toCol, moveType);

      assertDoesNotThrow(decision::hashCode);
    }
  }

  @Nested
  class ToStringTest {

    @Test
    void shouldContainClassName() {

      BotDecision decision = new BotDecision(1, 2, 3, 4, 5);

      String toString = decision.toString();

      assertTrue(toString.contains("BotDecision"));
    }

    @Test
    void shouldContainAllFieldValues() {

      BotDecision decision = new BotDecision(1, 2, 3, 4, 5);

      String toString = decision.toString();

      assertTrue(toString.contains("1"));
      assertTrue(toString.contains("2"));
      assertTrue(toString.contains("3"));
      assertTrue(toString.contains("4"));
      assertTrue(toString.contains("5"));
    }

    @Test
    void shouldContainFieldNames() {

      BotDecision decision = new BotDecision(1, 2, 3, 4, 5);

      String toString = decision.toString();

      assertTrue(toString.contains("fromRow"));
      assertTrue(toString.contains("fromCol"));
      assertTrue(toString.contains("toRow"));
      assertTrue(toString.contains("toCol"));
      assertTrue(toString.contains("moveType"));
    }

    @Test
    void shouldBeNotNullAndNotEmpty() {

      BotDecision decision = new BotDecision(1, 2, 3, 4, 5);

      String toString = decision.toString();

      assertNotNull(toString);
      assertFalse(toString.isEmpty());
    }

    @Test
    void shouldHandleNegativeValuesInToString() {

      BotDecision decision = new BotDecision(-1, -2, -3, -4, -5);

      String toString = decision.toString();

      assertTrue(toString.contains("-1"));
      assertTrue(toString.contains("-2"));
      assertTrue(toString.contains("-3"));
      assertTrue(toString.contains("-4"));
      assertTrue(toString.contains("-5"));
    }

    @Test
    void shouldHandleZeroValuesInToString() {

      BotDecision decision = new BotDecision(0, 0, 0, 0, 0);

      String toString = decision.toString();

      assertTrue(toString.contains("0"));
    }
  }

  @Nested
  class BusinessScenariosTest {

    @Test
    void shouldRepresentRegularMove() {

      BotDecision regularMove = new BotDecision(2, 1, 3, 2, 1);

      assertEquals(2, regularMove.fromRow());
      assertEquals(1, regularMove.fromCol());
      assertEquals(3, regularMove.toRow());
      assertEquals(2, regularMove.toCol());
      assertEquals(1, regularMove.moveType());
    }

    @Test
    void shouldRepresentCaptureMove() {

      BotDecision captureMove = new BotDecision(2, 1, 4, 3, 2);

      assertEquals(2, captureMove.fromRow());
      assertEquals(1, captureMove.fromCol());
      assertEquals(4, captureMove.toRow());
      assertEquals(3, captureMove.toCol());
      assertEquals(2, captureMove.moveType());
    }

    @Test
    void shouldRepresentKingMove() {

      BotDecision kingMove = new BotDecision(7, 0, 0, 7, 3);

      assertEquals(7, kingMove.fromRow());
      assertEquals(0, kingMove.fromCol());
      assertEquals(0, kingMove.toRow());
      assertEquals(7, kingMove.toCol());
      assertEquals(3, kingMove.moveType());
    }

    @Test
    void shouldAllowComparingDifferentMoveTypes() {

      BotDecision regularMove = new BotDecision(2, 1, 3, 2, 1);
      BotDecision captureMove = new BotDecision(2, 1, 4, 3, 2);
      BotDecision sameRegularMove = new BotDecision(2, 1, 3, 2, 1);

      assertNotEquals(regularMove, captureMove);
      assertEquals(regularMove, sameRegularMove);
    }

    @Test
    void shouldHandleMovesOnBoardEdges() {

      BotDecision topLeftCorner = new BotDecision(0, 0, 1, 1, 1);
      BotDecision topRightCorner = new BotDecision(0, 7, 1, 6, 1);
      BotDecision bottomLeftCorner = new BotDecision(7, 0, 6, 1, 1);
      BotDecision bottomRightCorner = new BotDecision(7, 7, 6, 6, 1);

      assertNotNull(topLeftCorner);
      assertNotNull(topRightCorner);
      assertNotNull(bottomLeftCorner);
      assertNotNull(bottomRightCorner);

      assertNotEquals(topLeftCorner, topRightCorner);
      assertNotEquals(topLeftCorner, bottomLeftCorner);
      assertNotEquals(topLeftCorner, bottomRightCorner);
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test
    void shouldHandleMoveToSamePosition() {

      BotDecision samePositionMove = new BotDecision(3, 4, 3, 4, 0);

      assertEquals(3, samePositionMove.fromRow());
      assertEquals(4, samePositionMove.fromCol());
      assertEquals(3, samePositionMove.toRow());
      assertEquals(4, samePositionMove.toCol());
      assertEquals(0, samePositionMove.moveType());
    }

    @Test
    void shouldHandleVeryLongMoves() {

      BotDecision longMove = new BotDecision(0, 0, 1000, 1000, 99);

      assertEquals(0, longMove.fromRow());
      assertEquals(0, longMove.fromCol());
      assertEquals(1000, longMove.toRow());
      assertEquals(1000, longMove.toCol());
      assertEquals(99, longMove.moveType());
    }

    @Test
    void shouldHandleAllZeroFields() {

      BotDecision allZeros = new BotDecision(0, 0, 0, 0, 0);

      assertEquals(0, allZeros.fromRow());
      assertEquals(0, allZeros.fromCol());
      assertEquals(0, allZeros.toRow());
      assertEquals(0, allZeros.toCol());
      assertEquals(0, allZeros.moveType());
    }

    @Test
    void shouldHandleAllMaxValueFields() {

      int maxVal = Integer.MAX_VALUE;
      BotDecision allMaxValues =
          new BotDecision(maxVal, maxVal, maxVal, maxVal, maxVal);

      assertEquals(maxVal, allMaxValues.fromRow());
      assertEquals(maxVal, allMaxValues.fromCol());
      assertEquals(maxVal, allMaxValues.toRow());
      assertEquals(maxVal, allMaxValues.toCol());
      assertEquals(maxVal, allMaxValues.moveType());
    }

    @Test
    void shouldHandleAllMinValueFields() {

      int minVal = Integer.MIN_VALUE;
      BotDecision allMinValues =
          new BotDecision(minVal, minVal, minVal, minVal, minVal);

      assertEquals(minVal, allMinValues.fromRow());
      assertEquals(minVal, allMinValues.fromCol());
      assertEquals(minVal, allMinValues.toRow());
      assertEquals(minVal, allMinValues.toCol());
      assertEquals(minVal, allMinValues.moveType());
    }
  }

  @Nested
  class ImmutabilityTest {

    @Test
    void recordShouldBeImmutableMultipleCallsShouldReturnSameValues() {

      BotDecision decision = new BotDecision(1, 2, 3, 4, 5);

      for (int i = 0; i < 10; i++) {
        assertEquals(1, decision.fromRow());
        assertEquals(2, decision.fromCol());
        assertEquals(3, decision.toRow());
        assertEquals(4, decision.toCol());
        assertEquals(5, decision.moveType());
      }
    }

    @Test
    void eachInstanceShouldMaintainItsValuesIndependently() {

      BotDecision decision1 = new BotDecision(1, 2, 3, 4, 5);
      BotDecision decision2 = new BotDecision(6, 7, 8, 9, 10);

      assertEquals(1, decision1.fromRow());
      assertEquals(6, decision2.fromRow());

      assertEquals(2, decision1.fromCol());
      assertEquals(7, decision2.fromCol());

      assertEquals(3, decision1.toRow());
      assertEquals(8, decision2.toRow());

      assertEquals(4, decision1.toCol());
      assertEquals(9, decision2.toCol());

      assertEquals(5, decision1.moveType());
      assertEquals(10, decision2.moveType());
    }
  }
}