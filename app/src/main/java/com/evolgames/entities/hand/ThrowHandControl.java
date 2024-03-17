package com.evolgames.entities.hand;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.MathUtils;

public class ThrowHandControl extends HandControl {

    private AngleChangerHandControl angleChangerHandControl;
    private float angle;
    private boolean thrown = false;

    @SuppressWarnings("unused")
    public ThrowHandControl() {
    }

    public ThrowHandControl(Hand hand, float angle) {
        super(hand);
        this.angle = angle;
        this.angleChangerHandControl = new AngleChangerHandControl(hand, angle);
    }

    @Override
    public void run() {
        super.run();
        if (this.hand.getMouseJoint() == null) {
            return;
        }
        Body body = this.hand.getMouseJoint().getBodyB();
        if (!isDead()) {
            assert body != null;
            float rot = body.getAngle();
            float error =
                    GeometryUtils.calculateShortestDirectedDistance(rot * MathUtils.radiansToDegrees, angle);
            this.angleChangerHandControl.run();
            if (!(Math.abs(error) > 5f)) {
                if (!this.thrown) {
                    setDead(true);
                    this.thrown = true;
                    this.hand.doThrow();
                }
            }
        }
    }

    @Override
    public void setHand(Hand hand) {
        super.setHand(hand);
        this.angleChangerHandControl.setHand(hand);
    }
}
