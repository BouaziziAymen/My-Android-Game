package com.evolgames.entities.hand;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.GameEntity;
import com.evolgames.scenes.hand.Hand;

import org.andengine.util.math.MathConstants;

public class SwingHandControl extends HandControl {
    private final float speed;
    private final Hand hand;
    private final float initialAngle;

    public SwingHandControl(Hand hand, int speed) {
        super((int) ((0.3f*600)/Math.abs(speed)));
        this.speed = speed;
        this.hand = hand;
        this.initialAngle = this.hand.getGrabbedEntity().getBody().getAngle();
    }

    @Override
    public void run() {
        super.run();
        if(hand.getMouseJoint()==null){
            return;
        }
        Body body = hand.getMouseJoint().getBodyB();
        if(body!=null) {
            if(!isDead()) {
                body.setAngularVelocity(speed);
            } else {
                body.setAngularVelocity(0f);
            }
        }
    }
}
