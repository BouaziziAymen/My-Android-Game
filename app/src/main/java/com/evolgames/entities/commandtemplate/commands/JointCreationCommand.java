package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class JointCreationCommand extends Command {

    private final JointDef jointDef;
    private GameEntity entity1;
    private GameEntity entity2;
    private Joint joint;

    public JointCreationCommand(JointDef jointDef, GameEntity entity1, GameEntity entity2) {
        this.jointDef = jointDef;
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        this.joint = physicsWorld.createJoint(jointDef);
        if (this.joint instanceof MouseJoint) {
            Invoker.gameScene.setMouseJoint((MouseJoint) joint, entity2.getHangedPointerId());
        }
        setAborted(true);
    }

    @Override
    protected boolean isReady() {
        jointDef.bodyA = entity1.getBody();
        jointDef.bodyB = entity2.getBody();
        return jointDef.bodyA != null && jointDef.bodyB != null;
    }

    public Joint getJoint() {
        return this.joint;
    }

    public void substitute(GameEntity splinter, JointBlock.Position position) {
       switch (position){
           case A:
               entity1 = splinter;
               break;
           case B:
               entity2 = splinter;
               break;
       }
        setAborted(false);
    }

    public void updateAnchor(Vector2 anchor, JointBlock.Position position) {
       switch (jointDef.type){
           case Unknown:
               break;
           case RevoluteJoint:
               RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
               if(position== JointBlock.Position.A){
                   revoluteJointDef.localAnchorA.set(anchor.cpy().mul(1/32f));
               } else {
                   revoluteJointDef.localAnchorB.set(anchor.cpy().mul(1/32f));
               }
               break;
           case PrismaticJoint:
               PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
               if(position== JointBlock.Position.A){
                   prismaticJointDef.localAnchorA.set(anchor.cpy().mul(1/32f));
               } else {
                   prismaticJointDef.localAnchorB.set(anchor.cpy().mul(1/32f));
               }
               break;
           case DistanceJoint:
               break;
           case PulleyJoint:
               break;
           case MouseJoint:
               break;
           case GearJoint:
               break;
           case LineJoint:
               break;
           case WeldJoint:
               WeldJointDef weldJointDef = (WeldJointDef) jointDef;
               if(position== JointBlock.Position.A){
                   weldJointDef.localAnchorA.set(anchor.cpy().mul(1/32f));
               } else {
                   weldJointDef.localAnchorB.set(anchor.cpy().mul(1/32f));
               }
               break;
           case FrictionJoint:
               break;
       }

    }
}
