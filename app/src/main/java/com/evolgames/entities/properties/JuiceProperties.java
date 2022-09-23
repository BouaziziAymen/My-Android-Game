package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class JuiceProperties extends Properties {

    private final String JuiceName;
    private final Color defaultColor;
    public JuiceProperties(String name,Color color){
        this.defaultColor = color;
        JuiceName = name;
    }

    @Override
    public Properties copy() {
        return null;
    }

    public String getJuiceName() {
        return JuiceName;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }
}
