package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.blocks.JointBlock;
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
        entity.getBody().getJointList().stream().filter(je->je.joint instanceof MouseJoint).forEach(je->{
            Invoker.gameScene.onDestroyMouseJoint((MouseJoint) je.joint);
        });
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
