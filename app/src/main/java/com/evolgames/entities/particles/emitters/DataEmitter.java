package com.evolgames.entities.particles.emitters;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.util.adt.transformation.Transformation;

public abstract class DataEmitter extends BaseParticleEmitter {

  protected final float[] data;

  DataEmitter(float pCenterX, float pCenterY, float[] data) {
    super(pCenterX, pCenterY);
    this.data = data;
  }

  protected abstract void prepareData();

  public void onStep(Transformation parentTransformation) {
    prepareData();
    parentTransformation.transform(data);
  }
}
