package com.evolgames.dollmutilationgame.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.particles.emitters.PolygonEmitter;
import com.evolgames.dollmutilationgame.entities.particles.modifiers.AlphaParticleModifier;
import com.evolgames.dollmutilationgame.entities.particles.systems.PulverizationParticleSystem;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.CoatingBlock;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.particles.initializers.AirFieldVelocityInitializer;
import com.evolgames.dollmutilationgame.entities.particles.modifiers.GroundCollisionStopModifier;
import com.evolgames.dollmutilationgame.physics.PhysicsConstants;
import com.evolgames.dollmutilationgame.physics.WorldFacade;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.UncoloredSprite;

public class PowderParticleWrapper extends MyParticleWrapper {
    private static final float RATE_MIN = 1000;
    private static final float RATE_MAX = 1000;
    private static final int PARTICLES_MAX = 1000;
    public BatchedSpriteParticleSystem particleSystem;

    private int step = 0;
    private boolean alive = true;

    public PowderParticleWrapper(
            WorldFacade worldFacade, GameEntity gameEntity, LayerBlock layerBlock, Vector2 bodyVelocity) {
        this.parent = gameEntity;
        PolygonEmitter emitter = new PolygonEmitter(layerBlock.getBlockGrid().getCoatingBlocks(), CoatingBlock::isPulverized);
        float ratio = layerBlock.getBlockArea() / (32f * 32f);

        this.particleSystem =
                new PulverizationParticleSystem(
                        emitter,
                        PowderParticleWrapper.RATE_MIN * ratio,
                        PowderParticleWrapper.RATE_MAX * ratio,
                        (int) (PowderParticleWrapper.PARTICLES_MAX * ratio + 1));

        AirFieldVelocityInitializer velocityInitializer =
                new AirFieldVelocityInitializer(worldFacade, bodyVelocity);
        this.particleSystem.addParticleInitializer(velocityInitializer);
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(5f));
        this.particleSystem.addParticleInitializer(new ScaleParticleInitializer<>(2f));
        this.particleSystem.addParticleModifier(new GroundCollisionStopModifier());
        this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(8f, 10f, 1f, 0));
        this.addGravity();
    }

    private void addGravity() {
        GravityParticleInitializer<UncoloredSprite> gravity = new GravityParticleInitializer<>();
        gravity.setAccelerationY(-3 * 10 * 32);
        this.particleSystem.addParticleInitializer(gravity);
    }
    public boolean isAllParticlesExpired() {
        for (Particle<?> particle : particleSystem.getParticles()) {
            if (particle != null && !particle.isExpired()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void attachTo(Scene scene) {
        this.particleSystem.setZIndex(this.parent.getZIndex());
        scene.attachChild(this.particleSystem);
    }

    public void update() {
        if (!alive) {
            return;
        }
        if (detached) {
            if (isAllParticlesExpired()) {
                ResourceManager.getInstance().activity.runOnUpdateThread(this::detachDirect);
            }
        } else {
            if (step > PhysicsConstants.PULVERIZATION_DURATION) {
                particleSystem.setParticlesSpawnEnabled(false);
                this.alive = false;
            }
        }
        step++;
    }

    @Override
    public void detach() {

    }

    @Override
    public void detachDirect() {

    }

}
