package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;

public abstract class FreshCut {
    protected final Vector2 splashVelocity;
    private final float length;
    private int limit;

    public FreshCut(float length, int limit, Vector2 velocity) {
        this.length = length;
        this.limit = limit;
        this.splashVelocity = velocity;
    }

    public float getLength() {
        return length;
    }

    public void decrementLimit(){
        limit--;
    }

    public int getLimit() {
        return limit;
    }

    public Vector2 getSplashVelocity() {
        return splashVelocity;
    }
}
