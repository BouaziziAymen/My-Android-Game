package com.evolgames.entities.hand;

public abstract class HandControl {

    protected int count;
    protected Runnable runnable;
    private  boolean dead = false;
    private final boolean hasLifespan;
    protected int lifespan;
    private final HandAction handAction;

    public HandControl(){
        this.hasLifespan = false;
        this.handAction = HandAction.NONE;
    }
    public HandControl(HandAction handAction){
        this.hasLifespan = false;
        this.handAction = handAction;
    }
    public HandControl(int lifespan) {
        this.hasLifespan = true;
        this.lifespan = lifespan;
        this.handAction = HandAction.NONE;
    }

    public void run(){
        if(dead){
            return;
        }
        if(hasLifespan) {
            count++;
            if(count>lifespan){
                dead = true;
            }
        }
    }

    public boolean isDead(){
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public HandAction getHandAction() {
        return handAction;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }
}
