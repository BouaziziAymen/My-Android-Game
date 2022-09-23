package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.GameEntity;
import com.evolgames.physics.entities.JointBlueprint;

import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointCreationCommand extends Command {

    private final JointBlueprint jointBlueprint;

    public JointCreationCommand(JointBlueprint jointBlueprint) {
        this.jointBlueprint = jointBlueprint;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        Joint joint = physicsWorld.createJoint(jointBlueprint.getJointDef());
        if (joint instanceof MouseJoint) Invoker.gameScene.setMouseJoint((MouseJoint) joint, ((GameEntity) jointBlueprint.getEntity2()).getHangedPointerId());
        jointBlueprint.setJoint(joint);
    }

    public JointBlueprint getBlueprint() {

        return jointBlueprint;
    }
}
