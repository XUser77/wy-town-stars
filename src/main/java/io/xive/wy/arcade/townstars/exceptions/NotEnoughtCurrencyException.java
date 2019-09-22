package io.xive.wy.arcade.townstars.exceptions;

public class NotEnoughtCurrencyException extends GameException {

  @Override
  public String getMessage() {
    return "Not enough currency";
  }

}
