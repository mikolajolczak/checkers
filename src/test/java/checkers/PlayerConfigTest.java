package checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class PlayerConfigTest {

  private PlayerConfig playerConfig;

  @BeforeEach
  void setUp() {
    playerConfig = new PlayerConfig();
  }

  @Nested
  class HumanColorTests {

    @Test
    void shouldSetAndGetHumanColor() {

      int expectedColor = 1;

      playerConfig.setHumanColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getHumanColor());
    }

    @Test
    void shouldHandleZeroHumanColor() {

      int expectedColor = 0;

      playerConfig.setHumanColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getHumanColor());
    }

    @Test
    void shouldHandleNegativeHumanColor() {

      int expectedColor = -1;

      playerConfig.setHumanColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getHumanColor());
    }

    @Test
    void shouldHandleLargeHumanColorValues() {

      int expectedColor = Integer.MAX_VALUE;

      playerConfig.setHumanColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getHumanColor());
    }

    @Test
    void shouldReturnDefaultValueBeforeSetting() {

      assertEquals(0, playerConfig.getHumanColor());
    }
  }

  @Nested
  class HumanKingColorTests {

    @Test
    void shouldSetAndGetHumanKingColor() {

      int expectedColor = 2;

      playerConfig.setHumanKingColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getHumanKingColor());
    }

    @Test
    void shouldHandleZeroHumanKingColor() {

      int expectedColor = 0;

      playerConfig.setHumanKingColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getHumanKingColor());
    }

    @Test
    void shouldHandleNegativeHumanKingColor() {

      int expectedColor = -5;

      playerConfig.setHumanKingColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getHumanKingColor());
    }

    @Test
    void shouldHandleLargeHumanKingColorValues() {

      int expectedColor = Integer.MAX_VALUE;

      playerConfig.setHumanKingColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getHumanKingColor());
    }

    @Test
    void shouldReturnDefaultValueBeforeSetting() {

      assertEquals(0, playerConfig.getHumanKingColor());
    }
  }

  @Nested
  class BotColorTests {

    @Test
    void shouldSetAndGetBotColor() {

      int expectedColor = 3;

      playerConfig.setBotColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getBotColor());
    }

    @Test
    void shouldHandleZeroBotColor() {

      int expectedColor = 0;

      playerConfig.setBotColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getBotColor());
    }

    @Test
    void shouldHandleNegativeBotColor() {

      int expectedColor = -10;

      playerConfig.setBotColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getBotColor());
    }

    @Test
    void shouldHandleLargeBotColorValues() {

      int expectedColor = Integer.MAX_VALUE;

      playerConfig.setBotColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getBotColor());
    }

    @Test
    void shouldReturnDefaultValueBeforeSetting() {

      assertEquals(0, playerConfig.getBotColor());
    }
  }

  @Nested
  class BotKingColorTests {

    @Test
    void shouldSetAndGetBotKingColor() {

      int expectedColor = 4;

      playerConfig.setBotKingColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getBotKingColor());
    }

    @Test
    void shouldHandleZeroBotKingColor() {

      int expectedColor = 0;

      playerConfig.setBotKingColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getBotKingColor());
    }

    @Test
    void shouldHandleNegativeBotKingColor() {

      int expectedColor = -15;

      playerConfig.setBotKingColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getBotKingColor());
    }

    @Test
    void shouldHandleLargeBotKingColorValues() {

      int expectedColor = Integer.MAX_VALUE;

      playerConfig.setBotKingColor(expectedColor);

      assertEquals(expectedColor, playerConfig.getBotKingColor());
    }

    @Test
    void shouldReturnDefaultValueBeforeSetting() {

      assertEquals(0, playerConfig.getBotKingColor());
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

      playerConfig.setHumanColor(humanColor);
      playerConfig.setHumanKingColor(humanKingColor);
      playerConfig.setBotColor(botColor);
      playerConfig.setBotKingColor(botKingColor);

      assertEquals(humanColor, playerConfig.getHumanColor());
      assertEquals(humanKingColor, playerConfig.getHumanKingColor());
      assertEquals(botColor, playerConfig.getBotColor());
      assertEquals(botKingColor, playerConfig.getBotKingColor());
    }

    @Test
    void shouldMaintainIndependentColorValues() {

      int sameColor = 5;

      playerConfig.setHumanColor(sameColor);
      playerConfig.setHumanKingColor(sameColor);
      playerConfig.setBotColor(sameColor);
      playerConfig.setBotKingColor(sameColor);

      playerConfig.setHumanColor(10);

      assertEquals(10, playerConfig.getHumanColor());
      assertEquals(sameColor, playerConfig.getHumanKingColor());
      assertEquals(sameColor, playerConfig.getBotColor());
      assertEquals(sameColor, playerConfig.getBotKingColor());
    }

    @Test
    void shouldHandleOverwritingValuesMultipleTimes() {

      int firstColor = 1;
      int secondColor = 2;
      int thirdColor = 3;

      playerConfig.setHumanColor(firstColor);
      playerConfig.setHumanColor(secondColor);
      playerConfig.setHumanColor(thirdColor);

      assertEquals(thirdColor, playerConfig.getHumanColor());
    }
  }

  @Nested
  class EdgeCaseTests {

    @Test
    void shouldHandleMinimumIntegerValue() {

      int minValue = Integer.MIN_VALUE;

      playerConfig.setHumanColor(minValue);
      playerConfig.setHumanKingColor(minValue);
      playerConfig.setBotColor(minValue);
      playerConfig.setBotKingColor(minValue);

      assertEquals(minValue, playerConfig.getHumanColor());
      assertEquals(minValue, playerConfig.getHumanKingColor());
      assertEquals(minValue, playerConfig.getBotColor());
      assertEquals(minValue, playerConfig.getBotKingColor());
    }

    @Test
    void shouldHandleMaximumIntegerValue() {

      int maxValue = Integer.MAX_VALUE;

      playerConfig.setHumanColor(maxValue);
      playerConfig.setHumanKingColor(maxValue);
      playerConfig.setBotColor(maxValue);
      playerConfig.setBotKingColor(maxValue);

      assertEquals(maxValue, playerConfig.getHumanColor());
      assertEquals(maxValue, playerConfig.getHumanKingColor());
      assertEquals(maxValue, playerConfig.getBotColor());
      assertEquals(maxValue, playerConfig.getBotKingColor());
    }
  }
}