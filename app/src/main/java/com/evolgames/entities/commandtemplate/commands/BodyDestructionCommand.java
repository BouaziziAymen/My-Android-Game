package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.GameEntity;

import org.andengine.extension.physics.box2d.PhysicsWorld;

public class BodyDestructionCommand extends Command {
    private final GameEntity entity;

    public BodyDestructionCommand(GameEntity entity) {
        this.entity = entity;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        physicsWorld.destroyBody(entity.getBody());
        entity.setBody(null);
        entity.detach();
    }

    @Override
    protected boolean isReady() {
     return entity.getBody()!=null;
    }

    public GameEntity getGameEntity() {
        return entity;
    }
}
