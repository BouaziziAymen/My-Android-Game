package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.blockvisitors.ImpactData;
import com.evolgames.entities.pools.ImpactDataPool;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class FluxRayCastCallback implements RayCastCallback {
  private float minFraction;
  private GameEntity gameEntity;
  private LayerBlock layerBlock;
  private Vector2 intersectionPoint;
  private boolean found = false;
  private GameEntity excepted;

  public void reset() {
    found = false;
    gameEntity = null;
    layerBlock = null;
    intersectionPoint = null;
    minFraction = Float.MAX_VALUE;
    excepted = null;
  }

  @Override
  public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
    GameEntity candidate = (GameEntity) fixture.getBody().getUserData();
    if (candidate != excepted && candidate.getBody().getType() == BodyDef.BodyType.DynamicBody) {
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

  public void setExcepted(GameEntity excepted) {
    this.excepted = excepted;
  }
}
