package com.evolgames.entities.serialization.infos;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;

public class BombInfo {
    private Vector2 bombPosition;
    private float fireRatio;
    private float smokeRatio;
    private float sparkRatio;
    private float force;
    private float heat;
    private float particles;
    private float speed;
    private GameEntity gameEntity;

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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

}
