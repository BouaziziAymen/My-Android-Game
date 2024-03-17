package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class SquareProperties {
    int squareId;
    private final Color color;

    public SquareProperties(int id, Color color) {
        this.squareId = id;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getSquareId() {
        return squareId;
    }
}
