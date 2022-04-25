package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class DecorationProperties extends BlockProperties {
public DecorationProperties(Color color){
    setDefaultColor(color);
}

    @Override
    public BlockProperties getCopy() {
        return new DecorationProperties(getDefaultColor());
    }
}
