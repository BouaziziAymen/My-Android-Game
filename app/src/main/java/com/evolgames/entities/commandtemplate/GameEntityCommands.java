package com.evolgames.entities.commandtemplate;

import com.evolgames.entities.commandtemplate.commands.Command;
import com.evolgames.entities.EntityWithBody;

import java.util.ArrayList;

public class GameEntityCommands {
    private EntityWithBody gameEntity;
    private ArrayList<Command> commands;
    private boolean aborted;

    public GameEntityCommands(EntityWithBody gameEntity) {
        this.gameEntity = gameEntity;
        commands = new ArrayList<>();
    }

    public EntityWithBody getGameEntity() {
        return gameEntity;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    public boolean isAborted() {
        return aborted;
    }
}
