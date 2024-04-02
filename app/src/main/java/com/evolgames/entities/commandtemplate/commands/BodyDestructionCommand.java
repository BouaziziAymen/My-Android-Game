package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.blocks.JointBlock;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.Hand;
import com.evolgames.scenes.PlayScene;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.Iterator;

public class BodyDestructionCommand extends Command {
    private final GameEntity entity;

    public BodyDestructionCommand(GameEntity entity) {
        this.entity = entity;
    }

    private void destroy(Body body) {
        PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
        body.getJointList().forEach(jointEdge -> {
            Joint joint = jointEdge.joint;
            JointBlock jointBlock = (JointBlock) joint.getUserData();
            if (jointBlock.getJointType() == JointDef.JointType.MouseJoint) {
                Invoker.scene.onDestroyMouseJoint((MouseJoint) joint);
            }
        });
        for (Iterator<Joint> it = physicsWorld.getJoints(); it.hasNext(); ) {
            Joint joint = it.next();
            if (joint.getBodyA() == body || joint.getBodyB() == body) {
                physicsWorld.destroyJoint(joint);
            }
        }
        physicsWorld.destroyBody(body);
    }

    @Override
    protected void run() {
        destroy(entity.getBody());
        if (entity.getMirrorBody() != null) {
            destroy(entity.getMirrorBody());
        }

        entity.setBody(null);
        entity.setMirrorBody(null);
        entity.detach();
        entity.getParentGroup().getEntities().remove(entity);
        Hand hand = Invoker.scene.getHand();
        if(hand!=null&&hand.getUsableEntity()==entity){
            if(entity.getHeir()!=null) {
                hand.setSelectedEntity(entity.getHeir());
            }
        }

    }

    @Override
    protected boolean isReady() {
        return entity.getBody() != null;
    }

    public GameEntity getGameEntity() {
        return entity;
    }
}
