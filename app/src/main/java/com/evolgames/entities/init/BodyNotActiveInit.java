package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.serialization.infos.InitInfo;

public class BodyNotActiveInit extends BodyInitDecorator {

    boolean notActive;
    public BodyNotActiveInit(BodyInit bodyInit, boolean notActive) {
        super(bodyInit);
        this.notActive = notActive;
    }
    @Override
    public void initialize(Body body) {
        super.initialize(body);
        if(notActive){
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
