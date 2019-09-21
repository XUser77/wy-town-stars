package io.xive.wy.arcade.townstars.objects;

import io.xive.wy.arcade.townstars.objects.BuildingTune;
import java.util.HashMap;
import java.util.Map;

public class ObjectsRepo {

  private Map<String, BuildingTune> allBuildings;

  public ObjectsRepo() {
    loadBuildings();
  }

  public void loadBuildings() {
    this.allBuildings = new HashMap<>();
    allBuildings.put("Wheat Field",
                     new BuildingTune("Wheat Field", 187, 250, 0,
                                      new String[] {"Wheat"}, false,
                                      new String[0], 0, false));

    allBuildings.put("Trade Depot",
                     new BuildingTune("Trade Depot", 1250, 5000, 30,
                                      new String[0], false,
                                      new String[0], 0, true));

    allBuildings.put("Well",
                     new BuildingTune("Well", 312, 1250, 0,
                                      new String[] {"Water"}, false,
                                      new String[0], 0, false));

    allBuildings.put("Silo",
                     new BuildingTune("Silo", 7500, 10000, 0,
                                      new String[0], false,
                                      new String[] {"Crop", "Wood"}, 20, false));

    allBuildings.put("Fuel Storage",
                     new BuildingTune("Fuel Storage", 3750, 15000, 0,
                                      new String[0], false,
                                      new String[] {"Gasoline"}, 40, false));
  }

  public BuildingTune findBuildingTune(String name) {
    return allBuildings.getOrDefault(name, null);
  }

  public BuildingTune[] getAllBuildingsTunes() {
    return allBuildings.entrySet().toArray(new BuildingTune[0]);
  }

}
