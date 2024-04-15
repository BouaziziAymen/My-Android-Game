package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;

import java.util.ArrayList;
import java.util.List;

public class RocketProperties extends Properties {
    private final List<FireSourceModel> fireSourceModelList;
    private List<Integer> fireSourceIds;
    private float power = 0.5f;
    private float fuel = 10;

    public RocketProperties() {
        this.fireSourceModelList = new ArrayList<>();
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public float getFuel() {
        return fuel;
    }

    public void setFuel(float fuel) {
        this.fuel = fuel;
    }

    public List<FireSourceModel> getFireSourceModelList() {
        return fireSourceModelList;
    }

    public List<Integer> getFireSourceIds() {
        return fireSourceIds;
    }

    public void setFireSourceIds(List<Integer> fireSourceIds) {
        this.fireSourceIds = fireSourceIds;
    }

    @Override
    public Object clone() {
        RocketProperties cloned = (RocketProperties) super.clone();
        // Deep copy of the list
        cloned.fireSourceIds = new ArrayList<>(this.fireSourceIds);
        return cloned;
    }
}
