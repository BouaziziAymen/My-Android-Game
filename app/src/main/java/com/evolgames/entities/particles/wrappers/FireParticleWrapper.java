package com.evolgames.entities.particles.wrappers;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.NRType;
import com.evolgames.entities.NonRotatingChild;

import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.particles.pools.SparkPool;
import com.evolgames.entities.particles.modifiers.MyColorParticleInitializer;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.MyColorUtils;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;


public class FireParticleWrapper extends NonRotatingChild{
    private static final float RATE_MIN = 100;
    private static final float RATE_MAX = 200;
    private static final int PARTICLES_MAX = 300;


    public BatchedSpriteParticleSystem particleSystem;
    public CircleParticleEmitter emitter;
    private MyColorParticleInitializer<UncoloredSprite> colorInitializer;
    private ColorParticleModifier<UncoloredSprite> colorModifier;
    private double temperature;


    public FireParticleWrapper(float x, float y, double average, GameEntity gameEntity) {
        super(x, y, 0, gameEntity, NRType.EMITTER);

        IEntityFactory<UncoloredSprite> ief = SparkPool::obtain;
        this.emitter = new CircleParticleEmitter(x, y, 10);


        this.particleSystem = new BatchedSpriteParticleSystem(ief,
                this.emitter,
                FireParticleWrapper.RATE_MIN,
                FireParticleWrapper.RATE_MAX,
                FireParticleWrapper.PARTICLES_MAX, ResourceManager.getInstance().plasmaParticle1, ResourceManager.getInstance().vbom

        );


        updateTemp(average);


        VelocityParticleInitializer<UncoloredSprite> velocityInitializer = new VelocityParticleInitializer<>(-5, 5, 80, 100);
        this.particleSystem.addParticleInitializer(velocityInitializer);
        this.particleSystem.addParticleInitializer(new ScaleParticleInitializer<>(0.8f));

        this.particleSystem.addParticleInitializer(new AlphaParticleInitializer<>(1f));

        this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(0f, 0.5f, 1f, 0.9f));
        this.particleSystem.addParticleModifier(new ScaleParticleModifier<>(0, 0.5f, 0.7f, 0.2f));
        this.particleSystem.addParticleModifier(new OffCameraExpireParticleModifier<>(ResourceManager.getInstance().firstCamera));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(0.5f));

    }

    public void setPositionOfEmitter(float x, float y) {
        this.emitter.setCenter(x, y);
    }


    public void updateTemp(double temperature) {

        this.temperature = temperature;
           setFlameColor();
    }


    private void setFlameColor() {

        Color initialColor = MyColorUtils.getColor(temperature);

        if (this.colorInitializer == null) {
            this.colorInitializer = new MyColorParticleInitializer<>(initialColor.getRed(), initialColor.getGreen(), initialColor.getBlue());
            this.particleSystem.addParticleInitializer(this.colorInitializer);
        } else
            this.colorInitializer.updateColor(initialColor);

        Color previous = MyColorUtils.getColor(temperature>750?temperature-500:temperature);
        if (this.colorModifier != null) this.particleSystem.removeParticleModifier(this.colorModifier);

        this.colorModifier = new ColorParticleModifier<>(0f, 0.5f, initialColor.getRed(), previous.getRed(), initialColor.getGreen(), previous.getGreen(), initialColor.getBlue(), previous.getBlue());
        this.particleSystem.addParticleModifier(this.colorModifier);
    }


    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }


    public void update(CoatingBlock coatingBlock)
    {
     updateTemp(coatingBlock.getTemperature()
     );
    }
}
