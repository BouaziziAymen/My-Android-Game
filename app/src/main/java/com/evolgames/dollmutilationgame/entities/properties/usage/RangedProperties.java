package com.evolgames.dollmutilationgame.entities.properties.usage;

import com.evolgames.dollmutilationgame.entities.properties.Properties;
import com.evolgames.dollmutilationgame.userinterface.model.toolmodels.ProjectileModel;

import java.util.ArrayList;
import java.util.List;

public class RangedProperties extends Properties {
    private final List<ProjectileModel> projectileModelList;
    private List<Integer> projectileIds;
    private int numberOfRounds = 1;
    private float reloadTime = 1;

    public RangedProperties() {
        this.projectileModelList = new ArrayList<>();
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

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public float getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(float reloadTime) {
        this.reloadTime = reloadTime;
    }

    @Override
    public Object clone() {
        RangedProperties cloned = (RangedProperties) super.clone();
        // Deep copy of the list
        cloned.projectileIds = new ArrayList<>(this.projectileIds);
        return cloned;
    }

}
