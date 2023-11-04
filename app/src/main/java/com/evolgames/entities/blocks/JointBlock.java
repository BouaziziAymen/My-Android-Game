package com.evolgames.entities.blocks;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.helpers.utilities.Utils;

import java.util.ArrayList;

public class JointBlock extends AssociatedBlock<JointBlock, JointProperties>{
    private JointDef.JointType jointType;
    private JointCreationCommand command;
    private Position position;

    public void recreate(GameEntity splinter){
        this.command.substitute(splinter,position);
        this.command.setAborted(false);
    }

    public enum Position{
        A, B
    }


    public void initialization(JointDef.JointType jointType, ArrayList<Vector2> vertices, Properties properties, int id, Position position) {
        super.initialization(vertices, properties, id);
        this.position = position;
        this.jointType = jointType;
    }


    @Override
    protected boolean shouldRectify() {
        return false;
    }

    @Override
    protected void calculateArea() {
    }

    @Override
    protected boolean shouldCalculateArea() { return false; }

    @Override
    protected JointBlock createChildBlock() {
        return new JointBlock();
    }

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

    public JointDef.JointType getJointType() {
        return jointType;
    }

    @Override
    public void performCut(Cut cut) {}

    @Override
    public void translate(Vector2 translationVector) {
            Utils.translatePoints(this.getVertices(), translationVector);
            command.updateAnchor(getVertices().get(0),position);
    }
}
