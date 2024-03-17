package com.evolgames.userinterface.model;

import com.evolgames.entities.properties.Properties;

public class ImageShapeModel extends Properties {

    private float rotation;
    private float x;
    private float y;
    private float width;
    private float height;

    public ImageShapeModel() {

    }

    public ImageShapeModel(float rotation, float x, float y, float width, float height) {
        this.rotation = rotation;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
