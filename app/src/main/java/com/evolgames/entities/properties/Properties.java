package com.evolgames.entities.properties;

import java.util.Arrays;

public class Properties {
    protected float[] properties;

    public void setProperties(float[] properties) {
        this.properties = properties;
    }

    public float[] getProperties() {
        return properties;
    }

    public Properties getCopy() {
        Properties props = new Properties();
        props.setProperties(Arrays.copyOf(properties,properties.length));
        return props;
    }
}
