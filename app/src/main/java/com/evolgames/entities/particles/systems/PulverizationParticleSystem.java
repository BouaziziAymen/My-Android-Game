package com.evolgames.entities.particles.systems;

import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.particles.emitters.PolygonEmitter;
import com.evolgames.entities.particles.emitters.PowderEmitter;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.MyColorUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.Particle;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class PulverizationParticleSystem extends FlowingParticleSystem {
    public PulverizationParticleSystem(IEntityFactory<Entity> ief, PolygonEmitter emitter, float rateMin, float rateMax, int particlesMax) {
        super(ief, emitter, rateMin, rateMax, particlesMax, ResourceManager.getInstance().pixelParticle);
    }

    @Override
    protected void onParticleSpawned(Particle<Entity> particle) {
        super.onParticleSpawned(particle);
        CoatingBlock coatingBlock = (CoatingBlock) particle.getEntity().getUserData();
        Color parentColor = coatingBlock.getParent().getProperties().getDefaultColor();
        MyColorUtils.blendColors(parentColor,parentColor,coatingBlock.getProperties().getDefaultColor());
        particle.getEntity().setColor(parentColor);
    }
}
