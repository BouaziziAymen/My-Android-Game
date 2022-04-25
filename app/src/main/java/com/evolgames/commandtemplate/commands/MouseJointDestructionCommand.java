package com.evolgames.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Joint;
import com.evolgames.commandtemplate.Invoker;

import org.andengine.extension.physics.box2d.PhysicsWorld;

public class MouseJointDestructionCommand extends Command {
    private Joint mouseJoint;

    public MouseJointDestructionCommand(Joint mouseJoint) {
        this.mouseJoint = mouseJoint;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        Invoker.gameScene.onDestroyMouseJoint(mouseJoint);
        physicsWorld.destroyJoint(mouseJoint);
    }
}
