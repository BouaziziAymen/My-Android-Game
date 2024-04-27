package com.evolgames.dollmutilationgame.physics.entities.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.blockvisitors.ImpactData;
import com.evolgames.dollmutilationgame.entities.pools.ImpactDataPool;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class FluxInnerRayCastCallback implements RayCastCallback {
    private float minFraction;
    private GameEntity gameEntity;
    private LayerBlock layerBlock;
    private Vector2 intersectionPoint;
    private boolean found = false;
    private GameEntity restricted;

    public void reset() {
        found = false;
        gameEntity = null;
        layerBlock = null;
        intersectionPoint = null;
        minFraction = Float.MAX_VALUE;
        restricted = null;
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        GameEntity candidate = (GameEntity) fixture.getBody().getUserData();
        if (candidate == restricted) {
            if (fraction < minFraction) {
                intersectionPoint = Vector2Pool.obtain(point);
                minFraction = fraction;
                gameEntity = candidate;
                layerBlock = (LayerBlock) fixture.getUserData();
                found = true;
            }
        }
        return 1;
    }

    public ImpactData getImpactData() {
        if (!found) {
            return null;
        }
        return ImpactDataPool.obtain(gameEntity, layerBlock, intersectionPoint);
    }

    public void setRestricted(GameEntity restricted) {
        this.restricted = restricted;
    }
}
