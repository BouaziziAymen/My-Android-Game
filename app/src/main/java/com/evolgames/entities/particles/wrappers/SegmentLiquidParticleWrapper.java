package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.emitters.SegmentEmitter;
import com.evolgames.entities.properties.LiquidProperties;

import org.andengine.util.adt.color.Color;

public class SegmentLiquidParticleWrapper extends DataLiquidParticleWrapper {
  public SegmentLiquidParticleWrapper(
      GameEntity gameEntity,
      float[] emitterData,
      Vector2 splashVelocity,
      LiquidProperties liquid,
      int lowerRate,
      int higherRate) {
    super(gameEntity, emitterData, null, splashVelocity, liquid, lowerRate, higherRate);
  }

  @Override
  protected DataEmitter createEmitter(float[] emitterData, float[] weights) {
    return new SegmentEmitter(emitterData);
  }
}
