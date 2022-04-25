package com.evolgames.entities.cut;
import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.particles.LiquidParticleWrapper;

public class FreshCut {
    public Vector2 first;
    public Vector2 second;
    private boolean alive = true;

    public LiquidParticleWrapper getLiquidParticleWrapper() {
        return liquidParticleWrapper;
    }

    public void setLiquidParticleWrapper(LiquidParticleWrapper liquidParticleWrapper) {
        this.liquidParticleWrapper = liquidParticleWrapper;
    }

    private LiquidParticleWrapper liquidParticleWrapper;

public String toString(){
    return "{"+first+first.hashCode()+","+second+second.hashCode()+"}";
}

    public FreshCut(Vector2 first, Vector2 second) {
        this.first = first;
        this.second = second;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }


    public float getLength() {
        return first.dst(second);
    }
}
