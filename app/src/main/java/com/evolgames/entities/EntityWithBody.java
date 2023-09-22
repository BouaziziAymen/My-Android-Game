package com.evolgames.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.evolgames.entities.commandtemplate.commands.Command;
import com.evolgames.scenes.hand.Hand;

import java.util.ArrayList;

public class EntityWithBody {
    protected Body body;
    private boolean hanged;
    private Hand holdingHand;
    private final ArrayList<Command> commands;

    public EntityWithBody() {
        this.commands = new ArrayList<>();
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void setHoldingHand(Hand holdingHand) {
        this.holdingHand = holdingHand;
    }

    public Hand getHoldingHand() {
        return holdingHand;
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
    public void setFilter(Filter filter) {
        body.getFixtureList().forEach(f -> f.setFilterData(filter));
    }

}
