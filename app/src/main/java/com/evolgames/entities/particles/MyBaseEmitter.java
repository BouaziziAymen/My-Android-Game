package com.evolgames.entities.particles;

import org.andengine.entity.particle.emitter.BaseParticleEmitter;
import org.andengine.util.adt.transformation.Transformation;

abstract class MyBaseEmitter extends BaseParticleEmitter {
     MyBaseEmitter(float pCenterX, float pCenterY) {
        super(pCenterX, pCenterY);
    }
    abstract void update(Transformation parentTransformation);
}
