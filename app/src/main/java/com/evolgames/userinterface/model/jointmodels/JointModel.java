package com.evolgames.userinterface.model.jointmodels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.entities.properties.JointProperties;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;
import com.evolgames.utilities.GeometryUtils;

public class JointModel extends ProperModel<JointProperties> {

    private final int jointId;
    private JointShape jointShape;
    private BodyModel bodyModel1;
    private BodyModel bodyModel2;


    public JointModel(int jointId, JointDef.JointType jointType, JointShape jointShape) {
        super("Joint" + jointId);
        this.jointShape = jointShape;
        this.jointId = jointId;
        this.properties = new JointProperties();
        this.properties.setJointType(jointType);
    }

    public JointModel(int jointId, JointDef.JointType jointType) {
        super("Joint" + jointId);
        this.jointId = jointId;
        this.properties = new JointProperties();
        this.properties.setJointType(jointType);
    }

    public BodyModel getBodyModel1() {
        return bodyModel1;
    }

    public void setBodyModel1(BodyModel bodyModel1) {
        this.bodyModel1 = bodyModel1;
    }

    public BodyModel getBodyModel2() {
        return bodyModel2;
    }

    public void setBodyModel2(BodyModel bodyModel2) {
        this.bodyModel2 = bodyModel2;
    }

    public JointShape getJointShape() {
        return jointShape;
    }

    public void setJointShape(JointShape jointShape) {
        this.jointShape = jointShape;
    }

    public int getJointId() {
        return jointId;
    }

    public JointDef createJointDef(Vector2 center1, Vector2 center2, boolean mirrored) {
        Vector2 u1 = properties.getLocalAnchorA().cpy().sub(center1).mul(1 / 32f);
        Vector2 u2 = properties.getLocalAnchorB().cpy().sub(center2).mul(1 / 32f);
        if (mirrored) {
            u1 = GeometryUtils.mirrorPoint(u1);
            u2 = GeometryUtils.mirrorPoint(u2);
        }
        switch (properties.getJointType()) {
            case Unknown:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.localAnchorA.set(u1);
                revoluteJointDef.localAnchorB.set(u2);
                revoluteJointDef.collideConnected = properties.isCollideConnected();


                revoluteJointDef.enableLimit = properties.isEnableLimit();

                revoluteJointDef.lowerAngle = properties.getLowerAngle();
                revoluteJointDef.upperAngle = properties.getUpperAngle();
                if (mirrored) {
                    revoluteJointDef.motorSpeed = -properties.getMotorSpeed();
                } else {
                    revoluteJointDef.motorSpeed = properties.getMotorSpeed();
                }
                revoluteJointDef.maxMotorTorque = properties.getMaxMotorTorque();
                revoluteJointDef.enableMotor = properties.isEnableMotor();
                revoluteJointDef.referenceAngle = properties.getReferenceAngle();
                return revoluteJointDef;
            case PrismaticJoint:
                PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
                prismaticJointDef.localAnchorA.set(u1);
                prismaticJointDef.localAnchorB.set(u2);
                prismaticJointDef.collideConnected = properties.isCollideConnected();
                prismaticJointDef.lowerTranslation = properties.getLowerTranslation();
                prismaticJointDef.upperTranslation = properties.getUpperTranslation();
                Vector2 localAxis1 = properties.getLocalAxis1();
                if (mirrored) {
                    prismaticJointDef.localAxis1.set(-localAxis1.x, localAxis1.y);
                } else {
                    prismaticJointDef.localAxis1.set(localAxis1.x, localAxis1.y);
                }
                prismaticJointDef.enableLimit = properties.isEnableLimit();
                prismaticJointDef.motorSpeed = properties.getMotorSpeed();
                prismaticJointDef.maxMotorForce = properties.getMaxMotorForce();
                prismaticJointDef.enableMotor = properties.isEnableMotor();
                prismaticJointDef.referenceAngle = properties.getReferenceAngle();
                return prismaticJointDef;
            case DistanceJoint:
                DistanceJointDef distanceJointDef = new DistanceJointDef();
                distanceJointDef.localAnchorA.set(u1);
                distanceJointDef.localAnchorB.set(u2);
                distanceJointDef.collideConnected = properties.isCollideConnected();
                distanceJointDef.length = properties.getLength();
                distanceJointDef.frequencyHz = properties.getFrequencyHz();
                distanceJointDef.dampingRatio = properties.getDampingRatio();
                return distanceJointDef;
            case PulleyJoint:
                break;
            case MouseJoint:
                MouseJointDef mouseJointDef = new MouseJointDef();
                mouseJointDef.target.set(properties.getTarget());
                mouseJointDef.collideConnected = properties.isCollideConnected();
                mouseJointDef.dampingRatio = properties.getDampingRatio();
                mouseJointDef.frequencyHz = properties.getFrequencyHz();
                mouseJointDef.maxForce = properties.getMaxForce();
                return mouseJointDef;
            case GearJoint:
                break;
            case LineJoint:
                break;
            case WeldJoint:
                WeldJointDef weldJointDef = new WeldJointDef();
                weldJointDef.referenceAngle = properties.getReferenceAngle();
                weldJointDef.collideConnected = properties.isCollideConnected();
                weldJointDef.localAnchorA.set(u1);
                weldJointDef.localAnchorB.set(u2);
                return weldJointDef;
            case FrictionJoint:
                break;
        }
        return null;
    }
}
