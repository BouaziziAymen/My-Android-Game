package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.FireSourceModel;

import java.util.ArrayList;
import java.util.List;

public class FlameThrowerProperties extends Properties {

    private final List<FireSourceModel> fireSources;
    private List<Integer> fireSourceIds;

    public FlameThrowerProperties(){
        fireSources = new ArrayList<>();
    }

    public List<FireSourceModel> getFireSources() {
        return fireSources;
    }

    public List<Integer> getFireSourceIds() {
        return fireSourceIds;
    }

    public void setFireSourceIds(List<Integer> fireSourceIds) {
        this.fireSourceIds = fireSourceIds;
    }
}
