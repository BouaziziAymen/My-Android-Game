package com.evolgames.entities.serialization.infos;

import android.support.annotation.Nullable;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;

public class FireSourceInfo {
    private Vector2 fireSourceOrigin;
    private Vector2 fireDirection;
    private transient GameEntity muzzleEntity;
    private String muzzleEntityUniqueId;
    private float fireRatio = 0.5f;
    private float smokeRatio = 0.2f;
    private float sparkRatio = 0.1f;
    private float speed = 0.2f;
    private float heat = 0.2f;
    private float particles = 0.5f;
    private float inFirePartSize = 1f;
    private float finFirePartSize = 0f;
    private float extent;

    public GameEntity getMuzzleEntity() {
        return muzzleEntity;
    }

    public String getMuzzleEntityUniqueId() {
        return muzzleEntityUniqueId;
    }

    public void setMuzzleEntity(@Nullable GameEntity muzzleEntity) {
        this.muzzleEntity = muzzleEntity;
        if (this.muzzleEntity != null) {
            this.muzzleEntityUniqueId = muzzleEntity.getUniqueID();
        }
    }

    public Vector2 getFireSourceOrigin() {
        return fireSourceOrigin;
    }

    public void setFireSourceOrigin(Vector2 fireSourceOrigin) {
        this.fireSourceOrigin = fireSourceOrigin;
    }

    public Vector2 getFireDirection() {
        return fireDirection;
    }

    public void setFireDirection(Vector2 fireDirection) {
        this.fireDirection = fireDirection;
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
        return particles+0.2f;
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
        return extent+1f;
    }

    public void setExtent(float extent) {
        this.extent = extent;
    }
}
