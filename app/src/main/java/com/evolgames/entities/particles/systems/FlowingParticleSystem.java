package com.evolgames.entities.particles.systems;

import com.evolgames.entities.particles.emitters.PolygonEmitter;
import com.evolgames.gameengine.ResourceManager;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.util.math.MathUtils;

public abstract class FlowingParticleSystem extends BatchedPseudoSpriteParticleSystem {

    protected final PolygonEmitter relativePolygonEmitter;
    private float rateMin;
    private float rateMax;
    private final float originalRateMin;
    private final float originalRateMax;

    public FlowingParticleSystem(IEntityFactory<Entity> ief, PolygonEmitter emitter, float rateMin, float rateMax, int particlesMax, TextureRegion particleTextureRegion) {
        super(ief, emitter, rateMin, rateMax, particlesMax, particleTextureRegion, ResourceManager.getInstance().vbom);
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
