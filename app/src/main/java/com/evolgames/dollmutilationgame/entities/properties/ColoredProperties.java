package com.evolgames.dollmutilationgame.entities.properties;

import org.andengine.util.adt.color.Color;

public abstract class ColoredProperties extends Properties {
    Color defaultColor;
    private int colorSquareId = -1;

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public int getColorSquareId() {
        return colorSquareId;
    }

    public void setColorSquareId(int colorSquareId) {
        this.colorSquareId = colorSquareId;
    }

    @Override
    public ColoredProperties clone() {
        ColoredProperties cloned = (ColoredProperties) super.clone();
        if(cloned.defaultColor!=null) {
            cloned.defaultColor = new Color(this.defaultColor);
        }
        return cloned;
    }
}
