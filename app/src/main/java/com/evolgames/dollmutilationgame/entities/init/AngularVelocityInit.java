package com.evolgames.dollmutilationgame.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.entities.serialization.infos.InitInfo;

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

    @Override
    public InitInfo getInitInfo(InitInfo initInfo) {
        initInfo.setAngularVelocity(angularVelocity);
        return this.getBodyInit().getInitInfo(initInfo);
    }
}
