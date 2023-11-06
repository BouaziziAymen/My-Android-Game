package com.evolgames.entities.serialization;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;

public class InitInfo {

    private Filter filter;
    private Vector2 linearVelocity;
    private Vector2 muzzleVelocity;
    private Vector2 point;
    private float recoil;
    private float angularVelocity;
    private float x;
    private float y;
    private float angle;
    private boolean isBullet;

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public Vector2 getMuzzleVelocity() {
        return muzzleVelocity;
    }

    public void setMuzzleVelocity(Vector2 muzzleVelocity) {
        this.muzzleVelocity = muzzleVelocity;
    }

    public Vector2 getPoint() {
        return point;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }

    public float getRecoil() {
        return recoil;
    }

    public void setRecoil(float recoil) {
        this.recoil = recoil;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public boolean isBullet() {
        return isBullet;
    }

    public void setBullet(boolean bullet) {
        isBullet = bullet;
    }
}
