package com.evolgames.dollmutilationgame.entities.init;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class BodyInitDecorator implements BodyInit {

    private final BodyInit bodyInit;

    public BodyInitDecorator(BodyInit bodyInit) {
        this.bodyInit = bodyInit;
    }

    public BodyInit getBodyInit() {
        return bodyInit;
    }

    @Override
    public void initialize(Body body) {
        bodyInit.initialize(body);
    }
}
