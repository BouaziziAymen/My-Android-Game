package com.evolgames.entities.properties;


import com.badlogic.gdx.math.Vector2;

public class ProjectileProperties extends Properties {

    private Vector2 projectileOrigin;
    private Vector2 projectileEnd;
    private float muzzleVelocity = 200f;
    private float recoil = 0.3f;
    private int fireSound = 1;
    private Explosive explosive = Explosive.NONE;
    private float smokeRatio;
    private float fireRatio;
    private float sparkRatio;
    private float fireIntensity;

    public ProjectileProperties(Vector2 begin, Vector2 end) {
        this.projectileOrigin = begin.cpy();
        this.projectileEnd = end.cpy();
    }

    public ProjectileProperties() {
    }

    @Override
    public Properties copy() {
        ProjectileProperties properties = new ProjectileProperties(projectileOrigin.cpy(), projectileEnd.cpy());
        properties.setFireSound(fireSound);
        properties.setRecoil(recoil);
        properties.setMuzzleVelocity(muzzleVelocity);
        properties.setProjectileEnd(projectileEnd.cpy());
        properties.setProjectileOrigin(projectileOrigin.cpy());
        return properties;
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

    public int getFireSound() {
        return fireSound;
    }

    public void setFireSound(int fireSound) {
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

    public float getFireIntensity() {
        return fireIntensity;
    }

    public void setFireIntensity(float fireIntensity) {
        this.fireIntensity = fireIntensity;
    }
}
