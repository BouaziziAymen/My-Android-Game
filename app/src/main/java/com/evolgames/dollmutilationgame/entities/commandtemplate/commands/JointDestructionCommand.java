package com.evolgames.dollmutilationgame.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.dollmutilationgame.entities.blocks.JointBlock;
import com.evolgames.dollmutilationgame.entities.commandtemplate.Invoker;

import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointDestructionCommand extends Command {
    private final Joint joint;

    public JointDestructionCommand(Joint joint) {
        this.joint = joint;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
        JointBlock jointBlock = (JointBlock) joint.getUserData();
        if (jointBlock.isNotAborted()) {
            jointBlock.getEntity().removeJointBlock(jointBlock);
            jointBlock.getBrother().getEntity().removeJointBlock(jointBlock.getBrother());
            jointBlock.setAborted(true);
            jointBlock.getBrother().setAborted(true);
            if (this.joint instanceof MouseJoint) {
                Invoker.scene.getHand().setMouseJoint(null,null,null);
            }
            physicsWorld.destroyJoint(joint);
        }
    }

    @Override
    protected boolean isReady() {
        return joint != null;
    }
}
