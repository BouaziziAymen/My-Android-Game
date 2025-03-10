package com.evolgames.dollmutilationgame.entities.properties.usage;

import com.evolgames.dollmutilationgame.entities.properties.Properties;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.FireSourceModel;

import java.util.ArrayList;
import java.util.List;

public class FlameThrowerProperties extends Properties {

    private final List<FireSourceModel> fireSources;
    private List<Integer> fireSourceIds;

    public FlameThrowerProperties() {
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

    @Override
    public Object clone() {
        FlameThrowerProperties cloned = (FlameThrowerProperties) super.clone();
        // Deep copy of the list
        cloned.fireSourceIds = new ArrayList<>(this.fireSourceIds);
        return cloned;
    }
}
