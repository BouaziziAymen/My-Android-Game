package com.evolgames.entities.commandtemplate.commands;

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
        entity.detach();
    }

    public GameEntity getGameEntity() {
        return entity;
    }
}
