package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.cut.FreshCut;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.initializers.GameEntityAttachedVelocityInitializer;
import com.evolgames.entities.particles.modifiers.AlphaParticleModifier;
import com.evolgames.entities.particles.modifiers.SmoothRotationModifier;
import com.evolgames.entities.particles.systems.BaseParticleSystem;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public abstract class LiquidParticleWrapper {
    private final DataEmitter emitter;
    private final BaseParticleSystem particleSystem;
    private final GameEntityAttachedVelocityInitializer velocityInitializer;
    private final GameEntity parent;
    private final Vector2 splashVelocity;
    private final float flammability;
    private final Color color;
    private boolean alive = true;
    private FreshCut freshCut;
    private boolean isDepleted = false;

    public LiquidParticleWrapper(
            GameEntity gameEntity,
            float[] data, float[] weights, Color color,
            float flammability,
            Vector2 splashVelocity,
            int lowerRate,
            int higherRate) {
        this.splashVelocity = splashVelocity;
        this.emitter = createEmitter(data, weights, gameEntity);
        this.parent = gameEntity;
        this.color = color;
        this.flammability = flammability;

        this.particleSystem =
                new BaseParticleSystem(
                        emitter,
                        lowerRate,
                        higherRate,
                        10 * lowerRate,
                        ResourceManager.getInstance().liquidParticle,
                        ResourceManager.getInstance().vbom);

        this.velocityInitializer = new GameEntityAttachedVelocityInitializer(gameEntity, new Vector2());
        this.particleSystem.addParticleInitializer(velocityInitializer);


        this.particleSystem.addParticleInitializer(
                new ColorParticleInitializer<>(
                        color.getRed(), color.getGreen(), color.getBlue()));
        this.particleSystem.addParticleInitializer(new ScaleParticleInitializer<>(0.3f));
        this.addGravity();
        this.particleSystem.addParticleModifier(new SmoothRotationModifier());
        this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(1f, 3f, 0.5f, 0.4f));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(3f));
        this.particleSystem.setParticlesSpawnEnabled(false);
    }

    private void addGravity() {
        GravityParticleInitializer<UncoloredSprite> gravity = new GravityParticleInitializer<>();
        gravity.setAccelerationY(-3 * 10 * 32);
        this.particleSystem.addParticleInitializer(gravity);
    }

    protected abstract DataEmitter createEmitter(float[] emitterData, float[] weights, GameEntity gameEntity);

    public void update() {
        if (freshCut != null) {
            this.particleSystem.setParticlesSpawnEnabled(parent.isAlive() && !freshCut.isFrozen());
        }
        if (!parent.isAlive()||isDepleted) {
            this.particleSystem.setParticlesSpawnEnabled(false);
        }
        if (isAlive()&&splashVelocity != null) {
            splashVelocity.mul(0.9f);
            velocityInitializer.getIndependentVelocity().set(splashVelocity.x, splashVelocity.y);
        }

        if (!isAlive()&&isAllParticlesExpired()) {
            ResourceManager.getInstance()
                    .activity
                    .runOnUpdateThread(
                            particleSystem::detachSelf);
        }
        if (isAlive()) {
            emitter.update();
        }
    }

    public Color getColor() {
        return color;
    }

    public float getFlammability() {
        return flammability;
    }


    public BaseParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public void finishSelf() {
        this.particleSystem.setParticlesSpawnEnabled(false);
        this.isDepleted = true;
        this.alive = false;
    }

    public boolean isAllParticlesExpired() {
        for (Particle<UncoloredSprite> p : getParticleSystem().getParticles())
            if (p != null && !p.isExpired()) {
                return false;
            }
        return true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setSpawnEnabled(boolean pParticlesSpawnEnabled) {
        if (this.particleSystem != null) {
            this.particleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
        }
    }

    public void detachDirect() {
        if (this.particleSystem != null) {
            particleSystem.detachSelf();
        }
    }

    public void detach() {
        if (this.particleSystem != null) {
            ResourceManager.getInstance().activity.runOnUpdateThread(particleSystem::detachSelf);
        }
    }

    public void setFreshCut(FreshCut freshCut) {
        this.freshCut = freshCut;
    }

    public void setDepleted(boolean depleted) {
        isDepleted = depleted;
    }
}
