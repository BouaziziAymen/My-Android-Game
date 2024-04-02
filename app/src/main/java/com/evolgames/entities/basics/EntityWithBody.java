package com.evolgames.entities.basics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.commandtemplate.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityWithBody {

    transient private final List<Command> commands;
    transient protected Body body;
    private short groupIndex;

    public EntityWithBody() {
        this.commands = new CopyOnWriteArrayList<>();
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        if (body != null) {
            body.setUserData(this);
        }
        this.body = body;
    }

    public void setFilter(Filter filter) {
        body.getFixtureList().forEach(f -> f.setFilterData(filter));
    }

    public short getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(short groupIndex) {
        this.groupIndex = groupIndex;
    }
}
