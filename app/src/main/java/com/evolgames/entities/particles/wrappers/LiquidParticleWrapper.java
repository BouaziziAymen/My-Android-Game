package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
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
import org.andengine.util.modifier.ease.EaseStrongInOut;

public abstract class LiquidParticleWrapper {
    private final DataEmitter emitter;
    private final BaseParticleSystem particleSystem;
    private final GameEntityAttachedVelocityInitializer velocityInitializer;
    private final GameEntity parent;
    private final Vector2 splashVelocity;
    private final float flammability;
    private final Color color;
    private boolean alive = true;
    private int timer = 0;

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
        timer++;
        if (splashVelocity != null) {
            float percentage = EaseStrongInOut.getInstance().getPercentage(timer, 30);
            velocityInitializer
                    .getIndependentVelocity()
                    .set(
                            (float) (splashVelocity.x * (percentage + Math.random() * 0.1f)),
                            (float) (splashVelocity.y * (percentage + Math.random() * 0.1f)));
        }
        if (!parent.isAlive()) {
            this.particleSystem.setParticlesSpawnEnabled(false);
            if (isAllParticlesExpired()) {
                particleSystem.detachSelf();
            }
            return;
        }
        if (isAlive()) {
            if (!this.particleSystem.isParticlesSpawnEnabled()) {
                this.particleSystem.setParticlesSpawnEnabled(true);
            }
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
        particleSystem.setParticlesSpawnEnabled(false);
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


}
