package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class PlayerConfigurationTest {

  private PlayerConfiguration playerConfiguration;

  @BeforeEach
  void setUp() {
    playerConfiguration = new PlayerConfiguration();
  }

  @Nested
  class HumanColorTests {

    @Test
    void shouldSetAndGetHumanColor() {

      int expectedColor = 1;

      playerConfiguration.setHumanColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getHumanColor());
    }

    @Test
    void shouldHandleZeroHumanColor() {

      int expectedColor = 0;

      playerConfiguration.setHumanColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getHumanColor());
    }

    @Test
    void shouldHandleNegativeHumanColor() {

      int expectedColor = -1;

      playerConfiguration.setHumanColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getHumanColor());
    }

    @Test
    void shouldHandleLargeHumanColorValues() {

      int expectedColor = Integer.MAX_VALUE;

      playerConfiguration.setHumanColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getHumanColor());
    }

    @Test
    void shouldReturnDefaultValueBeforeSetting() {

      assertEquals(0, playerConfiguration.getHumanColor());
    }
  }

  @Nested
  class HumanKingColorTests {

    @Test
    void shouldSetAndGetHumanKingColor() {

      int expectedColor = 2;

      playerConfiguration.setHumanKingColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getHumanKingColor());
    }

    @Test
    void shouldHandleZeroHumanKingColor() {

      int expectedColor = 0;

      playerConfiguration.setHumanKingColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getHumanKingColor());
    }

    @Test
    void shouldHandleNegativeHumanKingColor() {

      int expectedColor = -5;

      playerConfiguration.setHumanKingColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getHumanKingColor());
    }

    @Test
    void shouldHandleLargeHumanKingColorValues() {

      int expectedColor = Integer.MAX_VALUE;

      playerConfiguration.setHumanKingColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getHumanKingColor());
    }

    @Test
    void shouldReturnDefaultValueBeforeSetting() {

      assertEquals(0, playerConfiguration.getHumanKingColor());
    }
  }

  @Nested
  class BotColorTests {

    @Test
    void shouldSetAndGetBotColor() {

      int expectedColor = 3;

      playerConfiguration.setBotColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getBotColor());
    }

    @Test
    void shouldHandleZeroBotColor() {

      int expectedColor = 0;

      playerConfiguration.setBotColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getBotColor());
    }

    @Test
    void shouldHandleNegativeBotColor() {

      int expectedColor = -10;

      playerConfiguration.setBotColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getBotColor());
    }

    @Test
    void shouldHandleLargeBotColorValues() {

      int expectedColor = Integer.MAX_VALUE;

      playerConfiguration.setBotColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getBotColor());
    }

    @Test
    void shouldReturnDefaultValueBeforeSetting() {

      assertEquals(0, playerConfiguration.getBotColor());
    }
  }

  @Nested
  class BotKingColorTests {

    @Test
    void shouldSetAndGetBotKingColor() {

      int expectedColor = 4;

      playerConfiguration.setBotKingColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getBotKingColor());
    }

    @Test
    void shouldHandleZeroBotKingColor() {

      int expectedColor = 0;

      playerConfiguration.setBotKingColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getBotKingColor());
    }

    @Test
    void shouldHandleNegativeBotKingColor() {

      int expectedColor = -15;

      playerConfiguration.setBotKingColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getBotKingColor());
    }

    @Test
    void shouldHandleLargeBotKingColorValues() {

      int expectedColor = Integer.MAX_VALUE;

      playerConfiguration.setBotKingColor(expectedColor);

      assertEquals(expectedColor, playerConfiguration.getBotKingColor());
    }

    @Test
    void shouldReturnDefaultValueBeforeSetting() {

      assertEquals(0, playerConfiguration.getBotKingColor());
    }
  }

  @Nested
  class IntegrationTests {

    @Test
    void shouldHandleSettingAllColorsSimultaneously() {

      int humanColor = 1;
      int humanKingColor = 2;
      int botColor = 3;
      int botKingColor = 4;

      playerConfiguration.setHumanColor(humanColor);
      playerConfiguration.setHumanKingColor(humanKingColor);
      playerConfiguration.setBotColor(botColor);
      playerConfiguration.setBotKingColor(botKingColor);

      assertEquals(humanColor, playerConfiguration.getHumanColor());
      assertEquals(humanKingColor, playerConfiguration.getHumanKingColor());
      assertEquals(botColor, playerConfiguration.getBotColor());
      assertEquals(botKingColor, playerConfiguration.getBotKingColor());
    }

    @Test
    void shouldMaintainIndependentColorValues() {

      int sameColor = 5;

      playerConfiguration.setHumanColor(sameColor);
      playerConfiguration.setHumanKingColor(sameColor);
      playerConfiguration.setBotColor(sameColor);
      playerConfiguration.setBotKingColor(sameColor);

      playerConfiguration.setHumanColor(10);

      assertEquals(10, playerConfiguration.getHumanColor());
      assertEquals(sameColor, playerConfiguration.getHumanKingColor());
      assertEquals(sameColor, playerConfiguration.getBotColor());
      assertEquals(sameColor, playerConfiguration.getBotKingColor());
    }

    @Test
    void shouldHandleOverwritingValuesMultipleTimes() {

      int firstColor = 1;
      int secondColor = 2;
      int thirdColor = 3;

      playerConfiguration.setHumanColor(firstColor);
      playerConfiguration.setHumanColor(secondColor);
      playerConfiguration.setHumanColor(thirdColor);

      assertEquals(thirdColor, playerConfiguration.getHumanColor());
    }
  }

  @Nested
  class EdgeCaseTests {

    @Test
    void shouldHandleMinimumIntegerValue() {

      int minValue = Integer.MIN_VALUE;

      playerConfiguration.setHumanColor(minValue);
      playerConfiguration.setHumanKingColor(minValue);
      playerConfiguration.setBotColor(minValue);
      playerConfiguration.setBotKingColor(minValue);

      assertEquals(minValue, playerConfiguration.getHumanColor());
      assertEquals(minValue, playerConfiguration.getHumanKingColor());
      assertEquals(minValue, playerConfiguration.getBotColor());
      assertEquals(minValue, playerConfiguration.getBotKingColor());
    }

    @Test
    void shouldHandleMaximumIntegerValue() {

      int maxValue = Integer.MAX_VALUE;

      playerConfiguration.setHumanColor(maxValue);
      playerConfiguration.setHumanKingColor(maxValue);
      playerConfiguration.setBotColor(maxValue);
      playerConfiguration.setBotKingColor(maxValue);

      assertEquals(maxValue, playerConfiguration.getHumanColor());
      assertEquals(maxValue, playerConfiguration.getHumanKingColor());
      assertEquals(maxValue, playerConfiguration.getBotColor());
      assertEquals(maxValue, playerConfiguration.getBotKingColor());
    }
  }
}