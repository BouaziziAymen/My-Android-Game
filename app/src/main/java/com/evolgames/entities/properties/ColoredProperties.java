package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public abstract class ColoredProperties extends Properties {
    private Color defaultColor;
    public abstract ColoredProperties copy();

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }
}
