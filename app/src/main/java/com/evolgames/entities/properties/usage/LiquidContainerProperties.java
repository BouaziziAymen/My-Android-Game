package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.LiquidSourceModel;
import java.util.ArrayList;
import java.util.List;

public class LiquidContainerProperties extends Properties {
    float quantity;
    private final List<LiquidSourceModel> liquidSourceModelList;
    private List<Integer> liquidSourceIds;

    public LiquidContainerProperties() {
        this.liquidSourceModelList = new ArrayList<>();
    }

    public List<LiquidSourceModel> getLiquidSourceModelList() {
        return liquidSourceModelList;
    }

    @Override
    public Properties copy() {
        return null;
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
}
