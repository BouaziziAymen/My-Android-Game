package com.evolgames.entities.particles.wrappers;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.particles.emitters.FireEmitter;
import com.evolgames.entities.particles.emitters.PowderEmitter;
import com.evolgames.entities.particles.pools.SparkPool;
import com.evolgames.entities.particles.systems.FireParticleSystem;
import com.evolgames.entities.particles.systems.FlowingParticleSystem;
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
    private static final float RATE_MIN = 30 * 3;
    private static final float RATE_MAX = 50 * 3;
    private static final int PARTICLES_MAX = 75 * 3;

    public BatchedPseudoSpriteParticleSystem particleSystem;
    public FireEmitter emitter;

    private ColorParticleModifier<Entity> colorModifier;

    public FireParticleWrapperWithPolygonEmitter(GameEntity entity) {

        IEntityFactory<Entity> ief = SparkPool::obtain;
        this.emitter = new FireEmitter(entity);

        float area = 0;
        for (LayerBlock b : entity.getBlocks()){
                area += b.getBlockArea();
        }
        float ratio = area / (32f * 32f);

        this.particleSystem = new FireParticleSystem(ief,
                this.emitter,
                FireParticleWrapperWithPolygonEmitter.RATE_MIN * ratio,
                FireParticleWrapperWithPolygonEmitter.RATE_MAX * ratio,
                (int) (FireParticleWrapperWithPolygonEmitter.PARTICLES_MAX * ratio + 1), ResourceManager.getInstance().plasmaParticle4

        );

        particleSystem.setZIndex(entity.getMesh().getZIndex() + 1);

        VelocityParticleInitializer<Entity> velocityInitializer = new VelocityParticleInitializer<>(0, 0, 120, 140);
        this.particleSystem.addParticleInitializer(velocityInitializer);;
        this.particleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 0.5f, 0.9f, 0f));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(0.5f));
        setFlameColor();
    }

    public void update() {

        emitter.onStep();
    }

    public ParticleSystem<?> getParticleSystem() {
        return particleSystem;
    }

    private void setFlameColor() {

        double temperature = 1500;
        Color initialColor = MyColorUtils.getColor(temperature);


        Color previous = MyColorUtils.getColor(temperature - 1000);
        if (this.colorModifier != null) {
            this.particleSystem.removeParticleModifier(this.colorModifier);
        }

        this.colorModifier = new ColorParticleModifier<>(0f, 0.5f, initialColor.getRed(), previous.getRed(), initialColor.getGreen(), previous.getGreen(), initialColor.getBlue(), previous.getBlue());
        this.particleSystem.addParticleModifier(this.colorModifier);
    }

}
