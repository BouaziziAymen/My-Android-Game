package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;

public class SlashProperties extends Properties {
    private float speed;
    @Override
    public Properties copy() {
        return null;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
