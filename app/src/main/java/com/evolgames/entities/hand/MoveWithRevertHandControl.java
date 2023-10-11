package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.GameEntity;
import com.evolgames.scenes.Hand;

public class MoveWithRevertHandControl extends HandControl {

    private final Hand hand;
    private final Vector2 start;
    private final HoldHandControl control;
    private final Vector2 localPoint;
    private final Vector2 position;
    private final Vector2 target;

    public MoveWithRevertHandControl(Hand hand, Vector2 target, Vector2 localPoint) {
        super(60);
        this.localPoint = localPoint;
        this.start = hand.getMouseJoint().getTarget().cpy();
        this.hand = hand;
        this.position = new Vector2();
        this.target = new Vector2();
        this.position.set(getGrabbedEntity().getBody().getWorldPoint(localPoint));
        this.target.set(start);
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
            position.set(getGrabbedEntity().getBody().getWorldPoint(localPoint));
            target.set(hand.getMouseJoint().getTarget());
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
        if(hand.getMouseJoint()!=null) {
            hand.getMouseJoint().setTarget(start);
        }
    }

    public Vector2 getPosition() {
      return position;
    }
    public GameEntity getGrabbedEntity(){
        return hand.getGrabbedEntity();
    }

    public Vector2 getTarget() {
        return this.target;
    }
    public void setTarget(Vector2 target) {
        if(hand.getMouseJoint()!=null) {
            hand.getMouseJoint().setTarget(target);
        }
    }

}
