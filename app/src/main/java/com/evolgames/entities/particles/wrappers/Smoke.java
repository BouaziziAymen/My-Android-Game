package com.evolgames.entities.particles.wrappers;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.sprite.UncoloredSprite;

public interface Smoke extends ParticleEffect{
    ParticleSystem<UncoloredSprite> getSmokeParticleSystem();
    int getSmokeIndex();

}
