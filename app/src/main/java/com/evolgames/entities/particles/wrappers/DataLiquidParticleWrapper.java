package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.properties.LiquidProperties;

import org.andengine.util.adt.color.Color;

public abstract class DataLiquidParticleWrapper extends LiquidParticleWrapper {
  public DataLiquidParticleWrapper(
      GameEntity gameEntity,
      float[] emitterData,
      float[] weights,
      Vector2 splashVelocity,
      LiquidProperties liquid,
      int lowerRate,
      int higherRate) {
    super(gameEntity, liquid, emitterData, weights, splashVelocity, lowerRate, higherRate);
  }

  @Override
  protected abstract DataEmitter createEmitter(float[] emitterData, float[] weights);
}
