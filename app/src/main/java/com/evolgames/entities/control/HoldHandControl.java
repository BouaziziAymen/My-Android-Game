package com.evolgames.entities.control;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.helpers.utilities.MathUtils;

public class HoldHandControl extends HandControl {
    private float angle;
    public HoldHandControl(Body weapon, float angleDegree) {
        super(weapon);
        angle = angleDegree * MathUtils.degreesToRadians;
    }

    @Override
    public void run() {
        super.run();
        Body body = weapon;
        float rot = body.getAngle();
        float error = angle - rot;

        while (error < -Math.PI) error += 2 * Math.PI;
        while (error > Math.PI) error -= 2 * Math.PI;
if(Math.abs(error)>0.005f)
        body.setAngularVelocity(100 * error);
    }
}
