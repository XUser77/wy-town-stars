package io.xive.wy.arcade.townstars.exceptions;

public class NotEnoughSpaceException extends GameException {

  @Override
  public String getMessage() {
    return "Not enough space for more crafts";
  }

}
