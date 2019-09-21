package io.xive.wy.arcade.townstars.game;

import io.xive.wy.arcade.townstars.exceptions.BuildingAlreadyCraftingException;
import io.xive.wy.arcade.townstars.exceptions.BuildingNotFoundException;
import io.xive.wy.arcade.townstars.exceptions.CraftNotFoundException;
import io.xive.wy.arcade.townstars.exceptions.GameException;
import io.xive.wy.arcade.townstars.exceptions.GameFinishedException;
import io.xive.wy.arcade.townstars.exceptions.NoBuildingException;
import io.xive.wy.arcade.townstars.exceptions.NoSpaceException;
import io.xive.wy.arcade.townstars.exceptions.NotEnoughtCurrencyException;
import io.xive.wy.arcade.townstars.exceptions.NotMeetRequirements;
import io.xive.wy.arcade.townstars.exceptions.OutputNotEmptyException;
import io.xive.wy.arcade.townstars.exceptions.WrongCraftException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class Game {

  public static final long START_CURRENCY = 25000;
  public static final long START_POINTS = 0;
  public static final long GAME_DURATION = 6 * 60 * 60 * 1000;
  public static final long BUILDING_LABOR_PERIOD = 10 * 1000;
  public static final long BUILDING_CRAFT_PERIOD = 60 * 1000;

  private long currency;
  private long points;

  private Building[] buildings;

  private ObjectsRepo objectsRepo;

  private long startDate;
  private long skippedMs;

  public Game() {

    this.objectsRepo = new ObjectsRepo();

    this.startDate = new Date().getTime();
    this.skippedMs = 0;

    this.currency = START_CURRENCY;
    this.points = 0;

    this.buildings = new Building[257]; // Never access buildings[0]
    this.buildings[1] = newBuilding(objectsRepo.findBuildingTune("Wheat Field"));
    this.buildings[2] = newBuilding(objectsRepo.findBuildingTune("Wheat Field"));
    this.buildings[3] = newBuilding(objectsRepo.findBuildingTune("Trade Depot"));
    this.buildings[4] = newBuilding(objectsRepo.findBuildingTune("Well"));
    this.buildings[5] = newBuilding(objectsRepo.findBuildingTune("Silo"));
    this.buildings[6] = newBuilding(objectsRepo.findBuildingTune("Fuel Storage"));

  }

  private Building newBuilding(BuildingTune buildingTune) {
    return new Building(buildingTune.getName(),
                        buildingTune.getSellValue(), buildingTune.getSellValue(), buildingTune.getLaborValue(),
                        buildingTune.getProducesCrafts(), buildingTune.isIgnoreRequirements(),
                        buildingTune.getStoresCraftTypes(), buildingTune.getStorageCapacity(),
                        buildingTune.isTradeDepot(), getGameDate());
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

    return freeIndex;
  }

  public void sell(int buildingIndex) throws GameException {
    tick();
    if (isFinished()) throw new GameFinishedException();
    if (buildings[buildingIndex] == null) throw new NoBuildingException();

    currency += buildings[buildingIndex].getSellValue();
    buildings[buildingIndex] = null;

  }

  public void craft(int buildingIndex, String craftName) throws GameException {
    tick();
    Building building = buildings[buildingIndex];
    if (building == null) throw new NoBuildingException();

    if (!Arrays.asList(building.getProducesCrafts()).contains(craftName)) {
      throw new WrongCraftException();
    }

    if (building.craftOutside != null) throw new OutputNotEmptyException();

    if (building.craftStartDate != null) throw new BuildingAlreadyCraftingException();

    CraftTune craftTune = objectsRepo.findCraftTune(craftName);
    if (craftTune == null) throw new CraftNotFoundException();

    if (!building.isIgnoreRequirements()) {
      if (craftTune.getCraftRequired1() != null &&
          Collections.frequency(building.craftsInside, craftTune.getName()) < craftTune.getCraftRequiredAmount1()) {
        throw new NotMeetRequirements();
      }
      if (craftTune.getCraftRequired2() != null &&
          Collections.frequency(building.craftsInside, craftTune.getName()) < craftTune.getCraftRequiredAmount2()) {
        throw new NotMeetRequirements();
      }
      if (craftTune.getCraftRequired3() != null &&
          Collections.frequency(building.craftsInside, craftTune.getName()) < craftTune.getCraftRequiredAmount3()) {
        throw new NotMeetRequirements();
      }
    }

    building.craftStartDate = getGameDate();
    building.crafting = craftTune.getName();

  }



}
