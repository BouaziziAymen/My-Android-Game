package com.evolgames.entities.commandtemplate;

import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.hand.Hand;
import com.evolgames.physics.WorldFacade;
import com.evolgames.scenes.PlayScene;

public class EntityDestructionCommand extends TimedCommand {
    private String gameEntityUniqueId;


    @SuppressWarnings("unused")
    public EntityDestructionCommand() {
        super();
    }

    public EntityDestructionCommand(int timeLimit, GameEntity gameEntity, WorldFacade worldFacade) {
        super(timeLimit, () -> worldFacade.destroyGameEntity(gameEntity, true, true));
        this.gameEntityUniqueId = gameEntity.getUniqueID();
    }


    public String getGameEntityUniqueId() {
        return gameEntityUniqueId;
    }
}
