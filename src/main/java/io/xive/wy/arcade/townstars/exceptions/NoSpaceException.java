package io.xive.wy.arcade.townstars.exceptions;

public class NoSpaceException extends GameException {

  @Override
  public String getMessage() {
    return "No space left for a new building";
  }

}
