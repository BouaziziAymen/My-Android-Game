package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;

import java.util.ArrayList;
import java.util.List;

public class LiquidContainerProperties extends Properties {
    private final List<LiquidSourceModel> liquidSourceModelList;
    float quantity;
    private List<Integer> liquidSourceIds;

    public LiquidContainerProperties() {
        this.liquidSourceModelList = new ArrayList<>();
    }

    public List<LiquidSourceModel> getLiquidSourceModelList() {
        return liquidSourceModelList;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public List<Integer> getLiquidSourceIds() {
        return liquidSourceIds;
    }

    public void setLiquidSourceIds(List<Integer> liquidSourceIds) {
        this.liquidSourceIds = liquidSourceIds;
    }

    @Override
    public Object clone() {
        LiquidContainerProperties cloned = (LiquidContainerProperties) super.clone();
        // Deep copy of the list
        cloned.liquidSourceIds = new ArrayList<>(this.liquidSourceIds);
        return cloned;
    }
}
