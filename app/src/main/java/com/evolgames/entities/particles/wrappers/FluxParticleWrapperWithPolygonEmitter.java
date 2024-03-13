package com.evolgames.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.particles.emitters.RelativePolygonEmitter;
import com.evolgames.entities.particles.modifiers.AlphaParticleModifier;
import com.evolgames.entities.particles.modifiers.BezierModifier;
import com.evolgames.entities.particles.systems.FireParticleSystem;
import com.evolgames.activity.ResourceManager;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public class FluxParticleWrapperWithPolygonEmitter {
    private static final float RATE_MIN = 30 * 100;
    private static final float RATE_MAX = 50 * 100;
    private static final int PARTICLES_MAX = 750 * 100;
    private final BatchedSpriteParticleSystem energyParticleSystem;
    private final RelativePolygonEmitter startEmitter;
    private final RelativePolygonEmitter endEmitter;
    private final BezierModifier bezierModifier;
    private final GameEntity source;
    private final GameEntity target;


    public FluxParticleWrapperWithPolygonEmitter(GameEntity source, GameEntity target) {
        this.startEmitter = new RelativePolygonEmitter(source,  (b) -> b.getParent().getId()==3);
        this.endEmitter = new RelativePolygonEmitter(target, (b) -> true);
        this.source = source;
        this.target = target;

        float area = 0;
        for (LayerBlock b : source.getBlocks()) {
            area += b.getBlockArea();
        }
        float ratio = area / (32f * 32f);

        this.energyParticleSystem =
                new FireParticleSystem(
                        this.startEmitter,
                        FluxParticleWrapperWithPolygonEmitter.RATE_MIN * ratio,
                        FluxParticleWrapperWithPolygonEmitter.RATE_MAX * ratio,
                        (int) (FluxParticleWrapperWithPolygonEmitter.PARTICLES_MAX * ratio + 1),
                        ResourceManager.getInstance().plasmaParticle);

        final float lifespan = 1f;
        this.energyParticleSystem.setZIndex(source.getMesh().getZIndex() - 1);
        this.bezierModifier = new BezierModifier(this.endEmitter, lifespan);
        this.energyParticleSystem.addParticleModifier(this.bezierModifier);
        this.energyParticleSystem.addParticleInitializer(new ExpireParticleInitializer<>(lifespan));
        this.energyParticleSystem.addParticleInitializer(new AlphaParticleInitializer<>(0.1f));
        this.energyParticleSystem.addParticleModifier(new AlphaParticleModifier<>(lifespan-0.05f,lifespan,0.1f,0f));
        this.energyParticleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, lifespan-0.1f, 0.2f, 0.2f));
        this.energyParticleSystem.addParticleModifier(new ScaleParticleModifier<>(lifespan-0.1f, lifespan, 0.2f, 0f));
        setFluxColor(Color.BLACK, Color.BLACK);
    }
    public ParticleSystem<?> getParticleSystem() {
        return this.energyParticleSystem;
    }

    public void update() {
        this.startEmitter.onStep();
        this.endEmitter.onStep();

        bezierModifier.transform(new Vector2(source.getMesh().getX(),source.getMesh().getY()),new Vector2(target.getMesh().getX(),target.getMesh().getY()));
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
        this.energyParticleSystem.addParticleModifier(colorModifier);
    }
}

