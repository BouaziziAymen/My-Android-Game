package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

public class CasingProperties extends Properties {

    private Vector2 ammoOrigin;
    private Vector2 ammoDirection;
    private float rotationSpeed;
    private boolean rotationOrientation;
    private float linearSpeed;

    public CasingProperties() {
    }

    public CasingProperties(Vector2 begin, Vector2 direction) {
        this.ammoOrigin = begin.cpy();
        this.ammoDirection = direction.cpy();
    }

    @Override
    public Properties copy() {
        return null;
    }

    public Vector2 getAmmoOrigin() {
        return ammoOrigin;
    }

    public Vector2 getAmmoDirection() {
        return ammoDirection;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public boolean isRotationOrientation() {
        return rotationOrientation;
    }

    public void setRotationOrientation(boolean rotationOrientation) {
        this.rotationOrientation = rotationOrientation;
    }

    public float getLinearSpeed() {
        return linearSpeed;
    }

    public void setLinearSpeed(float linearSpeed) {
        this.linearSpeed = linearSpeed;
    }

    public void setAmmoOrigin(Vector2 ammoOrigin) {
        this.ammoOrigin = ammoOrigin;
    }

    public void setAmmoDirection(Vector2 ammoDirection) {
        this.ammoDirection = ammoDirection;
    }
}
