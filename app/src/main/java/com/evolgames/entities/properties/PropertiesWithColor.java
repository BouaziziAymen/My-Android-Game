package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class PropertiesWithColor extends Properties {
    private Color color;

    public Color getDefaultColor() {
        return color;
    }

    public void setDefaultColor(Color defaultColor) {
        this.color = defaultColor;
    }

    @Override
    public Properties getCopy() {
        PropertiesWithColor copy = new PropertiesWithColor();
        copy.setDefaultColor(new Color(color));
        return copy;
    }

}
