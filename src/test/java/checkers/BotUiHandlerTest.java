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

class BotUiHandlerTest {

  final UiController uiController = mock(UiController.class);
  final TurnManager turnManager = mock(TurnManager.class);

  @Nested
  class ConstructorTest {

    @Test
    void shouldCreateInstanceWithGivenValues() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);

      assertNotNull(handler);
      assertEquals(uiController, handler.uiController());
      assertEquals(turnManager, handler.turnManager());
    }

    @Test
    void shouldAcceptNullValues() {
      BotUiHandler handler = new BotUiHandler(null, null);

      assertNull(handler.uiController());
      assertNull(handler.turnManager());
    }

    @Test
    void shouldAcceptNullUIControllerOnly() {
      BotUiHandler handler = new BotUiHandler(null, turnManager);

      assertNull(handler.uiController());
      assertEquals(turnManager, handler.turnManager());
    }

    @Test
    void shouldAcceptNullTurnManagerOnly() {
      BotUiHandler handler = new BotUiHandler(uiController, null);

      assertEquals(uiController, handler.uiController());
      assertNull(handler.turnManager());
    }
  }

  @Nested
  class AccessorMethodsTest {

    @Test
    void uiControllerAccessorShouldReturnCorrectValue() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);
      assertEquals(uiController, handler.uiController());
    }

    @Test
    void turnManagerAccessorShouldReturnCorrectValue() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);
      assertEquals(turnManager, handler.turnManager());
    }

    @Test
    void eachInstanceShouldMaintainItsOwnValues() {
      UiController otherUI = mock(UiController.class);
      TurnManager otherTurnManager = mock(TurnManager.class);

      BotUiHandler handler1 = new BotUiHandler(uiController, turnManager);
      BotUiHandler handler2 = new BotUiHandler(otherUI, otherTurnManager);

      assertNotEquals(handler1, handler2);
      assertEquals(uiController, handler1.uiController());
      assertEquals(otherUI, handler2.uiController());
    }
  }

  @Nested
  class EqualsTest {

    @Test
    void shouldBeEqualToAnotherWithSameValues() {
      BotUiHandler handler1 = new BotUiHandler(uiController, turnManager);
      BotUiHandler handler2 = new BotUiHandler(uiController, turnManager);

      assertEquals(handler1, handler2);
      assertEquals(handler2, handler1);
    }

    @Test
    void shouldNotBeEqualIfDifferentUIController() {
      UiController otherUI = mock(UiController.class);
      BotUiHandler handler1 = new BotUiHandler(uiController, turnManager);
      BotUiHandler handler2 = new BotUiHandler(otherUI, turnManager);

      assertNotEquals(handler1, handler2);
    }

    @Test
    void shouldNotBeEqualIfDifferentTurnManager() {
      TurnManager otherTurnManager = mock(TurnManager.class);
      BotUiHandler handler1 = new BotUiHandler(uiController, turnManager);
      BotUiHandler handler2 = new BotUiHandler(uiController, otherTurnManager);

      assertNotEquals(handler1, handler2);
    }

    @Test
    void shouldNotBeEqualToNull() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);
      assertNotEquals(null, handler);
    }

    @Test
    void shouldBeReflexiveSymmetricAndTransitive() {
      BotUiHandler a = new BotUiHandler(uiController, turnManager);
      BotUiHandler b = new BotUiHandler(uiController, turnManager);
      BotUiHandler c = new BotUiHandler(uiController, turnManager);

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
      BotUiHandler handler1 = new BotUiHandler(uiController, turnManager);
      BotUiHandler handler2 = new BotUiHandler(uiController, turnManager);

      assertEquals(handler1.hashCode(), handler2.hashCode());
    }

    @Test
    void shouldReturnConsistentHashCode() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);

      int h1 = handler.hashCode();
      int h2 = handler.hashCode();

      assertEquals(h1, h2);
    }

    @Test
    void shouldReturnDifferentHashCodeForDifferentObjects() {
      BotUiHandler handler1 = new BotUiHandler(uiController, turnManager);
      BotUiHandler handler2 =
          new BotUiHandler(uiController, mock(TurnManager.class));

      assertNotEquals(handler1.hashCode(), handler2.hashCode());
    }

    @Test
    void shouldHandleNullFieldsInHashCode() {
      BotUiHandler handler1 = new BotUiHandler(null, turnManager);
      BotUiHandler handler2 = new BotUiHandler(null, turnManager);

      assertEquals(handler1.hashCode(), handler2.hashCode());

      BotUiHandler handler3 = new BotUiHandler(uiController, null);
      BotUiHandler handler4 = new BotUiHandler(uiController, null);

      assertEquals(handler3.hashCode(), handler4.hashCode());

      BotUiHandler handler5 = new BotUiHandler(null, null);
      BotUiHandler handler6 = new BotUiHandler(null, null);

      assertEquals(handler5.hashCode(), handler6.hashCode());
    }
  }

  @Nested
  class ToStringTest {

    @Test
    void shouldContainClassName() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);
      assertTrue(handler.toString().contains("BotUIHandler"));
    }

    @Test
    void shouldContainFieldNames() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);
      String text = handler.toString();

      assertTrue(text.contains("uiController"));
      assertTrue(text.contains("turnManager"));
    }

    @Test
    void shouldBeNotNullAndNotEmpty() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);

      String text = handler.toString();
      assertNotNull(text);
      assertFalse(text.isEmpty());
    }

    @Test
    void shouldHandleNullValuesInToString() {
      BotUiHandler handler = new BotUiHandler(null, null);
      String text = handler.toString();

      assertTrue(text.contains("null"));
    }
  }

  @Nested
  class ImmutabilityTest {

    @Test
    void recordShouldBeImmutableMultipleCallsReturnSameValues() {
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);

      for (int i = 0; i < 10; i++) {
        assertEquals(uiController, handler.uiController());
        assertEquals(turnManager, handler.turnManager());
      }
    }

    @Test
    void differentInstancesShouldRemainIndependent() {
      UiController otherUI = mock(UiController.class);
      TurnManager otherTurnManager = mock(TurnManager.class);

      BotUiHandler handler1 = new BotUiHandler(uiController, turnManager);
      BotUiHandler handler2 = new BotUiHandler(otherUI, otherTurnManager);

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
      BotUiHandler handler = new BotUiHandler(uiController, turnManager);

      handler.updateUiAndSwitchTurn();

      verify(uiController).refreshBoard();
      verify(uiController).checkGameEnd();
      verify(turnManager).switchTurn();
    }

    @Test
    void shouldThrowWhenUIControllerIsNull() {
      BotUiHandler handler = new BotUiHandler(null, turnManager);

      assertThrows(NullPointerException.class, handler::updateUiAndSwitchTurn);
    }

    @Test
    void shouldThrowWhenTurnManagerIsNull() {
      BotUiHandler handler = new BotUiHandler(uiController, null);

      assertThrows(NullPointerException.class, handler::updateUiAndSwitchTurn);
    }
  }

  @Nested
  class EdgeCaseTest {

    @Test
    void shouldHandleBothFieldsNull() {
      BotUiHandler handler = new BotUiHandler(null, null);

      assertNull(handler.uiController());
      assertNull(handler.turnManager());
      assertEquals(new BotUiHandler(null, null), handler);
    }

    @Test
    void shouldDifferentiateBetweenNullAndNonNull() {
      BotUiHandler handler1 = new BotUiHandler(uiController, null);
      BotUiHandler handler2 = new BotUiHandler(uiController, turnManager);

      assertNotEquals(handler1, handler2);
    }
  }
}
