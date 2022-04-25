package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.evolgames.entities.GameEntity;

import java.util.HashSet;

public class GameEntityQueryCallBack implements QueryCallback {
    private HashSet<GameEntity> entities = new HashSet<>();

    public void reset(){
        entities.clear();
    }
    @Override
    public boolean reportFixture(Fixture fixture)
    {
        GameEntity entity = (GameEntity) fixture.getBody().getUserData();
        if(entity!=null)
        entities.add(entity);
        return true;
    }

    public HashSet<GameEntity> getEntities() {
        return entities;
    }
}
