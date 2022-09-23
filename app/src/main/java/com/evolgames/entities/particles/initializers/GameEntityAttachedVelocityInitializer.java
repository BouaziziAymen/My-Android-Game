package com.evolgames.entities.particles.initializers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;

public class GameEntityAttachedVelocityInitializer implements IParticleInitializer<Entity> {

    private final GameEntity gameEntity;
    private final Vector2 independentVelocity;
    private final Vector2 worldPoint;

    public GameEntityAttachedVelocityInitializer(GameEntity gameEntity, Vector2 independentVelocity) {
        this.gameEntity = gameEntity;
        this.independentVelocity = independentVelocity;
        this.worldPoint = new Vector2();
    }

    @Override
    public void onInitializeParticle(Particle<Entity> pParticle) {
        if (gameEntity.getBody() == null) {
            return;
        }
        this.worldPoint.set(pParticle.getEntity().getX() / 32f, pParticle.getEntity().getY() / 32f);
        Vector2 linVel = gameEntity.getBody().getLinearVelocityFromWorldPoint(worldPoint);
        float vX = linVel.x * 32f;
        float vY = linVel.y * 32f;
        pParticle.getPhysicsHandler().setVelocity(independentVelocity.x + vX, independentVelocity.y + vY);
    }
}
