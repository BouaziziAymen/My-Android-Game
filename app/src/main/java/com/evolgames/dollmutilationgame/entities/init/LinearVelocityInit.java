package com.evolgames.dollmutilationgame.entities.init;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.entities.serialization.infos.InitInfo;

public class LinearVelocityInit extends BodyInitDecorator {

    private final Vector2 linearVelocity;

    public LinearVelocityInit(BodyInit bodyInit, Vector2 linVel) {
        super(bodyInit);
        this.linearVelocity = linVel;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        if (linearVelocity != null) {
            body.setLinearVelocity(linearVelocity);
        }
    }

    @Override
    public InitInfo getInitInfo(InitInfo initInfo) {
        initInfo.setLinearVelocity(linearVelocity);
        return this.getBodyInit().getInitInfo(initInfo);
    }
}
