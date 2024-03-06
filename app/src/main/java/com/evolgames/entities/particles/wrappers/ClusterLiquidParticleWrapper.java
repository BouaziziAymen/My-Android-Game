package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.ClusterEmitter;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.entities.properties.LiquidProperties;

public class ClusterLiquidParticleWrapper extends DataLiquidParticleWrapper {
  public ClusterLiquidParticleWrapper(
      GameEntity gameEntity,
      LiquidProperties liquid,
      float[] data,
      float[] weights,
      Vector2 splashVelocity,
      int lowerRate,
      int higherRate) {
    super(gameEntity, data, weights, splashVelocity, liquid, lowerRate, higherRate);
  }

  @Override
  protected DataEmitter createEmitter(float[] emitterData, float[] weights) {
    Vector2 center = GeometryUtils.calculateCentroid(emitterData);
    return new ClusterEmitter(center.x, center.y, emitterData, weights);
  }
}
