import io.xive.wy.arcade.townstars.exceptions.InvalidStorageException;
import io.xive.wy.arcade.townstars.exceptions.NotEnoughCraftsException;
import io.xive.wy.arcade.townstars.exceptions.OutputNotEmptyException;
import io.xive.wy.arcade.townstars.game.Building;
import io.xive.wy.arcade.townstars.game.CraftTune;
import io.xive.wy.arcade.townstars.game.Game;
import io.xive.wy.arcade.townstars.exceptions.BuildingAlreadyCraftingException;
import io.xive.wy.arcade.townstars.exceptions.GameException;
import io.xive.wy.arcade.townstars.exceptions.NotMeetRequirements;
import io.xive.wy.arcade.townstars.exceptions.WrongCraftException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CraftsTest {

  @Test(expected = WrongCraftException.class)
  public void testWrongCraft() throws GameException {
    Game game = new Game();
    game.craft(4, "Wheat");
  }

  @Test(expected = NotMeetRequirements.class)
  public void testNoReqCraft() throws GameException {
    Game game = new Game();
    game.craft(1, "Wheat");
  }

  @Test(expected = BuildingAlreadyCraftingException.class)
  public void testAlreadyCrafting() throws GameException {
    Game game = new Game();
    game.craft(4, "Water");
    game.craft(4, "Water");
  }

  @Test(expected = OutputNotEmptyException.class)
  public void testOutputNotEmpty() throws GameException {
    Game game = new Game();
    game.craft(4, "Water");
    game.skip(Game.BUILDING_CRAFT_PERIOD);
    game.craft(4, "Water");
  }

  @Test(expected = NotEnoughCraftsException.class)
  public void testStoreEmpty() throws GameException {
    Game game = new Game();
    game.store(4, 1, "Water", 1);
  }

  @Test(expected = NotEnoughCraftsException.class)
  public void testStoreSingle() throws GameException {
    Game game = new Game();
    game.craft(4, "Water");
    assertNull(game.getBuilding(4).getCraftOutside());

    game.skip(Game.BUILDING_CRAFT_PERIOD);
    game.tick();

    assertNotNull(game.getBuilding(4).getCraftOutside());
    assertEquals("Water", game.getBuilding(4).getCraftOutside().getName());

    game.store(4, 1, "Water", 2);
  }

  @Test(expected = InvalidStorageException.class)
  public void testInvalidStorage() throws GameException {
    Game game = new Game();
    game.craft(4, "Water");
    assertNull(game.getBuilding(4).getCraftOutside());

    game.skip(Game.BUILDING_CRAFT_PERIOD);
    game.tick();

    assertNotNull(game.getBuilding(4).getCraftOutside());
    assertEquals("Water", game.getBuilding(4).getCraftOutside().getName());

    game.store(4, 5, "Water", 1);
  }

  @Test
  public void testCraft() throws GameException {
    Game game = new Game();
    game.craft(4, "Water");
    assertNull(game.getBuilding(4).getCraftOutside());

    game.skip(Game.BUILDING_CRAFT_PERIOD);
    game.tick();

    assertNotNull(game.getBuilding(4).getCraftOutside());
    assertEquals("Water", game.getBuilding(4).getCraftOutside().getName());

    game.store(4, 1, "Water", 1);
    assertNull(game.getBuilding(4).getCraftOutside());
    assertEquals(1, game.getBuilding(1).getCraftsInside().length);
    assertEquals("Water", game.getBuilding(1).getCraftsInside()[0].getName());

    game.craft(4, "Water");
    game.skip(Game.BUILDING_CRAFT_PERIOD);
    game.store(4, 1, "Water", 1);

    game.craft(4, "Water");
    game.skip(Game.BUILDING_CRAFT_PERIOD);
    game.store(4, 1, "Water", 1);

    assertEquals(3, game.getBuilding(1).getCraftsInside().length);

    game.craft(1, "Wheat");
    assertNull(game.getBuilding(1).getCraftOutside());


  }

  @Test(expected = NotEnoughCraftsException.class)
  public void testConsumeNotEnough() throws GameException {
    Game game = new Game();
    game.craft(4, "Water");
    assertNull(game.getBuilding(4).getCraftOutside());

    game.skip(Game.BUILDING_CRAFT_PERIOD);
    game.tick();

    assertNotNull(game.getBuilding(4).getCraftOutside());
    assertEquals("Water", game.getBuilding(4).getCraftOutside().getName());

    game.consume(4, "Water", 10);
  }

  @Test(expected = NotEnoughCraftsException.class)
  public void testConsumeNotEnough2() throws GameException {
    Game game = new Game();

    game.consume(5, "Wheat", 10);
    game.consume(5, "Wheat", 10);
  }

  @Test
  public void testConsume() throws GameException {
    Game game = new Game();

    for (int i=0; i<5; i++) {
      game.craft(4, "Water");
      assertNull(game.getBuilding(4).getCraftOutside());
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();
      assertNotNull(game.getBuilding(4).getCraftOutside());
      assertEquals("Water", game.getBuilding(4).getCraftOutside().getName());
      game.store(4, 1, "Water", 1);

      game.craft(4, "Water");
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.store(4, 1, "Water", 1);

      game.craft(4, "Water");
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.store(4, 1, "Water", 1);

      game.craft(1, "Wheat");
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();

      game.store(1, 5, "Wheat", 1);

    }

    game.consume(5, "Wheat", 10);
    assertEquals(10, game.getTradeCrafts().length);

    assertEquals(8*5 + 1, game.compileLedger().split("\r\n").length);


  }

  @Test(expected = NotEnoughCraftsException.class)
  public void testTradeNotEnough2() throws GameException {
    Game game = new Game();

    game.consume(5, "Wheat", 5);
    game.consume(6, "Gasoline", 1);
    game.trade(3, "Wheat");
  }

  @Test
  public void testTrade() throws GameException {
    Game game = new Game();

    game.consume(5, "Wheat", 10);
    game.consume(6, "Gasoline", 1);
    game.trade(3, "Wheat");

    assertEquals(Game.START_CURRENCY, game.getCurrency());
    long craftPrice = game.getBuilding(3).getTradeCraft().getCityPrice();
    game.skip(Game.BUILDING_TRADE_PERIOD);
    game.tick();

    long totalLabor = 0;
    Building[] buildings = game.getBuildings();
    for(int i=1; i<buildings.length; i++) {
      if (buildings[i] != null) {
        totalLabor += buildings[i].getLaborValue();
      }
    }

    long spendCurrency = totalLabor * (game.getGameDate() / Game.BUILDING_LABOR_PERIOD);

    assertEquals(Game.START_CURRENCY - spendCurrency + craftPrice * 10, game.getCurrency());
    assertEquals(craftPrice * 10 / 1000, game.getPoints());
    assertNull(game.getBuilding(3).getTradeCraft());
    assertNull(game.getBuilding(3).getTradeStartDate());

    assertEquals(3, game.compileLedger().split("\r\n").length);

  }

}
