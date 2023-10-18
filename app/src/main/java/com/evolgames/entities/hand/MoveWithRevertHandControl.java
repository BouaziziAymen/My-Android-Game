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
    private final float maxForce;
    private final float y;

    public MoveWithRevertHandControl(Hand hand, Vector2 target, Vector2 localPoint) {
        super(60);
        this.localPoint = localPoint;
        this.start = hand.getMouseJoint().getTarget().cpy();
        this.hand = hand;
        this.position = new Vector2();
        this.target = new Vector2();
        this.position.set(getGrabbedEntity().getBody().getWorldPoint(localPoint));
        this.target.set(start);
        this.maxForce = hand.getMouseJoint().getMaxForce();
        hand.getMouseJoint().setMaxForce(0);
        Vector2 velocity = target.cpy().sub(hand.getMouseJoint().getTarget()).nor().mul(20f);
        hand.getGrabbedEntity().getBody().setLinearVelocity(velocity);
        this.control = new HoldHandControl(hand.getGrabbedEntity());
        this.y = hand.getGrabbedEntity().getBody().getPosition().y;
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
            if(getAdvance()>5f){
                goBack();
            }
            Vector2 pos = hand.getGrabbedEntity().getBody().getPosition();
            float angle = hand.getGrabbedEntity().getBody().getAngle();
            hand.getGrabbedEntity().getBody().setTransform(pos.x,y,angle);
            control.run();
        } else {
            goBack();
        }
    }

    public void goBack(){
        if(hand.getMouseJoint()!=null) {
            setDead(true);
            hand.getMouseJoint().setMaxForce(this.maxForce);
            hand.getMouseJoint().setTarget(start);
        }
    }
    public float getAdvance(){
        return position.dst(start);
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
            hand.getMouseJoint().setMaxForce(this.maxForce);
        }
    }

}
