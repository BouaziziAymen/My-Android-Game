package com.evolgames.dollmutilationgame.entities.cut;

import com.badlogic.gdx.math.Vector2;

public abstract class FreshCut {
    protected Vector2 splashVelocity;
    private float length;
    private int limit;
    private boolean frozen;

    public FreshCut() {
    }

    public FreshCut(float length, int limit, Vector2 velocity) {
        this.length = length;
        this.limit = limit;
        this.splashVelocity = velocity;
    }

    public float getLength() {
        return length;
    }

    public void decrementLimit() {
        limit--;
    }

    public int getLimit() {
        return limit;
    }

    public Vector2 getSplashVelocity() {
        return splashVelocity;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}
