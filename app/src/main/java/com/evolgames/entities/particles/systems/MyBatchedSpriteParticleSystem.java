package com.evolgames.entities.particles.systems;

import com.evolgames.entities.particles.LiquidParticleWrapper;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class MyBatchedSpriteParticleSystem extends BatchedPseudoSpriteParticleSystem {
    private LiquidParticleWrapper liquidParticleWrapper;

    public MyBatchedSpriteParticleSystem(IEntityFactory<Entity> factory, LiquidParticleWrapper liquidParticleWrapper, IParticleEmitter pParticleEmitter, float pRateMinimum, float pRateMaximum, int pParticlesMaximum, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(factory,pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum, pTextureRegion, pVertexBufferObjectManager);
    this.liquidParticleWrapper = liquidParticleWrapper;
    }


    @Override
    protected void onParticleSpawned(Particle<Entity> particle) {
        super.onParticleSpawned(particle);
        liquidParticleWrapper.performSpawnAction();
    }
}
