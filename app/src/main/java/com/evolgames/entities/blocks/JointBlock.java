package com.evolgames.entities.blocks;


import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.properties.JointProperties;

public abstract class JointBlock extends InvisibleBlock<JointBlock, JointProperties>{

    private JointCreationCommand command;

    @Override
    protected void calculateArea() {}

    @Override
    protected boolean shouldCalculateArea() { return false; }

    @Override
    protected boolean shouldArrangeVertices() {
        return false;
    }

    @Override
    protected boolean shouldCheckShape() {
        return false;
    }

    @Override
    protected JointBlock getThis() {
        return this;
    }

    public JointCreationCommand getCommand() {
        return command;
    }

    public void setCommand(JointCreationCommand command) {
        this.command = command;
    }
}
