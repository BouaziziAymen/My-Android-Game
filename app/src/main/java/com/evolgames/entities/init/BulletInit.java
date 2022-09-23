package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;

public class BulletInit extends BodyInitDecorator{
    final private boolean isBullet;

    public BulletInit(BodyInit bodyInit,boolean isBullet) {
        super(bodyInit);
        this.isBullet = isBullet;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        body.setBullet(isBullet);
    }
}
