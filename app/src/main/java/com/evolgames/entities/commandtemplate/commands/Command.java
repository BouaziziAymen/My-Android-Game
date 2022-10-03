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
        if(this.isReady()&&!isAborted()) {
            this.run();
            this.setAborted(true);
        }
    }
    protected abstract void run();

    protected boolean isReady(){
        return true;
    }
}
