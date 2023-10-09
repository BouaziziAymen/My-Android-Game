package com.evolgames.entities.commandtemplate.commands;

public class CustomCommand extends Command{


    private final Runnable runnable;

    public CustomCommand(Runnable runnable){
        this.runnable = runnable;
    }
    @Override
    protected void run() {
        runnable.run();
    }
}
