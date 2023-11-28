package com.evolgames.entities.commandtemplate;

import com.evolgames.userinterface.control.behaviors.actions.Action;

public class TimedCommand {

  transient private Action action;
  private int timeLimit;
  private int counter;
  private boolean timedOut;


  public TimedCommand() {
  }

  public TimedCommand(int timeLimit, Action action) {
    this.timeLimit = timeLimit;
    this.action = action;
  }

  public void update() {
    if (!timedOut) {
      if (counter++ > timeLimit) {
        timedOut = true;
        if (action != null) {
          action.performAction();
        }
      }
    }
  }

  public void setAction(Action action) {
    this.action = action;
  }

  public boolean isTimedOut() {
    return timedOut;
  }
}
