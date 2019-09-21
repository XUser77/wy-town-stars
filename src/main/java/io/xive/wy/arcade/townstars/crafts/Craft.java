package io.xive.wy.arcade.townstars.crafts;

public class Craft {

  private String name;
  private String type;
  private long cityPrice;

  private String craftRequired1;
  private long craftRequiredAmount1;

  private String craftRequired2;
  private long craftRequiredAmount2;

  private String craftRequired3;
  private long craftRequiredAmount3;

  public Craft(String name, String type, long cityPrice,
               String craftRequired1, long craftRequiredAmount1,
               String craftRequired2, long craftRequiredAmount2,
               String craftRequired3, long craftRequiredAmount3) {

    this.name = name;
    this.type = type;
    this.cityPrice = cityPrice;

    this.craftRequired1 = craftRequired1;
    this.craftRequiredAmount1 = craftRequiredAmount1;
    this.craftRequired2 = craftRequired2;
    this.craftRequiredAmount2 = craftRequiredAmount2;
    this.craftRequired3 = craftRequired3;
    this.craftRequiredAmount3 = craftRequiredAmount3;

  }

}
