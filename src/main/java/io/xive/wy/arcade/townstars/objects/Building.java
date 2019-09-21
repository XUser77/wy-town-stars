package io.xive.wy.arcade.townstars.objects;

import java.util.ArrayList;
import java.util.List;

public class Building {

  /*** TUNING PROPERTIES ***/
  private String name;
  private long buildCost;
  private long sellValue;
  private long laborValue;

  private String[] producesCrafts;
  private String[] storesCraftTypes;
  private long storageCapacity;

  private boolean ignoreRequirements;

  private boolean isTradeDepot;

  /*** RUNTIME PROPERTIES ***/
  public long craftStartDate;
  public List<Craft> craftsInside;

  public String craftOutside;

  public List<Craft> storedCrafts;

  public List<Craft> consumeCrafts;

  public long lastLaborDate;

  public Building(String name, long sellValue, long buildCost, long laborValue,
                  String[] producesCrafts, boolean hasRequirementsMet, String[] storesCraftTypes,
                  long storageCapacity, boolean isTradeDepot, long gameDate) {
    this.name = name;
    this.buildCost = buildCost;
    this.sellValue = sellValue;
    this.laborValue = laborValue;

    this.producesCrafts = producesCrafts;
    this.storesCraftTypes = storesCraftTypes;

    this.storageCapacity = storageCapacity;

    this.ignoreRequirements = hasRequirementsMet;

    this.isTradeDepot = isTradeDepot;

    if (this.storageCapacity > 0) {
      this.storedCrafts = new ArrayList<>();
    }

    if (this.producesCrafts.length > 0) {
      this.craftsInside = new ArrayList<>();
    }

    if (this.isTradeDepot) {
      this.consumeCrafts = new ArrayList<>();
    }

    this.lastLaborDate = gameDate;

  }

  public String getName() {
    return name;
  }

  public long getBuildCost() {
    return buildCost;
  }

  public long getSellValue() {
    return sellValue;
  }

  public long getLaborValue() {
    return laborValue;
  }

  public String[] getProducesCrafts() {
    return producesCrafts;
  }

  public String[] getStoresCraftTypes() {
    return storesCraftTypes;
  }

  public long getStorageCapacity() {
    return storageCapacity;
  }

  public boolean isIgnoreRequirements() {
    return ignoreRequirements;
  }

  public long getCraftStartDate() {
    return craftStartDate;
  }

  public boolean isTradeDepot() {
    return isTradeDepot;
  }

}
