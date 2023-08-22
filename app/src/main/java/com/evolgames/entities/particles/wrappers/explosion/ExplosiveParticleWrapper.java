package com.evolgames.entities.particles.wrappers.explosion;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.initializers.GameEntityAttachedMinMaxVelocityInitializer;
import com.evolgames.entities.particles.pools.UncoloredSpritePool;
import com.evolgames.entities.particles.systems.BaseParticleSystem;
import com.evolgames.entities.particles.wrappers.Fire;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MyColorUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.util.adt.color.Color;

import is.kul.learningandengine.particlesystems.modifiers.AlphaParticleModifier;

public abstract class ExplosiveParticleWrapper implements Fire {

    private static final float RATE_MIN = 40;
    private static final float RATE_MAX = 120;
    private static final int PARTICLES_MAX = 480;
    private final DataEmitter emitter;
    private final Vector2 normal;
    private BaseParticleSystem fireParticleSystem;
    private BaseParticleSystem smokeParticleSystem;
    private BaseParticleSystem sparkParticleSystem;
    private GameEntity parent;
    private boolean alive = true;
    private int timer = 0;

    public ExplosiveParticleWrapper(GameEntity gameEntity, Vector2 baseVelocity, float fireRatio, float smokeRatio, float sparkRatio, float intensity, float[] data) {
        float velocity = baseVelocity.len();
        this.normal = baseVelocity.nor();
        this.emitter = createEmitter(data);
        this.parent = gameEntity;
        float length = sourceLength(data);
        int rateMinFire = (int) (fireRatio * intensity * length * RATE_MIN);
        int rateMaxFire = (int) (fireRatio * intensity * length * RATE_MAX);
        int particlesMaxFire = (int) (fireRatio * intensity * length * PARTICLES_MAX);
        int rateMinSmoke = (int) (smokeRatio * intensity * length * RATE_MIN);
        int rateMaxSmoke = (int) (smokeRatio * intensity * length * RATE_MAX);
        int particlesMaxSmoke = (int) (smokeRatio * intensity * length * PARTICLES_MAX);
        int rateMinSpark = (int) (sparkRatio * intensity * length * RATE_MIN);
        int rateMaxSpark = (int) (sparkRatio * intensity * length * RATE_MAX);
        int particlesMaxSpark = (int) (sparkRatio * intensity * length * PARTICLES_MAX);

        float fireVerticalSpeed = 0.08f * velocity;
        float smokeVerticalSpeed = 0.02f * velocity;
        float sparkVerticalSpeed = 0.05f * velocity;
        float fireHorizontalSpeed = 0.01f * velocity;
        float smokeHorizontalSpeed = 0.01f * velocity;
        float sparkHorizontalSpeed = 0;

        this.fireParticleSystem = createFireSystem(rateMinFire, rateMaxFire, particlesMaxFire, fireVerticalSpeed, fireHorizontalSpeed, UncoloredSpritePool::obtain);
        this.smokeParticleSystem = createSmokeSystem(rateMinSmoke, rateMaxSmoke, particlesMaxSmoke, smokeVerticalSpeed, smokeHorizontalSpeed, UncoloredSpritePool::obtain);
        this.sparkParticleSystem = createSparkSystem(rateMinSpark, rateMaxSpark, particlesMaxSpark, sparkVerticalSpeed, sparkHorizontalSpeed, UncoloredSpritePool::obtain);
    }

    abstract protected float sourceLength(float[] data);

