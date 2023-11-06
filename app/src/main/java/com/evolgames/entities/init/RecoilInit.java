package com.evolgames.entities.init;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.serialization.InitInfo;

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
        Vector2 impulse = muzzleVelocity.cpy().nor().mul(body.getMass()).mul(-recoil*muzzleVelocity.len());
        muzzleBody.applyLinearImpulse(impulse,point);
    }

    @Override
    public InitInfo getInitInfo(InitInfo initInfo) {
        initInfo.setMuzzleVelocity(muzzleVelocity);
        initInfo.setRecoil(recoil);
        initInfo.setPoint(point);
        return this.getBodyInit().getInitInfo(initInfo);
    }
}
