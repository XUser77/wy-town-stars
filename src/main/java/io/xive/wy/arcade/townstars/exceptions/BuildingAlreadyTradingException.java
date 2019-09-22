package io.xive.wy.arcade.townstars.exceptions;

public class BuildingAlreadyTradingException extends GameException {

  @Override
  public String getMessage() {
    return "Building is already trading";
  }

}
