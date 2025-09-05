package checkers;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class BotUIHandlerTest {

  final UIController uiController = mock(UIController.class);
  final TurnManager turnManager = mock(TurnManager.class);

  @Nested
  class ConstructorTest {

    @Test
    void shouldCreateInstanceWithGivenValues() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);

      assertNotNull(handler);
      assertEquals(uiController, handler.uiController());
      assertEquals(turnManager, handler.turnManager());
    }

    @Test
    void shouldAcceptNullValues() {
      BotUIHandler handler = new BotUIHandler(null, null);

      assertNull(handler.uiController());
      assertNull(handler.turnManager());
    }

    @Test
    void shouldAcceptNullUIControllerOnly() {
      BotUIHandler handler = new BotUIHandler(null, turnManager);

      assertNull(handler.uiController());
      assertEquals(turnManager, handler.turnManager());
    }

    @Test
    void shouldAcceptNullTurnManagerOnly() {
      BotUIHandler handler = new BotUIHandler(uiController, null);

      assertEquals(uiController, handler.uiController());
      assertNull(handler.turnManager());
    }
  }

  @Nested
  class AccessorMethodsTest {

    @Test
    void uiControllerAccessorShouldReturnCorrectValue() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);
      assertEquals(uiController, handler.uiController());
    }

    @Test
    void turnManagerAccessorShouldReturnCorrectValue() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);
      assertEquals(turnManager, handler.turnManager());
    }

    @Test
    void eachInstanceShouldMaintainItsOwnValues() {
      UIController otherUI = mock(UIController.class);
      TurnManager otherTurnManager = mock(TurnManager.class);

      BotUIHandler handler1 = new BotUIHandler(uiController, turnManager);
      BotUIHandler handler2 = new BotUIHandler(otherUI, otherTurnManager);

      assertNotEquals(handler1, handler2);
      assertEquals(uiController, handler1.uiController());
      assertEquals(otherUI, handler2.uiController());
    }
  }

  @Nested
  class EqualsTest {

    @Test
    void shouldBeEqualToAnotherWithSameValues() {
      BotUIHandler handler1 = new BotUIHandler(uiController, turnManager);
      BotUIHandler handler2 = new BotUIHandler(uiController, turnManager);

      assertEquals(handler1, handler2);
      assertEquals(handler2, handler1);
    }

    @Test
    void shouldNotBeEqualIfDifferentUIController() {
      UIController otherUI = mock(UIController.class);
      BotUIHandler handler1 = new BotUIHandler(uiController, turnManager);
      BotUIHandler handler2 = new BotUIHandler(otherUI, turnManager);

      assertNotEquals(handler1, handler2);
    }

    @Test
    void shouldNotBeEqualIfDifferentTurnManager() {
      TurnManager otherTurnManager = mock(TurnManager.class);
      BotUIHandler handler1 = new BotUIHandler(uiController, turnManager);
      BotUIHandler handler2 = new BotUIHandler(uiController, otherTurnManager);

      assertNotEquals(handler1, handler2);
    }

    @Test
    void shouldNotBeEqualToNull() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);
      assertNotEquals(null, handler);
    }

    @Test
    void shouldBeReflexiveSymmetricAndTransitive() {
      BotUIHandler a = new BotUIHandler(uiController, turnManager);
      BotUIHandler b = new BotUIHandler(uiController, turnManager);
      BotUIHandler c = new BotUIHandler(uiController, turnManager);

      assertEquals(a, b);
      assertEquals(b, a);
      assertEquals(b, c);
      assertEquals(a, c);
    }
  }

  @Nested
  class HashCodeTest {

    @Test
    void shouldReturnSameHashCodeForEqualObjects() {
      BotUIHandler handler1 = new BotUIHandler(uiController, turnManager);
      BotUIHandler handler2 = new BotUIHandler(uiController, turnManager);

      assertEquals(handler1.hashCode(), handler2.hashCode());
    }

    @Test
    void shouldReturnConsistentHashCode() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);

      int h1 = handler.hashCode();
      int h2 = handler.hashCode();

      assertEquals(h1, h2);
    }

    @Test
    void shouldReturnDifferentHashCodeForDifferentObjects() {
      BotUIHandler handler1 = new BotUIHandler(uiController, turnManager);
      BotUIHandler handler2 =
          new BotUIHandler(uiController, mock(TurnManager.class));

      assertNotEquals(handler1.hashCode(), handler2.hashCode());
    }

    @Test
    void shouldHandleNullFieldsInHashCode() {
      BotUIHandler handler1 = new BotUIHandler(null, turnManager);
      BotUIHandler handler2 = new BotUIHandler(null, turnManager);

      assertEquals(handler1.hashCode(), handler2.hashCode());

      BotUIHandler handler3 = new BotUIHandler(uiController, null);
      BotUIHandler handler4 = new BotUIHandler(uiController, null);

      assertEquals(handler3.hashCode(), handler4.hashCode());

      BotUIHandler handler5 = new BotUIHandler(null, null);
      BotUIHandler handler6 = new BotUIHandler(null, null);

      assertEquals(handler5.hashCode(), handler6.hashCode());
    }
  }

  @Nested
  class ToStringTest {

    @Test
    void shouldContainClassName() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);
      assertTrue(handler.toString().contains("BotUIHandler"));
    }

    @Test
    void shouldContainFieldNames() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);
      String text = handler.toString();

      assertTrue(text.contains("uiController"));
      assertTrue(text.contains("turnManager"));
    }

    @Test
    void shouldBeNotNullAndNotEmpty() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);

      String text = handler.toString();
      assertNotNull(text);
      assertFalse(text.isEmpty());
    }

    @Test
    void shouldHandleNullValuesInToString() {
      BotUIHandler handler = new BotUIHandler(null, null);
      String text = handler.toString();

      assertTrue(text.contains("null"));
    }
  }

  @Nested
  class ImmutabilityTest {

    @Test
    void recordShouldBeImmutableMultipleCallsReturnSameValues() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);

      for (int i = 0; i < 10; i++) {
        assertEquals(uiController, handler.uiController());
        assertEquals(turnManager, handler.turnManager());
      }
    }

    @Test
    void differentInstancesShouldRemainIndependent() {
      UIController otherUI = mock(UIController.class);
      TurnManager otherTurnManager = mock(TurnManager.class);

      BotUIHandler handler1 = new BotUIHandler(uiController, turnManager);
      BotUIHandler handler2 = new BotUIHandler(otherUI, otherTurnManager);

      assertEquals(uiController, handler1.uiController());
      assertEquals(turnManager, handler1.turnManager());

      assertEquals(otherUI, handler2.uiController());
      assertEquals(otherTurnManager, handler2.turnManager());
    }
  }

  @Nested
  class BusinessMethodTest {

    @Test
    void shouldUpdateUIAndSwitchTurn() {
      BotUIHandler handler = new BotUIHandler(uiController, turnManager);

      handler.updateUIAndSwitchTurn();

      verify(uiController).refreshBoard();
      verify(uiController).checkGameEnd();
      verify(turnManager).switchTurn();
    }

    @Test
    void shouldThrowWhenUIControllerIsNull() {
      BotUIHandler handler = new BotUIHandler(null, turnManager);

      assertThrows(NullPointerException.class, handler::updateUIAndSwitchTurn);
    }

    @Test
    void shouldThrowWhenTurnManagerIsNull() {
      BotUIHandler handler = new BotUIHandler(uiController, null);

      assertThrows(NullPointerException.class, handler::updateUIAndSwitchTurn);
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test
    void shouldHandleBothFieldsNull() {
      BotUIHandler handler = new BotUIHandler(null, null);

      assertNull(handler.uiController());
      assertNull(handler.turnManager());
      assertEquals(new BotUIHandler(null, null), handler);
    }

    @Test
    void shouldDifferentiateBetweenNullAndNonNull() {
      BotUIHandler handler1 = new BotUIHandler(uiController, null);
      BotUIHandler handler2 = new BotUIHandler(uiController, turnManager);

      assertNotEquals(handler1, handler2);
    }
  }
}
