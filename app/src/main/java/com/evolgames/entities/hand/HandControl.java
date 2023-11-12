package com.evolgames.entities.hand;

public abstract class HandControl {

  private final boolean hasLifespan;
  protected int count;
  protected Runnable runnable;
  protected int lifespan;
  private boolean dead = false;

  public HandControl() {
    this.hasLifespan = false;
  }

  public HandControl(int lifespan) {
    this.hasLifespan = true;
    this.lifespan = lifespan;
  }

  public void run() {
    if (dead) {
      return;
    }
    if (hasLifespan) {
      count++;
      if (count > lifespan) {
        dead = true;
      }
    }
  }

  public boolean isDead() {
    return dead;
  }

  public void setDead(boolean dead) {
    this.dead = dead;
  }

  public void setRunnable(Runnable runnable) {
    this.runnable = runnable;
  }
}
