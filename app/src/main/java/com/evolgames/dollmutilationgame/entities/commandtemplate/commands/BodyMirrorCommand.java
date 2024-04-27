package com.evolgames.dollmutilationgame.entities.commandtemplate.commands;

import com.evolgames.dollmutilationgame.entities.basics.GameEntity;

public class BodyMirrorCommand extends Command {
    private final GameEntity entity;

    public BodyMirrorCommand(GameEntity entity) {
        this.entity = entity;
    }

    @Override
    protected void run() {
        entity.tryDynamicMirror();
    }
}
