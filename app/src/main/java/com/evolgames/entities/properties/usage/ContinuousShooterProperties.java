package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;

public class ContinuousShooterProperties extends RangedProperties {
  private float fireRate = 0.5f;

  @Override
  public Properties copy() {
    return null;
  }

  public float getFireRate() {
    return fireRate;
  }

  public void setFireRate(float fireRate) {
    this.fireRate = fireRate;
  }
}
