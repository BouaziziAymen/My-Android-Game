package com.evolgames.entities.particles.wrappers;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.emitters.DataEmitter;
import com.evolgames.entities.particles.emitters.SegmentEmitter;
import com.evolgames.entities.particles.initializers.RandomVelocityInitializer;
import com.evolgames.entities.particles.modifiers.GroundCollisionBump;
import com.evolgames.entities.particles.modifiers.GroundCollisionExpire;
import com.evolgames.entities.particles.systems.BaseParticleSystem;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;
import com.evolgames.gameengine.ResourceManager;
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

public class PointExplosiveParticleWrapper extends ExplosiveParticleWrapper {
    public PointExplosiveParticleWrapper(GameEntity gameEntity,  float[] data,float velocity, float fireRatio, float smokeRatio, float sparkRatio, float intensity, float flameTemp) {
        super(gameEntity, velocity, fireRatio, smokeRatio, sparkRatio, intensity,flameTemp, data);
        createSystems();
    }

    @Override
    public double getParticleTemperature(Particle<?> particle) {
        return flameTemperature;
    }

    @Override
    protected DataEmitter createEmitter(float[] data) {
        return new SegmentEmitter(data);
    }

    protected BaseParticleSystem createSparkSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief) {
        this.sparkParticleSystem = new BaseParticleSystem(emitter, lowerRate, higherRate, maxParticles, ResourceManager.getInstance().plasmaParticle4, ResourceManager.getInstance().vbom);
        Color fireColor = MyColorUtils.getColor(flameTemperature);
         this.sparkParticleSystem.addParticleInitializer(new RandomVelocityInitializer(0,verticalSpeed));
        this.sparkParticleSystem.addParticleInitializer(new ColorParticleInitializer<>(fireColor));
        this.sparkParticleSystem.addParticleInitializer(new AlphaParticleInitializer<>(0.9f));
        this.sparkParticleSystem.addParticleModifier(new AlphaParticleModifier<>(0f, 0.3f, 0.9f, 0f));
        this.sparkParticleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 0.3f, 0.1f, 0.1f));
        this.sparkParticleSystem.addParticleInitializer(new ExpireParticleInitializer<>(0.3f));
        this.sparkParticleSystem.addParticleModifier(new GroundCollisionExpire(20));
        return sparkParticleSystem;
    }

    protected BaseParticleSystem createFireSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief) {
        this.fireParticleSystem = new BaseParticleSystem(emitter, lowerRate, higherRate, maxParticles, ResourceManager.getInstance().plasmaParticle4, ResourceManager.getInstance().vbom);
        this.fireParticleSystem.addParticleInitializer(new RandomVelocityInitializer(0,verticalSpeed));
        Color fireColor = MyColorUtils.getColor(flameTemperature);
        Color secondColor = MyColorUtils.getColor(MyColorUtils.getPreviousTemperature(flameTemperature));
        this.fireParticleSystem.addParticleModifier(
                new org.andengine.entity.particle.modifier.ColorParticleModifier<>(0f, 0.3f, fireColor.getRed(), secondColor.getRed(), fireColor.getGreen(), secondColor.getGreen(), fireColor.getBlue(), secondColor.getBlue()));
        this.fireParticleSystem.addParticleInitializer(new ColorParticleInitializer<>(fireColor));
        this.fireParticleSystem.addParticleInitializer(new AlphaParticleInitializer<>(0.6f));
        this.fireParticleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 0.5f, 0.5f, 0f));
        this.fireParticleSystem.addParticleInitializer(new ExpireParticleInitializer<>(0.5f));
        this.fireParticleSystem.addParticleModifier(new GroundCollisionExpire(20));
        return fireParticleSystem;
    }

    protected BaseParticleSystem createSmokeSystem(int lowerRate, int higherRate, int maxParticles, float verticalSpeed, float horizontalSpeed, IEntityFactory<Entity> ief) {
        this.smokeParticleSystem = new BaseParticleSystem(emitter, lowerRate, higherRate, maxParticles, ResourceManager.getInstance().plasmaParticle4, ResourceManager.getInstance().vbom);
        this.smokeParticleSystem.addParticleInitializer(new RandomVelocityInitializer(0,verticalSpeed));
        this.smokeParticleSystem.addParticleInitializer(new ColorParticleInitializer<>(0.3f, 0.3f, 0.3f));
        this.smokeParticleSystem.addParticleInitializer(new AlphaParticleInitializer<>(0.2f));
        this.smokeParticleSystem.addParticleModifier(new AlphaParticleModifier<>(1f, 5f, 0.2f, 0f));
        this.smokeParticleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 5f, 0.99f, 0.99f));
        this.smokeParticleSystem.addParticleInitializer(new ExpireParticleInitializer<>(5f));
        this.smokeParticleSystem.addParticleModifier(new GroundCollisionBump(20));
        return smokeParticleSystem;
    }

}
