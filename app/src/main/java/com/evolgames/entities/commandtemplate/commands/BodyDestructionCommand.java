package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.commandtemplate.Invoker;
import com.evolgames.entities.hand.Hand;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.Iterator;

public class BodyDestructionCommand extends Command {
    private final GameEntity entity;
    private final boolean finalDestruction;

    public BodyDestructionCommand(GameEntity entity, boolean finalDestruction) {
        this.entity = entity;
        this.finalDestruction = finalDestruction;
    }

    private void destroy() {
        PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
        Body body = entity.getBody();
        Body mirrorBody = entity.getMirrorBody();
        for (Iterator<Joint> it = physicsWorld.getJoints(); it.hasNext(); ) {
            Joint joint = it.next();
            if (joint.getBodyA() == body || joint.getBodyB() == body) {
                physicsWorld.destroyJoint(joint);
            }
            if (joint.getBodyA() == mirrorBody || joint.getBodyB() == mirrorBody) {
                physicsWorld.destroyJoint(joint);
            }
        }
        if(body!=null) {
            physicsWorld.destroyBody(body);
        }
        if(mirrorBody !=null){
            physicsWorld.destroyBody(mirrorBody);
        }
        entity.setBody(null);
        entity.setMirrorBody(null);
        entity.detach();
        entity.getParentGroup().getEntities().remove(entity);
        if(finalDestruction){
            entity.recycle();
        }
        entity.setDestroyed();
    }

    @Override
    protected void run() {
        destroy();
        Hand hand = Invoker.scene.getHand();
        if(hand!=null&&hand.getGrabbedEntity()==entity){
           hand.onMouseJointDestroyed(hand.getMouseJoint());
        }
        if(hand!=null){
            if(hand.getSelectedEntity()==entity) {
                if (entity.getHeir() != null) {
                    hand.inheritSelectedEntity(entity.getHeir());
                } else {
                    hand.deselectDirect(true);
                }
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
