package com.evolgames.dollmutilationgame.entities.properties.usage;

import com.evolgames.dollmutilationgame.entities.properties.Properties;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.BombModel;

import java.util.ArrayList;
import java.util.List;

public class BombUsageProperties extends Properties {

    private final List<BombModel> bombModelList;

    protected float delay = 5f;

    private int safetyJoint = -1;
    private List<Integer> bombIds;

    public BombUsageProperties() {
        this.bombModelList = new ArrayList<>();
    }

    public List<BombModel> getBombModelList() {
        return bombModelList;
    }

    public List<Integer> getBombIds() {
        return bombIds;
    }

    public void setBombIds(List<Integer> bombIds) {
        this.bombIds = bombIds;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public int getSafetyJoint() {
        return safetyJoint;
    }

    public void setSafetyJoint(int safetyJoint) {
        this.safetyJoint = safetyJoint;
    }

    @Override
    public Object clone() {
        BombUsageProperties cloned = (BombUsageProperties) super.clone();
        // Deep copy of the list
        cloned.bombIds = new ArrayList<>(this.bombIds);
        return cloned;
    }
}
