package com.evolgames.entities.particles.initializers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.helpers.utilities.MathUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;

public class RandomVelocityInitializer implements IParticleInitializer<Entity> {

    private final float velocityMin;
    private final float velocityMax;
    private final Vector2 vector = new Vector2(1,0);

    public RandomVelocityInitializer(float velocityMin, float velocityMax){
        this.velocityMin = velocityMin;
        this.velocityMax = velocityMax;
    }
    @Override
    public void onInitializeParticle(Particle<Entity> pParticle) {
        float v = (float) (velocityMin+Math.random()*(velocityMax-velocityMin));
        vector.set(1,0);
        float rot = (float) (Math.PI * 2 * Math.random());
        MathUtils.rotateVectorByRadianAngle(vector,rot);
        float vX = vector.x * v;
        float vY = vector.y * v;
        pParticle.getPhysicsHandler().setVelocity( vX, vY);
    }
}
