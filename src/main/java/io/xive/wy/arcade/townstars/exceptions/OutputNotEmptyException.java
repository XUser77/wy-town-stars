package io.xive.wy.arcade.townstars.exceptions;

public class OutputNotEmptyException extends GameException {

  @Override
  public String getMessage() {
    return "Building output is not empty";
  }

}
