package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class HandControl {
    protected Body weapon;
    protected int count;
    private  boolean dead = false;
    private boolean hasLifespan = false;
    private int lifespan;

    public HandControl(Body weapon) {
        this.weapon = weapon;
    }

    public HandControl(Body weapon, int lifespan) {
        this.hasLifespan = true;
        this.weapon = weapon;
        this.lifespan = lifespan;
    }

    public void run(){
        if(dead)return;
        if(hasLifespan) {
            count++;
            if(count>lifespan)dead = true;
        }
    }

    public Body getWeapon() {
        return weapon;
    }
    public boolean isDead(){
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
