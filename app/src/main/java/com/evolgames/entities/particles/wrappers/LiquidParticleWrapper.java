package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.pools.LiquidParticlePool;
import com.evolgames.entities.particles.emitters.BaseEmitter;
import com.evolgames.entities.particles.initializers.GameEntityAttachedVelocityInitializer;
import com.evolgames.entities.particles.systems.BatchedSpriteParticleSystemWithCustomSpawnAction;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.userinterface.control.behaviors.actions.Action;

import is.kul.learningandengine.particlesystems.modifiers.AlphaParticleModifier;
import is.kul.learningandengine.particlesystems.modifiers.ExpireParticleInitializer;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.ease.EaseStrongInOut;

public abstract class LiquidParticleWrapper {

    public static final float LIQUID_PARTICLE_WIDTH = ResourceManager.getInstance().liquidParticle.getWidth();
    public static final float LIQUID_PARTICLE_HEIGHT = ResourceManager.getInstance().liquidParticle.getHeight();
    private final Color color;
    private final BaseEmitter emitter;
    private final BatchedSpriteParticleSystemWithCustomSpawnAction particleSystem;
    private final GameEntityAttachedVelocityInitializer velocityInitializer;
    private GameEntity parent;
    private Action spawnAction;
    private boolean alive = true;
    private int Limit;
    private final Vector2 splashVelocity;

    public LiquidParticleWrapper(GameEntity gameEntity, Color color, float[] data, Vector2 splashVelocity, int lowerRate, int higherRate) {
        this.color = color;
        this.splashVelocity = splashVelocity;

        emitter = createEmitter(data);
        IEntityFactory<Entity> ief = LiquidParticlePool::obtain;

        this.particleSystem = new BatchedSpriteParticleSystemWithCustomSpawnAction(ief, this, emitter, lowerRate, higherRate, higherRate, ResourceManager.getInstance().liquidParticle, ResourceManager.getInstance().vbom);

        this.velocityInitializer = new GameEntityAttachedVelocityInitializer(gameEntity, new Vector2());
        this.particleSystem.addParticleInitializer(velocityInitializer);

        this.particleSystem.addParticleInitializer(new ColorParticleInitializer<>(color.getRed(), color.getGreen(), color.getBlue()));
        this.particleSystem.addParticleInitializer(new AlphaParticleInitializer<>(color.getAlpha()));
        GravityParticleInitializer<Entity> gravity = new GravityParticleInitializer<>();
        gravity.setAccelerationY(-3 * 10 * 32);

        this.particleSystem.addParticleInitializer(gravity);
        particleSystem.addParticleModifier(new AlphaParticleModifier<>(1f, 3f, 1f, 0f));
        particleSystem.addParticleModifier(new OffCameraExpireParticleModifier<>(ResourceManager.getInstance().firstCamera));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(3f));


    }


    protected abstract BaseEmitter createEmitter(float[] emitterData);

    public Color getColor() {
        return color;
    }

    public void setParent(GameEntity entity) {
        this.parent = entity;
    }

    public void update() {
        updateEmitter();
    }

    private int timer = 0;
    private void updateEmitter() {
        if (parent == null) {
            return;
        }
        timer++;

        if(timer<1.5*60){
            if(splashVelocity !=null) {
                float percentage = EaseStrongInOut.getInstance().getPercentage(timer,20);
                velocityInitializer.getIndependentVelocity().set(splashVelocity.x *percentage, splashVelocity.y *percentage);
            }
        }
        float x = parent.getMesh().getX();
        float y = parent.getMesh().getY();
        float rot = parent.getMesh().getRotation();
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(x, y);
        GeometryUtils.transformation.preRotate(-rot);
        emitter.update(GeometryUtils.transformation);
    }

    public BatchedSpriteParticleSystemWithCustomSpawnAction getParticleSystem() {
        return particleSystem;
    }

    public void finishSelf() {
        particleSystem.setParticlesSpawnEnabled(false);
        setAlive(false);
    }


    public void setSpawnAction(Action spawnAction) {
        this.spawnAction = spawnAction;
    }


    public void performSpawnAction() {
        if (spawnAction != null) {
            spawnAction.performAction();
        }
    }

    public void recycleParticles() {
        for (Particle<Entity> p : getParticleSystem().getParticles())
            if (p != null){
                LiquidParticlePool.recycle((UncoloredSprite) p.getEntity());
            }
    }

    public boolean isAllParticlesExpired() {
        for (Particle<Entity> p : getParticleSystem().getParticles())
            if (p != null && !p.isExpired()){
                return false;
            }
        return true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getLimit() {
        return Limit;
    }

    public void setLimit(int limit) {
        this.Limit = limit * 10;
    }

    public void decrementLimit() {
        if (Limit >= 1) Limit--;
    }
}
