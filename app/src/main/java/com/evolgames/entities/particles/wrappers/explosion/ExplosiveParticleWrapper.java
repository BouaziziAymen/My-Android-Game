package com.evolgames.entities.particles.wrappers.explosion;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.pools.UncoloredSpritePool;
import com.evolgames.entities.particles.systems.BaseParticleSystem;
import com.evolgames.entities.particles.wrappers.Fire;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.Particle;

public abstract class ExplosiveParticleWrapper implements Fire {

    private static final float RATE_MIN = 500;
    private static final float RATE_MAX = 1000;
    private static final int PARTICLES_MAX = 2000;
    protected final DataEmitter emitter;
    protected final float particleDensity;
    private final float fireRatio;
    private final float smokeRatio;
    private final float sparkRatio;
    private final float velocity;
    protected float flameTemperature;
    protected BaseParticleSystem fireParticleSystem;
    protected BaseParticleSystem smokeParticleSystem;
    protected BaseParticleSystem sparkParticleSystem;
    protected GameEntity parent;
    private boolean alive = true;
    protected boolean attachedToParent = true;

    public ExplosiveParticleWrapper(GameEntity gameEntity, float velocity, float fireRatio, float smokeRatio, float sparkRatio, float particleDensity, float flameTemperature, float[] data) {
        this.emitter = createEmitter(data);
        this.parent = gameEntity;
        this.velocity = velocity;
        this.fireRatio = fireRatio;
        this.smokeRatio = smokeRatio;
        this.sparkRatio = sparkRatio;
        this.particleDensity = particleDensity;
        this.flameTemperature = flameTemperature;
        this.updateEmitter();
    }

    public void createSystems() {
        int rateMinFire = (int) (fireRatio * particleDensity * RATE_MIN);
        int rateMaxFire = (int) (fireRatio * particleDensity * RATE_MAX);
        int particlesMaxFire = (int) (fireRatio * particleDensity * PARTICLES_MAX);
        int rateMinSmoke = (int) (smokeRatio * particleDensity * RATE_MIN);
        int rateMaxSmoke = (int) (smokeRatio * particleDensity * RATE_MAX);
        int particlesMaxSmoke = (int) (smokeRatio * particleDensity * PARTICLES_MAX);
        int rateMinSpark = (int) (sparkRatio * particleDensity * RATE_MIN);
        int rateMaxSpark = (int) (sparkRatio * particleDensity * RATE_MAX);
        int particlesMaxSpark = (int) (sparkRatio * particleDensity * PARTICLES_MAX);

        float fireVerticalSpeed = 0.08f * velocity;
        float smokeVerticalSpeed = 0.02f * velocity;
        float sparkVerticalSpeed = 0.05f * velocity;
        float fireHorizontalSpeed = 0.01f * velocity;
        float smokeHorizontalSpeed = 0.01f * velocity;
        float sparkHorizontalSpeed = 0;
        if (fireRatio > 0) {
            this.fireParticleSystem = createFireSystem(rateMinFire, rateMaxFire, particlesMaxFire, fireVerticalSpeed, fireHorizontalSpeed, UncoloredSpritePool::obtain);
        }
        if (smokeRatio > 0) {
            this.smokeParticleSystem = createSmokeSystem(rateMinSmoke, rateMaxSmoke, particlesMaxSmoke, smokeVerticalSpeed, smokeHorizontalSpeed, UncoloredSpritePool::obtain);
        }
        if (sparkRatio > 0) {
            this.sparkParticleSystem = createSparkSystem(rateMinSpark, rateMaxSpark, particlesMaxSpark, sparkVerticalSpeed, sparkHorizontalSpeed, UncoloredSpritePool::obtain);
        }
    }

    protected abstract BaseParticleSystem createSparkSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief);

    protected abstract BaseParticleSystem createFireSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief);

    protected abstract BaseParticleSystem createSmokeSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief);

    protected abstract DataEmitter createEmitter(float[] data);

    public void setParent(GameEntity entity) {
        this.parent = entity;
    }

    public void detachFromParent() {
        attachedToParent = false;
    }

    public void update() {
        if (!attachedToParent||parent==null) {
            return;
        }
        if (!parent.isAlive()) {
            finishSelf();
        }
        if (!isAlive()) {
            return;
        }
        updateEmitter();
    }

    private void updateEmitter() {
        if (parent == null) {
            return;
        }
        float x = parent.getMesh().getX();
        float y = parent.getMesh().getY();
        float rot = parent.getMesh().getRotation();
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(x, y);
        GeometryUtils.transformation.preRotate(-rot);
        emitter.onStep(GeometryUtils.transformation);
    }

    public void setSpawnEnabled(boolean pParticlesSpawnEnabled) {
        if (fireParticleSystem != null) {
            this.fireParticleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
        }
        if (smokeParticleSystem != null) {
            this.smokeParticleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
        }
        if (sparkParticleSystem != null) {
            this.sparkParticleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
        }
    }


    public BaseParticleSystem getFireParticleSystem() {
        return fireParticleSystem;
    }

    public BaseParticleSystem getSmokeParticleSystem() {
        return smokeParticleSystem;
    }

    public BaseParticleSystem getSparkParticleSystem() {
        return sparkParticleSystem;
    }

    public void finishSelf() {
        setSpawnEnabled(false);
        this.alive = false;
    }

    public boolean isAllParticlesExpired() {
        for (Particle<Entity> p : getFireParticleSystem().getParticles())
            if (p != null && !p.isExpired()) {
                return false;
            }
        return true;
    }

    public boolean isAlive() {
        return alive;
    }

}
