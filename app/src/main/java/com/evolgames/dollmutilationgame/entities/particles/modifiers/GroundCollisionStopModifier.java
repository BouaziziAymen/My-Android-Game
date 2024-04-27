package com.evolgames.dollmutilationgame.entities.particles.modifiers;

import com.evolgames.dollmutilationgame.scenes.PlayScene;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;

public class GroundCollisionStopModifier implements IParticleModifier<UncoloredSprite> {

    @Override
    public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
    }

    @Override
    public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
        float vy = pParticle.getPhysicsHandler().getVelocityY();
        if (pParticle.getEntity().getY() + vy / 60f < PlayScene.groundY) {
            pParticle.getEntity().setY(PlayScene.groundY);
            pParticle.getPhysicsHandler().setEnabled(false);
        }
    }
}
