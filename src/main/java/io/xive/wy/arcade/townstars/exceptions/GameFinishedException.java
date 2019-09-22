package io.xive.wy.arcade.townstars.exceptions;

public class GameFinishedException extends GameException {

  @Override
  public String getMessage() {
    return "Game is finished";
  }

}
