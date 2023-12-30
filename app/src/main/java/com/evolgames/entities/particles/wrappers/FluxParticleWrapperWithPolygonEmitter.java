package com.evolgames.entities.particles.wrappers;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.particles.emitters.FireEmitter;
import com.evolgames.entities.particles.modifiers.AlphaParticleModifier;
import com.evolgames.entities.particles.pools.FireSpritePool;
import com.evolgames.entities.particles.systems.FireParticleSystem;
import com.evolgames.gameengine.ResourceManager;

import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public class FluxParticleWrapperWithPolygonEmitter{
    private static final float RATE_MIN = 30 * 3;
    private static final float RATE_MAX = 50 * 3;
    private static final int PARTICLES_MAX = 75 * 3;
    private final GameEntity gameEntity;
    public BatchedSpriteParticleSystem fluxParticleSystem;
    public FireEmitter emitter;

    public FluxParticleWrapperWithPolygonEmitter(GameEntity entity) {
        this.gameEntity = entity;
        IEntityFactory<UncoloredSprite> ief = FireSpritePool::obtain;
        this.emitter = new FireEmitter(entity);

        float area = 0;
        for (LayerBlock b : entity.getBlocks()) {
            area += b.getBlockArea();
        }
        float ratio = area / (32f * 32f);

        this.fluxParticleSystem =
                new FireParticleSystem(
                        this.emitter,
                        FluxParticleWrapperWithPolygonEmitter.RATE_MIN * ratio,
                        FluxParticleWrapperWithPolygonEmitter.RATE_MAX * ratio,
                        (int) (FluxParticleWrapperWithPolygonEmitter.PARTICLES_MAX * ratio + 1),
                        ResourceManager.getInstance().plasmaParticle);

        fluxParticleSystem.setZIndex(entity.getMesh().getZIndex() + 1);

        VelocityParticleInitializer<UncoloredSprite> velocityInitializer =
                new VelocityParticleInitializer<>(0, 0, 120, 140);
        this.fluxParticleSystem.addParticleInitializer(velocityInitializer);

        setFluxColor(Color.BLACK,Color.BLACK);
    }

    public void update() {
        this.emitter.onStep();
        this.setFluxColor(Color.RED,Color.RED);
    }

    private void setFluxColor(Color color1, Color color2) {
        ColorParticleModifier<UncoloredSprite> colorModifier = new ColorParticleModifier<>(
                0f,
                0.3f,
                color1.getRed(),
                color2.getRed(),
                color1.getGreen(),
                color2.getGreen(),
                color1.getBlue(),
                color2.getBlue());
        this.fluxParticleSystem.addParticleModifier(colorModifier);
    }
}

