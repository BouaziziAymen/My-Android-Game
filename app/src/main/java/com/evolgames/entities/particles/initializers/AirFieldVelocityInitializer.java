package com.evolgames.entities.particles.initializers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.GameScene;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;


public class AirFieldVelocityInitializer  implements IParticleInitializer<Entity> {


    private final WorldFacade worldFacade;
    private final Vector2 worldPoint = new Vector2();
    private final Vector2 bodyVelocity;

    public AirFieldVelocityInitializer(WorldFacade worldFacade, Vector2 bodyVelocity) {
        this.worldFacade = worldFacade;
        this.bodyVelocity = bodyVelocity;
    }

    @Override
    public void onInitializeParticle(Particle<Entity> pParticle) {
        this.worldPoint.set(pParticle.getEntity().getX() / 32f, pParticle.getEntity().getY() / 32f);
            Vector2 velocity = worldFacade.getAirVelocity(worldPoint);
            pParticle.getPhysicsHandler().setVelocity((velocity.x+this.bodyVelocity.x)*32f, (velocity.y+this.bodyVelocity.y)*32f);
    }
}
