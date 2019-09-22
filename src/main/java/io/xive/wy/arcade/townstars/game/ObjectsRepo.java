package io.xive.wy.arcade.townstars.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ObjectsRepo {

  private Map<String, BuildingTune> buildingTuneMap;
  private BuildingTune[] buildingTunes;

  private Map<String, CraftTune> craftTuneMap;
  private CraftTune[] craftTunes;

  public ObjectsRepo() throws IOException {
    /*loadBuildings();
    loadCrafts();*/
    loadObjects();
  }

  private void loadObjects() throws IOException {
    File excelFile = new File("Hackathon_TownStar_Tuning.xlsx");
    FileInputStream fis = new FileInputStream(excelFile);
    XSSFWorkbook workbook = new XSSFWorkbook(fis);

    this.buildingTuneMap = new HashMap<>();
    XSSFSheet sheet = workbook.getSheetAt(0);
    int index = 0;
    while(sheet.getRow(++index) != null) {
      XSSFRow row = sheet.getRow(index);

      String name = row.getCell(0).getStringCellValue().trim();
      long sellValue = (long)row.getCell(1).getNumericCellValue();
      long buyCost = (long)row.getCell(2).getNumericCellValue();
      long laborCost = (long)row.getCell(3).getNumericCellValue();
      String[] produceCrafts = new String[0];
      if (!"None".equals(row.getCell(4).getStringCellValue().trim())) {
        produceCrafts = row.getCell(4).getStringCellValue().trim().split(",");
      }
      boolean hasRequirementsMet = "Yes".equals(row.getCell(5).getStringCellValue().trim());
      String[] storesCraftTypes = new String[0];
      if (!"None".equals(row.getCell(6).getStringCellValue().trim())) {
        storesCraftTypes = row.getCell(6).getStringCellValue().trim().split(",");
      }
      int storageCapacity = (int)row.getCell(7).getNumericCellValue();

      buildingTuneMap.put(name,
                          new BuildingTune(name, sellValue, buyCost, laborCost,
                                           produceCrafts, hasRequirementsMet,
                                           storesCraftTypes, storageCapacity, name.equals("Trade Depot")));
    }

    List<BuildingTune> buildingTunes = new ArrayList<>();
    for(Entry<String, BuildingTune> keySet: buildingTuneMap.entrySet()) {
      buildingTunes.add(keySet.getValue());
    }
    this.buildingTunes = buildingTunes.toArray(new BuildingTune[0]);

    this.craftTuneMap = new HashMap<>();
    sheet = workbook.getSheetAt(1);
    index = 0;
    while(sheet.getRow(++index) != null) {
      XSSFRow row = sheet.getRow(index);
      String name = row.getCell(0).getStringCellValue();
      long cityPrice = (long)row.getCell(1).getNumericCellValue();
      String type = row.getCell(2).getStringCellValue();

      String req1 = row.getCell(3).getStringCellValue();
      if (req1.trim().equals("none")) req1 = null;
      long req1Number = (long)row.getCell(4).getNumericCellValue();

      String req2 = row.getCell(5).getStringCellValue();
      if (req2.trim().equals("none")) req2 = null;
      long req2Number = (long)row.getCell(6).getNumericCellValue();

      String req3 = row.getCell(7).getStringCellValue();
      if (req3.trim().equals("none")) req3 = null;
      long req3Number = (long)row.getCell(8).getNumericCellValue();

      craftTuneMap.put(name,
                       new CraftTune(name, type, cityPrice,
                                     req1, req1Number,
                                     req2, req2Number,
                                     req3 , req3Number));

    }


    List<CraftTune> craftTunes = new ArrayList<>();
    for(Entry<String, CraftTune> keySet: craftTuneMap.entrySet()) {
      craftTunes.add(keySet.getValue());
    }
    this.craftTunes = craftTunes.toArray(new CraftTune[0]);

  }

  /*private void loadBuildings() {
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
                                      new String[0], 0, false));

    List<BuildingTune> buildingTunes = new ArrayList<>();
    for(Entry<String, BuildingTune> keySet: buildingTuneMap.entrySet()) {
      buildingTunes.add(keySet.getValue());
    }
    this.buildingTunes = buildingTunes.toArray(new BuildingTune[0]);
  }*/

  /*private void loadCrafts() {
    this.craftTuneMap = new HashMap<>();

    craftTuneMap.put("Water",
                     new CraftTune("Water", "Natural", 100,
                                   null, 0,
                                   null, 0,
                                   null , 0));

    craftTuneMap.put("Wheat",
                     new CraftTune("Wheat", "Crop", 400,
                                   "Water", 3,
                                   null, 0,
                                   null , 0));

    craftTuneMap.put("Gasoline",
                     new CraftTune("Gasoline", "Produced", 100,
                                   "Petroleum", 1,
                                   "Water", 4,
                                   "Energy" , 1));

    List<CraftTune> craftTunes = new ArrayList<>();
    for(Entry<String, CraftTune> keySet: craftTuneMap.entrySet()) {
      craftTunes.add(keySet.getValue());
    }
    this.craftTunes = craftTunes.toArray(new CraftTune[0]);

  }*/

  public BuildingTune findBuildingTune(String name) {
    return buildingTuneMap.getOrDefault(name, null);
  }

  public BuildingTune[] getAllBuildingsTunes() {
    return buildingTunes;
  }

  public CraftTune findCraftTune(String name) {
    return craftTuneMap.getOrDefault(name, null);
  }

}
