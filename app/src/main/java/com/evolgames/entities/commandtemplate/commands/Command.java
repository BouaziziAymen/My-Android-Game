package com.evolgames.entities.commandtemplate.commands;

public abstract class Command {

    private boolean aborted;

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    public void execute(){
            run();
            setAborted(true);
    }
    protected abstract void run();


}
