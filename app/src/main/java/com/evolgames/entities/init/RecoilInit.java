package com.evolgames.entities.init;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class RecoilInit extends BodyInitDecorator{
    final private Body muzzleBody;
    final private Vector2 muzzleVelocity;
    final private Vector2 point;
    final private float recoil;

    public RecoilInit(BodyInit bodyInit, Body muzzleBody, float recoil, Vector2 muzzleVelocity, Vector2 point) {
        super(bodyInit);
        this.muzzleBody = muzzleBody;
        this.muzzleVelocity = muzzleVelocity;
        this.point = point;
        this.recoil = recoil;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        Vector2 impulse = muzzleVelocity.cpy().mul(body.getMass()).mul(-60*60*recoil);
        muzzleBody.applyLinearImpulse(impulse,point);
    }
}
