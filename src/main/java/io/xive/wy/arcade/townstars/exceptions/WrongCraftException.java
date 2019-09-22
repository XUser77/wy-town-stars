package io.xive.wy.arcade.townstars.exceptions;

public class WrongCraftException extends GameException {

  @Override
  public String getMessage() {
    return "Cannot craft this item";
  }

}
