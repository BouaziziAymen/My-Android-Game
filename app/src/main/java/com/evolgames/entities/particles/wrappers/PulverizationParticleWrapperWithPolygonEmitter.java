package com.evolgames.entities.particles.wrappers;

import android.support.v4.graphics.drawable.IconCompat;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.blocks.LayerBlock;
import com.evolgames.entities.particles.emitters.PowderEmitter;
import com.evolgames.entities.particles.initializers.AirFieldVelocityInitializer;
import com.evolgames.entities.particles.modifiers.AlphaParticleModifier;
import com.evolgames.entities.particles.modifiers.GroundCollisionStop;
import com.evolgames.entities.particles.pools.FireSpritePool;
import com.evolgames.entities.particles.systems.PulverizationParticleSystem;
import com.evolgames.physics.WorldFacade;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.sprite.UncoloredSprite;

public class PulverizationParticleWrapperWithPolygonEmitter {
        private static final float RATE_MIN =   1000;
    private static final float RATE_MAX = 1000;
    private static final int PARTICLES_MAX = 1000;
    public BatchedSpriteParticleSystem particleSystem;
    public PowderEmitter emitter;
    private int step = 0;
    private boolean alive = true;

    public PulverizationParticleWrapperWithPolygonEmitter(WorldFacade worldFacade, LayerBlock layerBlock, Vector2 bodyVelocity) {

        IEntityFactory<UncoloredSprite> ief = FireSpritePool::obtain;
        this.emitter = new PowderEmitter(layerBlock.getBlockGrid().getCoatingBlocks());
        float ratio = layerBlock.getBlockArea() / (32f * 32f);

        this.particleSystem = new PulverizationParticleSystem(ief,
                this.emitter,
                PulverizationParticleWrapperWithPolygonEmitter.RATE_MIN * ratio,
                PulverizationParticleWrapperWithPolygonEmitter.RATE_MAX * ratio,
                (int) (PulverizationParticleWrapperWithPolygonEmitter.PARTICLES_MAX * ratio + 1)
        );


        // private final ColorParticleInitializer<Entity> colorParticleInitializer;
        AirFieldVelocityInitializer velocityInitializer = new AirFieldVelocityInitializer(worldFacade, bodyVelocity);
       this.particleSystem.addParticleInitializer(velocityInitializer);
        this.particleSystem.addParticleInitializer(new ScaleParticleInitializer<>(0.05f));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(10f));
      this.particleSystem.addParticleModifier(new GroundCollisionStop(20));
     this.particleSystem.addParticleModifier(new AlphaParticleModifier<>(8f,10f,1f,0));
       //this.colorParticleInitializer = new ColorParticleInitializer<>(layerBlock.getProperties().getDefaultColor());
        addGravity();
       // this.particleSystem.addParticleInitializer(colorParticleInitializer);
    }


    private void addGravity() {
        GravityParticleInitializer<UncoloredSprite> gravity = new GravityParticleInitializer<>();
        gravity.setAccelerationY(-3 * 10 * 32);
        this.particleSystem.addParticleInitializer(gravity);
    }


    public BatchedSpriteParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public boolean allExpired(){
        for(Particle particle:particleSystem.getParticles()){
            if(particle!=null&&!particle.isExpired()){
                return false;
            }
        }
        return true;
    }
    public void update() {
        if(step>10){
            particleSystem.setParticlesSpawnEnabled(false);
        }
        step++;
    }
    public void finishSelf() {
        particleSystem.detachSelf();
        this.alive = false;
    }

    public boolean isAlive() {
        return alive;
    }
}

