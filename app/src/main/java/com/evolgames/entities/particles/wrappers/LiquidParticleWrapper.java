package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.initializers.GameEntityAttachedVelocityInitializer;
import com.evolgames.entities.particles.modifiers.SmoothRotationModifier;
import com.evolgames.entities.particles.systems.BaseParticleSystem;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.ease.EaseStrongInOut;

import is.kul.learningandengine.particlesystems.modifiers.AlphaParticleModifier;
import is.kul.learningandengine.particlesystems.modifiers.ExpireParticleInitializer;

public abstract class LiquidParticleWrapper {

    private final Color liquidColor;
    private final DataEmitter emitter;
    private final BaseParticleSystem particleSystem;
    private final GameEntityAttachedVelocityInitializer velocityInitializer;
    private GameEntity parent;
    private boolean alive = true;
    private final Vector2 splashVelocity;

    public LiquidParticleWrapper(GameEntity gameEntity, Color liquidColor, float[] data, Vector2 splashVelocity, int lowerRate, int higherRate) {
        this.liquidColor = liquidColor;
        this.splashVelocity = splashVelocity;
        emitter = createEmitter(data);

        this.particleSystem = new BaseParticleSystem(emitter, lowerRate, higherRate, higherRate, ResourceManager.getInstance().liquidParticle, ResourceManager.getInstance().vbom);

        this.velocityInitializer = new GameEntityAttachedVelocityInitializer(gameEntity, new Vector2());
        this.particleSystem.addParticleInitializer(velocityInitializer);

        this.particleSystem.addParticleInitializer(new ColorParticleInitializer<>(liquidColor.getRed(), liquidColor.getGreen(), liquidColor.getBlue()));
        this.particleSystem.addParticleInitializer(new ScaleParticleInitializer<>(0.4f));
        this.addGravity();
        this.particleSystem.addParticleModifier(new SmoothRotationModifier());
        this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(1f, 3f, 1f, 0f));
        this.particleSystem.addParticleModifier(new OffCameraExpireParticleModifier<>(ResourceManager.getInstance().firstCamera));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(3f));
    }

    private void addGravity() {
        GravityParticleInitializer<UncoloredSprite> gravity = new GravityParticleInitializer<>();
        gravity.setAccelerationY(-3 * 10 * 32);
        this.particleSystem.addParticleInitializer(gravity);
    }


    protected abstract DataEmitter createEmitter(float[] emitterData);

    public Color getLiquidColor() {
        return liquidColor;
    }

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

    private int timer = 0;

    private void updateEmitter() {
        if (parent == null) {
            return;
        }
        timer++;

        if (splashVelocity != null) {
            float percentage = EaseStrongInOut.getInstance().getPercentage(timer, 30);
            velocityInitializer.getIndependentVelocity().set((float) (splashVelocity.x * (percentage + Math.random() * 0.1f)), (float) (splashVelocity.y * (percentage + Math.random() * 0.1f)));
        }

        float x = parent.getMesh().getX();
        float y = parent.getMesh().getY();
        float rot = parent.getMesh().getRotation();
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(x, y);
        GeometryUtils.transformation.preRotate(-rot);
        emitter.onStep(GeometryUtils.transformation);
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

}
