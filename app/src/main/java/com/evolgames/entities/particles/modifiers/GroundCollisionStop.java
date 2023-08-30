package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;

public class GroundCollisionStop implements IParticleModifier<Entity> {
    private final int groundY;
    public GroundCollisionStop(int groundY){
        this.groundY = groundY;
    }
    @Override
    public void onInitializeParticle(Particle<Entity> pParticle) {
    }
    @Override
    public void onUpdateParticle(Particle<Entity> pParticle) {
        if(pParticle.getEntity().getY()-(pParticle.getEntity().getHeight()/3f*pParticle.getEntity().getScaleX())<groundY){
            pParticle.getPhysicsHandler().setEnabled(false);
        }
    }
}
