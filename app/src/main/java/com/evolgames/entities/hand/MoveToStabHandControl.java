package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.basics.GameEntity;

public class MoveToStabHandControl extends HandControl {

    private Vector2 start;
    private Vector2 localPoint;
    private Vector2 position;
    private float maxForce;
    private float initialY;

    @SuppressWarnings("unused")
    public MoveToStabHandControl() {
    }

    public MoveToStabHandControl(Hand hand, Vector2 target, Vector2 localPoint) {
        super(hand, 60);
        this.localPoint = localPoint;
        this.start = hand.getMouseJoint().getTarget().cpy();
        this.position = new Vector2();
        this.position.set(getGrabbedEntity().getBody().getWorldPoint(localPoint));
        this.maxForce = hand.getMouseJoint().getMaxForce();
        hand.getMouseJoint().setMaxForce(0);
        Vector2 velocity = target.cpy().sub(hand.getMouseJoint().getTarget()).nor().mul(30f);
        hand.getGrabbedEntity().getBody().setLinearVelocity(velocity);
        this.initialY = hand.getGrabbedEntity().getBody().getPosition().y;
    }

    @Override
    public void run() {
        super.run();
        MouseJoint mouseJoint = this.hand.getMouseJoint();
        if (mouseJoint == null || !this.hand.getGrabbedEntity().isAlive()) {
            return;
        }
        if (!isDead()) {
            position.set(getGrabbedEntity().getBody().getWorldPoint(localPoint));
            if (position.dst(start) > Hand.STAB_ADVANCE) {
                this.setTarget(position);
            }
            float angle = this.hand.getGrabbedEntity().getBody().getAngle();
            this.hand.getGrabbedEntity()
                    .getBody()
                    .setTransform(this.hand.getGrabbedEntity().getBody().getPosition().x, initialY, angle);
        } else {
            goBack();
        }
    }

    public void goBack() {
        if (this.hand.getMouseJoint() != null) {
            this.hand.getGrabbedEntity().getBody().setLinearVelocity(0, 0);
            this.setTarget(this.start);
        }
    }

    public GameEntity getGrabbedEntity() {
        return this.hand.getGrabbedEntity();
    }

    public void setTarget(Vector2 target) {
        if (this.hand.getMouseJoint() != null) {
            this.hand.updateTarget(target);
            this.hand.getMouseJoint().setMaxForce(this.maxForce);
        }
    }

    public float getCurrentAdvance() {
        return hand.getMouseJoint().getTarget().dst(start);
    }
}
