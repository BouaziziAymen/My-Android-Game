package com.evolgames.dollmutilationgame.entities.properties;

import com.badlogic.gdx.math.Vector2;

public class FireSourceProperties extends Properties {

    private Vector2 fireSourceOrigin;
    private Vector2 fireSourceDirection;
    private float fireRatio = 0.5f;
    private float smokeRatio = 0.2f;
    private float sparkRatio = 0.1f;
    private float speedRatio = 0.2f;
    private float heatRatio = 0.2f;
    private float particles = 0.5f;
    private float inFirePartSize = 0.5f;
    private float finFirePartSize = 0f;
    private float extent = 0.05f;

    @SuppressWarnings("unused")
    public FireSourceProperties() {
    }

    public FireSourceProperties(Vector2 begin, Vector2 direction) {
        this.fireSourceOrigin = begin.cpy();
        this.fireSourceDirection = direction.cpy();
    }

    public Vector2 getFireSourceOrigin() {
        return fireSourceOrigin;
    }

    public void setFireSourceOrigin(Vector2 fireSourceOrigin) {
        this.fireSourceOrigin = fireSourceOrigin;
    }

    public Vector2 getFireSourceDirection() {
        return fireSourceDirection;
    }

    public void setFireSourceDirection(Vector2 fireSourceDirection) {
        this.fireSourceDirection = fireSourceDirection;
    }

    public float getFireRatio() {
        return fireRatio;
    }

    public void setFireRatio(float fireRatio) {
        this.fireRatio = fireRatio;
    }

    public float getSmokeRatio() {
        return smokeRatio;
    }

    public void setSmokeRatio(float smokeRatio) {
        this.smokeRatio = smokeRatio;
    }

    public float getSparkRatio() {
        return sparkRatio;
    }

    public void setSparkRatio(float sparkRatio) {
        this.sparkRatio = sparkRatio;
    }

    public float getSpeedRatio() {
        return speedRatio;
    }

    public void setSpeedRatio(float speed) {
        this.speedRatio = speed;
    }

    public float getHeatRatio() {
        return heatRatio;
    }

    public void setHeatRatio(float heatRatio) {
        this.heatRatio = heatRatio;
    }

    public float getParticles() {
        return particles;
    }

    public void setParticles(float particles) {
        this.particles = particles;
    }

    public float getInFirePartSize() {
        return inFirePartSize;
    }

    public void setInFirePartSize(float inFirePartSize) {
        this.inFirePartSize = inFirePartSize;
    }

    public float getFinFirePartSize() {
        return finFirePartSize;
    }

    public void setFinFirePartSize(float finFirePartSize) {
        this.finFirePartSize = finFirePartSize;
    }

    public float getExtent() {
        return extent;
    }

    public void setExtent(float extent) {
        this.extent = extent;
    }
}
