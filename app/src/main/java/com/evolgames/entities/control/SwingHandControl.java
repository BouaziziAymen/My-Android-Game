package com.evolgames.entities.control;

import com.badlogic.gdx.physics.box2d.Body;

public class SwingHandControl extends HandControl {
    private int duration;
    private float impulse;
    public SwingHandControl(Body weapon,int lifespan, int duration, float impulse) {
        super(weapon,lifespan);
        this.impulse = impulse;
        this.duration = duration;
    }

    @Override
    public void run() {
        super.run();
        if(count<duration){
            weapon.applyAngularImpulse(impulse);
        }
    }
}
