package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

public class DragProperties extends Properties {

    private Vector2 dragOrigin;
    private Vector2 dragNormal;
    private float extent1;
    private float extent2;
    private float magnitude = 0.5f;
    private boolean symmetrical;

    @SuppressWarnings("unused")
    public DragProperties() {}

    public DragProperties(Vector2 begin, Vector2 normal) {
        this.dragOrigin = begin.cpy();
        this.dragNormal = normal.cpy();
    }
    @Override
    public Properties copy() {
        return null;
    }

    public Vector2 getDragOrigin() {
        return dragOrigin;
    }

    public void setDragOrigin(Vector2 dragOrigin) {
        this.dragOrigin = dragOrigin;
    }

    public Vector2 getDragNormal() {
        return dragNormal;
    }

    public void setDragNormal(Vector2 dragNormal) {
        this.dragNormal = dragNormal;
    }

    public float getExtent1() {
        return extent1;
    }

    public void setExtent1(float extent1) {
        this.extent1 = extent1;
    }

    public void setExtent2(float extent2) {
        this.extent2 = extent2;
    }

    public float getExtent2() {
        return extent2;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setSymmetrical(boolean symmetrical) {
        this.symmetrical = symmetrical;
    }

    public boolean isSymmetrical() {
        return symmetrical;
    }
}
