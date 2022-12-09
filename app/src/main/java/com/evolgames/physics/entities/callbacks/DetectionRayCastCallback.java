package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;

import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class DetectionRayCastCallback implements RayCastCallback {
    private GameEntity entity;
    private LayerBlock layerBlock;
    private Vector2 intersectionPoint;
    private float minFraction;

    public Vector2 getIntersectionPoint() {
        return intersectionPoint;
    }
public void reset(){
        intersectionPoint = null;
    layerBlock = null;
        minFraction = Float.MAX_VALUE;
}
    public void setEntity(GameEntity entity) {
        this.entity = entity;
    }

    public LayerBlock getLayerBlock() {
        return layerBlock;
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        GameEntity candidate = (GameEntity) fixture.getBody().getUserData();
        if (candidate == entity) {
            if(fraction< minFraction) {
                layerBlock = (LayerBlock) fixture.getUserData();
                intersectionPoint = Vector2Pool.obtain(point);
                minFraction = fraction;
            }
        }
        return 1;
    }

    public float getFraction() {
        return minFraction;
    }
}
