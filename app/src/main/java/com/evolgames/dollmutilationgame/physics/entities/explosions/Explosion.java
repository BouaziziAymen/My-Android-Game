package com.evolgames.dollmutilationgame.physics.entities.explosions;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.particles.wrappers.DataExplosiveParticleWrapper;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;
import com.evolgames.dollmutilationgame.scenes.PlayScene;

public class Explosion {

    private final PhysicsScene scene;
    private final Vector2 center;
    private final Runnable runnable;
    private final float heatRatio;
    private final float velocity;
    private final Vector2 vector = new Vector2();
    private final GameEntity source;
    private Vector2 movingCenter;
    private float force;
    private DataExplosiveParticleWrapper explosionParticleWrapper;
    private int time = 0;
    private boolean alive = true;
    private int count = 0;
    private boolean fireWorkCreated = false;

    public Explosion(PhysicsScene scene, GameEntity source, Vector2 center, float particlesRatio, float force, float speedRatio, float heatRatio, float fireRatio, float smokeRatio, float sparkRatio, float inParticleSizeRatio, float finParticleSizeRatio) {
        this.scene = scene;
        this.source = source;
        this.center = center;
        this.force = force;
        this.heatRatio = heatRatio;
        this.velocity = PhysicsConstants.getParticleVelocity(speedRatio) * 32f;
        runnable = () -> {
            if (fireRatio > 0.1f || smokeRatio > 0.1f || sparkRatio > 0.1f) {
                Vector2 c = movingCenter.cpy().mul(32f);
                this.explosionParticleWrapper = this.scene.getWorldFacade().createPointFireSource(null, new float[]{c.x, c.y, c.x, c.y}, velocity, fireRatio, smokeRatio, sparkRatio, particlesRatio, 1000 + 1000 * heatRatio, inParticleSizeRatio, finParticleSizeRatio, false);

            }
        };
        if (source == null) {
            movingCenter = center;
            runnable.run();
        }

    }

    public boolean isAlive() {
        return alive;
    }

    public void update() {

        if (source != null && source.getBody() != null) {
            if (movingCenter == null) {
                movingCenter = new Vector2();
            }
            Vector2 newCenter = source.getBody().getWorldPoint(this.center);
            this.movingCenter.set(newCenter);
            if (!fireWorkCreated) {
                runnable.run();
                fireWorkCreated = true;
            }
        }
        if (!alive) {
            return;
        }
        if (force > 0 && movingCenter != null) {
            float delta = count > PhysicsConstants.EXPLOSION_LIFESPAN ? force : Math.min(force, PhysicsConstants.EXPLOSION_FORCE_THRESHOLD);
            force -= delta;
            count++;
            scene.getWorldFacade().performFlux(movingCenter, (gameEntity, impacts) -> {
                impacts.forEach(i -> {
                    Vector2 p = i.getWorldPoint();
                    vector.set(p.x - movingCenter.x, p.y - movingCenter.y);
                    float d = Math.max(i.isInner() ? 0.01f : 3f, vector.len());
                    i.setImpactImpulse(1000f * delta / (d * d));
                    i.setDistanceFromSource(d);
                    vector.mul(i.getImpactImpulse() / 2000f);
                    gameEntity.getBody().applyLinearImpulse(vector.x, vector.y, p.x, p.y);
                });
                scene.getWorldFacade().applyImpacts(gameEntity, impacts);
                scene.getWorldFacade().applyImpactHeat(heatRatio, impacts);
            }, source);
        }
        if (time > PhysicsConstants.EXPLOSION_LIFESPAN) {
            this.explosionParticleWrapper.detach();
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

    public GameEntity getCarrierEntity() {
        return this.source;
    }
}
