package com.evolgames.entities.properties;


import com.badlogic.gdx.math.Vector2;
import com.evolgames.userinterface.model.toolmodels.ProjectileTriggerType;

public class ProjectileProperties extends Properties {

    private Vector2 projectileOrigin;
    private Vector2 projectileDirection;
    private float muzzleVelocity = 200f;
    private float recoil = 0.3f;
    private float upperLimit;
    private int fireSound = 1;

    public ProjectileProperties(Vector2 begin, Vector2 direction) {
        this.projectileOrigin = begin.cpy();
        this.projectileDirection = direction.cpy();
    }

    public ProjectileProperties() {
    }

    @Override
    public Properties copy() {
        ProjectileProperties properties =new ProjectileProperties(projectileOrigin.cpy(),projectileDirection.cpy());
        properties.setFireSound(fireSound);
        properties.setUpperLimit(upperLimit);
        properties.setRecoil(recoil);
        properties.setMuzzleVelocity(muzzleVelocity);
        properties.setProjectileDirection(projectileDirection.cpy());
        properties.setProjectileOrigin(projectileOrigin.cpy());
        return properties;
    }

    public Vector2 getProjectileOrigin() {
        return projectileOrigin;
    }

    public void setProjectileOrigin(Vector2 projectileOrigin) {
        this.projectileOrigin = projectileOrigin;
    }

    public Vector2 getProjectileDirection() {
        return projectileDirection;
    }

    public void setProjectileDirection(Vector2 projectileDirection) {
        this.projectileDirection = projectileDirection;
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

    public float getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(float upperLimit) {
        this.upperLimit = upperLimit;
    }

    public int getFireSound() {
        return fireSound;
    }

    public void setFireSound(int fireSound) {
        this.fireSound = fireSound;
    }
}
