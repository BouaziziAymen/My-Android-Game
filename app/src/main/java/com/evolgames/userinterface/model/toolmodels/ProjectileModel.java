package com.evolgames.userinterface.model.toolmodels;

import com.evolgames.entities.GameEntity;
import com.evolgames.entities.properties.ProjectileProperties;
import com.evolgames.entities.serialization.infos.ProjectileInfo;
import com.evolgames.userinterface.model.ProperModel;
import com.evolgames.userinterface.view.shapes.indicators.itemIndicators.ProjectileShape;
import com.evolgames.userinterface.view.windows.windowfields.itemwindow.ProjectileField;

public class ProjectileModel extends ProperModel<ProjectileProperties> {

  private final int projectileId;
  private final int bodyId;
  private ProjectileShape projectileShape;
  private String missileFile;
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

  public String getMissileFile() {
    return missileFile;
  }

  public void setMissileFile(String missileFile) {
    this.missileFile = missileFile;
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

  public ProjectileInfo toProjectileInfo() {
    ProjectileInfo pi = new ProjectileInfo();
    pi.setProjectileEnd(this.properties.getProjectileEnd());
    pi.setProjectileOrigin(this.properties.getProjectileOrigin());
    pi.setMuzzleVelocity(this.properties.getMuzzleVelocity());
    pi.setFireSound(this.properties.getFireSound());
    pi.setRecoil(this.properties.getRecoil());
    pi.setMuzzleEntity(this.muzzleEntity);
    pi.setFireRatio(this.properties.getFireRatio());
    pi.setSmokeRatio(this.properties.getSmokeRatio());
    pi.setSparkRatio(this.properties.getSparkRatio());
    pi.setCasingInfo(this.casingModel.toCasingInfo());
    pi.setMissileFile(this.missileFile);

/*    Vector2 dir = pi.getProjectileEnd().cpy().sub(pi.getProjectileOrigin()).nor();
    Vector2 nor = new Vector2(-dir.y, dir.x);
    pi.setAxisExtent(ToolUtils.getAxisExtent(pi.getMissileModel(), nor) / 2f);*/
    return pi;
  }
}
