package com.evolgames.dollmutilationgame.entities.particles.initializers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.physics.WorldFacade;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.sprite.UncoloredSprite;

public class AirFieldVelocityInitializer implements IParticleInitializer<UncoloredSprite> {

    private final WorldFacade worldFacade;
    private final Vector2 worldPoint = new Vector2();
    private final Vector2 bodyVelocity;

    public AirFieldVelocityInitializer(WorldFacade worldFacade, Vector2 bodyVelocity) {
        this.worldFacade = worldFacade;
        this.bodyVelocity = bodyVelocity;
    }

    @Override
    public void onInitializeParticle(Particle<UncoloredSprite> pParticle) {
        this.worldPoint.set(pParticle.getEntity().getX() / 32f, pParticle.getEntity().getY() / 32f);
        Vector2 velocity = worldFacade.getAirVelocity(worldPoint).add(bodyVelocity);
        float value = velocity.len();
        velocity =
                value < PhysicsConstants.PARTICLE_TERMINAL_VELOCITY
                        ? velocity
                        : velocity.cpy().nor().mul(PhysicsConstants.PARTICLE_TERMINAL_VELOCITY);
        pParticle.getPhysicsHandler().setVelocity(velocity.x * 32f, velocity.y * 32f);
    }
}
