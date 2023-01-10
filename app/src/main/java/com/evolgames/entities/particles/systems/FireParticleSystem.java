package com.evolgames.entities.particles.systems;

import com.evolgames.entities.particles.emitters.FireEmitter;
import com.evolgames.entities.particles.emitters.PolygonEmitter;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.Particle;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class FireParticleSystem extends FlowingParticleSystem{
    public FireParticleSystem(IEntityFactory<Entity> ief, PolygonEmitter emitter, float rateMin, float rateMax, int particlesMax, TextureRegion textureRegion) {
        super(ief, emitter, rateMin, rateMax, particlesMax, textureRegion);
    }


}
