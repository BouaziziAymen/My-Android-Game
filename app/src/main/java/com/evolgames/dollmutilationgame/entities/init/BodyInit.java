package com.evolgames.dollmutilationgame.entities.init;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.entities.serialization.infos.InitInfo;

public interface BodyInit {
    void initialize(Body body);

    InitInfo getInitInfo(InitInfo initInfo);
}
