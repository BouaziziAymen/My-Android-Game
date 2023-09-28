package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.helpers.utilities.BlockUtils;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class JointCreationCommand extends Command {

    private final JointDef jointDef;
    private GameEntity entity1;
    private GameEntity entity2;
    private Joint joint;
    private JointBlock jointBlock1;
    private JointBlock jointBlock2;
    private boolean createdJointBlocks;

    public JointCreationCommand(JointDef jointDef, GameEntity entity1, GameEntity entity2) {
        this.jointDef = jointDef;
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    @Override
    protected void run() {
        PhysicsWorld physicsWorld = Invoker.gameScene.getPhysicsWorld();
        if(this.joint!=null&& this.joint instanceof MouseJoint){
            MouseJointDef mouseJointDef = (MouseJointDef) this.jointDef;
            Vector2 point = mouseJointDef.bodyB.getWorldPoint(jointBlock2.getVertices().get(0).cpy().mul(1/32f)).cpy();
            mouseJointDef.target.set(point);
        }
        this.joint = physicsWorld.createJoint(jointDef);
        if (this.joint instanceof MouseJoint) {
            Invoker.gameScene.setMouseJoint((MouseJoint) joint, entity2);
        }
        if(!createdJointBlocks) {
            addJointBlocks();
        }
        setAborted(true);
    }

    private void addJointBlocks() {
        this.createdJointBlocks = true;
        jointBlock1 = new JointBlock();
        jointBlock2 = new JointBlock();
        jointBlock1.setCommand(this);
        jointBlock2.setCommand(this);
        Vector2 anchorA = null;
        Vector2 anchorB = null;

        switch (jointDef.type) {
            case Unknown:
            case GearJoint:
            case MouseJoint:
                MouseJointDef mouseJointDef = ((MouseJointDef) jointDef) ;
                anchorB = jointDef.bodyB.getLocalPoint(mouseJointDef.target).cpy().mul(32);
                System.out.println("AncherB:"+anchorB);
            case LineJoint:
            case PulleyJoint:
            case DistanceJoint:
            case FrictionJoint:
                break;
            case RevoluteJoint:
                anchorA = ((RevoluteJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((RevoluteJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
            case PrismaticJoint:
                anchorA = ((PrismaticJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((PrismaticJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
            case WeldJoint:
                anchorA = ((WeldJointDef) jointDef).localAnchorA.cpy().mul(32f);
                anchorB = ((WeldJointDef) jointDef).localAnchorB.cpy().mul(32f);
                break;
        }

        if (anchorA != null) {
            jointBlock1.initialization(jointDef.type,new ArrayList<>(Collections.singletonList(anchorA)), new JointProperties(jointDef), 0, JointBlock.Position.A);
            BlockUtils.getNearestBlock(anchorA,entity1.getBlocks()).addAssociatedBlock(jointBlock1);
        }
        if (anchorB != null) {
            jointBlock2.initialization(jointDef.type,new ArrayList<>(Collections.singletonList(anchorB)), new JointProperties(jointDef), 0, JointBlock.Position.B);
            BlockUtils.getNearestBlock(anchorB,entity2.getBlocks()).addAssociatedBlock(jointBlock2);
        }
    }

    @Override
    protected boolean isReady() {
        jointDef.bodyA = entity1.getBody();
        jointDef.bodyB = entity2.getBody();
        return isBodyAlive(jointDef.bodyA) && isBodyAlive(jointDef.bodyB);
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
               MouseJointDef mouseJointDef = (MouseJointDef) jointDef;
               mouseJointDef.target.set(((MouseJoint)this.joint).getTarget());
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

    public JointDef getJointDef() {
        return jointDef;
    }

    private boolean isBodyAlive(Body body){
        if(body==null){
            return false;
        }
        AtomicBoolean result = new AtomicBoolean(false);
        Invoker.gameScene.getPhysicsWorld().getBodies().forEachRemaining(body1 -> {
            if(body1 == body){
                result.set(true);
            }
        });
        return result.get();
    }

    public Joint getJoint() {
        return joint;
    }
}
