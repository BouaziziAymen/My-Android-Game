package com.evolgames.dollmutilationgame.entities.particles.modifiers;

import com.evolgames.dollmutilationgame.scenes.PlayScene;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;

public class GroundCollisionExpireModifier implements IParticleModifier<UncoloredSprite> {
    @Override
    public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
    }

    @Override
    public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
        if (pParticle.getEntity().getY() < PlayScene.groundY) {
            pParticle.setExpired(true);
        }
    }
}
