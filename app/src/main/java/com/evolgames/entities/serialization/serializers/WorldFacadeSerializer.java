package com.evolgames.entities.serialization.serializers;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.commandtemplate.EntityDestructionCommand;
import com.evolgames.entities.commandtemplate.TimedCommand;
import com.evolgames.entities.contact.Pair;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PhysicsScene;
import java.util.List;
import java.util.stream.Collectors;

public class WorldFacadeSerializer {

    private List<Pair<String>> nonCollidingPairs;
    public List<TimedCommand> timedCommandList;

    @SuppressWarnings("unused")
    public WorldFacadeSerializer() {
    }

    public WorldFacadeSerializer(WorldFacade worldFacade) {
        this.timedCommandList = worldFacade.getTimedCommands();
        this.nonCollidingPairs = worldFacade.getNonCollidingPairs().stream().map(p->new Pair<>(p.first.getUniqueID(),p.second.getUniqueID())).collect(Collectors.toList());
    }

    public void afterCreate(PhysicsScene<?> physicsScene){
        for(TimedCommand timedCommand:this.timedCommandList){
            if(timedCommand instanceof EntityDestructionCommand){
                EntityDestructionCommand entityDestructionCommand = (EntityDestructionCommand)timedCommand;
                GameEntity gameEntity = physicsScene.getGameEntityByUniqueId(entityDestructionCommand.getGameEntityUniqueId());
              timedCommand.setAction(()->physicsScene.getWorldFacade().destroyGameEntity(gameEntity,true,true));
            }
        }
        physicsScene.getWorldFacade().getTimedCommands().addAll(this.timedCommandList);
    }

    public List<Pair<String>> getNonCollidingPairs() {
        return nonCollidingPairs;
    }
}
