package com.evolgames.entities.commandtemplate.commands;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;

import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointCreationCommand extends Command {

    private final JointDef jointDef;
    private final GameEntity entity1;
    private final GameEntity entity2;
    private final JointBlock mainBlock;

    public JointCreationCommand(
            JointDef jointDef, GameEntity entity1, GameEntity entity2, JointBlock main) {
        this.jointDef = jointDef;
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.mainBlock = main;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
        Joint joint = physicsWorld.createJoint(jointDef);
        joint.setUserData(this.mainBlock);
        this.mainBlock.setJoint(joint);
        this.mainBlock.getBrother().setJoint(joint);
        if (joint instanceof MouseJoint) {
            Invoker.scene.setMouseJoint((MouseJoint) joint, entity2, (MouseJointDef) jointDef);
        }
    }

    @Override
    protected boolean isReady() {
        jointDef.bodyA = entity1.getBody();
        jointDef.bodyB = entity2.getBody();
        return isJointDefReady(jointDef.bodyA,jointDef.bodyB);
    }

    public static boolean isJointDefReady(Body bodyA, Body bodyB) {
        return isBodyAlive(bodyA) && isBodyAlive(bodyB);
    }

}
