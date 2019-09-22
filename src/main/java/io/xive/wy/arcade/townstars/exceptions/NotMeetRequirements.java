package io.xive.wy.arcade.townstars.exceptions;

public class NotMeetRequirements extends GameException {

  @Override
  public String getMessage() {
    return "Requirements are not met";
  }

}
