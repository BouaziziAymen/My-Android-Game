package com.evolgames.entities.particles;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.systems.MyBatchedSpriteParticleSystem;
import com.evolgames.gameengine.ResourceManager;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.userinterface.control.behaviors.actions.Action;

import is.kul.learningandengine.particlesystems.modifiers.AlphaParticleModifier;
import is.kul.learningandengine.particlesystems.modifiers.ExpireParticleInitializer;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.GravityParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.OffCameraExpireParticleModifier;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.util.adt.color.Color;

public abstract class LiquidParticleWrapper {

    private final VelocityParticleInitializer<Entity> velocityInitializer;
    private final Color color;
    private final MyBaseEmitter emitter;
    private MyBatchedSpriteParticleSystem particleSystem;
    private GameEntity parent;
    private Action spawnAction;
    private boolean alive = true;
    public static final float particleWidth = ResourceManager.getInstance().liquidParticle.getWidth();
    public static final float particleHeight = ResourceManager.getInstance().liquidParticle.getHeight();
    private int Limit;

    public LiquidParticleWrapper(Color color,float[] data,int lowerRate,int higherRate) {
        this.color = color;

        emitter = createEmitter(data);
        IEntityFactory<Entity> ief = LiquidParticlePool::obtain;


        this.particleSystem = new MyBatchedSpriteParticleSystem(ief,this,emitter, lowerRate, higherRate, 2*higherRate, ResourceManager.getInstance().liquidParticle, ResourceManager.getInstance().vbom);

        velocityInitializer = new VelocityParticleInitializer<>(0, 0, 0, 0);
        this.particleSystem.addParticleInitializer(velocityInitializer);

        this.particleSystem.addParticleInitializer(new ColorParticleInitializer<>(color.getRed(), color.getGreen(), color.getBlue()));
        this.particleSystem.addParticleInitializer(new AlphaParticleInitializer<>(color.getAlpha()));
        GravityParticleInitializer gravity = new GravityParticleInitializer<>();
        gravity.setAccelerationY(-32 * 10);

        this.particleSystem.addParticleInitializer(gravity);
        particleSystem.addParticleModifier(new AlphaParticleModifier<>(0.5f,1f,1f,0f));
        particleSystem.addParticleModifier(new OffCameraExpireParticleModifier<>(ResourceManager.getInstance().firstCamera));
        this.particleSystem.addParticleInitializer(new ExpireParticleInitializer<>(1f));



    }


    protected abstract MyBaseEmitter createEmitter(float[] emitterData);

    public Color getColor() {
        return color;
    }

    public void setParent(GameEntity entity) {
        this.parent = entity;
    }
public void update(){

        updatePosition();
        if(parent!=null)
        if(parent.getBody()!=null) {
            Vector2 velocity = parent.getBody().getLinearVelocityFromLocalPoint(new Vector2(emitter.getCenterX() / 32f, emitter.getCenterY() / 32)).cpy().mul(16);
            setInitialVelocity(velocity.x, velocity.y, velocity.x, velocity.y);
        }
}
    private void updatePosition() {
        if(parent==null)return;
        float x = parent.getMesh().getX();
        float y = parent.getMesh().getY();
        float rot = parent.getMesh().getRotation();
        GeometryUtils.transformation.setToIdentity();
        GeometryUtils.transformation.preTranslate(x, y);
        GeometryUtils.transformation.preRotate(-rot);
        emitter.update(GeometryUtils.transformation);
    }

    public MyBatchedSpriteParticleSystem getParticleSystem() {
        return particleSystem;
    }

    private void setInitialVelocity(float Vxmin, float Vxmax, float Vymin, float Vymax) {
        velocityInitializer.setVelocity(Vxmin, Vxmax, Vymin, Vymax);
    }

    public void finishSelf() {
        particleSystem.setParticlesSpawnEnabled(false);
        setAlive(false);
    }


    public void setSpawnAction(Action spawnAction) {
        this.spawnAction = spawnAction;
    }


    public void performSpawnAction(){
        if(spawnAction!=null)
        spawnAction.performAction();
    }
    public void recycleParticles(){
        for(Particle p:getParticleSystem().getParticles())
            if(p!=null) LiquidParticlePool.recycle((UncoloredSprite) p.getEntity());
    }
public boolean isAllParticlesExpired(){
    for(Particle p:getParticleSystem().getParticles())
       if(p!=null&&!p.isExpired())return false;
       return true;
}

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setLimit(int limit) {
        this.Limit = limit*10;
    }

    public int getLimit() {
        return Limit;
    }

    public void decrementLimit() {
        if(Limit>=1)Limit--;
    }
}
