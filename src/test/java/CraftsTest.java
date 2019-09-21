import io.xive.wy.arcade.townstars.exceptions.NotEnoughCraftsException;
import io.xive.wy.arcade.townstars.exceptions.OutputNotEmptyException;
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



}
