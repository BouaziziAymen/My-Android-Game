package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.userinterface.model.BodyModel;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;

public class ProjectileModel extends ProperModel {
    private final ProjectileShape projectileShape;
    private final int projectileId;
    private final int bodyId;
    private int energy;
    private float fireRate;
    private ProjectileTriggerType projectileTriggerType;
    private float recoil;
    private BodyModel bodyModel;

    @Override
    public String getModelName() {
        return super.getModelName();
    }

    public ProjectileModel(int bodyId, int projectileId, ProjectileShape projectileShape) {
        super("Proj"+projectileId);
        this.projectileShape = projectileShape;
        this.bodyId = bodyId;
        this.projectileId = projectileId;
    }

    public int getProjectileId() {
        return projectileId;
    }

    public int getBodyId() {
        return bodyId;
    }

    public ProjectileShape getProjectileShape() {
        return projectileShape;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public float getFireRate() {
        return fireRate;
    }

    public void setProjectileTriggerType(ProjectileTriggerType projectileTriggerType) {
        this.projectileTriggerType = projectileTriggerType;
    }

    public ProjectileTriggerType getProjectileTriggerType() {
        return projectileTriggerType;
    }


    public void setRecoil(float recoil) {
        this.recoil = recoil;
    }

    public float getRecoil() {
        return recoil;
    }

    public void setBodyModel(BodyModel bodyModel) {
        this.bodyModel = bodyModel;
    }

    public BodyModel getBodyModel() {
        return bodyModel;
    }
}
