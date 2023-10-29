package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.commandtemplate.Invoker;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.List;

public class JointDestructionCommand extends Command {
    private final Joint joint;

    public JointDestructionCommand(Joint joint) {
        this.joint = joint;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        System.out.println("-----------------Before Destroy-------------------"+joint);
        physicsWorld.getJoints().forEachRemaining(j-> {
            if (j == joint) {
                physicsWorld.destroyJoint(joint);
                System.out.println("-----------------After Destroy-------------------");
                if(this.joint instanceof MouseJoint) {
                    Invoker.gameScene.onDestroyMouseJoint((MouseJoint) joint);
                }
            }
        });

    }

    @Override
    protected boolean isReady() {
        return joint!=null;
    }
}
