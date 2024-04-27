package com.evolgames.dollmutilationgame.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.entities.serialization.infos.InitInfo;

public class BulletInit extends BodyInitDecorator {

    private final boolean isBullet;

    public BulletInit(BodyInit bodyInit, boolean isBullet) {
        super(bodyInit);
        this.isBullet = isBullet;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        body.setBullet(isBullet);
    }

    @Override
    public InitInfo getInitInfo(InitInfo initInfo) {
        initInfo.setBullet(isBullet);
        return this.getBodyInit().getInitInfo(initInfo);
    }
}
