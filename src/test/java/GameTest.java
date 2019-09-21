import io.xive.wy.arcade.townstars.Game;
import io.xive.wy.arcade.townstars.exceptions.GameException;
import io.xive.wy.arcade.townstars.exceptions.NotEnoughtCurrencyException;
import io.xive.wy.arcade.townstars.objects.BuildingTune;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameTest {

  @Test
  public void testStartItems() {
    Game game = new Game();

    assertEquals(25000, game.getCurrency());
    assertEquals(0, game.getPoints());

    assertNotNull(game.getBuildings());
    assertNotNull(game.getBuildings()[1]);
    assertEquals("Wheat Field", game.getBuildings()[1].getName());
    assertNotNull(game.getBuildings()[2]);
    assertEquals("Wheat Field", game.getBuildings()[2].getName());
    assertNotNull(game.getBuildings()[3]);
    assertEquals("Trade Depot", game.getBuildings()[3].getName());
    assertNotNull(game.getBuildings()[4]);
    assertEquals("Well", game.getBuildings()[4].getName());
    assertNotNull(game.getBuildings()[5]);
    assertEquals("Silo", game.getBuildings()[5].getName());
    assertNotNull(game.getBuildings()[6]);
    assertEquals("Fuel Storage", game.getBuildings()[6].getName());
  }

  @Test
  public void testBuyAndSellCurrency() throws GameException {
    Game game = new Game();
    assertEquals(Game.START_CURRENCY, game.getCurrency());

    BuildingTune buildingTune = null;
    BuildingTune[] buildingTunes = game.getAllBuildingTunes();
    for(int i=0; i<buildingTunes.length; i++) {
      if (buildingTunes[i].getBuildCost() < game.getCurrency()) {
        buildingTune = buildingTunes[i];
        break;
      }
    }

    assertNotNull(buildingTune);
    int index = game.build(buildingTune.getName());

    assertEquals(Game.START_CURRENCY - buildingTune.getBuildCost(), game.getCurrency());

    game.sell(index);
    assertEquals(Game.START_CURRENCY - buildingTune.getBuildCost() + buildingTune.getSellValue(), game.getCurrency());

  }

  @Test(expected = NotEnoughtCurrencyException.class)
  public void testNotEnoughMoney() throws GameException {
    Game game = new Game();
    assertEquals(Game.START_CURRENCY, game.getCurrency());

    BuildingTune buildingTune = null;
    BuildingTune[] buildingTunes = game.getAllBuildingTunes();
    for(int i=0; i<buildingTunes.length; i++) {
      if (buildingTunes[i].getBuildCost() > game.getCurrency()) {
        buildingTune = buildingTunes[i];
        break;
      }
    }

    assertNotNull(buildingTune);

    game.build(buildingTune.getName());
  }

}
