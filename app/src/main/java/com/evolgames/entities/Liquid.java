package com.evolgames.entities;

import com.evolgames.entities.properties.JuiceProperties;

public class Liquid {
    public Liquid(JuiceProperties properties) {
        this.properties = properties;
    }

    public JuiceProperties getProperties() {
        return properties;
    }

    private JuiceProperties properties;

}
