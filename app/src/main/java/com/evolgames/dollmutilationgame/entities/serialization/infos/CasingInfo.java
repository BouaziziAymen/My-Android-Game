package com.evolgames.dollmutilationgame.entities.serialization.infos;

import com.badlogic.gdx.math.Vector2;

public class CasingInfo {

    private Vector2 ammoOrigin;
    private Vector2 ammoDirection;
    private boolean rotationOrientation;
    private float rotationSpeed;
    private float linearSpeed;

    public Vector2 getAmmoOrigin() {
        return ammoOrigin;
    }

    public void setAmmoOrigin(Vector2 ammoOrigin) {
        this.ammoOrigin = ammoOrigin;
    }

    public Vector2 getAmmoDirection() {
        return ammoDirection;
    }

    public void setAmmoDirection(Vector2 ammoDirection) {
        this.ammoDirection = ammoDirection;
    }

    public boolean isRotationOrientation() {
        return rotationOrientation;
    }

    public void setRotationOrientation(boolean rotationOrientation) {
        this.rotationOrientation = rotationOrientation;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getLinearSpeed() {
        return linearSpeed;
    }

    public void setLinearSpeed(float linearSpeed) {
        this.linearSpeed = linearSpeed;
    }
}
