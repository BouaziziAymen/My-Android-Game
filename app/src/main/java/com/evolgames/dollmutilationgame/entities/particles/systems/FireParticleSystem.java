package com.evolgames.dollmutilationgame.entities.particles.systems;

import com.evolgames.dollmutilationgame.entities.particles.emitters.PolygonEmitter;
import com.evolgames.dollmutilationgame.entities.blocks.CoatingBlock;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.opengl.texture.region.TextureRegion;

public class FireParticleSystem extends FlowingParticleSystem {
    public FireParticleSystem(
            PolygonEmitter emitter,
            float rateMin,
            float rateMax,
            int particlesMax,
            TextureRegion textureRegion) {
        super(emitter, rateMin, rateMax, particlesMax, textureRegion);
    }

    @Override
    protected void onParticleSpawned(Particle<UncoloredSprite> particle) {
        super.onParticleSpawned(particle);
        CoatingBlock coatingBlock = relativePolygonEmitter.getActiveCoatingBlock();
        particle.getEntity().setUserData(coatingBlock);
    }
}
