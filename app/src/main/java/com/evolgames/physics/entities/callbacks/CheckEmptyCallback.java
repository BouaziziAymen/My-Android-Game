package com.evolgames.physics.entities.callbacks;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;

import org.andengine.util.algorithm.collision.EntityCollisionChecker;

public class CheckEmptyCallback implements QueryCallback {
    private boolean isEmpty;
    private GameEntity gameEntity;
    private float[] bounds;

    public void reset(float[] bounds){
        gameEntity = null;
        isEmpty = true;
        this.bounds = bounds;
    }
    @Override
    public boolean reportFixture(Fixture fixture) {
        Body body = fixture.getBody();
        if(!body.isActive()){
            return true;
        }

        GameEntity gameEntity = (GameEntity) body.getUserData();
        if(!gameEntity.isDestroyed()){
        if(EntityCollisionChecker.checkCollision(bounds,gameEntity.getMesh().getBounds(),gameEntity.getMesh().getLocalToSceneTransformation())) {
                isEmpty = false;
                this.gameEntity = gameEntity;
                return false;
            }
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
