package com.evolgames.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.basics.GameEntity;
import com.evolgames.entities.cut.Cut;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.entities.properties.Properties;
import com.evolgames.entities.serialization.infos.JointInfo;
import com.evolgames.utilities.GeometryUtils;
import com.evolgames.utilities.Utils;

import java.util.ArrayList;

public class JointBlock extends AssociatedBlock<JointBlock, JointProperties> {

    private JointDef.JointType jointType;
    private Position position;
    private String jointUniqueId;
    private transient JointBlock brother;
    private transient GameEntity entity;
    private JointInfo jointInfo;

    private  int jointId;

    @SuppressWarnings("Unused")
    public JointBlock(){}
    public JointBlock(int jointId){
        this.jointId = jointId;
    }

    public int getJointId() {
        return jointId;
    }

    public GameEntity getEntity() {
        return entity;
    }

    public void setEntity(GameEntity entity) {
        this.entity = entity;
    }

    public JointDef prepareJointDef() {
        JointDef jointDef = getProperties().getJointDef();
        JointBlock a = this.position == Position.A ? this : this.getBrother();
        JointBlock b = this.position == Position.B ? this : this.getBrother();
        switch (this.jointType) {
            case Unknown:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
                revoluteJointDef.localAnchorA.set(a.getAnchorWorld());
                revoluteJointDef.localAnchorB.set(b.getAnchorWorld());
                break;
            case PrismaticJoint:
                PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
                prismaticJointDef.localAnchorA.set(a.getAnchorWorld());
                prismaticJointDef.localAnchorB.set(b.getAnchorWorld());
                break;
            case DistanceJoint:
                DistanceJointDef distanceJointDef = (DistanceJointDef) jointDef;
                distanceJointDef.localAnchorA.set(a.getAnchorWorld());
                distanceJointDef.localAnchorB.set(b.getAnchorWorld());
                break;
            case PulleyJoint:
                break;
            case MouseJoint:
                break;
            case GearJoint:
                break;
            case LineJoint:
                break;
            case WeldJoint:
                WeldJointDef weldJointDef = (WeldJointDef) jointDef;
                weldJointDef.localAnchorA.set(a.getAnchorWorld());
                weldJointDef.localAnchorB.set(b.getAnchorWorld());
                break;
            case FrictionJoint:
                break;
        }
        return jointDef;
    }

    private Vector2 getAnchorWorld() {
        return this.getVertices().get(0).cpy().mul(1 / 32f);
    }

    public void initialization(
            GameEntity entity,
            String jointUniqueId,
            JointDef.JointType jointType,
            ArrayList<Vector2> vertices,
            Properties properties,
            int id,
            Position position,
            JointBlock brother) {
        super.initialization(vertices, properties, id);
        this.position = position;
        this.jointType = jointType;
        this.jointUniqueId = jointUniqueId;
        this.brother = brother;
        this.entity = entity;
    }

    public JointBlock getBrother() {
        return brother;
    }

    public void setBrother(JointBlock brother) {
        this.brother = brother;
    }

    @Override
    protected boolean shouldRectify() {
        return false;
    }

    @Override
    protected void calculateArea() {
    }

    @Override
    protected boolean shouldCalculateArea() {
        return false;
    }

