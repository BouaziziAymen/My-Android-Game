package com.evolgames.userinterface.model.jointmodels;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.view.shapes.indicators.jointindicators.JointShape;
import com.evolgames.utilities.GeometryUtils;

public class JointModel {

    private final int jointId;
    private final String jointName;
    // Joint fields
    private final Vector2 localAnchorA = new Vector2();
    private final Vector2 localAnchorB = new Vector2();
    private final Vector2 localAxis1 = new Vector2();
    private boolean selected;
    private JointShape jointShape;
    private BodyModel bodyModel1;
    private BodyModel bodyModel2;
    private JointDef.JointType jointType;
    private boolean collideConnected;
    // distance
    private float length;
    private float frequencyHz;
    private float dampingRatio;
    // mouse
    private Vector2 target;
    private float maxForce;
    // weld
    private float referenceAngle;
    // revolute
    private boolean enableLimit;
    private float lowerAngle;
    private float upperAngle;
    private boolean enableMotor;
    private float motorSpeed;
    private float maxMotorTorque;
    // prismatic
    private float lowerTranslation;
    private float upperTranslation;
    private float maxMotorForce;

    public JointModel(int jointId, JointDef.JointType jointType, JointShape jointShape) {
        this.jointShape = jointShape;
        this.jointId = jointId;
        this.jointName = "Joint" + jointId;
        this.jointType = jointType;
    }

