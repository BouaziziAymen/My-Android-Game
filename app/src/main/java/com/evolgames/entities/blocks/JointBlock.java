package com.evolgames.entities.blocks;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.evolgames.entities.GameEntity;
import com.evolgames.entities.commandtemplate.commands.JointCreationCommand;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.entities.serialization.JointInfo;
import com.evolgames.helpers.utilities.Utils;

import java.util.ArrayList;

public class JointBlock extends AssociatedBlock<JointBlock, JointProperties>{

    private JointDef.JointType jointType;
    transient private JointCreationCommand command;
    private JointInfo jointInfo;
    private Position position;
    private String uniqueId1;
    private String uniqueId2;
    private String jointUniqueId;

    public void recreate(GameEntity newEntity){
        this.command.substitute(newEntity,position);
        this.command.setAborted(false);
        if(position==Position.A){
            uniqueId1 = newEntity.getUniqueID();
        } else {
            uniqueId2 = newEntity.getUniqueID();
        }
    }

    public enum Position{
        A, B
    }

    public void initialization(JointCreationCommand jointCreationCommand,String jointUniqueId, JointDef.JointType jointType, ArrayList<Vector2> vertices, Properties properties, int id, Position position) {
        super.initialization(vertices, properties, id);
        this.position = position;
        this.jointType = jointType;
        this.command = jointCreationCommand;
        this.uniqueId1 = command.getEntity1().getUniqueID();
        this.uniqueId2 = command.getEntity2().getUniqueID();
        this.jointUniqueId = jointUniqueId;
        this.jointInfo = new JointInfo();
        this.jointInfo.setJointType(jointType);
        switch (this.jointType){
            case Unknown:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) command.getJointDef();
                this.jointInfo.setAnchorA(revoluteJointDef.localAnchorA);
                this.jointInfo.setAnchorB(revoluteJointDef.localAnchorB);
                this.jointInfo.setEnableLimit(revoluteJointDef.enableLimit);
                this.jointInfo.setLowerAngle(revoluteJointDef.lowerAngle);
                this.jointInfo.setUpperAngle(revoluteJointDef.upperAngle);
                this.jointInfo.setReferenceAngle(revoluteJointDef.referenceAngle);
                this.jointInfo.setCollideConnected(revoluteJointDef.collideConnected);
                this.jointInfo.setJointType(jointType);
                this.jointInfo.setEnableMotor(revoluteJointDef.enableMotor);
                this.jointInfo.setMotorSpeed(revoluteJointDef.motorSpeed);
                this.jointInfo.setMaxMotorTorque(revoluteJointDef.maxMotorTorque);
                break;
            case PrismaticJoint:
                break;
            case DistanceJoint:
                break;
            case PulleyJoint:
                break;
            case MouseJoint:
                MouseJointDef mouseJointDef = (MouseJointDef) command.getJointDef();
                this.jointInfo.setCollideConnected(mouseJointDef.collideConnected);
                this.jointInfo.setTarget(mouseJointDef.target);
                this.jointInfo.setDampingRatio(mouseJointDef.dampingRatio);
                this.jointInfo.setFrequencyHz(mouseJointDef.frequencyHz);
                this.jointInfo.setMaxForce(mouseJointDef.maxForce);
                break;
            case GearJoint:
                break;
            case LineJoint:
                break;
            case WeldJoint:
                break;
            case FrictionJoint:
                break;
        }
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

    public JointDef.JointType getJointType() {
        return jointType;
    }

    @Override
    public void performCut(Cut cut) {}

    @Override
    public void translate(Vector2 translationVector) {
            Utils.translatePoints(this.getVertices(), translationVector);
                command.updateAnchor(getVertices().get(0), position);
    }

    public String getUniqueId1() {
        return uniqueId1;
    }

    public String getUniqueId2() {
        return uniqueId2;
    }

    public String getJointUniqueId() {
        return jointUniqueId;
    }

    public JointInfo getJointInfo() {
        return jointInfo;
    }
}
