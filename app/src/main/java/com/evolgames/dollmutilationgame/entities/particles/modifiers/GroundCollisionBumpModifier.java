package com.evolgames.dollmutilationgame.entities.particles.modifiers;

import com.evolgames.dollmutilationgame.scenes.PlayScene;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;

public class GroundCollisionBumpModifier implements IParticleModifier<UncoloredSprite> {
    @Override
    public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
    }

    @Override
    public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
        float vx = pParticle.getPhysicsHandler().getVelocityX();
        float vy = pParticle.getPhysicsHandler().getVelocityY();
        if (pParticle.getEntity().getY() < PlayScene.groundY) {
            if (vy < 0) {
                vy = -vy / 10;
            }
            pParticle.getPhysicsHandler().setVelocity(vx, vy);
        }
    }
}
