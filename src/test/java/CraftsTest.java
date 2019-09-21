import io.xive.wy.arcade.townstars.game.Game;
import io.xive.wy.arcade.townstars.exceptions.BuildingAlreadyCraftingException;
import io.xive.wy.arcade.townstars.exceptions.GameException;
import io.xive.wy.arcade.townstars.exceptions.NotMeetRequirements;
import io.xive.wy.arcade.townstars.exceptions.WrongCraftException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

  @Test
  public void testCraft() throws GameException {
    Game game = new Game();
    game.craft(4, "Water");
    assertNull(game.getBuilding(4).getCraftOutside());

    game.skip(Game.BUILDING_CRAFT_PERIOD);
    game.tick();

    assertEquals("Water", game.getBuilding(4).getCraftOutside());

  }



}
