package com.evolgames.dollmutilationgame.entities.commandtemplate;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.physics.WorldFacade;

public class EntityDestructionCommand extends TimedCommand {
    private String gameEntityUniqueId;


    @SuppressWarnings("unused")
    public EntityDestructionCommand() {
        super();
    }

    public EntityDestructionCommand(int timeLimit, GameEntity gameEntity, WorldFacade worldFacade) {
        super(timeLimit, () -> worldFacade.destroyGameEntity(gameEntity, true));
        this.gameEntityUniqueId = gameEntity.getUniqueID();
    }


    public String getGameEntityUniqueId() {
        return gameEntityUniqueId;
    }
}
