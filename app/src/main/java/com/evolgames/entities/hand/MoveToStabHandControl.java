package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.GameEntity;
import com.evolgames.scenes.Hand;

public class MoveToStabHandControl extends HandControl {

    private final Hand hand;
    private final Vector2 start;
    private final Vector2 localPoint;
    private final Vector2 position;
    private final float maxForce;
    private final float initialY;

    public MoveToStabHandControl(Hand hand, Vector2 target, Vector2 localPoint) {
        super(60);
        this.localPoint = localPoint;
        this.start = hand.getMouseJoint().getTarget().cpy();
        this.hand = hand;
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
        MouseJoint mouseJoint = hand.getMouseJoint();
        if (mouseJoint == null||!hand.getGrabbedEntity().isAlive()) {
            return;
        }
        if (!isDead()) {
            position.set(getGrabbedEntity().getBody().getWorldPoint(localPoint));
            if(position.dst(start)>Hand.STAB_ADVANCE){
              goBack();
              setDead(true);
            }
            float angle = hand.getGrabbedEntity().getBody().getAngle();
            hand.getGrabbedEntity().getBody().setTransform(hand.getGrabbedEntity().getBody().getPosition().x, initialY,angle);
        } else {
            goBack();
        }
    }

    public void goBack(){
        if(hand.getMouseJoint()!=null) {
            hand.getGrabbedEntity().getBody().setLinearVelocity(0,0);
            this.setTarget(this.start);
        }
    }

    public GameEntity getGrabbedEntity(){
        return hand.getGrabbedEntity();
    }

    public void setTarget(Vector2 target) {
        if(hand.getMouseJoint()!=null) {
            hand.getMouseJoint().setTarget(target);
            hand.getMouseJoint().setMaxForce(this.maxForce);
        }
    }

}
