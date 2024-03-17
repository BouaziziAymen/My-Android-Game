package com.evolgames.entities.particles.wrappers;

import com.evolgames.activity.ResourceManager;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.CoatingBlock;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.particles.emitters.FireEmitter;
import com.evolgames.entities.particles.modifiers.AlphaParticleModifier;
import com.evolgames.entities.particles.systems.FireParticleSystem;
import com.evolgames.utilities.MyColorUtils;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public class FireParticleWrapper implements Fire {
    private static final float RATE_MIN = 30 * 3;
    private static final float RATE_MAX = 50 * 3;
    private static final int PARTICLES_MAX = 75 * 3;
    private final GameEntity gameEntity;
    public BatchedSpriteParticleSystem particleSystem;
    public FireEmitter emitter;
    private ColorParticleModifier<UncoloredSprite> colorModifier;

    public FireParticleWrapper(GameEntity entity) {
        this.gameEntity = entity;
        this.emitter = new FireEmitter(entity);

        float area = 0;
        for (LayerBlock b : entity.getBlocks()) {
            area += b.getBlockArea();
        }
        float ratio = area / (32f * 32f);

        this.particleSystem =
                new FireParticleSystem(
                        this.emitter,
                        FireParticleWrapper.RATE_MIN * ratio,
                        FireParticleWrapper.RATE_MAX * ratio,
                        (int) (FireParticleWrapper.PARTICLES_MAX * ratio + 1),
                        ResourceManager.getInstance().plasmaParticle);

        particleSystem.setZIndex(entity.getMesh().getZIndex() + 1);

        VelocityParticleInitializer<UncoloredSprite> velocityInitializer =
                new VelocityParticleInitializer<>(0, 0, 120, 140);
        this.particleSystem.addParticleInitializer(velocityInitializer);
        this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(0f, 0.5f, 1f, 0f));
        this.particleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, 0.5f, 0.7f, 0.2f));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(0.5f));
        setFlameColor();
    }

    public void update() {
        this.emitter.onStep();
        this.setFlameColor();
    }

    public ParticleSystem<?> getParticleSystem() {
        return particleSystem;
    }

    private void setFlameColor() {
        float totalTemp =
                (float)
                        gameEntity.getBlocks().stream()
                                .flatMapToDouble(
                                        b ->
                                                b.getBlockGrid().getCoatingBlocks().stream()
                                                        .mapToDouble(CoatingBlock::getTemperature))
                                .sum();
        int count =
                (int)
                        gameEntity.getBlocks().stream()
                                .flatMap(b -> b.getBlockGrid().getCoatingBlocks().stream())
                                .count();
        float temperature = totalTemp / count;
        Color initialColor = MyColorUtils.getColor(temperature);
        Color previous = MyColorUtils.getColor(MyColorUtils.getPreviousTemperature(temperature));
        if (this.colorModifier != null) {
            this.particleSystem.removeParticleModifier(this.colorModifier);
        }

        this.colorModifier =
                new ColorParticleModifier<>(
                        0f,
                        0.3f,
                        initialColor.getRed(),
                        previous.getRed(),
                        initialColor.getGreen(),
                        previous.getGreen(),
                        initialColor.getBlue(),
                        previous.getBlue());
        this.particleSystem.addParticleModifier(this.colorModifier);
    }

    @Override
    public ParticleSystem<UncoloredSprite> getFireParticleSystem() {
        return particleSystem;
    }

    @Override
    public double getParticleTemperature(Particle<?> particle) {
        return ((CoatingBlock) particle.getEntity().getUserData()).getTemperature();
    }
}
