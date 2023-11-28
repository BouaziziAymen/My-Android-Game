package com.evolgames.entities.commandtemplate;

import com.evolgames.entities.GameEntity;
import com.evolgames.physics.WorldFacade;

public class EntityDestructionCommand extends TimedCommand {
    private String gameEntityUniqueId;


    @SuppressWarnings("unused")
    public EntityDestructionCommand() {
        super();
    }

    public EntityDestructionCommand(int timeLimit, GameEntity gameEntity, WorldFacade worldFacade) {
        super(timeLimit, ()->worldFacade.destroyGameEntity(gameEntity,true,true));
        this.gameEntityUniqueId = gameEntity.getUniqueID();
    }

    public String getGameEntityUniqueId() {
        return gameEntityUniqueId;
    }
}
