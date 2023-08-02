package com.evolgames.entities.properties.usage;
import com.evolgames.entities.properties.Properties;

public class ManualProperties extends RangedProperties {
    private float reloadTime;

    @Override
    public Properties copy() {
        return null;
    }

    public float getReloadTime() {
        return reloadTime;
    }
    public void setReloadTime(float reloadTime) {
        this.reloadTime = reloadTime;
    }

}
