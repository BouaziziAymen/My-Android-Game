package com.evolgames.entities.serialization;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class JointInfo {
    private JointDef.JointType jointType;
    private Vector2 anchorA;
    private Vector2 anchorB;
    private boolean collideConnected;
    //distance
    private float length;
    private float frequencyHz;
    private float dampingRatio;
    //mouse
    private Vector2 target;
    private float maxForce;
    //weld
    private float referenceAngle;
    //revolute
    private boolean enableLimit;
    private float lowerAngle;
    private float upperAngle;
    private boolean enableMotor;
    private float motorSpeed;
    private float maxMotorTorque;

    public void setJointType(JointDef.JointType jointType) {
        this.jointType = jointType;
    }

    public void setAnchorA(Vector2 anchorA) {
        this.anchorA = anchorA;
    }

    public void setAnchorB(Vector2 anchorB) {
        this.anchorB = anchorB;
    }

    public void setCollideConnected(boolean collideConnected) {
        this.collideConnected = collideConnected;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setFrequencyHz(float frequencyHz) {
        this.frequencyHz = frequencyHz;
    }

    public void setDampingRatio(float dampingRatio) {
        this.dampingRatio = dampingRatio;
    }

    public void setTarget(Vector2 target) {
        this.target = target;
    }


    public void setMaxForce(float maxForce) {
        this.maxForce = maxForce;
    }

    public void setReferenceAngle(float referenceAngle) {
        this.referenceAngle = referenceAngle;
    }

    public void setEnableLimit(boolean enableLimit) {
        this.enableLimit = enableLimit;
    }

    public void setLowerAngle(float lowerAngle) {
        this.lowerAngle = lowerAngle;
    }

    public void setUpperAngle(float upperAngle) {
        this.upperAngle = upperAngle;
    }

    public void setEnableMotor(boolean enableMotor) {
        this.enableMotor = enableMotor;
    }

    public void setMotorSpeed(float motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    public void setMaxMotorTorque(float maxMotorTorque) {
        this.maxMotorTorque = maxMotorTorque;
    }

    public JointDef getJointDef(){
        switch (jointType){
            case Unknown:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.localAnchorA.set(this.anchorA);
                revoluteJointDef.localAnchorB.set(this.anchorB);
                revoluteJointDef.collideConnected = collideConnected;
                revoluteJointDef.upperAngle = upperAngle;
                revoluteJointDef.lowerAngle = lowerAngle;
                revoluteJointDef.enableLimit = enableLimit;
                revoluteJointDef.motorSpeed = motorSpeed;
                revoluteJointDef.maxMotorTorque = maxMotorTorque;
                revoluteJointDef.enableMotor = enableMotor;
                revoluteJointDef.referenceAngle = referenceAngle;
                return revoluteJointDef;
            case PrismaticJoint:
                break;
            case DistanceJoint:
                break;
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
                break;
            case FrictionJoint:
                break;
        }
        return null;
    }
}
