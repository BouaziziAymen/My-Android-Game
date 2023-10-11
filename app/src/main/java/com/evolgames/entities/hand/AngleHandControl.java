package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.scenes.Hand;

public class AngleHandControl extends HandControl {

    private final Hand hand;
    private final float angle;
    private boolean thrown = false;
    private final AngleTrigger angleTrigger;

    public AngleHandControl(Hand hand, float angle) {
        super();
        this.angle = angle;
        this.angleTrigger = new AngleTrigger(hand.getGrabbedEntity(), angle);
        this.hand = hand;
    }

    @Override
    public void run() {
        super.run();
        if (hand.getMouseJoint() == null) {
            return;
        }
        Body body = hand.getMouseJoint().getBodyB();
        if (!isDead()) {
            assert body != null;
            float rot = body.getAngle();
            float error = GeometryUtils.calculateShortestDirectedDistance(rot * MathUtils.radiansToDegrees, angle);
            this.angleTrigger.run();
            if (!(Math.abs(error) > 5f)) {
                if (!thrown) {
                    setDead(true);
                    thrown = true;
                    if(this.runnable!=null) {
                        this.runnable.run();
                    }
                }
            }
        }
    }
}
