package io.xive.wy.arcade.townstars.exceptions;

public class InvalidStorageException extends GameException {

  @Override
  public String getMessage() {
    return "Storage cannot store this craft";
  }

}
