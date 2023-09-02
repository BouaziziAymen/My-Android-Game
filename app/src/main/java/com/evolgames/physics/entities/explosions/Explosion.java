package com.evolgames.physics.entities.explosions;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blockvisitors.ImpactData;
import com.evolgames.entities.particles.wrappers.PointExplosiveParticleWrapper;
import com.evolgames.scenes.GameScene;

import java.util.ArrayList;

public class Explosion {
    private final GameScene gameScene;
    private final ArrayList<ImpactData> impacts = new ArrayList<>();
    private final Vector2 center;
    private final float force;
    private final float heat;
    private final float velocity;
    private final Vector2 vector = new Vector2();
    private final GameEntity source;

    private PointExplosiveParticleWrapper explosionParticleWrapper;
    private int time = 0;
    private boolean alive = true;

    public Explosion(GameScene gameScene, GameEntity source, Vector2 center, float particles, float force, float speed, float heat, float fireRatio, float smokeRatio, float sparkRatio) {
        this.gameScene = gameScene;
        this.source = source;
        this.center = center;
        this.force = force;
        this.heat = heat;
        this.velocity = (1000 + speed * 1500);

        if (fireRatio > 0.1f || smokeRatio > 0.1f || sparkRatio > 0.1f) {
            this.explosionParticleWrapper = gameScene.getWorldFacade().createPointFireSource(null, center.cpy().mul(32f), velocity, fireRatio, smokeRatio, sparkRatio, particles, 2000, false);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void update() {
        if (!alive) {
            return;
        }
        if (time < 1) {
            gameScene.getWorldFacade().performFlux(center, (gameEntity, impacts) -> {
               impacts.forEach(i->{
                   Vector2 p = i.getWorldPoint();
                   vector.set(p.x-center.x,p.y-center.y);
                   float d = Math.max(1f,vector.len());
                   i.setImpactEnergy(100f * force/(d));
                   vector.mul(i.getImpactEnergy()/320f);
                   gameEntity.getBody().applyLinearImpulse(vector.x,vector.y,p.x,p.y);
               });
                gameScene.getWorldFacade().applyImpacts(gameEntity,impacts);
                gameScene.getWorldFacade().applyImpactHeat(heat, impacts);
            }, source);
        }
        if (time > 10) {
            this.explosionParticleWrapper.stopFinal();
            alive = false;
        }


        time++;
    }

    public Vector2 getCenter() {
        return center;
    }

    public float getVelocity() {
        return velocity;
    }
}