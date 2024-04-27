package com.evolgames.dollmutilationgame.entities.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.cut.Cut;
import com.evolgames.dollmutilationgame.entities.serialization.infos.JointInfo;
import com.evolgames.dollmutilationgame.entities.properties.JointBlockProperties;
import com.evolgames.dollmutilationgame.entities.properties.Properties;

import java.util.ArrayList;

public class JointBlock extends AssociatedBlock<JointBlock, JointBlockProperties> {

    private JointInfo jointInfo;
    private JointDef.JointType jointType;
    private Position position;
    private String jointUniqueId;
    private int jointId;
    private transient JointBlock brother;
    private transient GameEntity entity;
    private transient Joint joint;
    private boolean frozen;


    public JointBlock() {
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

    public void setJointId(int jointId) {
        this.jointId = jointId;
    }

    @Override
    protected boolean shouldCalculateArea() {
        return false;
    }

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

    public JointDef.JointType getJointType() {
        return jointType;
    }

    @Override
    public void performCut(Cut cut) {
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
                DistanceJointDef distanceJointDef = (DistanceJointDef) jointDef;
                jointInfo.setFrequencyHz(distanceJointDef.frequencyHz);
                jointInfo.setLowerTranslation(distanceJointDef.dampingRatio);
                jointInfo.setUpperTranslation(distanceJointDef.length);
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
        mirrorJointDef();
    }

    public void mirrorJointDef() {
        if (this.position == Position.A) {
            JointDef jointDef = getProperties().getJointDef();
            switch (jointType) {
                case Unknown:
                case WeldJoint:
                case LineJoint:
                case GearJoint:
                case MouseJoint:
                case PulleyJoint:
                case DistanceJoint:
                case FrictionJoint:
                    break;
                case RevoluteJoint:
                    RevoluteJointDef revoluteJointDef = (RevoluteJointDef) jointDef;
                    float upper = revoluteJointDef.upperAngle;
                    revoluteJointDef.upperAngle = -revoluteJointDef.lowerAngle;
                    revoluteJointDef.lowerAngle = -upper;
                    break;
                case PrismaticJoint:
                    PrismaticJointDef prismaticJointDef = (PrismaticJointDef) jointDef;
                    prismaticJointDef.localAxis1.x = -prismaticJointDef.localAxis1.x;
                    break;
            }
        }
    }

    public void setJoint(Joint joint) {
        this.joint = joint;
    }

    public Joint getJoint() {
        return joint;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public enum Position {
        A,
        B
    }
}
