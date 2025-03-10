package com.evolgames.dollmutilationgame.entities.particles.wrappers;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.sprite.UncoloredSprite;

public interface Fire {

    ParticleSystem<UncoloredSprite> getFireParticleSystem();

    double getParticleTemperature(Particle<?> particle);

}
