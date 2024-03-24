package com.evolgames.entities.commandtemplate.commands;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;

import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointDestructionCommand extends Command {
    private final Joint joint;

    public JointDestructionCommand(Joint joint) {
        this.joint = joint;
    }

    @Override
    protected void run() {
        Log.e("Mirror", joint + "-------------Destroying joint-----------:" + joint.getType());

        PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
        JointBlock jointBlock = (JointBlock) joint.getUserData();
        if(jointBlock.isNotAborted()) {
            jointBlock.getEntity().removeJointBlock(jointBlock);
            jointBlock.getBrother().getEntity().removeJointBlock(jointBlock.getBrother());
            jointBlock.setAborted(true);
            jointBlock.getBrother().setAborted(true);
            if (this.joint instanceof MouseJoint) {
                Invoker.scene.onDestroyMouseJoint((MouseJoint) joint);
            }
            physicsWorld.destroyJoint(joint);
        }
    }

    @Override
    protected boolean isReady() {
        return joint != null;
    }
}
