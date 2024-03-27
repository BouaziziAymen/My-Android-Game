package com.evolgames.entities.properties.usage;

import java.util.List;

public class ImpactBombUsageProperties extends BombUsageProperties {

    private float minImpact = 10f;

    private List<Integer> sensitiveLayers;

    public ImpactBombUsageProperties() {
        this.delay = 0;
    }

    public float getMinImpact() {
        return minImpact;
    }

    public void setMinImpact(float minImpact) {
        this.minImpact = minImpact;
    }

    public List<Integer> getSensitiveLayers() {
        return sensitiveLayers;
    }

    public void setSensitiveLayers(List<Integer> sensitiveLayers) {
        this.sensitiveLayers = sensitiveLayers;
    }
}
