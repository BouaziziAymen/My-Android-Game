package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.particles.wrappers.explosion.ExplosiveParticleWrapper;
import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.model.ToolModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;

public class ProjectileModel extends ProperModel<ProjectileProperties> {

    private ProjectileShape projectileShape;
    private final int projectileId;
    private final int bodyId;
    private ToolModel missileModel;
    private CasingModel ammoModel;
    private GameEntity muzzleEntity;
    private ExplosiveParticleWrapper fireSource;

    public ProjectileModel(int bodyId, int projectileId, ProjectileShape projectileShape) {
        super("Proj" + projectileId);
        this.properties = new ProjectileProperties(projectileShape.getBegin(),projectileShape.getDirection());
        this.projectileShape = projectileShape;
        this.bodyId = bodyId;
        this.projectileId = projectileId;
    }
    public ProjectileModel(int bodyId, int projectileId,String projectileName) {
        super(projectileName);
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

    public void setProjectileShape(ProjectileShape projectileShape) {
        this.projectileShape = projectileShape;
    }

    public ToolModel getMissileModel() {
        return missileModel;
    }

    public void setMissileModel(ToolModel missileModel) {
        this.missileModel = missileModel;
    }

    public CasingModel getAmmoModel() {
        return ammoModel;
    }

    public void setAmmoModel(CasingModel ammoModel) {
        this.ammoModel = ammoModel;
    }

    public GameEntity getMuzzleEntity() {
        return muzzleEntity;
    }

    public void setMuzzleEntity(GameEntity muzzleEntity) {
        this.muzzleEntity = muzzleEntity;
    }

    public void setFireSource(ExplosiveParticleWrapper fireSource) {
        this.fireSource = fireSource;
    }

    public ExplosiveParticleWrapper getFireSource() {
        return fireSource;
    }
}
