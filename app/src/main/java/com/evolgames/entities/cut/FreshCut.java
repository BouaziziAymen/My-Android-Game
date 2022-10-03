package com.evolgames.entities.cut;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.particles.wrappers.LiquidParticleWrapper;

public abstract class FreshCut {
    private boolean alive = true;
    private LiquidParticleWrapper liquidParticleWrapper;


    public LiquidParticleWrapper getLiquidParticleWrapper() {
        return liquidParticleWrapper;
    }

    public void setLiquidParticleWrapper(LiquidParticleWrapper liquidParticleWrapper) {
        this.liquidParticleWrapper = liquidParticleWrapper;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }


    public abstract float getLength();
}
