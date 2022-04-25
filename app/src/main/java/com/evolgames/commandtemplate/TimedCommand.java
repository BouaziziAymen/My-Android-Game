package com.evolgames.commandtemplate;

import com.evolgames.commandtemplate.commands.Command;
import com.evolgames.userinterface.control.behaviors.actions.Action;

public class TimedCommand {
    private final Action action;
    private int timeLimit;
    private int counter;
    private boolean timedOut;

    public TimedCommand(int timeLimit, Action action) {
        this.timeLimit = timeLimit;
        this.action = action;
    }
    public void update(){
        if(!timedOut){
            if(counter++>timeLimit){
                timedOut = true;
                action.performAction();
            }

        }
    }
}
