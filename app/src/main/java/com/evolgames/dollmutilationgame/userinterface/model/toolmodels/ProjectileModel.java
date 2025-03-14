package com.evolgames.dollmutilationgame.userinterface.model.toolmodels;

import com.badlogic.gdx.math.Vector2;
import com.evolgames.dollmutilationgame.entities.serialization.infos.ProjectileInfo;
import com.evolgames.dollmutilationgame.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.dollmutilationgame.entities.basics.GameEntity;
import com.evolgames.dollmutilationgame.entities.properties.ProjectileProperties;
import com.evolgames.dollmutilationgame.entities.usage.Muzzle;
import com.evolgames.dollmutilationgame.userinterface.model.ProperModel;
import com.evolgames.dollmutilationgame.userinterface.view.windows.windowfields.itemwindow.ProjectileField;
import com.evolgames.dollmutilationgame.utilities.GeometryUtils;

import java.util.UUID;

public class ProjectileModel extends ProperModel<ProjectileProperties> {

    private final int projectileId;
    private final int bodyId;
    private ProjectileShape projectileShape;
    private CasingModel casingModel;
    private GameEntity muzzleEntity;
    private ProjectileField projectileField;

    public ProjectileModel(int bodyId, int projectileId, ProjectileShape projectileShape) {
        super("Proj" + projectileId);
        this.properties =
                new ProjectileProperties(projectileShape.getBegin(), projectileShape.getDirection());
        this.projectileShape = projectileShape;
        this.bodyId = bodyId;
        this.projectileId = projectileId;
    }

    public ProjectileModel(int bodyId, int projectileId, String projectileName) {
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

    public CasingModel getCasingModel() {
        return casingModel;
    }

    public void setCasingModel(CasingModel casingModel) {
        this.casingModel = casingModel;
    }

    public void setMuzzleEntity(GameEntity muzzleEntity) {
        this.muzzleEntity = muzzleEntity;
    }

    public ProjectileField getProjectileField() {
        return projectileField;
    }

    public void setProjectileField(ProjectileField projectileField) {
        this.projectileField = projectileField;
    }

    public ProjectileInfo toProjectileInfo(boolean mirrored) {
        ProjectileInfo pi = new ProjectileInfo();
        Vector2 originProjected = this.properties.getProjectileOrigin().cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f);
        Vector2 endProjected = this.properties.getProjectileEnd().cpy().sub(muzzleEntity.getCenter()).mul(1 / 32f);
        if (mirrored) {
            originProjected = GeometryUtils.mirrorPoint(originProjected);
            endProjected = GeometryUtils.mirrorPoint(endProjected);
        }
        pi.setProjectileOrigin(originProjected);
        pi.setProjectileEnd(endProjected);
        pi.setMuzzleVelocity(this.properties.getMuzzleVelocity());
        pi.setFireSound(this.properties.getFireSound());
        pi.setRecoil(this.properties.getRecoil());
        pi.setFireRatio(this.properties.getFireRatio());
        pi.setSmokeRatio(this.properties.getSmokeRatio());
        pi.setSparkRatio(this.properties.getSparkRatio());
        pi.setProjectileInfoUniqueId(UUID.randomUUID().toString());
        Muzzle muzzle = new Muzzle(this.muzzleEntity, pi);
        this.muzzleEntity.getUseList().add(muzzle);
        pi.setMuzzle(muzzle);
        if (casingModel != null) {
            pi.setCasingInfo(this.casingModel.toCasingInfo(mirrored, this.muzzleEntity));
        }
        pi.setMissileFile(this.properties.getMissileFile());
        pi.setAssetsMissile(this.properties.isAssetsMissile());
        pi.setId(this.projectileId);
        return pi;
    }
}
