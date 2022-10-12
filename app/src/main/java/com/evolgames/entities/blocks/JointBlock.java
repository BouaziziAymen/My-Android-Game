package com.evolgames.entities.blocks;


import com.badlogic.gdx.math.Vector2;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.helpers.utilities.Utils;

import java.util.ArrayList;

public class JointBlock extends AssociatedBlock<JointBlock, JointProperties>{

    public void substitute(GameEntity splinter) {
        command.substitute(splinter,position);
    }

    public enum JointZoneType{
       PUNCTUAL, OVERLAP
    }
    public enum Position{
        A, B
    }

    private JointCreationCommand command;
    private JointZoneType jointZoneType;
    private Position position;

    public void initialization(ArrayList<Vector2> vertices, Properties properties, int id, JointZoneType jointZoneType,Position position) {
        this.jointZoneType = jointZoneType;
        super.initialization(vertices, properties, id);
        this.position = position;
    }

    @Override
    protected boolean shouldRectify() {
        return false;
    }

    @Override
    protected void calculateArea() {
    }

    @Override
    protected boolean shouldCalculateArea() { return this.jointZoneType==JointZoneType.OVERLAP; }

    @Override
    protected JointBlock createChildBlock() {
        return new JointBlock();
    }

    @Override
    protected boolean shouldArrangeVertices() {
        return this.jointZoneType==JointZoneType.OVERLAP;
    }

    @Override
    protected boolean shouldCheckShape() {
        return this.jointZoneType==JointZoneType.OVERLAP;
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

    @Override
    public void performCut(Cut cut) {
        if(this.jointZoneType==JointZoneType.OVERLAP) {

            super.performCut(cut);
        }
    }
    @Override
    public void translate(Vector2 translationVector) {
            Utils.translatePoints(this.getVertices(), translationVector);
            command.updateAnchor(getVertices().get(0),position);
    }

    @Override
    public void preProjectCut() {
        super.preProjectCut();
        //find overlap between parent layer block and other entity layer blocks
    }
}
