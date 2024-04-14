package com.evolgames.entities.properties;

import androidx.annotation.NonNull;

import com.badlogic.gdx.math.Vector2;

public class ProjectileProperties extends Properties {

    private Vector2 projectileOrigin;
    private Vector2 projectileEnd;
    private float muzzleVelocity = 0.5f;
    private float recoil = 0.3f;
    private String fireSound;
    private Explosive explosive = Explosive.OTHER;
    private float smokeRatio;
    private float fireRatio;
    private float sparkRatio;
    private float particles;
    private String missileFile = "";

    private boolean assetsMissile;

    public ProjectileProperties(Vector2 begin, Vector2 end) {
        this.projectileOrigin = begin.cpy();
        this.projectileEnd = end.cpy();
    }

    @SuppressWarnings("unused")
    public ProjectileProperties() {
    }

    @NonNull
    @Override
    public Object clone() {
        ProjectileProperties projectileProperties = (ProjectileProperties) super.clone();
        projectileProperties.setProjectileOrigin(projectileOrigin.cpy());
        projectileProperties.setProjectileEnd(projectileEnd.cpy());
        return projectileProperties;
    }

    public Vector2 getProjectileOrigin() {
        return projectileOrigin;
    }

    public void setProjectileOrigin(Vector2 projectileOrigin) {
        this.projectileOrigin = projectileOrigin;
    }

    public Vector2 getProjectileEnd() {
        return projectileEnd;
    }

    public void setProjectileEnd(Vector2 projectileEnd) {
        this.projectileEnd = projectileEnd;
    }

    public float getMuzzleVelocity() {
        return muzzleVelocity;
    }

    public void setMuzzleVelocity(float muzzleVelocity) {
        this.muzzleVelocity = muzzleVelocity;
    }

    public float getRecoil() {
        return recoil;
    }

    public void setRecoil(float recoil) {
        this.recoil = recoil;
    }

    public String getFireSound() {
        return fireSound;
    }

    public void setFireSound(String fireSound) {
        this.fireSound = fireSound;
    }

    public Explosive getExplosive() {
        return explosive;
    }

    public void setExplosive(Explosive explosive) {
        this.explosive = explosive;
    }

    public float getSmokeRatio() {
        return smokeRatio;
    }

    public void setSmokeRatio(float smokeRatio) {
        this.smokeRatio = smokeRatio;
    }

    public float getFireRatio() {
        return fireRatio;
    }

    public void setFireRatio(float fireRatio) {
        this.fireRatio = fireRatio;
    }

    public float getSparkRatio() {
        return sparkRatio;
    }

    public void setSparkRatio(float sparkRatio) {
        this.sparkRatio = sparkRatio;
    }

    public float getParticles() {
        return particles;
    }

    public void setParticles(float particles) {
        this.particles = particles;
    }

    public String getMissileFile() {
        return missileFile;
    }

    public void setMissileFile(String missileFile) {
        this.missileFile = missileFile;
    }

    public boolean isAssetsMissile() {
        return assetsMissile;
    }

    public void setAssetsMissile(boolean assetsMissile) {
        this.assetsMissile = assetsMissile;
    }
}
