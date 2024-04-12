package com.evolgames.entities.particles.wrappers;

import android.util.Log;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.systems.BaseParticleSystem;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.sprite.UncoloredSprite;

public abstract class ExplosiveParticleWrapper implements Fire, Smoke {

    private static final float RATE_MIN = 30 * 4;
    private static final float RATE_MAX = 40 * 4;
    private static final int PARTICLES_MAX = 200 * 4;
    private static final float RATE_MIN_SMOKE = 5;
    private static final float RATE_MAX_SMOKE = 10;
    private static final int PARTICLES_MAX_SMOKE = 50;
    private static final float RATE_MIN_SPARK = 20;
    private static final float RATE_MAX_SPARK = 40;
    private static final int PARTICLES_MAX_SPARK = 40;

    protected final DataEmitter emitter;
    protected final float particleDensity;
    protected final GameEntity parent;
    private final float fireRatio;
    private final float smokeRatio;
    private final float sparkRatio;
    private final float velocity;
    private final float initialFlameParticleSize;
    private final float finalFlameParticleSize;
    protected float flameTemperature;
    protected BaseParticleSystem fireParticleSystem;
    protected BaseParticleSystem smokeParticleSystem;
    protected BaseParticleSystem sparkParticleSystem;
    protected boolean followParent = true;
    private boolean alive = true;

    public ExplosiveParticleWrapper(
            GameEntity gameEntity,
            float velocity,
            float fireRatio,
            float smokeRatio,
            float sparkRatio,
            float particleDensity,
            float flameTemperature,
            float initialFlameParticleSize,
            float finalFlameParticleSize,
            float[] data) {
        this.emitter = createEmitter(data, gameEntity);
        this.parent = gameEntity;
        this.velocity = velocity;
        this.fireRatio = fireRatio;
        this.smokeRatio = smokeRatio;
        this.sparkRatio = sparkRatio;
        this.particleDensity = particleDensity;
        this.flameTemperature = flameTemperature;
        this.initialFlameParticleSize = initialFlameParticleSize;
        this.finalFlameParticleSize = finalFlameParticleSize;
        this.emitter.update();
    }

    public void createSystems() {
        int rateMinFire = (int) (fireRatio * particleDensity * RATE_MIN);
        int rateMaxFire = (int) (fireRatio * particleDensity * RATE_MAX);
        int particlesMaxFire = (int) (fireRatio * particleDensity * PARTICLES_MAX);
        int rateMinSmoke = (int) (smokeRatio * particleDensity * RATE_MIN_SMOKE);
        int rateMaxSmoke = (int) (smokeRatio * particleDensity * RATE_MAX_SMOKE);
        int particlesMaxSmoke = (int) (smokeRatio * particleDensity * PARTICLES_MAX_SMOKE);
        int rateMinSpark = (int) (sparkRatio * particleDensity * RATE_MIN_SPARK);
        int rateMaxSpark = (int) (sparkRatio * particleDensity * RATE_MAX_SPARK);
        int particlesMaxSpark = (int) (sparkRatio * particleDensity * PARTICLES_MAX_SPARK);

        float fireVerticalSpeed = 0.08f * velocity;
        float smokeVerticalSpeed = 0.06f * velocity;
        float sparkVerticalSpeed = 0.1f * velocity;
        float fireHorizontalSpeed = 0.01f * velocity;
        float smokeHorizontalSpeed = 0.02f * velocity;
        float sparkHorizontalSpeed = 0;
        if (fireRatio > 0) {
            this.fireParticleSystem =
                    createFireSystem(
                            rateMinFire, rateMaxFire, particlesMaxFire, fireVerticalSpeed, fireHorizontalSpeed, initialFlameParticleSize, finalFlameParticleSize);
        }
        if (smokeRatio > 0) {
            this.smokeParticleSystem =
                    createSmokeSystem(
                            rateMinSmoke,
                            rateMaxSmoke,
                            particlesMaxSmoke,
                            smokeVerticalSpeed,
                            smokeHorizontalSpeed);
        }
        if (sparkRatio > 0) {
            this.sparkParticleSystem =
                    createSparkSystem(
                            rateMinSpark,
                            rateMaxSpark,
                            particlesMaxSpark,
                            sparkVerticalSpeed,
                            sparkHorizontalSpeed);
        }
    }

    protected abstract BaseParticleSystem createSparkSystem(
            int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed);

    protected abstract BaseParticleSystem createFireSystem(
            int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, float initialFlameParticleSize, float finalFlameParticleSize);

    protected abstract BaseParticleSystem createSmokeSystem(
            int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed);

    protected abstract DataEmitter createEmitter(float[] data, GameEntity gameEntity);

    public void stopFollowingParent() {
        followParent = false;
    }

    public void followParent() {
        followParent = true;
    }

    public void update() {
        if (parent != null && !parent.isAlive()) {
            stopFinal();
        }
        if (!isAlive()) {
            if (isAllParticlesExpired()) {
                finishSelf();
            }
            return;
        }
        Log.e("data:",followParent+"/"+parent);
        if (followParent && parent != null) {
            emitter.update();
        }
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

    @Override
    public BaseParticleSystem getFireParticleSystem() {
        return fireParticleSystem;
    }

    @Override
    public BaseParticleSystem getSmokeParticleSystem() {
        return smokeParticleSystem;
    }

    public BaseParticleSystem getSparkParticleSystem() {
        return sparkParticleSystem;
    }

    public void stopFinal() {
        setSpawnEnabled(false);
        this.alive = false;
    }

    private void finishSelf() {
        ResourceManager.getInstance()
                .activity
                .runOnUpdateThread(
                        () -> {
                            if (smokeParticleSystem != null) this.smokeParticleSystem.detachSelf();
                            if (sparkParticleSystem != null) this.sparkParticleSystem.detachSelf();
                            if (fireParticleSystem != null) this.fireParticleSystem.detachSelf();
                        });
    }

    public void detach() {
        stopFinal();
        finishSelf();
    }

    public boolean isAllParticlesExpired() {
        if (getFireParticleSystem() != null)
            for (Particle<UncoloredSprite> p : getFireParticleSystem().getParticles()) {
                if (p != null && !p.isExpired()) {
                    return false;
                }
            }
        if (getSmokeParticleSystem() != null)
            for (Particle<UncoloredSprite> p : getSmokeParticleSystem().getParticles()) {
                if (p != null && !p.isExpired()) {
                    return false;
                }
            }
        if (getSparkParticleSystem() != null)
            for (Particle<UncoloredSprite> p : getSparkParticleSystem().getParticles()) {
                if (p != null && !p.isExpired()) {
                    return false;
                }
            }
        return true;
    }

    public boolean isAlive() {
        return alive;
    }

}
