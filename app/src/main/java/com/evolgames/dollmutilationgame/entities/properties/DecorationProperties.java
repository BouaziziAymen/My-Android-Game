package com.evolgames.dollmutilationgame.entities.properties;

import androidx.annotation.NonNull;

import org.andengine.util.adt.color.Color;

public class DecorationProperties extends ColoredProperties {
    public DecorationProperties(Color color) {
        setDefaultColor(color);
    }

    @SuppressWarnings("unused")
    public DecorationProperties() {
    }

    @NonNull
    @Override
    public DecorationProperties clone() {
        DecorationProperties clone = (DecorationProperties) super.clone();
        clone.setDefaultColor(new Color(getDefaultColor()));
        return clone;
    }

}
