package com.evolgames.dollmutilationgame.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.usage.Slasher;

public class MoveToSlashHandControl extends HandControl {

    private HoldHandControl holdHandControl;
    private Vector2 localPoint;
    private Vector2 start;
    private Vector2 position;
    private Vector2 target;

    @SuppressWarnings("unused")
    public MoveToSlashHandControl() {
    }

    public MoveToSlashHandControl(Hand hand, Vector2 target, Vector2 localPoint) {
        super(hand, 60);
        this.localPoint = localPoint;
        this.start = this.hand.getMouseJoint().getTarget().cpy();
        this.position = new Vector2();
        this.target = new Vector2();
        this.position.set(getGrabbedEntity().getBody().getWorldPoint(localPoint));
        this.target.set(start);
        this.hand.getMouseJoint().setTarget(target);
        this.holdHandControl = new HoldHandControl(this.hand);
    }

    @Override
    public void run() {
        super.run();
        if (this.hand == null) {
            return;
        }
        MouseJoint mouseJoint = this.hand.getMouseJoint();
        if (mouseJoint == null || this.hand.getGrabbedEntity() == null || !this.hand.getGrabbedEntity().hasActiveUsage(Slasher.class)) {
            return;
        }

        this.hand.doSlash();
        if (!isDead()) {
            position.set(getGrabbedEntity().getBody().getWorldPoint(localPoint));
            target.set(this.hand.getMouseJoint().getTarget());
            holdHandControl.run();
            Vector2 v = mouseJoint.getBodyB().getLinearVelocity();
            if ((v.len() < 0.01f && count > 10)) {
                setDead(true);
                goBack();
            }
        } else {
            goBack();
        }
    }

    public void goBack() {
        this.hand.getMouseJoint().setTarget(start);
        if (this.hand.getMouseJoint() != null) {
            this.hand.getMouseJoint().setTarget(start);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public GameEntity getGrabbedEntity() {
        return this.hand.getGrabbedEntity();
    }

    public Vector2 getTarget() {
        return this.target;
    }

    public void setTarget(Vector2 target) {
        this.hand.getMouseJoint().setTarget(target);
        if (this.hand.getMouseJoint() != null) {
            this.hand.getMouseJoint().setTarget(target);
        }
    }

    @Override
    public void setHand(Hand hand) {
        super.setHand(this.hand);
        this.holdHandControl.setHand(this.hand);
    }
}