    private BaseParticleSystem createSparkSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief) {
        this.sparkParticleSystem = new BaseParticleSystem(emitter, lowerRate, higherRate, maxParticles, ResourceManager.getInstance().pixelParticle, ResourceManager.getInstance().vbom);
        Color fireColor = MyColorUtils.getColor(3000);
        this.sparkParticleSystem.addParticleInitializer(new GameEntityAttachedMinMaxVelocityInitializer(parent, normal.cpy().nor(), -horizontalSpeed, horizontalSpeed, 0, verticalSpeed));
        this.sparkParticleSystem.addParticleInitializer(new ColorParticleInitializer<>(fireColor));
        this.sparkParticleSystem.addParticleInitializer(new AlphaParticleInitializer<>(0.9f));
        this.sparkParticleSystem.addParticleModifier(new AlphaParticleModifier<>(0f, 0.3f, 0.9f, 0f));
        this.sparkParticleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 0.3f, 0.05f, 0.04f));
        this.sparkParticleSystem.addParticleInitializer(new ExpireParticleInitializer<>(0.3f));
        return sparkParticleSystem;
    }

    private BaseParticleSystem createFireSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief) {
        this.fireParticleSystem = new BaseParticleSystem(emitter, lowerRate, higherRate, maxParticles, ResourceManager.getInstance().plasmaParticle4, ResourceManager.getInstance().vbom);
        this.fireParticleSystem.addParticleInitializer(new GameEntityAttachedMinMaxVelocityInitializer(parent, normal.cpy().nor(), -horizontalSpeed, horizontalSpeed, 0, verticalSpeed));
        Color fireColor = MyColorUtils.getColor(2000);
        Color secondColor = MyColorUtils.getColor(500);
        this.fireParticleSystem.addParticleModifier(
                new org.andengine.entity.particle.modifier.ColorParticleModifier<>(0f, 0.3f, fireColor.getRed(), secondColor.getRed(), fireColor.getGreen(), secondColor.getGreen(), fireColor.getBlue(), secondColor.getBlue()));
        this.fireParticleSystem.addParticleInitializer(new ColorParticleInitializer<>(fireColor));
        this.fireParticleSystem.addParticleInitializer(new AlphaParticleInitializer<>(0.6f));
        this.fireParticleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 0.5f, 0.5f, 0f));
        this.fireParticleSystem.addParticleInitializer(new ExpireParticleInitializer<>(0.5f));
        return fireParticleSystem;
    }

    private BaseParticleSystem createSmokeSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief) {
        this.smokeParticleSystem = new BaseParticleSystem(emitter, lowerRate, higherRate, maxParticles, ResourceManager.getInstance().plasmaParticle4, ResourceManager.getInstance().vbom);
        this.smokeParticleSystem.addParticleInitializer(new GameEntityAttachedMinMaxVelocityInitializer(parent, normal.cpy().nor(), -horizontalSpeed, horizontalSpeed, 0, verticalSpeed));
        this.smokeParticleSystem.addParticleInitializer(new ColorParticleInitializer<>(0.3f, 0.3f, 0.3f));
        this.smokeParticleSystem.addParticleInitializer(new AlphaParticleInitializer<>(0.2f));
        this.smokeParticleSystem.addParticleModifier(new AlphaParticleModifier<>(1f, 5f, 0.2f, 0f));
        this.smokeParticleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 5f, 0.99f, 0.99f));
        this.smokeParticleSystem.addParticleInitializer(new ExpireParticleInitializer<>(5f));
        return smokeParticleSystem;
    }

    protected abstract DataEmitter createEmitter(float[] data);

    public void setParent(GameEntity entity) {
        this.parent = entity;
    }

    public void update() {
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
        transformEmitter();
    }

    public void setSpawnEnabled(boolean pParticlesSpawnEnabled) {
        this.fireParticleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
        this.smokeParticleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
        this.sparkParticleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
    }

    private void transformEmitter() {
        float x = parent.getMesh().getX();
        float y = parent.getMesh().getY();
        float rot = parent.getMesh().getRotation();
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(x, y);
        GeometryUtils.transformation.preRotate(-rot);
        emitter.onStep(GeometryUtils.transformation);
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
        fireParticleSystem.setParticlesSpawnEnabled(false);
        sparkParticleSystem.setParticlesSpawnEnabled(false);
        smokeParticleSystem.setParticlesSpawnEnabled(false);
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
