package com.evolgames.entities.properties.usage;

public class ImpactBombUsageProperties extends BombUsageProperties {

  public ImpactBombUsageProperties(){
    this.delay = 0;
  }
  private float minImpact = 10f;

  public float getMinImpact() {
    return minImpact;
  }

  public void setMinImpact(float minImpact) {
    this.minImpact = minImpact;
  }
}
