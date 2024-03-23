package com.evolgames.entities.properties.usage;

import com.badlogic.gdx.math.Vector2;

public class BowProperties extends RangedProperties{
    private Vector2 upper;
    private Vector2 middle;
    private Vector2 lower;

    public Vector2 getUpper() {
        return upper;
    }

    public void setUpper(Vector2 upper) {
        this.upper = upper;
    }

    public Vector2 getMiddle() {
        return middle;
    }

    public void setMiddle(Vector2 middle) {
        this.middle = middle;
    }

    public Vector2 getLower() {
        return lower;
    }

    public void setLower(Vector2 lower) {
        this.lower = lower;
    }
}
