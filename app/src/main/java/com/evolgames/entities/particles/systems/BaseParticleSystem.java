package com.evolgames.entities.particles.systems;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.emitter.IParticleEmitter;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class BaseParticleSystem extends BatchedSpriteParticleSystem {
    private SpawnAction spawnAction;

    public BaseParticleSystem(IParticleEmitter pParticleEmitter, float pRateMinimum, float pRateMaximum, int pParticlesMaximum, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pParticleEmitter, pRateMinimum, pRateMaximum, pParticlesMaximum, pTextureRegion, pVertexBufferObjectManager);
    }

    public void setSpawnAction(SpawnAction spawnAction) {
        this.spawnAction = spawnAction;
    }

    @Override
    protected void onParticleSpawned(Particle<UncoloredSprite> particle) {
        super.onParticleSpawned(particle);
        if(spawnAction!=null) {
            spawnAction.run(particle);
        }
    }
}
