package io.xive.wy.arcade.townstars.exceptions;

public class NotEnoughCraftsException extends GameException {

  @Override
  public String getMessage() {
    return "Not enough crafts";
  }

}
