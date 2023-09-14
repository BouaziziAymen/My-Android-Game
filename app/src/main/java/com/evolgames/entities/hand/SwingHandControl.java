package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

public class SwingHandControl extends HandControl {
    private final int duration;
    private final float impulse;
    private final MouseJoint mouseJoint;
    private float dx = -2f;

    public SwingHandControl(Body weapon, MouseJoint mouseJoint,int lifespan, int duration, float impulse) {
        super(weapon,lifespan);
        this.impulse = impulse;
        this.duration = duration;
        this.mouseJoint = mouseJoint;
    }
private boolean affected = false;
    @Override
    public void run() {
        super.run();
        this.weapon.setBullet(true);

        if(!isDead()&&count<duration/2){
            if(!affected){
                affected = true;
                this.mouseJoint.setTarget(this.mouseJoint.getTarget().cpy().add(dx,0));
            }
            weapon.setAngularVelocity(impulse/100);
        } else {
            if(affected){
                affected = false;
                this.mouseJoint.setTarget(this.mouseJoint.getTarget().cpy().add(-dx,0));
            }
           // this.weapon.setBullet(false);
        }
    }
}
