package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;

public class TransformInit extends BodyInitDecorator {
    private final float x;
    private final float y;
    private final float angle;

    public TransformInit(BodyInit bodyInit, float x, float y, float angle) {
        super(bodyInit);
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        body.setTransform(x,y,angle);
    }
}
