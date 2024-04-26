package com.evolgames.entities.commandtemplate.commands;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.basics.GameGroup;
import com.evolgames.entities.commandtemplate.Invoker;

import org.andengine.extension.physics.box2d.PhysicsWorld;

import java.util.Iterator;

public class GameGroupDestructionCommand extends Command{
    final GameGroup group;

    public GameGroupDestructionCommand(GameGroup group) {
        this.group = group;
    }

    private void destroy(GameEntity entity) {
        PhysicsWorld physicsWorld = Invoker.scene.getPhysicsWorld();
        Body body = entity.getBody();
        Body mirrorBody = entity.getMirrorBody();
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
       // entity.recycle();
        entity.setDestroyed();
    }

    @Override
    protected void run() {
        for(GameEntity gameEntity:group.getGameEntities()){
            destroy(gameEntity);
        }
        group.Destroyed(true);
    }
}
