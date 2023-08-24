package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;

public class GroundCollisionExpire implements IParticleModifier<Entity> {
    private final int groundY;
    public GroundCollisionExpire(int groundY){
        this.groundY = groundY;
    }
    @Override
    public void onInitializeParticle(Particle<Entity> pParticle) {
    }
    @Override
    public void onUpdateParticle(Particle<Entity> pParticle) {
        if(pParticle.getEntity().getY()-(pParticle.getEntity().getHeight()/3f*pParticle.getEntity().getScaleX())<20){
             pParticle.setExpired(true);
        }
    }
}