    @Override
    protected JointBlock createChildBlock() {
        return new JointBlock(jointId);
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

    public JointDef.JointType getJointType() {
        return jointType;
    }

    @Override
    public void performCut(Cut cut) {
    }

    @Override
    public void translate(Vector2 translationVector) {
        Utils.translatePoints(this.getVertices(), translationVector);
    }

    public Position getPosition() {
        return position;
    }

    public String getJointUniqueId() {
        return jointUniqueId;
    }

    public JointDef getJointDefFromJointInfo(JointInfo jointInfo) {
        return jointInfo.getJointDef(
                this.position == Position.A ? this.getAnchorWorld() : this.brother.getAnchorWorld(),
                this.position == Position.B ? this.getAnchorWorld() : this.brother.getAnchorWorld());
    }

    public void generateJointInfo() {
        this.jointInfo = new JointInfo();
        JointDef jointDef = getProperties().getJointDef();
        jointInfo.setJointType(this.getJointType());
        jointInfo.setCollideConnected(jointDef.collideConnected);
        switch (this.jointType) {
            case Unknown:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
                jointInfo.setEnableLimit(revoluteJointDef.enableLimit);
                jointInfo.setLowerAngle(revoluteJointDef.lowerAngle);
                jointInfo.setUpperAngle(revoluteJointDef.upperAngle);
                jointInfo.setEnableMotor(revoluteJointDef.enableMotor);
                jointInfo.setMotorSpeed(revoluteJointDef.motorSpeed);
                jointInfo.setMaxMotorTorque(revoluteJointDef.maxMotorTorque);
                jointInfo.setReferenceAngle(revoluteJointDef.referenceAngle);
                break;
            case PrismaticJoint:
                PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
                jointInfo.setEnableLimit(prismaticJointDef.enableLimit);
                jointInfo.setLowerTranslation(prismaticJointDef.lowerTranslation);
                jointInfo.setUpperTranslation(prismaticJointDef.upperTranslation);
                jointInfo.setEnableMotor(prismaticJointDef.enableMotor);
                jointInfo.setMotorSpeed(prismaticJointDef.motorSpeed);
                jointInfo.setMaxMotorForce(prismaticJointDef.maxMotorForce);
                jointInfo.setReferenceAngle(prismaticJointDef.referenceAngle);
                break;
            case DistanceJoint:
                break;
            case PulleyJoint:
                break;
            case MouseJoint:
                MouseJointDef mouseJointDef = (MouseJointDef) jointDef;
                jointInfo.setTarget(mouseJointDef.target);
                jointInfo.setDampingRatio(mouseJointDef.dampingRatio);
                jointInfo.setFrequencyHz(mouseJointDef.frequencyHz);
                jointInfo.setMaxForce(mouseJointDef.maxForce);
                break;
            case GearJoint:
                break;
            case LineJoint:
                break;
            case WeldJoint:
                WeldJointDef weldJointDef = (WeldJointDef) jointDef;
                jointInfo.setReferenceAngle(weldJointDef.referenceAngle);
                break;
            case FrictionJoint:
                break;
        }
    }

    public JointInfo getJointInfo() {
        return jointInfo;
    }

    @Override
    public void mirror() {
        super.mirror();
        JointDef jointDef = getProperties().getJointDef();
        switch (jointType) {
            case Unknown:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
                revoluteJointDef.localAnchorA.set(GeometryUtils.mirrorPoint(revoluteJointDef.localAnchorA));
                revoluteJointDef.localAnchorB.set(GeometryUtils.mirrorPoint(revoluteJointDef.localAnchorB));
                break;
            case PrismaticJoint:
                PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
                prismaticJointDef.localAnchorA.set(GeometryUtils.mirrorPoint(prismaticJointDef.localAnchorA));
                prismaticJointDef.localAnchorB.set(GeometryUtils.mirrorPoint(prismaticJointDef.localAnchorB));
                break;
            case DistanceJoint:
                DistanceJointDef distanceJointDef = (DistanceJointDef) jointDef;
                distanceJointDef.localAnchorA.set(GeometryUtils.mirrorPoint(distanceJointDef.localAnchorA));
                distanceJointDef.localAnchorB.set(GeometryUtils.mirrorPoint(distanceJointDef.localAnchorB));
                break;
            case PulleyJoint:
                break;
            case MouseJoint:

                break;
            case GearJoint:
                break;
            case LineJoint:
                break;
            case WeldJoint:
                WeldJointDef weldJointDef = (WeldJointDef) jointDef;
                weldJointDef.localAnchorA.set(GeometryUtils.mirrorPoint(weldJointDef.localAnchorA));
                weldJointDef.localAnchorB.set(GeometryUtils.mirrorPoint(weldJointDef.localAnchorB));
                break;
            case FrictionJoint:
                break;
        }
    }

    public enum Position {
        A,
        B
    }
}
