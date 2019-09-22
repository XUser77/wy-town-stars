package io.xive.wy.arcade.townstars.exceptions;

public class CraftNotFoundException extends GameException {

  @Override
  public String getMessage() {
    return "Craft not found";
  }

}
