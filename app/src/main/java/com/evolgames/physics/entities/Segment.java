package com.evolgames.physics.entities;

import com.badlogic.gdx.math.Vector2;

public class Segment {
    private final Vector2 point1;
    private final Vector2 point2;

    public Segment(Vector2 p1, Vector2 p2) {
        point1 = p1;
        point2 = p2;
    }

    public Vector2 getPoint1() {
        return point1;
    }

    public Vector2 getPoint2() {
        return point2;
    }

    public String toString() {
        return "[" + point1 + "," + point2 + "]";
    }
}
