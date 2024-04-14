package com.evolgames.entities.properties;

import com.badlogic.gdx.math.Vector2;

public class BombProperties extends Properties {
    private Vector2 bombPosition;
    private float fireRatio = 0.5f;
    private float smokeRatio = 0.2f;
    private float sparkRatio = 0.1f;
    private float force = 0.2f;
    private float speed = 0.2f;
    private float heat = 0.2f;
    private float particles = 0.5f;

    public BombProperties() {
    }

    public Vector2 getBombPosition() {
        return bombPosition;
    }

    public void setBombPosition(Vector2 bombPosition) {
        this.bombPosition = bombPosition;
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

    public float getForce() {
        return force;
    }

    public void setForce(float force) {
        this.force = force;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHeat() {
        return heat;
    }

    public void setHeat(float heat) {
        this.heat = heat;
    }

    public float getParticles() {
        return particles;
    }

    public void setParticles(float particles) {
        this.particles = particles;
    }

}
