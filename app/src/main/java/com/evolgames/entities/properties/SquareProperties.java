package com.evolgames.entities.properties;

import org.andengine.util.adt.color.Color;

public class SquareProperties {
    private final Color color;
    int squareId;

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
