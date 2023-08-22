package com.evolgames.entities.particles.systems;

import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.particles.emitters.PolygonEmitter;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.Particle;
import org.andengine.opengl.texture.region.TextureRegion;

public class FireParticleSystem extends FlowingParticleSystem{
    public FireParticleSystem(IEntityFactory<Entity> ief, PolygonEmitter emitter, float rateMin, float rateMax, int particlesMax, TextureRegion textureRegion) {
        super(ief, emitter, rateMin, rateMax, particlesMax, textureRegion);
    }
    @Override
    protected void onParticleSpawned(Particle<Entity> particle) {
        super.onParticleSpawned(particle);
        CoatingBlock coatingBlock = relativePolygonEmitter.getActiveCoatingBlock();
        particle.getEntity().setUserData(coatingBlock);
    }
}
