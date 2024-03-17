package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.evolgames.entities.blocks.LayerBlock;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class BlockIntersectionCallback implements RayCastCallback {
    private Vector2 intersectionPoint;
    private float minFraction;
    private LayerBlock layerBlock;

    public Vector2 getIntersectionPoint() {
        return intersectionPoint;
    }

    public void reset(LayerBlock layerBlock) {
        this.intersectionPoint = null;
        this.minFraction = Float.MAX_VALUE;
        this.layerBlock = layerBlock;
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        LayerBlock layerBlock = (LayerBlock) fixture.getUserData();
        if (layerBlock.equals(this.layerBlock))
            if (fraction < minFraction) {
                intersectionPoint = Vector2Pool.obtain(point);
                minFraction = fraction;
            }
        return 1;
    }

    public float getFraction() {
        return minFraction;
    }
}
