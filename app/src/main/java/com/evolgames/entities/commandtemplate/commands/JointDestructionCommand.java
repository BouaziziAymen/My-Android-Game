package com.evolgames.entities.commandtemplate.commands;

import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.physics.entities.JointBlueprint;

import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointDestructionCommand extends Command {
    private final JointBlueprint jointBlueprint;

    public JointDestructionCommand(JointBlueprint blueprint) {
        this.jointBlueprint = blueprint;
    }


    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        physicsWorld.destroyJoint(jointBlueprint.getJoint());
        jointBlueprint.setJoint(null);
    }
}
