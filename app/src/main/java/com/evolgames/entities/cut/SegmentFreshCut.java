package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.physics.PhysicsConstants;

public class SegmentFreshCut extends FreshCut {
    public Vector2 first;
    public Vector2 second;
    private final boolean isInner;

    public SegmentFreshCut(Vector2 first, Vector2 second, boolean isInner, float density) {
        super(first.dst(second), (int) (first.dst(second)* PhysicsConstants.BLEEDING_CONSTANT*density),null);
        this.first = first;
        this.second = second;
        this.isInner = isInner;
    }
    public SegmentFreshCut(Vector2 first, Vector2 second, boolean isInner, float density, Vector2 splashVelocity) {
        super(first.dst(second), (int) (first.dst(second)* PhysicsConstants.BLEEDING_CONSTANT*density),splashVelocity);
        this.first = first;
        this.second = second;
        this.isInner = isInner;
    }

    public SegmentFreshCut(Vector2 first, Vector2 second,int limit, boolean isInner) {
        super(first.dst(second),limit, null);
        this.first = first;
        this.second = second;
        this.isInner = isInner;
    }

    @Override
    public String toString() {
        return "SegmentFreshCut{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    public boolean isInner() {
        return isInner;
    }
}
