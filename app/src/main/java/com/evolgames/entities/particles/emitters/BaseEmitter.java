package com.evolgames.entities.particles.emitters;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.util.adt.transformation.Transformation;

public abstract class BaseEmitter extends BaseParticleEmitter {

    final float[] data;
     BaseEmitter(float pCenterX, float pCenterY, float[] data) {
        super(pCenterX, pCenterY);
        this.data = data;
    }
    protected abstract void prepareData();
    public void update(Transformation parentTransformation) {
        prepareData();
        parentTransformation.transform(data);
    }
}
