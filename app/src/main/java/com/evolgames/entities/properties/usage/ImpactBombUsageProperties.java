package com.evolgames.entities.properties.usage;

public class ImpactBombUsageProperties extends BombUsageProperties {

    private float minImpact = 10f;

    public ImpactBombUsageProperties() {
        this.delay = 0;
    }

    public float getMinImpact() {
        return minImpact;
    }

    public void setMinImpact(float minImpact) {
        this.minImpact = minImpact;
    }
}
