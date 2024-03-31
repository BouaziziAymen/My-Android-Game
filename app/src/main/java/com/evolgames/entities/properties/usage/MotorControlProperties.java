package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.jointmodels.JointModel;

public class MotorControlProperties extends Properties {
    private float forwardSpeed;
    private float backwardSpeed;
    private float power;
    private JointModel jointModel;
    private int jointId;
    private boolean brakes;

    public float getForwardSpeed() {
        return forwardSpeed;
    }

    public void setForwardSpeed(float forwardSpeed) {
        this.forwardSpeed = forwardSpeed;
    }

    public float getBackwardSpeed() {
        return backwardSpeed;
    }

    public void setBackwardSpeed(float backwardSpeed) {
        this.backwardSpeed = backwardSpeed;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public void setJointModel(JointModel jointModel) {
        this.jointModel = jointModel;
        if(jointModel!=null) {
            this.jointId = jointModel.getJointId();
        } else {
            this.jointId = -1;
        }
    }

    public JointModel getJointModel() {
        return jointModel;
    }

    public int getJointId() {
        return jointId;
    }

    public void setBrakes(boolean brakes) {
        this.brakes = brakes;
    }

    public boolean isBrakes() {
        return brakes;
    }
}
