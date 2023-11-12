package com.evolgames.entities.serialization;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.EntityDestructionCommand;
import com.evolgames.entities.commandtemplate.TimedCommand;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;

import java.util.List;

public class WorldFacadeSerializer {
    List<TimedCommand> timedCommandList;

    @SuppressWarnings("unused")
    public WorldFacadeSerializer() {
    }

    public WorldFacadeSerializer(WorldFacade worldFacade) {
        this.timedCommandList = worldFacade.getTimedCommands();
    }

    public void afterCreate(PhysicsScene<?> physicsScene){
        for(TimedCommand timedCommand:this.timedCommandList){
            if(timedCommand instanceof EntityDestructionCommand){
                EntityDestructionCommand entityDestructionCommand = (EntityDestructionCommand)timedCommand;
                GameEntity gameEntity = physicsScene.getGameEntityByUniqueId(entityDestructionCommand.getGameEntityUniqueId());
              timedCommand.setAction(()->physicsScene.getWorldFacade().destroyGameEntity(gameEntity,true));
            }
        }
        physicsScene.getWorldFacade().getTimedCommands().addAll(this.timedCommandList);
    }
}
