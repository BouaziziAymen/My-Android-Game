package com.evolgames.dollmutilationgame.entities.properties.usage;

import com.evolgames.dollmutilationgame.entities.properties.Properties;
import com.evolgames.dollmutilationgame.userinterface.model.jointmodels.JointModel;

import java.util.List;

public class MotorControlProperties extends Properties {
    private float forwardSpeed;
    private float backwardSpeed;
    private float power;
    private List<JointModel> jointModels;
    private List<Integer> jointIds;
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

    public List<JointModel> getJointModels() {
        return jointModels;
    }

    public void setJointModels(List<JointModel> jointModels) {
        this.jointModels = jointModels;
    }

    public List<Integer> getJointIds() {
        return jointIds;
    }

    public void setJointIds(List<Integer> jointIds) {
        this.jointIds = jointIds;
    }

    public void setBrakes(boolean brakes) {
        this.brakes = brakes;
    }

    public boolean isBrakes() {
        return brakes;
    }
}
