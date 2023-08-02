package com.evolgames.entities.properties.usage;

import com.evolgames.entities.properties.Properties;
import com.evolgames.userinterface.model.toolmodels.ProjectileModel;

import java.util.ArrayList;
import java.util.List;

public class RangedProperties extends Properties {
    private final List<ProjectileModel> projectileModelList;
    private List<Integer> projectileIds;
    public RangedProperties() {
        this.projectileModelList = new ArrayList<>();
    }
    @Override
    public Properties copy() {
        return null;
    }

    public List<ProjectileModel> getProjectileModelList() {
        return projectileModelList;
    }

    public List<Integer> getProjectileIds() {
        return projectileIds;
    }

    public void setProjectileIds(List<Integer> projectileIds) {
        this.projectileIds = projectileIds;
    }
}
