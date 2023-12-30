package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;

public class MissileProperties extends RocketProperties {
    private float control = 1f;
    public MissileProperties() {
        super();
    }
    
    @Override
    public Properties copy() {
        return null;
    }

    public float getControl() {
        return control;
    }

    public void setControl(float controlSurface) {
        this.control = controlSurface;
    }
}
