package io.xive.wy.arcade.townstars.game;

public class Craft {

  private String name;
  private String type;
  private long cityPrice;

  public Craft(String name, String type, long cityPrice) {

    this.name = name;
    this.type = type;
    this.cityPrice = cityPrice;

  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Craft) {
      return this.name.equals(((Craft)obj).name);
    }
    return false;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public long getCityPrice() {
    return cityPrice;
  }

}
