package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointCreationCommand extends Command {

    private final JointDef jointDef;
    private final GameEntity entity1;
    private final GameEntity entity2;

    public JointCreationCommand(JointDef jointDef, GameEntity entity1, GameEntity entity2) {
        this.jointDef = jointDef;
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        Joint joint = physicsWorld.createJoint(jointDef);
        if (joint instanceof MouseJoint) {
            // Invoker.gameScene.setMouseJoint((MouseJoint) joint, ((GameEntity) jointBlueprint.getEntity2()).getHangedPointerId());
        }
        setAborted(true);
    }

    @Override
    protected boolean isReady() {
        jointDef.bodyA = entity1.getBody();
        jointDef.bodyB = entity2.getBody();
        return jointDef.bodyA != null && jointDef.bodyB != null;
    }

}
