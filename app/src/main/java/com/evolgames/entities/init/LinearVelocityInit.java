package com.evolgames.entities.init;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class LinearVelocityInit extends BodyInitDecorator {
    private final Vector2 linearVelocity;

    public LinearVelocityInit(BodyInit bodyInit, Vector2 linVel){
        super(bodyInit);
        this.linearVelocity = linVel;
    }
    @Override
    public void initialize(Body body) {
        super.initialize(body);
        if(linearVelocity!=null) {
            body.setLinearVelocity(linearVelocity);
        }
    }
}
