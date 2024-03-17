package com.evolgames.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.serialization.infos.InitInfo;

public interface BodyInit {
    void initialize(Body body);

    InitInfo getInitInfo(InitInfo initInfo);
}
