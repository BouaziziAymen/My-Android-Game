package com.evolgames.dollmutilationgame.physics.entities.callbacks;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

public class DebugCallback implements QueryCallback {
    @Override
    public boolean reportFixture(Fixture fixture) {
        Body body = fixture.getBody();
        GameEntity gameEntity = (GameEntity) body.getUserData();
        return true;
    }
}
