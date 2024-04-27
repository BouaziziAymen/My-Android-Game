package com.evolgames.dollmutilationgame.entities.particles.wrappers;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.particles.emitters.RelativePolygonEmitter;
import com.evolgames.dollmutilationgame.entities.particles.modifiers.AlphaParticleModifier;
import com.evolgames.dollmutilationgame.entities.particles.systems.FireParticleSystem;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.blocks.LayerBlock;
import com.evolgames.dollmutilationgame.entities.particles.modifiers.BezierModifier;

import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public class FluxParticleWrapper extends MyParticleWrapper{
    private static final float RATE_MIN = 30 * 30;
    private static final float RATE_MAX = 50 * 30;
    private static final int PARTICLES_MAX = 750 * 30;
    private final BatchedSpriteParticleSystem particleSystem;
    private final RelativePolygonEmitter startEmitter;
    private final RelativePolygonEmitter endEmitter;
    private final BezierModifier bezierModifier;
    private final GameEntity source;
    private final GameEntity target;


    public FluxParticleWrapper(GameEntity source, GameEntity target) {
        this.startEmitter = new RelativePolygonEmitter(source, (b) -> b.getParent().getId() == 3);
        this.endEmitter = new RelativePolygonEmitter(target, (b) -> true);
        this.source = source;
        this.target = target;

        float area = 0;
        for (LayerBlock b : source.getBlocks()) {
            area += b.getBlockArea();
        }
        float ratio = area / (32f * 32f);

        this.particleSystem =
                new FireParticleSystem(
                        this.startEmitter,
                        FluxParticleWrapper.RATE_MIN * ratio,
                        FluxParticleWrapper.RATE_MAX * ratio,
                        (int) (FluxParticleWrapper.PARTICLES_MAX * ratio + 1),
                        ResourceManager.getInstance().plasmaParticle);

        final float lifespan = 1f;
        this.particleSystem.setZIndex(source.getZIndex() - 1);
        this.bezierModifier = new BezierModifier(this.endEmitter, lifespan);
        this.particleSystem.addParticleModifier(this.bezierModifier);
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(lifespan));
        this.particleSystem.addParticleInitializer(new AlphaParticleInitializer<>(0.1f));
        this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(lifespan - 0.5f, lifespan, 0.1f, 0.05f));
        this.particleSystem.addParticleModifier(new ScaleParticleModifier<>(0f, lifespan, 0.9f, 0.4f));
        setFluxColor();
        update();
    }

    public ParticleSystem<?> getParticleSystem() {
        return this.particleSystem;
    }

    @Override
    public void attachTo(Scene scene) {

    }

    public void update() {
        if(this.endEmitter!=null) {
            this.endEmitter.update();
        }
        if(this.startEmitter!=null){
            this.startEmitter.update();
        }
        if (detached) {
            if (isAllParticlesExpired()) {
                ResourceManager.getInstance().activity.runOnUpdateThread(this::detachDirect);
            }
        } else {
            bezierModifier.transform(new Vector2(source.getX(), source.getY()), new Vector2(target.getX(), target.getY()));
        }
        }

    @Override
    public void detach() {
        this.detached = true;
        this.setSpawnEnabled(false);
    }

    @Override
    public void detachDirect() {
        if(alive) {
            this.alive = false;
            this.particleSystem.detachSelf();
            this.particleSystem.dispose();
        }
    }

    public void setSpawnEnabled(boolean pParticlesSpawnEnabled) {
        if (particleSystem != null) {
            this.particleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
        }
    }

    private void setFluxColor() {
        ColorParticleModifier<UncoloredSprite> colorModifier = new ColorParticleModifier<>(
                0f,
                0.3f,
                Color.BLACK.getRed(),
                Color.BLACK.getRed(),
                Color.BLACK.getGreen(),
                Color.BLACK.getGreen(),
                Color.BLACK.getBlue(),
                Color.BLACK.getBlue());
        this.particleSystem.addParticleModifier(colorModifier);
    }
    @Override
    public boolean isAllParticlesExpired() {
        for (Particle<?> particle : this.particleSystem.getParticles()) {
            if (particle != null && !particle.isExpired()) {
                return false;
            }
        }
        return true;
    }
}

