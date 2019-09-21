import io.xive.wy.arcade.townstars.Game;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameTest {

  @Test
  public void testStartItems() {
    Game game = new Game();
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

}
