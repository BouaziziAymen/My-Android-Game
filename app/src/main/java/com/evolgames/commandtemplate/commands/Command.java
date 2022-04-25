package com.evolgames.commandtemplate.commands;

public abstract class Command {

    private boolean aborted;

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    public void exectue(){

            run();
            setAborted(true);

    }
    protected abstract void run();


}
