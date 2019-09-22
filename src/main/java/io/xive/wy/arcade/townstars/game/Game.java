package io.xive.wy.arcade.townstars.game;

import io.xive.wy.arcade.townstars.exceptions.BuildingAlreadyCraftingException;
import io.xive.wy.arcade.townstars.exceptions.BuildingAlreadyTradingException;
import io.xive.wy.arcade.townstars.exceptions.BuildingNotFoundException;
import io.xive.wy.arcade.townstars.exceptions.CraftNotFoundException;
import io.xive.wy.arcade.townstars.exceptions.GameException;
import io.xive.wy.arcade.townstars.exceptions.GameFinishedException;
import io.xive.wy.arcade.townstars.exceptions.InvalidStorageException;
import io.xive.wy.arcade.townstars.exceptions.NoBuildingException;
import io.xive.wy.arcade.townstars.exceptions.NoSpaceException;
import io.xive.wy.arcade.townstars.exceptions.NotEnoughCraftsException;
import io.xive.wy.arcade.townstars.exceptions.NotEnoughSpaceException;
import io.xive.wy.arcade.townstars.exceptions.NotEnoughtCurrencyException;
import io.xive.wy.arcade.townstars.exceptions.NotMeetRequirements;
import io.xive.wy.arcade.townstars.exceptions.OutputNotEmptyException;
import io.xive.wy.arcade.townstars.exceptions.WrongCraftException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Game {

  public static final long START_CURRENCY = 25000;
  public static final long START_POINTS = 0;
  public static final long GAME_DURATION = 5 * 60 * 60 * 1000;
  public static final long BUILDING_LABOR_PERIOD = 10 * 1000;
  public static final long BUILDING_CRAFT_PERIOD = 60 * 1000;
  public static final long BUILDING_TRADE_PERIOD = 2 * 60 * 1000;

  private long currency;
  private long points;

  private Building[] buildings;

  private ObjectsRepo objectsRepo;

  private long startDate;
  private long skippedMs;

  private List<Craft> tradeCrafts;

  private List<String> ledger;

  public Game() throws IOException {

    this.objectsRepo = new ObjectsRepo();

    this.startDate = new Date().getTime();
    this.skippedMs = 0;

    this.currency = START_CURRENCY;
    this.points = 0;

    this.tradeCrafts = new ArrayList<>();

    this.buildings = new Building[257]; // Never access buildings[0]
    this.buildings[1] = newBuilding(objectsRepo.findBuildingTune("Wheat Field"));
    this.buildings[2] = newBuilding(objectsRepo.findBuildingTune("Wheat Field"));
    this.buildings[3] = newBuilding(objectsRepo.findBuildingTune("Trade Depot"));
    this.buildings[4] = newBuilding(objectsRepo.findBuildingTune("Well"));
    this.buildings[5] = newBuilding(objectsRepo.findBuildingTune("Silo"));
    this.buildings[6] = newBuilding(objectsRepo.findBuildingTune("Fuel Storage"));

    for (int i=0; i<10; i++) {
      this.buildings[5].storedCrafts.add(objectsRepo.findCraftTune("Wheat").newCraft());
    }

    for (int i=0; i<40; i++) {
      this.buildings[6].storedCrafts.add(objectsRepo.findCraftTune("Gasoline").newCraft());
    }

    this.ledger = new ArrayList<>();

  }

  private Building newBuilding(BuildingTune buildingTune) {
    return new Building(buildingTune.getName(),
                        buildingTune.getSellValue(), buildingTune.getSellValue(), buildingTune.getLaborValue(),
                        buildingTune.getProducesCrafts(), buildingTune.isIgnoreRequirements(),
                        buildingTune.getStoresCraftTypes(), buildingTune.getStorageCapacity(),
                        buildingTune.isTradeDepot(), getGameDate());
  }

  private void checkOutputBuilding(Building building, Craft craft, int amount) throws NotEnoughCraftsException {
    if (building.getStorageCapacity() > 0) {
      if (Collections.frequency(building.storedCrafts, craft) < amount) {
        throw new NotEnoughCraftsException();
      }
    } else if (amount > 1) {
      throw new NotEnoughCraftsException();
    } else {
      if (building.craftOutside == null || !building.craftOutside.equals(craft)) {
        throw new NotEnoughCraftsException();
      }
    }
  }

  private void popOutputBuilding(Building building, Craft craft, int amount) {
    if (building.getStorageCapacity() > 0) {
      for(int i=0; i<amount; i++) building.storedCrafts.remove(craft);
    } else {
      building.craftOutside = null;
    }
  }

  private void logLedger(String action, String item, Integer from, Integer to, Integer amount) {
    ledger.add(getGameDate() + "," + action + "," + item + "," +
                   (from != null ? from : "") + "," + (to != null ? to : "") + "," + (amount != null ? amount : ""));
  }

  /*** Helpers ***/
  public BuildingTune[] getAllBuildingTunes() {
    return objectsRepo.getAllBuildingsTunes();
  }

  public Building[] getBuildings() {
    return buildings;
  }

  public Building getBuilding(int index) {
    return buildings[index];
  }

  public long getCurrency() {
    return currency;
  }

  public long getPoints() {
    return points;
  }

  public long getGameDate() {
    return new Date().getTime() + skippedMs - startDate;
  }

  public void skip(long ms) {
    this.skippedMs += ms;
  }

  public boolean isFinished() {
    return getGameDate() >= GAME_DURATION;
  }

  public Craft[] getTradeCrafts() {
    return tradeCrafts.toArray(new Craft[0]);
  }

  public String compileLedger() {
    return String.join("\r\n", ledger);
  }

  public void tick() {
    long gameDate = getGameDate();
    for(int i=1; i<buildings.length; i++) {
      if (buildings[i] == null) continue;

      if (gameDate - buildings[i].lastLaborDate >= BUILDING_LABOR_PERIOD) {
        long passed = (gameDate - buildings[i].lastLaborDate) / BUILDING_LABOR_PERIOD;
        currency -= buildings[i].getLaborValue() * passed;
        buildings[i].lastLaborDate += passed * BUILDING_LABOR_PERIOD;
      }

      if (buildings[i].craftStartDate != null && gameDate - buildings[i].craftStartDate >= BUILDING_CRAFT_PERIOD) {
        synchronized (buildings[i]) {
          buildings[i].craftOutside = buildings[i].crafting;
          buildings[i].craftsInside.clear();
          buildings[i].craftStartDate = null;
          buildings[i].crafting = null;
        }
      }

      if (buildings[i].tradeStartDate != null && gameDate - buildings[i].tradeStartDate >= BUILDING_TRADE_PERIOD) {
        synchronized (buildings[i]) {
          currency += buildings[i].tradeCraft.getCityPrice() * 10;
          points += Math.max(1, buildings[i].tradeCraft.getCityPrice() * 10 / 1000);
          buildings[i].tradeCraft = null;
          buildings[i].tradeStartDate = null;
        }
      }

    }
  }

  /*** GAME ACTIONS ***/
  public int build(String buildingName) throws GameException {
    tick();
    if (isFinished()) throw new GameFinishedException();
    BuildingTune buildingTune = objectsRepo.findBuildingTune(buildingName);

    if (buildingTune == null) throw new BuildingNotFoundException();
    if (currency < buildingTune.getBuildCost()) throw new NotEnoughtCurrencyException();

    int freeIndex = 0;
    for(int i=1; i<buildings.length; i++) {
      if (buildings[i] == null) {
        freeIndex = i;
        break;
      }
    }

    if (freeIndex == 0) throw new NoSpaceException();

    currency -= buildingTune.getBuildCost();
    buildings[freeIndex] = newBuilding(buildingTune);

    logLedger("BUILD", buildings[freeIndex].getName(), null, freeIndex, null);

    return freeIndex;
  }

  public void sell(int buildingIndex) throws GameException {
    tick();
    if (isFinished()) throw new GameFinishedException();
    if (buildings[buildingIndex] == null) throw new NoBuildingException();

    Building building = buildings[buildingIndex];

    currency += buildings[buildingIndex].getSellValue();
    buildings[buildingIndex] = null;

    logLedger("SELL", building.getName(), buildingIndex, null, null);

  }

  public void craft(int buildingIndex, String craftName) throws GameException {
    tick();
    if (isFinished()) throw new GameFinishedException();

    Building building = buildings[buildingIndex];
    if (building == null) throw new NoBuildingException();

    if (!Arrays.asList(building.getProducesCrafts()).contains(craftName)) {
      throw new WrongCraftException();
    }

    if (building.craftOutside != null) throw new OutputNotEmptyException();

    if (building.craftStartDate != null) throw new BuildingAlreadyCraftingException();

    CraftTune craftTune = objectsRepo.findCraftTune(craftName);
    if (craftTune == null) throw new CraftNotFoundException();
    Craft newCraft = craftTune.newCraft();

    if (!building.isIgnoreRequirements()) {

      if (craftTune.getCraftRequired1() != null) {
        Craft inCraft = objectsRepo.findCraftTune(craftTune.getCraftRequired1()).newCraft();
        if (Collections.frequency(building.craftsInside, inCraft) < craftTune.getCraftRequiredAmount1()) {
          throw new NotMeetRequirements();
        }
      }

      if (craftTune.getCraftRequired2() != null) {
        Craft inCraft = objectsRepo.findCraftTune(craftTune.getCraftRequired2()).newCraft();
        if (Collections.frequency(building.craftsInside, inCraft) < craftTune.getCraftRequiredAmount2()) {
          throw new NotMeetRequirements();
        }
      }

      if (craftTune.getCraftRequired3() != null) {
        Craft inCraft = objectsRepo.findCraftTune(craftTune.getCraftRequired3()).newCraft();
        if (Collections.frequency(building.craftsInside, inCraft) < craftTune.getCraftRequiredAmount3()) {
          throw new NotMeetRequirements();
        }
      }
    }

    building.craftStartDate = getGameDate();
    building.crafting = newCraft;

    logLedger("CRAFT", newCraft.getName(), null, buildingIndex, null);

  }

  public void store(int fromIndex, int toIndex, String craftName, int amount) throws GameException {
    tick();
    if (isFinished()) throw new GameFinishedException();

    Building fromBuilding = getBuilding(fromIndex);
    if (fromBuilding == null) throw new BuildingNotFoundException();

    Building toBuilding = getBuilding(toIndex);
    if (toBuilding == null) throw new BuildingNotFoundException();

    CraftTune craftTune = objectsRepo.findCraftTune(craftName);
    if (craftTune == null) throw new CraftNotFoundException();
    Craft newCraft = craftTune.newCraft();

    checkOutputBuilding(fromBuilding, newCraft, amount);

    if (toBuilding.getStorageCapacity() > 0) {
      if (!Arrays.asList(toBuilding.getStoresCraftTypes()).contains(craftTune.getType()) &&
          !Arrays.asList(toBuilding.getStoresCraftTypes()).contains(craftTune.getName())) {
        throw new InvalidStorageException();
      }
      if (toBuilding.storedCrafts.size() + amount > toBuilding.getStorageCapacity()) {
        throw new NotEnoughSpaceException();
      }
    }

    popOutputBuilding(fromBuilding, newCraft, 1);

    if (toBuilding.getStorageCapacity() > 0) {
      for (int i = 0; i < amount; i++) toBuilding.storedCrafts.add(craftTune.newCraft());
    } else if (toBuilding.isTradeDepot()) {
      for (int i = 0; i < amount; i++) tradeCrafts.add(craftTune.newCraft());
    } else {
      for(int i=0; i<amount; i++) toBuilding.craftsInside.add(craftTune.newCraft());
    }

    logLedger("STORE", newCraft.getName(), fromIndex, toIndex, null);

  }

  public void consume(int buildingIndex, String craftName, int amount) throws GameException {
    tick();
    if (isFinished()) throw new GameFinishedException();

    Building building = getBuilding(buildingIndex);
    if (building == null) throw new NoBuildingException();

    CraftTune craftTune = objectsRepo.findCraftTune(craftName);
    if (craftTune == null) throw new CraftNotFoundException();
    Craft newCraft = craftTune.newCraft();

    checkOutputBuilding(building, newCraft, amount);

    popOutputBuilding(building, newCraft, amount);

    for(int i=0; i<amount; i++) {
      tradeCrafts.add(craftTune.newCraft());
    }

    logLedger("CONSUME", newCraft.getName(), buildingIndex, null, amount);

  }

  public void trade(int buildingIndex, String craftName) throws GameException {
    tick();
    if (isFinished()) throw new GameFinishedException();

    Building building = getBuilding(buildingIndex);
    if (building == null) throw new BuildingNotFoundException();

    CraftTune craftTune = objectsRepo.findCraftTune(craftName);
    if (craftTune == null) throw new CraftNotFoundException();
    Craft newCraft = craftTune.newCraft();

    if (Collections.frequency(tradeCrafts, newCraft) < 10) {
      throw new NotEnoughCraftsException();
    }

    if (!tradeCrafts.contains(objectsRepo.findCraftTune("Gasoline").newCraft())) {
      throw new NotEnoughCraftsException();
    }

    if (building.tradeStartDate != null) {
      throw new BuildingAlreadyTradingException();
    }

    for(int i=0; i<10; i++) {
      tradeCrafts.remove(newCraft);
    }

    tradeCrafts.remove(objectsRepo.findCraftTune("Gasoline").newCraft());

    building.tradeStartDate = getGameDate();
    building.tradeCraft = newCraft;

    logLedger("TRADE", newCraft.getName(), buildingIndex, null, null);

  }

}
