package com.evolgames.dollmutilationgame.entities.particles.modifiers;

import com.badlogic.gdx.math.Vector2;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;

public class SmoothRotationModifier implements IParticleModifier<UncoloredSprite> {
    Vector2 v = new Vector2();

    @Override
    public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
    }

    @Override
    public void onUpdateParticle(Particle<UncoloredSprite> pParticle) {
        v.set(1, 0);
        v.set(
                        pParticle.getPhysicsHandler().getVelocityX(),
                        pParticle.getPhysicsHandler().getVelocityY())
                .nor();
        float angleRadians = (float) Math.atan2(v.y, v.x);
        float angleDegrees = (float) Math.toDegrees(angleRadians);
        pParticle.getEntity().setRotation(270 - angleDegrees);
    }
}
