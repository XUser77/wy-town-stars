package io.xive.wy.arcade.townstars.ui;

import io.xive.wy.arcade.townstars.exceptions.GameException;
import io.xive.wy.arcade.townstars.game.Game;
import java.util.ArrayList;
import java.util.List;

public class BestExecution {

  private Game game;
  private List<Integer> wells = new ArrayList<>();
  private List<Integer> wheatFields = new ArrayList<>();
  private List<Integer> tradeDepos = new ArrayList<>();
  private List<Integer> troughs = new ArrayList<>();


  public void execute(Game game) throws GameException {
    this.game = game;
    // 25000

    wells.add(4);
    tradeDepos.add(3);

    for(int i=0; i<14; i++) {
      wells.add(game.build("Well"));
    }

    wheatFields.add(1);
    wheatFields.add(2);

    for(int i=0; i<wells.size() / 3 - 2; i++) {
      wheatFields.add(game.build("Wheat Field"));
    }

    for(int k=0; k<20; k++) {
      generateWater();
      generateAndSellWheat();
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();
    }

    // 20 minutes

    for(int i=0; i<15; i++) {
      wells.add(game.build("Well"));
    }

    for(int i=0; i<5; i++) {
      wheatFields.add(game.build("Wheat Field"));
    }

    tradeDepos.add(game.build("Trade Depot"));

    for(int k=0; k<20; k++) {
      generateWater();
      generateAndSellWheat();
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();
    }

    // 40 minutes

    for(int i=0; i<2; i++) {
      troughs.add(game.build("Trough"));
    }

    for(int k=0; k<20; k++) {
      generateWater();
      generateAndSellFeeds();
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();
    }

    // 1 hour
    for(int i=0; i<30; i++) {
      wells.add(game.build("Well"));
    }

    for(int i=0; i<10; i++) {
      wheatFields.add(game.build("Wheat Field"));
    }

    for(int i=0; i<2; i++) {
      troughs.add(game.build("Trough"));
    }

    tradeDepos.add(game.build("Trade Depot"));

    for(int k=0; k<20; k++) {
      generateWater();
      generateAndSellFeeds();
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();
    }

  }

  private void generateWater() throws GameException {
    for(int i=0; i<wells.size(); i++) {
      if (game.getBuilding(wells.get(i)).getCraftOutside() != null) {
        game.store(wells.get(i), wheatFields.get(i / 3), "Water", 1);
      }

      game.craft(wells.get(i), "Water");
    }

  }

  private void generateAndSellWheat() throws GameException {
    for(int i=0; i<wheatFields.size(); i++) {
      if (game.getBuilding(wheatFields.get(i)).getCraftOutside() != null) {
        game.consume(wheatFields.get(i), "Wheat", 1);
      }

      if (game.getBuilding(wheatFields.get(i)).getCraftsInside().length >= 3) {
        game.craft(wheatFields.get(i), "Wheat");
      }
    }

    checkTrade("Wheat");
  }

  private void checkTrade(String craftName) throws GameException {
    if (game.getTradeCrafts().length >= 10) {
      for(int i=0; i<tradeDepos.size(); i++) {
        if (game.getBuilding(tradeDepos.get(i)).getTradeStartDate() == null) {
          game.consume(6, "Gasoline", 1);
          game.trade(tradeDepos.get(i), craftName);
          break;
        }
      }

    }
  }

  private void generateAndSellFeeds() throws GameException {
    for(int i=0; i<wheatFields.size(); i++) {
      if (game.getBuilding(wheatFields.get(i)).getCraftOutside() != null) {
        game.store(wheatFields.get(i), troughs.get(i / 5), "Wheat", 1);
      }
      if (game.getBuilding(wheatFields.get(i)).getCraftsInside().length >= 3) {
        game.craft(wheatFields.get(i), "Wheat");
      }
    }

    for(int i = 0; i< troughs.size(); i++) {
      if (game.getBuilding(troughs.get(i)).getCraftOutside() != null) {
        game.consume(troughs.get(i), "Feed", 1);
      }
      if (game.getBuilding(troughs.get(i)).getCraftsInside().length >= 5) {
        game.craft(troughs.get(i), "Feed");
      }
    }

    checkTrade("Feed");
  }


}
