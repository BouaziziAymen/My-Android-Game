package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Joint;
import com.evolgames.entities.commandtemplate.Invoker;

import org.andengine.extension.physics.box2d.PhysicsWorld;

public class MouseJointDestructionCommand extends Command {
    private final Joint mouseJoint;

    public MouseJointDestructionCommand(Joint mouseJoint) {
        this.mouseJoint = mouseJoint;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        if(mouseJoint!=null) {
            Invoker.gameScene.onDestroyMouseJoint(mouseJoint);
            physicsWorld.destroyJoint(mouseJoint);
        }
    }
}
