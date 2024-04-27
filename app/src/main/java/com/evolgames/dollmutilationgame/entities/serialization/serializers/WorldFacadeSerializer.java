package com.evolgames.dollmutilationgame.entities.serialization.serializers;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.commandtemplate.EntityDestructionCommand;
import com.evolgames.dollmutilationgame.entities.commandtemplate.TimedCommand;
import com.evolgames.dollmutilationgame.entities.contact.Pair;
import com.evolgames.dollmutilationgame.physics.WorldFacade;
import com.evolgames.dollmutilationgame.scenes.PhysicsScene;

import java.util.List;
import java.util.stream.Collectors;

public class WorldFacadeSerializer {

    public List<TimedCommand> timedCommandList;
    private List<Pair<String>> nonCollidingPairs;

    @SuppressWarnings("unused")
    public WorldFacadeSerializer() {
    }

    public WorldFacadeSerializer(WorldFacade worldFacade) {
        this.timedCommandList = worldFacade.getTimedCommands();
        this.nonCollidingPairs = worldFacade.getNonCollidingPairs().stream().filter(e->e.first!=null&&e.second!=null).map(p -> new Pair<>(p.first.getUniqueID(), p.second.getUniqueID())).collect(Collectors.toList());
    }

    public void afterCreate(PhysicsScene physicsScene) {
        for (TimedCommand timedCommand : this.timedCommandList) {
            if (timedCommand instanceof EntityDestructionCommand) {
                EntityDestructionCommand entityDestructionCommand = (EntityDestructionCommand) timedCommand;
                GameEntity gameEntity = physicsScene.getGameEntityByUniqueId(entityDestructionCommand.getGameEntityUniqueId());
                timedCommand.setAction(() -> physicsScene.getWorldFacade().destroyGameEntity(gameEntity, true));
            }
        }
        physicsScene.getWorldFacade().getTimedCommands().addAll(this.timedCommandList);
    }

    public List<Pair<String>> getNonCollidingPairs() {
        return nonCollidingPairs;
    }
}
