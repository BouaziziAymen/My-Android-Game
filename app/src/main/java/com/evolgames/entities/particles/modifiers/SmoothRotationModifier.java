package com.evolgames.entities.particles.modifiers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.modifier.IParticleModifier;

public class SmoothRotationModifier implements IParticleModifier<Entity> {
 Vector2 v = new Vector2();
    @Override
    public void onInitializeParticle(Particle<Entity> pParticle) {
    }
    @Override
    public void onUpdateParticle(Particle<Entity> pParticle) {
       v.set(1,0);
       v.set(pParticle.getPhysicsHandler().getVelocityX(),pParticle.getPhysicsHandler().getVelocityY()).nor();
        float angleRadians = (float) Math.atan2(v.y, v.x);
        float angleDegrees = (float) Math.toDegrees(angleRadians);
        pParticle.getEntity().setRotation(270-angleDegrees);
    }
}
