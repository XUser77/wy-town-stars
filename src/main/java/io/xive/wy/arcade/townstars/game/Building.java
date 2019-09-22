package io.xive.wy.arcade.townstars.game;

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
  protected Long craftStartDate;
  protected List<Craft> craftsInside;
  protected Craft crafting;
  protected Craft craftOutside;

  protected List<Craft> storedCrafts;

  protected List<Craft> consumeCrafts;

  protected long lastLaborDate;

  protected Craft tradeCraft;
  protected Long tradeStartDate;

  private String shortName;

  protected Building(String name, long sellValue, long buildCost, long laborValue,
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

    this.shortName = "";
    String[] ar = this.name.split(" ");
    if (ar.length > 1) {
      for (int i = 0; i < ar.length; i++) {
        this.shortName += ar[i].substring(0, 1).toUpperCase();
      }
    } else {
      this.shortName = this.name.substring(0, 2).toUpperCase();
    }

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

  public Long getCraftStartDate() {
    return craftStartDate;
  }

  public boolean isTradeDepot() {
    return isTradeDepot;
  }

  public Craft[] getCraftsInside() {
    return craftsInside.toArray(new Craft[0]);
  }

  public Craft getCrafting() {
    return crafting;
  }

  public Craft getCraftOutside() {
    return craftOutside;
  }

  public Craft[] getStoredCrafts() {
    return storedCrafts.toArray(new Craft[0]);
  }

  public Craft[] getConsumeCrafts() {
    return consumeCrafts.toArray(new Craft[0]);
  }

  public long getLastLaborDate() {
    return lastLaborDate;
  }

  public Craft getTradeCraft() {
    return tradeCraft;
  }

  public Long getTradeStartDate() {
    return tradeStartDate;
  }

  public String getShortName() {
    return shortName;
  }

}
