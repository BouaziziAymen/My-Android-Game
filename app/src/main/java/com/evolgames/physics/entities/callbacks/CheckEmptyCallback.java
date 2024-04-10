package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.evolgames.entities.basics.GameEntity;

public class CheckEmptyCallback implements QueryCallback {
    private boolean isEmpty;
    private GameEntity gameEntity;
    public void reset(){
        isEmpty = true;
    }
    @Override
    public boolean reportFixture(Fixture fixture) {
        Body body = fixture.getBody();
        GameEntity gameEntity = (GameEntity) body.getUserData();
        if(gameEntity.isAlive()){
            isEmpty = false;
            this.gameEntity = gameEntity;
            return false;
        }
        return true;
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
