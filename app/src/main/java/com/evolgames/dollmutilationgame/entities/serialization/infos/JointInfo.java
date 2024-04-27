package com.evolgames.dollmutilationgame.entities.serialization.infos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class JointInfo {

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

    public JointDef getJointDef(Vector2 anchorA, Vector2 anchorB) {
        switch (jointType) {
            case Unknown:
                break;
            case RevoluteJoint:
                RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                revoluteJointDef.localAnchorA.set(anchorA);
                revoluteJointDef.localAnchorB.set(anchorB);
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
                PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
                prismaticJointDef.localAnchorA.set(anchorA);
                prismaticJointDef.localAnchorB.set(anchorB);
                prismaticJointDef.collideConnected = collideConnected;
                prismaticJointDef.lowerTranslation = lowerTranslation;
                prismaticJointDef.upperTranslation = upperTranslation;
                prismaticJointDef.enableLimit = enableLimit;
                prismaticJointDef.motorSpeed = motorSpeed;
                prismaticJointDef.maxMotorForce = maxMotorForce;
                prismaticJointDef.enableMotor = enableMotor;
                prismaticJointDef.referenceAngle = referenceAngle;
                return prismaticJointDef;
            case DistanceJoint:
                DistanceJointDef distanceJointDef = new DistanceJointDef();
                distanceJointDef.localAnchorA.set(anchorA);
                distanceJointDef.localAnchorB.set(anchorB);
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
                weldJointDef.localAnchorA.set(anchorA);
                weldJointDef.localAnchorB.set(anchorB);
                return weldJointDef;
            case FrictionJoint:
                break;
        }
        return null;
    }


}
