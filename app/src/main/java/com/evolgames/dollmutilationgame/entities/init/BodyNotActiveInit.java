package com.evolgames.dollmutilationgame.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.entities.serialization.infos.InitInfo;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

public class BodyNotActiveInit extends BodyInitDecorator {

    boolean notActive;

    public BodyNotActiveInit(BodyInit bodyInit, boolean notActive) {
        super(bodyInit);
        this.notActive = notActive;
    }

    @Override
    public void initialize(Body body) {
        super.initialize(body);
        if (notActive) {
            GameEntity gameEntity = (GameEntity) body.getUserData();
            gameEntity.setVisible(false);
            body.getFixtureList().forEach(fixture -> fixture.setSensor(true));
        }
    }

    @Override
    public InitInfo getInitInfo(InitInfo initInfo) {
        initInfo.setNotActive(notActive);
        return this.getBodyInit().getInitInfo(initInfo);
    }
}
