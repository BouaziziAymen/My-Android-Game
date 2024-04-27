package com.evolgames.dollmutilationgame.entities.properties;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.JointDef;

public class JointProperties extends Properties {
    private final Vector2 localAnchorA = new Vector2();
    private final Vector2 localAnchorB = new Vector2();
    private final Vector2 localAxis1 = new Vector2();
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

    public Vector2 getLocalAnchorA() {
        return localAnchorA;
    }

    public Vector2 getLocalAnchorB() {
        return localAnchorB;
    }

    public Vector2 getLocalAxis1() {
        return localAxis1;
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

    @Override
    public Object clone() {
        JointProperties cloned = (JointProperties) super.clone();
        cloned.localAnchorA.set(this.localAnchorA);
        cloned.localAnchorB.set(this.localAnchorB);
        cloned.localAxis1.set(this.localAxis1);
        cloned.target = (this.target != null) ? new Vector2(this.target) : null;
        return cloned;
    }
}
