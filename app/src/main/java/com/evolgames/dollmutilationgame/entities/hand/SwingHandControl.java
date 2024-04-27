package com.evolgames.dollmutilationgame.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;

public class SwingHandControl extends HandControl {

    private float speed;

    @SuppressWarnings("unused")
    public SwingHandControl() {
    }

    public SwingHandControl(Hand hand, int speed, float ratio) {
        super(hand, (int) (ratio / Math.abs(speed)));
        this.speed = speed;
    }

    @Override
    public void run() {
        super.run();
        if (this.hand == null || this.hand.getMouseJoint() == null) {
            return;
        }
        Body body = this.hand.getMouseJoint().getBodyB();
        if (body != null) {
            if (!isDead()) {
                body.setAngularVelocity(speed);
            } else {
                body.setAngularVelocity(0f);
            }
        }
    }
}
