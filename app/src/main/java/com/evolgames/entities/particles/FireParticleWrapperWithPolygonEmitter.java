package com.evolgames.entities.particles;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.BlockA;
import com.evolgames.entities.particles.systems.FlameParticleSystem;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.MyColorUtils;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.util.adt.color.Color;

public class FireParticleWrapperWithPolygonEmitter {
    private static final float RATE_MIN = 30 * 2;
    private static final float RATE_MAX = 50 * 2;
    private static final int PARTICLES_MAX = 75 * 2;

    public BatchedPseudoSpriteParticleSystem particleSystem;
    public PolygonEmitter emitter;

    private ColorParticleModifier<Entity> colorModifier;

    public FireParticleWrapperWithPolygonEmitter(GameEntity entity) {

        IEntityFactory<Entity> ief = SparkPool::obtain;
        this.emitter = new PolygonEmitter(entity);

        float area = 0;
        for (BlockA b : entity.getBlocks()) area += b.getArea();
        float ratio = area / (32f * 32f);

        this.particleSystem = new FlameParticleSystem(ief,
                this.emitter,
                FireParticleWrapperWithPolygonEmitter.RATE_MIN * ratio,
                FireParticleWrapperWithPolygonEmitter.RATE_MAX * ratio,
                (int) (FireParticleWrapperWithPolygonEmitter.PARTICLES_MAX * ratio + 1), ResourceManager.getInstance().plasmaParticle4, ResourceManager.getInstance().vbom

        );

        particleSystem.setZIndex(entity.getMesh().getZIndex() + 1);

        VelocityParticleInitializer<Entity> velocityInitializer = new VelocityParticleInitializer<>(0, 0, 120, 140);
        this.particleSystem.addParticleInitializer(velocityInitializer);

        //this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(0f, 0.5f, 1f, 0.9f));
        //this.particleSystem.addParticleModifier(new ScaleParticleModifier<>(0.4f, 0.5f, 0.6f, 1f));
        this.particleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 0.5f, 0.9f, 0f));

        // this.particleSystem.addParticleModifier(new OffCameraExpireParticleModifier<>(ResourceManager.getInstance().firstCamera));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(0.5f));
        setFlameColor();
    }

    public void update() {
        emitter.update();
    }

    public ParticleSystem<?> getParticleSystem() {
        return particleSystem;
    }

    private void setFlameColor() {

        double temperature = 1500;
        Color initialColor = MyColorUtils.getColor(temperature);


        Color previous = MyColorUtils.getColor(temperature - 1000);
        if (this.colorModifier != null)
            this.particleSystem.removeParticleModifier(this.colorModifier);

        this.colorModifier = new ColorParticleModifier<>(0f, 0.5f, initialColor.getRed(), previous.getRed(), initialColor.getGreen(), previous.getGreen(), initialColor.getBlue(), previous.getBlue());
        this.particleSystem.addParticleModifier(this.colorModifier);
    }

}
