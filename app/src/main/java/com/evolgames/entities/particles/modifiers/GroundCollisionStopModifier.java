package com.evolgames.entities.particles.modifiers;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;

public class GroundCollisionStopModifier implements IParticleModifier<UncoloredSprite> {
    private final int groundY;

    public GroundCollisionStopModifier(int groundY) {
        this.groundY = groundY;
    }

    @Override
    public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
    }

    @Override
    public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
        float vy = pParticle.getPhysicsHandler().getVelocityY();
        if (pParticle.getEntity().getY() + vy / 60f < groundY) {
            pParticle.getEntity().setY(groundY);
            pParticle.getPhysicsHandler().setEnabled(false);
        }
    }
}
