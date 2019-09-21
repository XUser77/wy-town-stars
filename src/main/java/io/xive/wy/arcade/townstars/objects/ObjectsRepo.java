package io.xive.wy.arcade.townstars.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ObjectsRepo {

  private Map<String, BuildingTune> buildingTuneMap;
  private List<BuildingTune> buildingTunes;

  public ObjectsRepo() {
    loadBuildings();
  }

  public void loadBuildings() {
    this.buildingTuneMap = new HashMap<>();

    buildingTuneMap.put("Wheat Field",
                        new BuildingTune("Wheat Field", 187, 250, 0,
                                      new String[] {"Wheat"}, false,
                                      new String[0], 0, false));


    buildingTuneMap.put("Trade Depot",
                        new BuildingTune("Trade Depot", 1250, 5000, 30,
                                      new String[0], false,
                                      new String[0], 0, true));

    buildingTuneMap.put("Well",
                        new BuildingTune("Well", 312, 1250, 0,
                                      new String[] {"Water"}, false,
                                      new String[0], 0, false));

    buildingTuneMap.put("Silo",
                        new BuildingTune("Silo", 7500, 10000, 0,
                                      new String[0], false,
                                      new String[] {"Crop", "Wood"}, 20, false));

    buildingTuneMap.put("Fuel Storage",
                        new BuildingTune("Fuel Storage", 3750, 15000, 0,
                                      new String[0], false,
                                      new String[] {"Gasoline"}, 40, false));

    buildingTuneMap.put("Steel Mill",
                        new BuildingTune("Steel Mill", 75000, 100000, 150,
                                      new String[] {"Steel", "Blue Steel"}, false,
                                      new String[0], 40, false));

    List<BuildingTune> buildingTunes = new ArrayList<>();
    for(Entry<String, BuildingTune> keySet: buildingTuneMap.entrySet()) {
      buildingTunes.add(keySet.getValue());
    }
    this.buildingTunes = buildingTunes;
  }

  public BuildingTune findBuildingTune(String name) {
    return buildingTuneMap.getOrDefault(name, null);
  }

  public BuildingTune[] getAllBuildingsTunes() {
    return buildingTunes.toArray(new BuildingTune[0]);
  }

}
