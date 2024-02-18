package com.evolgames.entities.usage;

import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.PlayerSpecialAction;

import java.util.List;

public abstract class Use {

  protected boolean active;

  public abstract void onStep(float deltaTime, WorldFacade worldFacade);

  public abstract List<PlayerSpecialAction> getActions();

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
