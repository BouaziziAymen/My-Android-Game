package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.evolgames.entities.GameEntity;
import java.util.HashSet;
import org.andengine.extension.physics.box2d.util.Vector2Pool;

public class SimpleDetectionRayCastCallback implements RayCastCallback {
  private final HashSet<GameEntity> exceptedGroup = new HashSet<>();
  private Vector2 intersectionPoint;
  private float minFraction;

  public Vector2 getIntersectionPoint() {
    return intersectionPoint;
  }

  public void addExcepted(GameEntity gameEntity) {
    if (gameEntity == null) return;
    exceptedGroup.add(gameEntity);
  }

  public void reset() {
    intersectionPoint = null;
    minFraction = Float.MAX_VALUE;
  }

  @Override
  public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
    GameEntity candidate = (GameEntity) fixture.getBody().getUserData();
    if (!exceptedGroup.contains(candidate))
      if (fraction < minFraction) {
        intersectionPoint = Vector2Pool.obtain(point);
        minFraction = fraction;
      }
    return 1;
  }

  public float getFraction() {
    return minFraction;
  }

  public void resetExcepted() {
    exceptedGroup.clear();
  }
}
