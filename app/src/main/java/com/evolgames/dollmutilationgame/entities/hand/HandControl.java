package com.evolgames.dollmutilationgame.entities.hand;

public abstract class HandControl {

    protected int count;
    protected int lifespan;
    transient protected Hand hand;
    private boolean hasLifespan;
    private boolean dead = false;

    public HandControl() {
    }

    public HandControl(Hand hand) {
        this.hasLifespan = false;
        this.hand = hand;
    }

    public HandControl(Hand hand, int lifespan) {
        this.hasLifespan = true;
        this.lifespan = lifespan;
        this.hand = hand;
    }

    public void run() {
        if (dead) {
            return;
        }
        if (hasLifespan) {
            count++;
            if (count > lifespan) {
                dead = true;
            }
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
