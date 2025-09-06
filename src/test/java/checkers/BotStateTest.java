package checkers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class BotStateTest {

  final BoardState boardMock = mock(BoardState.class);
  final PlayerConfig configMock = mock(PlayerConfig.class);

  @Nested
  class ConstructorTest {

    @Test
    void shouldCreateInstanceWithGivenValues() {
      BotState state = new BotState(boardMock, configMock);

      assertNotNull(state);
      assertEquals(boardMock, state.board());
      assertEquals(configMock, state.playerConfig());
    }

    @Test
    void shouldAcceptNullValues() {
      BotState state = new BotState(null, null);

      assertNull(state.board());
      assertNull(state.playerConfig());
    }

    @Test
    void shouldAcceptNullBoard() {
      BotState state = new BotState(null, configMock);

      assertNull(state.board());
      assertEquals(configMock, state.playerConfig());
    }

    @Test
    void shouldAcceptNullConfig() {
      BotState state = new BotState(boardMock, null);

      assertEquals(boardMock, state.board());
      assertNull(state.playerConfig());
    }
  }

  @Nested
  class AccessorMethodsTest {

    @Test
    void boardAccessorShouldReturnCorrectValue() {
      BotState state = new BotState(boardMock, configMock);
      assertEquals(boardMock, state.board());
    }

    @Test
    void playerConfigAccessorShouldReturnCorrectValue() {
      BotState state = new BotState(boardMock, configMock);
      assertEquals(configMock, state.playerConfig());
    }

    @Test
    void eachInstanceShouldMaintainItsOwnValues() {
      BoardState otherBoard = mock(BoardState.class);
      PlayerConfig otherConfig = mock(PlayerConfig.class);

      BotState state1 = new BotState(boardMock, configMock);
      BotState state2 = new BotState(otherBoard, otherConfig);

      assertNotEquals(state1, state2);
      assertEquals(boardMock, state1.board());
      assertEquals(otherBoard, state2.board());
    }
  }

  @Nested
  class EqualsTest {

    @Test
    void shouldBeEqualToAnotherWithSameValues() {
      BotState state1 = new BotState(boardMock, configMock);
      BotState state2 = new BotState(boardMock, configMock);

      assertEquals(state1, state2);
      assertEquals(state2, state1);
    }

    @Test
    void shouldNotBeEqualIfDifferentBoard() {
      BoardState otherBoard = mock(BoardState.class);
      BotState state1 = new BotState(boardMock, configMock);
      BotState state2 = new BotState(otherBoard, configMock);

      assertNotEquals(state1, state2);
    }

    @Test
    void shouldNotBeEqualIfDifferentConfig() {
      PlayerConfig otherConfig = mock(PlayerConfig.class);
      BotState state1 = new BotState(boardMock, configMock);
      BotState state2 = new BotState(boardMock, otherConfig);

      assertNotEquals(state1, state2);
    }

    @Test
    void shouldNotBeEqualToNull() {
      BotState state = new BotState(boardMock, configMock);
      assertNotEquals(null, state);
    }

    @Test
    void shouldBeReflexiveSymmetricAndTransitive() {
      BotState a = new BotState(boardMock, configMock);
      BotState b = new BotState(boardMock, configMock);
      BotState c = new BotState(boardMock, configMock);

      assertEquals(a, b);
      assertEquals(b, a);
      assertEquals(a, b);
      assertEquals(b, c);
      assertEquals(a, c);
    }
  }

  @Nested
  class HashCodeTest {

    @Test
    void shouldReturnSameHashCodeForEqualObjects() {
      BotState state1 = new BotState(boardMock, configMock);
      BotState state2 = new BotState(boardMock, configMock);

      assertEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    void shouldReturnConsistentHashCode() {
      BotState state = new BotState(boardMock, configMock);

      int h1 = state.hashCode();
      int h2 = state.hashCode();

      assertEquals(h1, h2);
    }

    @Test
    void shouldReturnDifferentHashCodeForDifferentObjects() {
      PlayerConfig otherConfig = mock(PlayerConfig.class);
      BotState state1 = new BotState(boardMock, configMock);
      BotState state2 = new BotState(boardMock, otherConfig);

      assertNotEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    void shouldHandleNullFieldsInHashCode() {
      BotState state1 = new BotState(null, configMock);
      BotState state2 = new BotState(null, configMock);

      assertEquals(state1.hashCode(), state2.hashCode());

      BotState state3 = new BotState(boardMock, null);
      BotState state4 = new BotState(boardMock, null);

      assertEquals(state3.hashCode(), state4.hashCode());

      BotState state5 = new BotState(null, null);
      BotState state6 = new BotState(null, null);

      assertEquals(state5.hashCode(), state6.hashCode());
    }
  }

  @Nested
  class ToStringTest {

    @Test
    void shouldContainClassName() {
      BotState state = new BotState(boardMock, configMock);
      assertTrue(state.toString().contains("BotState"));
    }

    @Test
    void shouldContainFieldNames() {
      BotState state = new BotState(boardMock, configMock);
      String text = state.toString();

      assertTrue(text.contains("board"));
      assertTrue(text.contains("playerConfig"));
    }

    @Test
    void shouldBeNotNullAndNotEmpty() {
      BotState state = new BotState(boardMock, configMock);

      String text = state.toString();
      assertNotNull(text);
      assertFalse(text.isEmpty());
    }

    @Test
    void shouldHandleNullValuesInToString() {
      BotState state = new BotState(null, null);
      String text = state.toString();

      assertTrue(text.contains("null"));
    }
  }

  @Nested
  class ImmutabilityTest {

    @Test
    void recordShouldBeImmutableMultipleCallsReturnSameValues() {
      BotState state = new BotState(boardMock, configMock);

      for (int i = 0; i < 10; i++) {
        assertEquals(boardMock, state.board());
        assertEquals(configMock, state.playerConfig());
      }
    }

    @Test
    void differentInstancesShouldRemainIndependent() {
      BoardState otherBoard = mock(BoardState.class);
      PlayerConfig otherConfig = mock(PlayerConfig.class);

      BotState state1 = new BotState(boardMock, configMock);
      BotState state2 = new BotState(otherBoard, otherConfig);

      assertEquals(boardMock, state1.board());
      assertEquals(configMock, state1.playerConfig());

      assertEquals(otherBoard, state2.board());
      assertEquals(otherConfig, state2.playerConfig());
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test
    void shouldHandleBothFieldsNull() {
      BotState state = new BotState(null, null);

      assertNull(state.board());
      assertNull(state.playerConfig());
      assertEquals(new BotState(null, null), state);
    }

    @Test
    void shouldHandleBoardNullOnly() {
      BotState state = new BotState(null, configMock);

      assertNull(state.board());
      assertEquals(configMock, state.playerConfig());
    }

    @Test
    void shouldHandleConfigNullOnly() {
      BotState state = new BotState(boardMock, null);

      assertEquals(boardMock, state.board());
      assertNull(state.playerConfig());
    }

    @Test
    void shouldDifferentiateBetweenNullAndNonNull() {
      BotState state1 = new BotState(boardMock, null);
      BotState state2 = new BotState(boardMock, configMock);

      assertNotEquals(state1, state2);
    }
  }
}
