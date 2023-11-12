package com.evolgames.entities.commandtemplate.commands;

import com.evolgames.entities.commandtemplate.Invoker;

public abstract class Command {

  private boolean aborted;

  public boolean isAborted() {
    return aborted;
  }

  public void setAborted(boolean aborted) {
    this.aborted = aborted;
  }

  public void execute() {
    if (!Invoker.scene.getPhysicsWorld().isLocked() && this.isReady() && !isAborted()) {
      this.run();
      this.setAborted(true);
    }
  }

  protected abstract void run();

  protected boolean isReady() {
    return true;
  }
}
