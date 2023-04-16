package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.physics.PhysicsConstants;

public class SegmentFreshCut extends FreshCut {
    public Vector2 first;
    public Vector2 second;

    public SegmentFreshCut(Vector2 first, Vector2 second, LayerBlock block) {
        this.first = first;
        this.second = second;
        this.limit = computeLimit(block);
    }



    public SegmentFreshCut(Vector2 first, Vector2 second, float limit) {
        this.first = first;
        this.second = second;
        this.limit = limit;
    }
    @Override
    public float getLength() {
        return first.dst(second);
    }
}
