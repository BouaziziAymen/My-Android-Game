package com.evolgames.entities.particles.systems;

import com.evolgames.entities.particles.emitters.PolygonEmitter;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

public class FlameParticleSystem extends BatchedPseudoSpriteParticleSystem {

    private PolygonEmitter polygonEmitter;
    private float rateMin;
    private float rateMax;
    private float originalRateMin;
    private float originalRateMax;

    public FlameParticleSystem(IEntityFactory<Entity> ief, PolygonEmitter emitter, float rateMin, float rateMax, int particlesMax, TextureRegion plasmaParticle4, VertexBufferObjectManager vbom) {
        super(ief, emitter, rateMin, rateMax, particlesMax, plasmaParticle4, vbom);
        polygonEmitter = emitter;
        this.rateMin = rateMin;
        this.rateMax = rateMax;
        originalRateMin = rateMin;
        originalRateMax = rateMax;

    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        float ratio = polygonEmitter.getCoverageRatio();
        rateMin = originalRateMin * ratio;
        rateMax = originalRateMax * ratio;
    }

    @Override
    protected void onParticleSpawned(Particle<Entity> particle) {
        super.onParticleSpawned(particle);
        particle.getEntity().setUserData(polygonEmitter.getTemperature());
        particle.setExpired(false);
    }

    @Override
    protected float determineCurrentRate() {
        if (this.rateMin == this.rateMax) {
            return this.rateMin;
        } else {
            return MathUtils.random(this.rateMin, this.rateMax);
        }
    }
}
