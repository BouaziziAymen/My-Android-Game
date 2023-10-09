package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.usage.Projectile;
import com.evolgames.helpers.utilities.GeometryUtils;
import com.evolgames.helpers.utilities.MathUtils;
import com.evolgames.scenes.hand.Hand;

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
                    body.setFixedRotation(false);
                    thrown = true;
                    hand.getGrabbedEntity().getGameScene().getPhysicsWorld().destroyJoint(hand.getMouseJoint());
                    Vector2 u = new Vector2(0,1);
                    GeometryUtils.rotateVectorDeg(u,angle);
                    hand.getGrabbedEntity().getBody().setLinearVelocity(50*u.x, 50*u.y);
                    hand.getGrabbedEntity().getUseList().add(new Projectile());
                    hand.onMouseJointDestroyed();
                    hand.clearStack();
                }
            }
        }
    }
}
