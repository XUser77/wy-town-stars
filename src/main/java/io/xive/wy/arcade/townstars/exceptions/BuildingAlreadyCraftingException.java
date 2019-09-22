package io.xive.wy.arcade.townstars.exceptions;

public class BuildingAlreadyCraftingException extends GameException {

  @Override
  public String getMessage() {
    return "Building is already crafting";
  }

}
