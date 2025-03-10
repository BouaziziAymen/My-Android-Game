package com.evolgames.dollmutilationgame.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

public class MoveHandControl extends HandControl {


    @SuppressWarnings("unused")
    public MoveHandControl() {
    }

    public MoveHandControl(Hand hand, Vector2 target) {
        super(hand);
        hand.getMouseJoint().setTarget(target);
    }

    @Override
    public void run() {
        super.run();
        MouseJoint mouseJoint = hand.getMouseJoint();
        if (mouseJoint == null || !hand.getGrabbedEntity().isAlive()) {
            return;
        }
        if (!isDead()) {
            Vector2 v = mouseJoint.getBodyB().getLinearVelocity();
            if ((v.len() < 0.01f && count > 10)) {
                setDead(true);
            }
        }
    }
}
