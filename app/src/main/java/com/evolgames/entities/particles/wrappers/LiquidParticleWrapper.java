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
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public abstract class LiquidParticleWrapper extends MyParticleWrapper{
    private final BaseParticleSystem particleSystem;
    private final GameEntityAttachedVelocityInitializer velocityInitializer;
    private final Vector2 splashVelocity;
    private final float flammability;
    private final Color color;
    private final DataEmitter emitter;
    private FreshCut freshCut;
    public LiquidParticleWrapper(
            GameEntity gameEntity,
            float[] data, float[] weights, Color color,
            float flammability,
            Vector2 splashVelocity,
            int lowerRate,
            int higherRate) {
        this.splashVelocity = splashVelocity;
        this.emitter = createEmitter(data, weights, gameEntity);
        this.color = color;
        this.flammability = flammability;
        this.parent = gameEntity;

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


    private void updateSplashVelocity() {
        if(splashVelocity!=null) {
            splashVelocity.mul(0.9f);
            velocityInitializer.getIndependentVelocity().set(splashVelocity.x, splashVelocity.y);
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

    public void setFreshCut(FreshCut freshCut) {
        this.freshCut = freshCut;
    }


    @Override
    public void update() {
        if(!alive){
            return;
        }
        if(this.emitter!=null) {
            this.emitter.update();
        }
        if(!detached) {
            if (freshCut != null){
             setSpawnEnabled(!freshCut.isFrozen());
            }
            updateSplashVelocity();
        } else {
                if (isAllParticlesExpired()) {
                    ResourceManager.getInstance().activity.runOnUpdateThread(this::detachDirect);
                }
        }
    }
    @Override
    public void attachTo(Scene scene) {
         scene.attachChild(particleSystem);
         this.particleSystem.setZIndex(5);
    }

    @Override
    public void detach() {
          this.detached = true;
          this.setSpawnEnabled(false);
    }

    @Override
    public void detachDirect() {
        if(alive) {
            this.alive = false;
            this.particleSystem.detachSelf();
            this.particleSystem.dispose();
        }
    }

    @Override
    public boolean isAllParticlesExpired() {
        for (Particle<?> particle : particleSystem.getParticles()) {
            if (particle != null && !particle.isExpired()) {
                return false;
            }
        }
        return true;
    }

    public void setSpawnEnabled(boolean b) {
        this.particleSystem.setParticlesSpawnEnabled(b);
    }
}
