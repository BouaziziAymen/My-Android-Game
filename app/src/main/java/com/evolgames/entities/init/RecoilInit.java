package com.evolgames.entities.init;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.serialization.infos.InitInfo;

public class RecoilInit extends BodyInitDecorator {

    private final Body muzzleBody;
    private final Vector2 muzzleVelocity;
    private final Vector2 point;
    private final float recoil;

    public RecoilInit(
            BodyInit bodyInit, Body muzzleBody, float recoil, Vector2 muzzleVelocity, Vector2 point) {
        super(bodyInit);
        this.muzzleBody = muzzleBody;
        this.muzzleVelocity = muzzleVelocity;
        this.point = point;
        this.recoil = recoil;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        if(muzzleBody!=null&&muzzleBody.isActive()) {
            Vector2 impulse =
                    muzzleVelocity.cpy().nor().mul(body.getMass()).mul(-recoil * 20f * muzzleVelocity.len());
            muzzleBody.applyLinearImpulse(impulse, point);
        }
    }

    @Override
    public InitInfo getInitInfo(InitInfo initInfo) {
        initInfo.setMuzzleVelocity(muzzleVelocity);
        initInfo.setRecoil(recoil);
        initInfo.setPoint(point);
        return this.getBodyInit().getInitInfo(initInfo);
    }
}
