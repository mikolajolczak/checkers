package checkers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PositionValidatorTest {


  private static final int BOARD_SIZE = GameConstants.BOARD_SIZE;

  @Nested
  class IsValidPositionTests {

    @Test
    void shouldReturnTrueForValidPositions() {

      assertTrue(PositionValidator.isValidPosition(0, 0),
          "Top-left corner should be valid");
      assertTrue(PositionValidator.isValidPosition(0, BOARD_SIZE - 1),
          "Top-right corner should be valid");
      assertTrue(PositionValidator.isValidPosition(BOARD_SIZE - 1, 0),
          "Bottom-left corner should be valid");
      assertTrue(
          PositionValidator.isValidPosition(BOARD_SIZE - 1, BOARD_SIZE - 1),
          "Bottom-right corner should be valid");
      assertTrue(PositionValidator.isValidPosition(3, 4),
          "Middle position should be valid");
      assertTrue(PositionValidator.isValidPosition(1, 1),
          "Position (1,1) should be valid");
      assertTrue(
          PositionValidator.isValidPosition(BOARD_SIZE / 2, BOARD_SIZE / 2),
          "Center position should be valid");
    }

    @ParameterizedTest

    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void shouldReturnTrueForValidRowsWithColumnZero(int row) {
      assertTrue(PositionValidator.isValidPosition(row, 0));
    }

    @ParameterizedTest

    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7})
    void shouldReturnTrueForValidColumnsWithRowZero(int col) {
      assertTrue(PositionValidator.isValidPosition(0, col));
    }

    @Test
    void shouldReturnFalseForNegativeRows() {
      assertFalse(PositionValidator.isValidPosition(-1, 0),
          "Negative row should be invalid");
      assertFalse(PositionValidator.isValidPosition(-1, 3),
          "Negative row with valid column should be invalid");
      assertFalse(PositionValidator.isValidPosition(-10, 5),
          "Large negative row should be invalid");
    }

    @Test
    void shouldReturnFalseForNegativeColumns() {
      assertFalse(PositionValidator.isValidPosition(0, -1),
          "Negative column should be invalid");
      assertFalse(PositionValidator.isValidPosition(3, -1),
          "Valid row with negative column should be invalid");
      assertFalse(PositionValidator.isValidPosition(5, -10),
          "Valid row with large negative column should be invalid");
    }

    @Test
    void shouldReturnFalseForBothNegativeValues() {
      assertFalse(PositionValidator.isValidPosition(-1, -1),
          "Both negative values should be invalid");
      assertFalse(PositionValidator.isValidPosition(-5, -3),
          "Both large negative values should be invalid");
    }

    @Test
    void shouldReturnFalseForRowsOutOfUpperBound() {
      assertFalse(PositionValidator.isValidPosition(BOARD_SIZE, 0),
          "Row equal to BOARD_SIZE should be invalid");
      assertFalse(PositionValidator.isValidPosition(BOARD_SIZE + 1, 3),
          "Row greater than BOARD_SIZE should be invalid");
      assertFalse(PositionValidator.isValidPosition(100, 5),
          "Large row value should be invalid");
    }

    @Test
    void shouldReturnFalseForColumnsOutOfUpperBound() {
      assertFalse(PositionValidator.isValidPosition(0, BOARD_SIZE),
          "Column equal to BOARD_SIZE should be invalid");
      assertFalse(PositionValidator.isValidPosition(3, BOARD_SIZE + 1),
          "Column greater than BOARD_SIZE should be invalid");
      assertFalse(PositionValidator.isValidPosition(5, 100),
          "Large column value should be invalid");
    }

    @Test
    void shouldReturnFalseForBothValuesOutOfBounds() {
      assertFalse(PositionValidator.isValidPosition(BOARD_SIZE, BOARD_SIZE),
          "Both values equal to BOARD_SIZE should be invalid");
      assertFalse(
          PositionValidator.isValidPosition(BOARD_SIZE + 1, BOARD_SIZE + 1),
          "Both values greater than BOARD_SIZE should be invalid");
      assertFalse(PositionValidator.isValidPosition(100, 50),
          "Both large values should be invalid");
    }
  }

  @Nested
  class IsNotOnSameDiagonalTests {

    @Test
    void shouldReturnFalseForMainDiagonalPositions() {

      assertFalse(PositionValidator.isNotOnSameDiagonal(0, 0, 1, 1),
          "Points (0,0) and (1,1) are on main diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(1, 1, 3, 3),
          "Points (1,1) and (3,3) are on main diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(2, 3, 4, 5),
          "Points (2,3) and (4,5) are on main diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(0, 2, 2, 4),
          "Points (0,2) and (2,4) are on main diagonal");
    }

    @Test
    void shouldReturnFalseForAntiDiagonalPositions() {

      assertFalse(PositionValidator.isNotOnSameDiagonal(0, 3, 3, 0),
          "Points (0,3) and (3,0) are on anti-diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(1, 4, 2, 3),
          "Points (1,4) and (2,3) are on anti-diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(2, 5, 4, 3),
          "Points (2,5) and (4,3) are on anti-diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(0, 7, 7, 0),
          "Points (0,7) and (7,0) are on anti-diagonal");
    }

    @ParameterizedTest

    @CsvSource({
        "0, 0, 2, 2",
        "1, 2, 3, 4",
        "3, 1, 1, 3",
        "2, 6, 4, 4",
        "5, 1, 7, 3",
        "1, 7, 3, 5"
    })
    void shouldReturnFalseForDiagonalPositions(int c1, int r1, int c2, int r2) {
      assertFalse(PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2));
    }

    @Test
    void shouldReturnFalseForSamePosition() {
      assertFalse(PositionValidator.isNotOnSameDiagonal(3, 3, 3, 3),
          "Same position should be considered on diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(0, 0, 0, 0),
          "Same position (0,0) should be considered on diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(5, 2, 5, 2),
          "Same position (5,2) should be considered on diagonal");
    }

    @Test
    void shouldReturnTrueForNonDiagonalPositions() {
      assertTrue(PositionValidator.isNotOnSameDiagonal(0, 0, 1, 0),
          "Horizontal move should not be diagonal");
      assertTrue(PositionValidator.isNotOnSameDiagonal(0, 0, 0, 1),
          "Vertical move should not be diagonal");
      assertTrue(PositionValidator.isNotOnSameDiagonal(1, 2, 3, 1),
          "Non-diagonal positions should return true");
      assertTrue(PositionValidator.isNotOnSameDiagonal(2, 1, 4, 7),
          "Non-diagonal positions should return true");
    }

    @ParameterizedTest

    @CsvSource({
        "0, 0, 1, 0",
        "0, 0, 0, 1",
        "1, 2, 2, 5",
        "3, 1, 5, 7",
        "2, 4, 6, 1",
    })
    void shouldReturnTrueForNonDiagonalPositions(int c1, int r1, int c2,
                                                 int r2) {
      assertTrue(PositionValidator.isNotOnSameDiagonal(c1, r1, c2, r2));
    }

    @Test
    void shouldHandleNegativeCoordinatesCorrectly() {

      assertFalse(PositionValidator.isNotOnSameDiagonal(-1, -1, 1, 1),
          "Negative coords on main diagonal should return false");
      assertFalse(PositionValidator.isNotOnSameDiagonal(-2, 5, -1, 4),
          "Negative coords on anti-diagonal should return false");
      assertTrue(PositionValidator.isNotOnSameDiagonal(-1, 0, 1, 0),
          "Negative coords not on diagonal should return true");
    }

    @Test
    void shouldBeSymmetric() {

      assertEquals(
          PositionValidator.isNotOnSameDiagonal(1, 2, 3, 4),
          PositionValidator.isNotOnSameDiagonal(3, 4, 1, 2),
          "Function should be symmetric"
      );

      assertEquals(
          PositionValidator.isNotOnSameDiagonal(0, 3, 3, 0),
          PositionValidator.isNotOnSameDiagonal(3, 0, 0, 3),
          "Function should be symmetric for anti-diagonal"
      );

      assertEquals(
          PositionValidator.isNotOnSameDiagonal(2, 5, 4, 1),
          PositionValidator.isNotOnSameDiagonal(4, 1, 2, 5),
          "Function should be symmetric for non-diagonal positions"
      );
    }
  }

  @Nested
  class EdgeCasesTests {

    @Test
    void shouldHandleExtremeValues() {

      assertFalse(PositionValidator.isValidPosition(Integer.MAX_VALUE, 0));
      assertFalse(PositionValidator.isValidPosition(0, Integer.MAX_VALUE));
      assertFalse(PositionValidator.isValidPosition(Integer.MIN_VALUE, 0));
      assertFalse(PositionValidator.isValidPosition(0, Integer.MIN_VALUE));
    }

    @Test
    void shouldHandleZeroValuesInDiagonalCheck() {

      assertFalse(PositionValidator.isNotOnSameDiagonal(0, 0, 0, 0),
          "Same zero position");
      assertTrue(PositionValidator.isNotOnSameDiagonal(0, 0, 1, 0),
          "Zero to non-diagonal");
      assertTrue(PositionValidator.isNotOnSameDiagonal(0, 0, 0, 1),
          "Zero to non-diagonal");
      assertFalse(PositionValidator.isNotOnSameDiagonal(0, 0, 1, 1),
          "Zero to diagonal");
    }
  }
}