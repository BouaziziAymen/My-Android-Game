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

    public BodyDestructionCommand(GameEntity entity) {
        this.entity = entity;
    }

    private void destroy(Body body) {
        PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
        for (Iterator<Joint> it = physicsWorld.getJoints(); it.hasNext(); ) {
            Joint joint = it.next();
            if (joint.getBodyA() == body || joint.getBodyB() == body) {
                physicsWorld.destroyJoint(joint);
            }
        }
        if(Invoker.scene.getChasedEntity()==entity){
            Invoker.scene.resetChasedEntity();
        }
        physicsWorld.destroyBody(body);
        entity.hideOutline();
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
        if(hand!=null){
            if(entity.getHeir()!=null&&hand.getSelectedEntity()==entity) {
                hand.inheritSelectedEntity(entity.getHeir());
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
