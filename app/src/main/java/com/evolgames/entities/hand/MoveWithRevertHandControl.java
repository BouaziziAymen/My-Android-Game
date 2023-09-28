package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.scenes.hand.Hand;

public class MoveWithRevertHandControl extends HandControl {

    private final Hand hand;
    private final Vector2 start;
    private final HoldHandControl control;

    public MoveWithRevertHandControl(Hand hand, Vector2 target) {
        super(60);
        this.start = hand.getMouseJoint().getTarget().cpy();
        this.hand = hand;
        hand.getMouseJoint().setTarget(target);
        this.control = new HoldHandControl(hand.getGrabbedEntity());
    }

    @Override
    public void run() {
        super.run();
        MouseJoint mouseJoint = hand.getMouseJoint();
        if (mouseJoint == null||!hand.getGrabbedEntity().isAlive()) {
            return;
        }

        if (runnable != null) {
              runnable.run();
        }
        if (!isDead()) {
            control.run();
            Vector2 v = mouseJoint.getBodyB().getLinearVelocity();
            if((v.len()<0.01f&&count>10)){
               setDead(true);
               mouseJoint.setTarget(start);
            }
        } else {
            mouseJoint.setTarget(start);
        }
    }

}
