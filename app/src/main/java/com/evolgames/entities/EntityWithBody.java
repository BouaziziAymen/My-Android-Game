package com.evolgames.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.evolgames.entities.commandtemplate.commands.Command;

import java.util.ArrayList;

public class EntityWithBody {
    protected Body body;
    private boolean hanged;
    private final ArrayList<Command> commands;

    public EntityWithBody() {
        this.commands = new ArrayList<>();
    }

    public ArrayList<Command> getCommands() {
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
        if(body!=null){
            body.setUserData(this);
        }
        this.body = body;
    }

}
