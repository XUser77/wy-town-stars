package io.xive.wy.arcade.townstars.ui;

import io.xive.wy.arcade.townstars.exceptions.GameException;
import io.xive.wy.arcade.townstars.game.Game;
import java.util.ArrayList;
import java.util.List;

public class BestExecution {

  private Game game;
  private List<Integer> wells = new ArrayList<>();
  private List<Integer> wheatFields = new ArrayList<>();
  private List<Integer> tradeDepots = new ArrayList<>();
  private List<Integer> troughs = new ArrayList<>();

  private List<Integer> oilPumps = new ArrayList<>();
  private List<Integer> petroliumRefineres = new ArrayList<>();
  private List<Integer> gasolineRefineres = new ArrayList<>();
  private List<Integer> refWells = new ArrayList<>();
  private List<Integer> windTurbines = new ArrayList<>();


  public void execute(Game game) throws GameException {
    this.game = game;
    // 25000

    wells.add(4);
    tradeDepots.add(3);

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

    tradeDepots.add(game.build("Trade Depot"));

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

    tradeDepots.add(game.build("Trade Depot"));

    for(int k=0; k<20; k++) {
      generateWater();
      generateAndSellFeeds();
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();
    }
    // 1 hour 20 minutes

    for(int i=0; i<2; i++) {
      oilPumps.add(game.build("Oil Pump"));
    }

    for(int i=0; i<5; i++) {
      refWells.add(game.build("Well"));
    }

    for(int i=0; i<3; i++) {
      windTurbines.add(game.build("Wind Turbine"));
    }

    for(int i=0; i<1; i++) {
      petroliumRefineres.add(game.build("Refinery"));
    }

    for(int i=0; i<1; i++) {
      gasolineRefineres.add(game.build("Refinery"));
    }

    for(int k=0; k<22; k++) {
      generateWater();
      generateAndSellFeeds();
      produceGasoline();
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();
    }

    for(int i=0; i<60; i++) {
      wells.add(game.build("Well"));
    }

    for(int i=0; i<20; i++) {
      wheatFields.add(game.build("Wheat Field"));
    }

    for(int i=0; i<4; i++) {
      troughs.add(game.build("Trough"));
    }

    for(int k=0; k<190; k++) {
      generateWater();
      generateAndSellFeeds();
      produceGasoline();
      game.skip(Game.BUILDING_CRAFT_PERIOD);
      game.tick();
    }

  }

  private void produceGasoline() throws GameException {
    for(int i=0; i<windTurbines.size(); i++) {
      if (game.getBuilding(windTurbines.get(i)).getCraftOutside() != null) {
        if (i < 2) {
          game.store(windTurbines.get(i), petroliumRefineres.get(0), "Energy", 1);
        } else {
          game.store(windTurbines.get(i), gasolineRefineres.get(0), "Energy", 1);
        }
      }

      game.craft(windTurbines.get(i), "Energy");

    }

    for(int i=0; i<oilPumps.size(); i++) {
      if (game.getBuilding(oilPumps.get(i)).getCraftOutside() != null) {
        game.store(oilPumps.get(i), petroliumRefineres.get(i /2), "Crude Oil", 1);
      }
      game.craft(oilPumps.get(i), "Crude Oil");
    }

    for(int i=0; i<refWells.size(); i++) {
      if (game.getBuilding(refWells.get(i)).getCraftOutside() != null) {
        if (i == 0) {
          game.store(refWells.get(i), petroliumRefineres.get(0), "Water", 1);
        } else {
          game.store(refWells.get(i), gasolineRefineres.get(0), "Water", 1);
        }
      }
      game.craft(refWells.get(i), "Water");
    }

    for(int i=0; i<petroliumRefineres.size(); i++) {
      if (game.getBuilding(petroliumRefineres.get(i)).getCraftOutside() != null) {
        game.store(petroliumRefineres.get(i), gasolineRefineres.get(0), "Petroleum", 1);
      }
      if (game.getBuilding(petroliumRefineres.get(i)).getCraftsInside().length >= 4) {
        game.craft(petroliumRefineres.get(i), "Petroleum");
      }
    }

    for(int i=0; i<gasolineRefineres.size(); i++) {
      if (game.getBuilding(gasolineRefineres.get(i)).getCraftOutside() != null) {
        if (game.getBuilding(6).getStoredCrafts().length >= game.getBuilding(6).getStorageCapacity()) {
          game.store(gasolineRefineres.get(i), 0, "Gasoline", 1);
        } else {
          game.store(gasolineRefineres.get(i), 6, "Gasoline", 1);
        }

      }

      if (game.getBuilding(gasolineRefineres.get(i)).getCraftsInside().length >= 6) {
        game.craft(gasolineRefineres.get(i), "Gasoline");
      }
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
      for(int i = 0; i< tradeDepots.size(); i++) {
        if (game.getBuilding(tradeDepots.get(i)).getTradeStartDate() == null) {
          game.consume(6, "Gasoline", 1);
          game.trade(tradeDepots.get(i), craftName);
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
