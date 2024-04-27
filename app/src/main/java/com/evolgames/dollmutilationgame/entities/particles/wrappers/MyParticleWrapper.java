package com.evolgames.dollmutilationgame.entities.particles.wrappers;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import org.andengine.entity.scene.Scene;

public abstract class MyParticleWrapper {

    protected  GameEntity parent;
    protected boolean alive = true;
    protected boolean detached = false;

    public abstract void attachTo(Scene scene);
    public abstract void update();

    public abstract void detach();

    public abstract void detachDirect();

    protected abstract boolean isAllParticlesExpired();

    public boolean isAlive() {
        return alive;
    }

    public GameEntity getParent() {
        return parent;
    }
}
