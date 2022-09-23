package com.evolgames.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.commandtemplate.GameEntityCommands;

public class EntityWithBody {
    protected Body body;
    private boolean hanged;
    private final GameEntityCommands commands;

    public EntityWithBody() {
        this.commands = new GameEntityCommands(this);
    }

    public GameEntityCommands getCommandContainer() {
        return commands;
    }

    public boolean getHanged() {
        return hanged;
    }

    public void setHanged(boolean hanged) {
        this.hanged = hanged;
    }

    public boolean isHanged() {
        return hanged;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        body.setUserData(this);
        this.body = body;
    }

}
