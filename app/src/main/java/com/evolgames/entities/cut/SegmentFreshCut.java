package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.particles.wrappers.LiquidParticleWrapper;

public class SegmentFreshCut extends FreshCut {
    public Vector2 first;
    public Vector2 second;

    public SegmentFreshCut(Vector2 first, Vector2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public float getLength() {
        return first.dst(second);
    }
}