    public JointModel(int jointId, JointDef.JointType jointType) {
        this.jointId = jointId;
        this.jointName = "Joint" + jointId;
        this.jointType = jointType;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public void selectJoint() {
        setSelected(true);
        jointShape.select();
    }

    public void deselect() {
        setSelected(false);
        if (jointShape != null) {
            jointShape.release();
        }
    }

    public String getJointName() {
        return jointName;
    }

    public Vector2 getLocalAnchorA() {
        return localAnchorA;
    }

    public Vector2 getLocalAnchorB() {
        return localAnchorB;
    }

    public JointDef.JointType getJointType() {
        return jointType;
    }

    public void setJointType(JointDef.JointType jointType) {
        this.jointType = jointType;
    }

    public boolean isCollideConnected() {
        return collideConnected;
    }

    public void setCollideConnected(boolean collideConnected) {
        this.collideConnected = collideConnected;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getFrequencyHz() {
        return frequencyHz;
    }

    public void setFrequencyHz(float frequencyHz) {
        this.frequencyHz = frequencyHz;
    }

    public float getDampingRatio() {
        return dampingRatio;
    }

    public void setDampingRatio(float dampingRatio) {
        this.dampingRatio = dampingRatio;
    }

    public Vector2 getTarget() {
        return target;
    }

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public float getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    public float getReferenceAngle() {
        return referenceAngle;
    }

    public void setReferenceAngle(float referenceAngle) {
        this.referenceAngle = referenceAngle;
    }

    public boolean isEnableLimit() {
        return enableLimit;
    }

    public void setEnableLimit(boolean enableLimit) {
        this.enableLimit = enableLimit;
    }

    public float getLowerAngle() {
        return lowerAngle;
    }

    public void setLowerAngle(float lowerAngle) {
        this.lowerAngle = lowerAngle;
    }

    public float getUpperAngle() {
        return upperAngle;
    }

    public void setUpperAngle(float upperAngle) {
        this.upperAngle = upperAngle;
    }

    public boolean isEnableMotor() {
        return enableMotor;
    }

    public void setEnableMotor(boolean enableMotor) {
        this.enableMotor = enableMotor;
    }

    public float getMotorSpeed() {
        return motorSpeed;
    }

    public void setMotorSpeed(float motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    public float getMaxMotorTorque() {
        return maxMotorTorque;
    }

    public void setMaxMotorTorque(float maxMotorTorque) {
        this.maxMotorTorque = maxMotorTorque;
    }

    public float getLowerTranslation() {
        return lowerTranslation;
    }

    public void setLowerTranslation(float lowerTranslation) {
        this.lowerTranslation = lowerTranslation;
    }

    public float getUpperTranslation() {
        return upperTranslation;
    }

    public void setUpperTranslation(float upperTranslation) {
        this.upperTranslation = upperTranslation;
    }

    public float getMaxMotorForce() {
        return maxMotorForce;
    }

    public void setMaxMotorForce(float maxMotorForce) {
        this.maxMotorForce = maxMotorForce;
    }

    public JointDef createJointDef(Vector2 center1, Vector2 center2, boolean mirrored) {
        Vector2 u1 = this.localAnchorA.cpy().sub(center1).mul(1 / 32f);
        Vector2 u2 = this.localAnchorB.cpy().sub(center2).mul(1 / 32f);
        if (mirrored) {
           u1 = GeometryUtils.mirrorPoint(u1);
           u2 = GeometryUtils.mirrorPoint(u2);
        }
        Log.e("rocket",jointType+""+u1+"/"+u2);
        switch (jointType) {
            case Unknown:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.localAnchorA.set(u1);
                revoluteJointDef.localAnchorB.set(u2);
                revoluteJointDef.collideConnected = collideConnected;
                if (mirrored) {
                    revoluteJointDef.lowerAngle = -upperAngle;
                    revoluteJointDef.upperAngle = -lowerAngle;
                } else {
                    revoluteJointDef.upperAngle = upperAngle;
                    revoluteJointDef.lowerAngle = lowerAngle;
                }

                revoluteJointDef.enableLimit = enableLimit;
                if(mirrored) {
                    revoluteJointDef.motorSpeed = -motorSpeed;
                } else {
                    revoluteJointDef.motorSpeed = motorSpeed;
                }
                revoluteJointDef.maxMotorTorque = maxMotorTorque;
                revoluteJointDef.enableMotor = enableMotor;
                revoluteJointDef.referenceAngle = referenceAngle;
                return revoluteJointDef;
            case PrismaticJoint:
                PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
                prismaticJointDef.localAnchorA.set(u1);
                prismaticJointDef.localAnchorB.set(u2);
                prismaticJointDef.collideConnected = collideConnected;
                prismaticJointDef.lowerTranslation = lowerTranslation;
                prismaticJointDef.upperTranslation = upperTranslation;
                if(mirrored){
                    prismaticJointDef.localAxis1.set(-this.localAxis1.x,this.localAxis1.y);
                } else {
                    prismaticJointDef.localAxis1.set(this.localAxis1.x,this.localAxis1.y);
                }
                prismaticJointDef.enableLimit = enableLimit;
                prismaticJointDef.motorSpeed = motorSpeed;
                prismaticJointDef.maxMotorForce = maxMotorForce;
                prismaticJointDef.enableMotor = enableMotor;
                prismaticJointDef.referenceAngle = referenceAngle;
                return prismaticJointDef;
            case DistanceJoint:
                DistanceJointDef distanceJointDef = new DistanceJointDef();
                distanceJointDef.localAnchorA.set(u1);
                distanceJointDef.localAnchorB.set(u2);
                distanceJointDef.collideConnected = collideConnected;
                distanceJointDef.length = length;
                distanceJointDef.frequencyHz = frequencyHz;
                distanceJointDef.dampingRatio = dampingRatio;
                return distanceJointDef;
            case PulleyJoint:
                break;
            case MouseJoint:
                MouseJointDef mouseJointDef = new MouseJointDef();
                mouseJointDef.target.set(target);
                mouseJointDef.collideConnected = collideConnected;
                mouseJointDef.dampingRatio = dampingRatio;
                mouseJointDef.frequencyHz = frequencyHz;
                mouseJointDef.maxForce = maxForce;
                return mouseJointDef;
            case GearJoint:
                break;
            case LineJoint:
                break;
            case WeldJoint:
                WeldJointDef weldJointDef = new WeldJointDef();
                weldJointDef.referenceAngle = referenceAngle;
                weldJointDef.collideConnected = collideConnected;
                weldJointDef.localAnchorA.set(u1);
                weldJointDef.localAnchorB.set(u2);
                return weldJointDef;
            case FrictionJoint:
                break;
        }
        return null;
    }

    public Vector2 getLocalAxis1() {
        return localAxis1;
    }
}
