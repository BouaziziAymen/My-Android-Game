package com.evolgames.entities.usage;

import com.evolgames.entities.properties.usage.TimeBombUsageProperties;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.entities.PlayerSpecialAction;
import com.evolgames.userinterface.model.toolmodels.UsageModel;

import java.util.Collections;
import java.util.List;

public class TimeBomb extends Bomb {
  
  private float countDown;
  private boolean count;

  @SuppressWarnings("unused")
  public TimeBomb() {
  }
  public TimeBomb(UsageModel<?> usageModel) {
    super(usageModel);
    TimeBombUsageProperties properties = (TimeBombUsageProperties) usageModel.getProperties();
    this.countDown = properties.getDelay();
  }

  @Override
  protected boolean isBombOn() {
    return countDown<0;
  }

  @Override
  protected boolean isActivated() {
    return count;
  }

  @Override
  public void onStep(float deltaTime, WorldFacade worldFacade) {
    super.onStep(deltaTime,worldFacade);
    if (count&&alive) {
      countDown -= deltaTime;
    }
  }

  public void onTriggerReleased() {
    count = true;
  }

}
