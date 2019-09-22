package io.xive.wy.arcade.townstars.exceptions;

public class NoBuildingException extends GameException {

  @Override
  public String getMessage() {
    return "No building in field";
  }

}
