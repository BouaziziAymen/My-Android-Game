package com.evolgames.dollmutilationgame.entities.particles.wrappers;

import com.evolgames.dollmutilationgame.activity.ResourceManager;
import com.evolgames.dollmutilationgame.entities.particles.emitters.DataEmitter;
import com.evolgames.dollmutilationgame.entities.particles.emitters.SegmentEmitter;
import com.evolgames.dollmutilationgame.entities.particles.modifiers.AlphaParticleModifier;
import com.evolgames.dollmutilationgame.entities.particles.systems.BaseParticleSystem;

import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public class FrostParticleWrapper extends MyParticleWrapper {
    private final BaseParticleSystem particleSystem;
    private final Color color;
    private final DataEmitter emitter;
    private boolean alive = true;

    public FrostParticleWrapper(
            float[] data, Color color,
            float startVelocity,
            int lowerRate,
            int higherRate) {
        emitter = createEmitter(data);
        this.color = color;

        this.particleSystem =
                new BaseParticleSystem(
                        emitter,
                        lowerRate,
                        higherRate,
                        10 * lowerRate,
                        ResourceManager.getInstance().frostParticle,
                        ResourceManager.getInstance().vbom);

        VelocityParticleInitializer<UncoloredSprite> velocityInitializer =
                new VelocityParticleInitializer<>(-50, 50, 0, 0);
        this.particleSystem.addParticleInitializer(velocityInitializer);


        this.particleSystem.addParticleInitializer(
                new ColorParticleInitializer<>(
                        color.getRed(), color.getGreen(), color.getBlue()));
        this.particleSystem.addParticleInitializer(new ScaleParticleInitializer<>(0.1f,1f));
        this.particleSystem.addParticleInitializer(new RotationParticleInitializer<>(0,360));
        this.addGravity();
        this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(1f, 3f, 0.5f, 0.4f));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(3f));
        this.particleSystem.setParticlesSpawnEnabled(true);
    }

    private void addGravity() {
        GravityParticleInitializer<UncoloredSprite> gravity = new GravityParticleInitializer<>();
        gravity.setAccelerationY(-10 * 32);
        this.particleSystem.addParticleInitializer(gravity);
    }

    protected DataEmitter createEmitter(float[] emitterData) {
        return new SegmentEmitter(null, emitterData);
    }
    public void update(float x, float y) {
        emitter.updatePosition(x,y);
    }

    public Color getColor() {
        return color;
    }

    public BaseParticleSystem getParticleSystem() {
        return particleSystem;
    }

    @Override
    public void attachTo(Scene scene) {
         particleSystem.setZIndex(5);
         scene.attachChild(particleSystem);
    }

    @Override
    public void update() {
        if(!alive){
            return;
        }
        if(this.emitter!=null) {
            this.emitter.update();
        }
        if (detached) {
            if (isAllParticlesExpired()) {
                ResourceManager.getInstance().activity.runOnUpdateThread(this::detachDirect);
            }
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

    public boolean isAllParticlesExpired() {
        for (Particle<UncoloredSprite> p : getParticleSystem().getParticles())
            if (p != null && !p.isExpired()) {
                return false;
            }
        return true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setSpawnEnabled(boolean pParticlesSpawnEnabled) {
        if (this.particleSystem != null) {
            this.particleSystem.setParticlesSpawnEnabled(pParticlesSpawnEnabled);
        }
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
