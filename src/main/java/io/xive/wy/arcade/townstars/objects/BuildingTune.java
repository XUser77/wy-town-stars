package io.xive.wy.arcade.townstars.objects;

public class BuildingTune {

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

  public BuildingTune(String name, long sellValue, long buildCost, long laborValue,
                  String[] producesCrafts, boolean hasRequirementsMet, String[] storesCraftTypes, long storageCapacity, boolean isTradeDepot) {
    this.name = name;
    this.buildCost = buildCost;
    this.sellValue = sellValue;
    this.laborValue = laborValue;

    this.producesCrafts = producesCrafts;
    this.storesCraftTypes = storesCraftTypes;

    this.storageCapacity = storageCapacity;

    this.ignoreRequirements = hasRequirementsMet;

    this.isTradeDepot = isTradeDepot;
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

  public boolean isTradeDepot() {
    return isTradeDepot;
  }

}
