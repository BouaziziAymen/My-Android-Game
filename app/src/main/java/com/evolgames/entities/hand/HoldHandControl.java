package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.basics.GameEntity;

public class HoldHandControl extends HandControl {

    private float angle;

    @SuppressWarnings("unused")
    public HoldHandControl() {
    }

    public HoldHandControl(Hand hand) {
        super(hand);
    }

    @Override
    public void run() {
        super.run();
        GameEntity heldEntity = hand.getHeldEntity();
        if (heldEntity == null || heldEntity.getBody() == null) {
            return;
        }
        Body body = heldEntity.getBody();
        if (!isDead()) {
            if (body != null) {
                float rot = body.getAngle();
                float error = this.angle - rot;

                while (error < -Math.PI) error += 2 * Math.PI;
                while (error > Math.PI) error -= 2 * Math.PI;
                if (Math.abs(error) > 0.01f) {
                    body.setAngularVelocity(10 * error);
                }
            }
        }
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
