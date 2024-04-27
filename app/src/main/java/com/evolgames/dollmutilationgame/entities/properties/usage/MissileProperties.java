package com.evolgames.dollmutilationgame.entities.properties.usage;

public class MissileProperties extends RocketProperties {
    private float control = 1f;

    public MissileProperties() {
        super();
    }

    public float getControl() {
        return control;
    }

    public void setControl(float controlSurface) {
        this.control = controlSurface;
    }
}
