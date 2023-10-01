package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.GameEntity;
import com.evolgames.scenes.GameScene;
import com.evolgames.scenes.hand.Hand;

import org.andengine.util.adt.color.Color;

public class MoveWithRevertHandControl extends HandControl {

    private final Hand hand;
    private final Vector2 start;
    private final HoldHandControl control;
    private final Vector2 localPoint;

    public MoveWithRevertHandControl(Hand hand, Vector2 target, Vector2 localPoint) {
        super(60);
        this.localPoint = localPoint;
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
               goBack();
            }
        } else {
           goBack();
        }
    }
    public void goBack(){
        hand.getMouseJoint().setTarget(start);
    }

    public Vector2 getPosition() {
        return hand.getGrabbedEntity().getBody().getWorldPoint(localPoint).cpy();
    }
    public GameEntity getGrabbedEntity(){
        return hand.getGrabbedEntity();
    }

    public Vector2 getTarget() {
        return hand.getMouseJoint().getTarget();
    }
    public void setTarget(Vector2 target) {
        hand.getMouseJoint().setTarget(target);
    }

}
