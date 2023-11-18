package com.evolgames.entities.hand;

import com.evolgames.scenes.entities.Hand;

public abstract class HandControl {

  private boolean hasLifespan;
  protected int count;
  protected int lifespan;
  private boolean dead = false;
  transient protected  Hand hand;

  public HandControl() {}

  public HandControl(Hand hand) {
    this.hasLifespan = false;
    this.hand = hand;
  }

  public HandControl(Hand hand,int lifespan) {
    this.hasLifespan = true;
    this.lifespan = lifespan;
    this.hand = hand;
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

  public void setHand(Hand hand) {
    this.hand = hand;
  }
}
