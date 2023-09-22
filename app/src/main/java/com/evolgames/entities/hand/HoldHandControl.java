package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.helpers.utilities.MathUtils;

public class HoldHandControl extends HandControl {
    private final Body weapon;
    private float angle;
    public HoldHandControl(Body weapon, float angleDegrees) {
        super();
        this.weapon = weapon;
        this.angle = angleDegrees * MathUtils.degreesToRadians;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public void run() {
        super.run();
        if(weapon==null){
            setDead(true);
        }
        if(!isDead()) {
            assert weapon != null;
            float rot = weapon.getAngle();
            float error = angle - rot;

            while (error < -Math.PI) error += 2 * Math.PI;
            while (error > Math.PI) error -= 2 * Math.PI;
            if (Math.abs(error) > 0.005f) weapon.setAngularVelocity(10 * error);
        }
    }
}
