package io.xive.wy.arcade.townstars.exceptions;

public class BuildingNotFoundException extends GameException {

  @Override
  public String getMessage() {
    return "Building not found";
  }

}
