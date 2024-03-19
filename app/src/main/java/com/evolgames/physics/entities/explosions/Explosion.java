package com.evolgames.physics.entities.explosions;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.particles.wrappers.DataExplosiveParticleWrapper;
import com.evolgames.physics.PhysicsConstants;
import com.evolgames.scenes.PhysicsScene;
import com.evolgames.scenes.PlayScene;

public class Explosion {

    private final PhysicsScene<?> scene;
    private final Vector2 center;
    private final float heatRatio;
    private final float velocity;
    private final Vector2 vector = new Vector2();
    private final GameEntity source;
    private float force;
    private DataExplosiveParticleWrapper explosionParticleWrapper;
    private int time = 0;
    private boolean alive = true;
    private int count = 0;

    public Explosion(PhysicsScene<?> scene, GameEntity source, Vector2 center, float particlesRatio, float force, float speedRatio, float heatRatio, float fireRatio, float smokeRatio, float sparkRatio, float inParticleSizeRatio, float finParticleSizeRatio) {
        this.scene = scene;
        this.source = source;
        this.center = center;
        this.force = force;
        this.heatRatio = heatRatio;
        this.velocity = PhysicsConstants.getParticleVelocity(speedRatio) * 32f;

        if (fireRatio > 0.1f || smokeRatio > 0.1f || sparkRatio > 0.1f) {
            Vector2 c = center.cpy().mul(32f);
            this.explosionParticleWrapper = this.scene.getWorldFacade().createPointFireSource(null, new float[]{c.x, c.y, c.x, c.y}, velocity, fireRatio, smokeRatio, sparkRatio, particlesRatio, 1000+1000*heatRatio, inParticleSizeRatio, finParticleSizeRatio, false);

        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void update(float stepFactor) {

        if (!alive) {
            return;
        }
        if (force > 0) {
            float delta = count > PhysicsConstants.EXPLOSION_LIFESPAN ? force : Math.min(force, PhysicsConstants.EXPLOSION_FORCE_THRESHOLD);
            force -= delta;
            count++;
            scene.getWorldFacade().performFlux(center, (gameEntity, impacts) -> {
                impacts.forEach(i -> {
                    Vector2 p = i.getWorldPoint();
                    vector.set(p.x - center.x, p.y - center.y);
                    float d = Math.max(0.05f, vector.len());
                    i.setImpactImpulse(1000f * delta / (d * d));
                    vector.mul(i.getImpactImpulse() / 2000f);
                    gameEntity.getBody().applyLinearImpulse(vector.x, vector.y, p.x, p.y);
                });
                scene.getWorldFacade().applyImpacts(gameEntity, impacts);
                scene.getWorldFacade().applyImpactHeat(heatRatio, impacts);
            }, source);
        }
        if (time > PhysicsConstants.EXPLOSION_LIFESPAN) {
            this.explosionParticleWrapper.stopFinal();
            if (scene instanceof PlayScene) {
                ((PlayScene) scene).unlockSaving();
            }
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
