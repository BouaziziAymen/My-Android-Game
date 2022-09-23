package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;

public class AngularVelocityInit extends BodyInitDecorator {
    final float angularVelocity;

    public AngularVelocityInit(BodyInit bodyInit, float angularVelocity) {
        super(bodyInit);
        this.angularVelocity = angularVelocity;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        body.setAngularVelocity(angularVelocity);
    }
}
