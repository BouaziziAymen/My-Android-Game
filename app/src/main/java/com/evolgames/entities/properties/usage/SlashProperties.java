package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;

public class SlashProperties extends Properties {
    private float force;
    @Override
    public Properties copy() {
        return null;
    }

    public float getForce() {
        return force;
    }

    public void setForce(float force) {
        this.force = force;
    }
}
