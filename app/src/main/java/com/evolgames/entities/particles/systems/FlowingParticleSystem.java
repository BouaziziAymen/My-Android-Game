package com.evolgames.entities.particles.systems;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.particles.emitters.PolygonEmitter;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.math.MathUtils;

public abstract class FlowingParticleSystem extends BatchedSpriteParticleSystem {

    protected final PolygonEmitter relativePolygonEmitter;
    private final float originalRateMin;
    private final float originalRateMax;
    private float rateMin;
    private float rateMax;

    public FlowingParticleSystem(
            PolygonEmitter emitter,
            float rateMin,
            float rateMax,
            int particlesMax,
            TextureRegion particleTextureRegion) {
        super(
                emitter,
                rateMin,
                rateMax,
                particlesMax,
                particleTextureRegion,
                ResourceManager.getInstance().vbom);
        relativePolygonEmitter = emitter;
        this.rateMin = rateMin;
        this.rateMax = rateMax;
        originalRateMin = rateMin;
        originalRateMax = rateMax;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        float ratio = relativePolygonEmitter.getCoverageRatio();
        rateMin = originalRateMin * ratio;
        rateMax = originalRateMax * ratio;
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
