package com.evolgames.physics.entities.explosions;

import static com.evolgames.physics.PhysicsConstants.FLUX_PRECISION;

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
    private final GameEntity gameEntity;

    private PointExplosiveParticleWrapper explosionParticleWrapper;
    private int time = 0;
    private boolean alive = true;

    public Explosion(GameScene gameScene, GameEntity gameEntity,Vector2 center, float particles, float force, float speed, float heat, float fireRatio, float smokeRatio, float sparkRatio) {
        this.gameScene = gameScene;
        this.gameEntity = gameEntity;
        this.center = center;
        this.force = force;
        this.heat = heat;
        this.velocity = (3000 + speed * 5000);

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
        if (time == 0) {
            gameScene.getWorldFacade().performFlux(center, (gameEntity, impacts) -> {
               impacts.forEach(i->{
                   Vector2 p = i.getWorldPoint();
                   vector.set(p.x-center.x,p.y-center.y);
                   float d = Math.min(3f,vector.len());
                   i.setImpactEnergy(10000000 * force/(FLUX_PRECISION*(d*d)));
                   vector.mul(i.getImpactEnergy()/300f);
                   gameEntity.getBody().applyLinearImpulse(vector.x,vector.y,p.x,p.y);
               });
                gameScene.getWorldFacade().applyImpacts(gameEntity,impacts);
                gameScene.getWorldFacade().applyImpactHeat(heat, impacts);
            },gameEntity);
        }
        if (time > 10) {
            this.explosionParticleWrapper.setSpawnEnabled(false);
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