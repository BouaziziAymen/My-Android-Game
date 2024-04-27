package com.evolgames.dollmutilationgame.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.commandtemplate.Invoker;
import com.evolgames.dollmutilationgame.entities.hand.Hand;

import org.andengine.extension.physics.box2d.PhysicsWorld;

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
        physicsWorld.destroyBody(body);
        if(mirrorBody !=null){
            physicsWorld.destroyBody(mirrorBody);
        }
        if(Invoker.scene.getChasedEntity()==entity){
            Invoker.scene.resetChasedEntity();
        }
        entity.setBody(null);
        entity.setMirrorBody(null);
        entity.detach();
        entity.getParentGroup().getEntities().remove(entity);
        entity.setDestroyed();
    }

    @Override
    protected void run() {
        destroy();
        Hand hand = Invoker.scene.getHand();
        if(hand!=null&&hand.getGrabbedEntity()==entity){
           hand.setMouseJoint(null,null,null);
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
