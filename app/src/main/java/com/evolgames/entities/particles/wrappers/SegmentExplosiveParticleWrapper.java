package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.emitters.SegmentEmitter;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;

import org.andengine.entity.particle.Particle;

public class SegmentExplosiveParticleWrapper extends ExplosiveParticleWrapper {
    protected final float[] data;
    public SegmentExplosiveParticleWrapper(GameEntity gameEntity, float[] data, Vector2 power, float fireRatio, float smokeRatio, float sparkRatio, float intensity) {
        super(gameEntity, power, fireRatio, smokeRatio,sparkRatio,intensity, data);
        this.data = data;
    }

    @Override
    protected float sourceLength(float[] data) {
        float x = (data[2]-data[0]);
        float y = (data[3]-data[1]);
        return (float) Math.sqrt(x*x+y*y);
    }

    @Override
    protected DataEmitter createEmitter(float[] data) {
        return new SegmentEmitter(data);
    }

    @Override
    public double getParticleTemperature(Particle<?> particle) {
        return 2000;
    }
}
