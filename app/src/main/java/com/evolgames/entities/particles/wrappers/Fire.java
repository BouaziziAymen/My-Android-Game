package com.evolgames.entities.particles.wrappers;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;

public interface Fire {
    ParticleSystem<Entity> getFireParticleSystem();
    double getParticleTemperature(Particle<?> particle);
}
