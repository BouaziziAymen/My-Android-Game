package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;

public class GroundCollisionBump implements IParticleModifier<Entity> {
    private final int groundY;

    public GroundCollisionBump(int groundY){
        this.groundY = groundY;
    }
    @Override
    public void onInitializeParticle(Particle<Entity> pParticle) {

    }

    @Override
    public void onUpdateParticle(Particle<Entity> pParticle) {
         if(pParticle.getEntity().getY()-(pParticle.getEntity().getHeight()/3f*pParticle.getEntity().getScaleX())<groundY){
             float vx = pParticle.getPhysicsHandler().getVelocityX();
             float vy = pParticle.getPhysicsHandler().getVelocityY();
             if(vy<0){
                 vy = -vy;
             }
             pParticle.getPhysicsHandler().setVelocity(vx,vy);
         }
    }
}
